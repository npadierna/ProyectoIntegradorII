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
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.restful.contract.WebServicePathContract;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamImageWebServiceImpl extends AbstractContextWebService implements
		IExamImageWebService {

	private static final String TAG = ExamImageWebServiceImpl.class
			.getSimpleName();

	public ExamImageWebServiceImpl() {
		super();
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

	@Override()
	public boolean uploadReferenceExamImageFile(Bitmap referenceExamImageBitmap)
			throws OMRGraderWebServiceException {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
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
			int imageFieldId) throws OMRGraderWebServiceException {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap
				.put(WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER,
						"temporal");
		parametersMap
				.put(WebServicePathContract.ExamImageWebServiceContract.STUDENT_EXAM_IMAGE_FILE_ID_PARAMETER,
						Integer.valueOf(imageFieldId));

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
}