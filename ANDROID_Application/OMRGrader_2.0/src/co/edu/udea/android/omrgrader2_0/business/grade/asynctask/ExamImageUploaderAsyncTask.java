package co.edu.udea.android.omrgrader2_0.business.grade.asynctask;

import java.net.URL;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;
import co.edu.udea.android.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;
import co.edu.udea.android.omrgrader2_0.webservice.restful.impl.ExamImageWebServiceImpl;

public class ExamImageUploaderAsyncTask extends
		AsyncTask<Object, Void, Integer> {

	public static final int SUCCESS_UPLOADING = 0;
	public static final int FAILED_UPLOADING = -1;
	public static final int INVALID_PARAMETERS = -2;

	public static final String UPLOAD_REFERENCE_EXAM_IMAGE = "Reference Exam Image";
	public static final String UPLOAD_STUDENT_EXAM_IMAGE = "Student Exam Image";

	private IExamImageWebService examImageWebService;

	public ExamImageUploaderAsyncTask() {
		this.examImageWebService = ExamImageWebServiceImpl.getInstance();
	}

	@Override()
	protected Integer doInBackground(Object... parameters) {
		if (this.checkParameters(parameters)) {
			String asyncTaskKey = (String) parameters[0];
			URL webServiceURL = (URL) parameters[1];
			GraderSession graderSession = (GraderSession) parameters[2];
			Bitmap examImageBitmap = (Bitmap) parameters[3];
			Integer imageFileId = null;

			boolean uploadingResult = false;

			try {
				if (asyncTaskKey.equals(UPLOAD_STUDENT_EXAM_IMAGE)) {
					imageFileId = (Integer) parameters[4];

					uploadingResult = this.examImageWebService
							.uploadStudentExamImageFile(webServiceURL,
									examImageBitmap, imageFileId.intValue(),
									graderSession);
				} else {
					uploadingResult = this.examImageWebService
							.uploadReferenceExamImageFile(webServiceURL,
									examImageBitmap, graderSession);
				}
			} catch (OMRGraderWebServiceException e) {

				return (FAILED_UPLOADING);
			}

			return ((uploadingResult) ? SUCCESS_UPLOADING : FAILED_UPLOADING);
		}

		return (INVALID_PARAMETERS);
	}

	private boolean checkParameters(Object... parameters) {
		if ((parameters == null) || (parameters.length < 3)
				|| !(parameters[0] instanceof String)
				|| !(parameters[1] instanceof URL)
				|| !(parameters[2] instanceof GraderSession)
				|| !(parameters[3] instanceof Bitmap)) {

			return (false);
		}

		String asyncTaskKey = null;
		URL webServiceURL = null;
		GraderSession graderSession = null;
		Bitmap examImageBitmap = null;
		Integer imageFileId = null;

		try {
			asyncTaskKey = (String) parameters[0];
			if ((asyncTaskKey == null)
					|| (TextUtils.isEmpty(asyncTaskKey.trim()))) {

				return (false);
			}

			if ((!asyncTaskKey.equals(UPLOAD_REFERENCE_EXAM_IMAGE))
					&& (!asyncTaskKey.equals(UPLOAD_STUDENT_EXAM_IMAGE))) {

				return (false);
			}

			if (asyncTaskKey.equals(UPLOAD_STUDENT_EXAM_IMAGE)) {
				if (parameters[4] instanceof Integer) {
					imageFileId = (Integer) parameters[4];

					if ((imageFileId == null) || (imageFileId.intValue() <= 0)) {

						return (false);
					}
				} else {

					return (false);
				}
			}

			webServiceURL = (URL) parameters[1];
			if ((webServiceURL == null)
					|| (TextUtils.isEmpty(webServiceURL.getProtocol()))
					|| (TextUtils.isEmpty(webServiceURL.getHost()))
					|| (webServiceURL.getPort() == -1)
					|| (TextUtils.isEmpty(webServiceURL.getFile()))) {

				return (false);
			}

			graderSession = (GraderSession) parameters[2];
			if ((graderSession == null)
					|| (graderSession.getGraderSessionPK() == null)
					|| (TextUtils.isEmpty(graderSession.getGraderSessionPK()
							.getElectronicMail().trim()))
					|| (TextUtils.isEmpty(graderSession.getGraderSessionPK()
							.getSessionName().trim()))
					|| (!RegexValidator.isValidEMail(graderSession
							.getGraderSessionPK().getElectronicMail()))) {

				return (false);
			}
		} catch (Exception e) {

			return (false);
		}

		examImageBitmap = (Bitmap) parameters[3];
		if (examImageBitmap == null) {

			return (false);
		}

		if ((graderSession.getRequest() == null)
				|| (graderSession.getApprovalPercentage() <= 0.0F)
				|| (TextUtils.isEmpty(graderSession.getDecimalPrecision()))
				|| (Integer.valueOf(graderSession.getDecimalPrecision()) <= 0)
				|| (graderSession.getMaximumGrade() <= 0.0F)) {

			return (false);
		}

		return (true);
	}
}