package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;
import co.edu.udea.android.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;
import co.edu.udea.android.omrgrader2_0.webservice.restful.contract.WebServicePathContract;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamImageWebServiceImpl extends AbstractContextWebService
		implements IExamImageWebService {

	private static final String TAG = ExamImageWebServiceImpl.class
			.getSimpleName();

	private static ExamImageWebServiceImpl instance = null;

	private ExamImageWebServiceImpl() {
		super();
	}

	public static synchronized ExamImageWebServiceImpl getInstance() {
		if (instance == null) {
			instance = new ExamImageWebServiceImpl();
		}

		return (instance);
	}

	@Override()
	public int buildStorageDirectoryPathName(GraderSession graderSession) {

		return (Math.abs(graderSession.geteMailAccount().hashCode()
				+ graderSession.getGraderSessionName().hashCode()
				+ graderSession.getRequestTimeStamp().hashCode()));
	}

	@Override()
	public boolean uploadReferenceExamImageFile(
			Bitmap referenceExamImageBitmap, GraderSession graderSession)
			throws OMRGraderWebServiceException {
		if (!this.checkParameters(referenceExamImageBitmap, graderSession,
				null, true)) {

			return (false);
		}

		Map<String, Object> parametersMap = new HashMap<String, Object>();
		// parametersMap
		// .put(WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER,
		// Integer.valueOf(this
		// .buildStorageDirectoryPathName(graderSession)));
		parametersMap
				.put(WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER,
						"temporal");

		try {
			HttpPost httpPost = this.createRequestForImageUploading(
					referenceExamImageBitmap, true);
			HttpEntity httpEntity = super
					.executeHTTPMethod(
							new String[] {
									WebServicePathContract.ExamImageWebServiceContract.ROOT_PATH,
									WebServicePathContract.ExamImageWebServiceContract.UPLOAD_REFERENCE_IMAGE_FILE_PATH },
							parametersMap, httpPost);

			if (httpEntity != null) {
				String entityResponse = EntityUtils.toString(httpEntity);

				Log.v(TAG, entityResponse);
			}
		} catch (Exception e) {
			throw new OMRGraderWebServiceException(
					"The application could not load the Referece Exam Image.",
					e);
		}

		return (true);
	}

	@Override()
	public boolean uploadStudentExamImageFile(Bitmap studentExamImageBitmap,
			Integer imageFileId, GraderSession graderSession)
			throws OMRGraderWebServiceException {
		if (!this.checkParameters(studentExamImageBitmap, graderSession,
				imageFileId, false)) {

			return (false);
		}

		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap
				.put(WebServicePathContract.ExamImageWebServiceContract.STUDENT_EXAM_IMAGE_FILE_ID_PARAMETER,
						imageFileId);
		// parametersMap
		// .put(WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER,
		// Integer.valueOf(this
		// .buildStorageDirectoryPathName(graderSession)));
		parametersMap
				.put(WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER,
						"temporal");

		try {
			HttpPost httpPost = this.createRequestForImageUploading(
					studentExamImageBitmap, true);
			HttpEntity httpEntity = super
					.executeHTTPMethod(
							new String[] {
									WebServicePathContract.ExamImageWebServiceContract.ROOT_PATH,
									WebServicePathContract.ExamImageWebServiceContract.UPLOAD_STUDENT_IMAGE_FILE_PATH },
							parametersMap, httpPost);

			if (httpEntity != null) {
				String entityResponse = EntityUtils.toString(httpEntity);

				Log.v(TAG, entityResponse);
			}
		} catch (Exception e) {
			throw new OMRGraderWebServiceException(
					"The application could not load the Student Exam Image.", e);
		}

		return (true);
	}

	private boolean checkParameters(Bitmap examImageBitmap,
			GraderSession graderSession, Integer imageFileId,
			boolean isReference) {
		if (examImageBitmap == null) {

			return (false);
		}

		try {
			if ((graderSession == null)
					|| (TextUtils.isEmpty(graderSession.geteMailAccount()
							.trim()))
					|| (TextUtils.isEmpty(graderSession.getGraderSessionName()
							.trim()))
					|| (!RegexValidator.isValidEMail(graderSession
							.geteMailAccount()))) {

				return (false);
			}
		} catch (Exception e) {

			return (false);
		}

		if ((graderSession.getRequestTimeStamp() == null)
				|| (graderSession.getApprovalPercentage() <= 0.0F)
				|| (graderSession.getDecimalPrecision() <= 0)
				|| (graderSession.getMaximumGrade() <= 0.0F)) {

			return (false);
		}

		if (!isReference) {
			if ((imageFileId == null) || (imageFileId.intValue() <= 0)) {

				return (false);
			}
		}

		return (true);
	}

	private HttpPost createRequestForImageUploading(Bitmap examImageBitmap,
			boolean isReferenceExamImage) {
		HttpPost httpPost = new HttpPost();
		ByteArrayOutputStream byteArrayOutpuStream = new ByteArrayOutputStream();

		examImageBitmap
				.compress(CompressFormat.JPEG, 100, byteArrayOutpuStream);

		byte[] imageData = byteArrayOutpuStream.toByteArray();
		// FIXME: What's about the: "test.jpg"?
		ByteArrayBody byteArrayBody = new ByteArrayBody(imageData, "test.jpg");
		MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		if (isReferenceExamImage) {
			multipartEntity
					.addPart(
							WebServicePathContract.ExamImageWebServiceContract.CONTENT_REFERENCE_IMAGE_FILE_PARAMETER,
							byteArrayBody);
		} else {
			multipartEntity
					.addPart(
							WebServicePathContract.ExamImageWebServiceContract.CONTENT_STUDENT_IMAGE_FILE_PARAMETER,
							byteArrayBody);
		}

		httpPost.setEntity(multipartEntity);

		return (httpPost);
	}
}