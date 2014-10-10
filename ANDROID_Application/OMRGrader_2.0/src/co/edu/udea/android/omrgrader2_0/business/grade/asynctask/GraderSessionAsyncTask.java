package co.edu.udea.android.omrgrader2_0.business.grade.asynctask;

import android.os.AsyncTask;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;
import co.edu.udea.android.omrgrader2_0.webservice.IGraderSessionWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;
import co.edu.udea.android.omrgrader2_0.webservice.restful.impl.GraderSessionWebServiceImpl;

public class GraderSessionAsyncTask extends AsyncTask<Object, Void, Object[]> {

	public static final int GRADER_SESSION_OK = 0;
	public static final int GRADER_SESSION_FAIL = -1;
	public static final int INVALID_PARAMETERS = -2;

	public static final String CREATE_GRADER_SESSION = "Create: Grader Session";
	public static final String FINISH_GRADER_SESSION = "Finish: Grader Session";

	private IGraderSessionWebService graderSessionWebService;

	public GraderSessionAsyncTask() {
		this.graderSessionWebService = GraderSessionWebServiceImpl
				.getInstance();
	}

	@Override()
	protected Object[] doInBackground(Object... parameters) {
		if (this.checkParameters(parameters)) {
			String asyncTaskKey = (String) parameters[0];
			GraderSession graderSession = (GraderSession) parameters[1];

			GraderSession resultGraderSession = null;

			try {
				if (asyncTaskKey.equals(CREATE_GRADER_SESSION)) {
					resultGraderSession = this.graderSessionWebService
							.createGraderSession(graderSession);
				} else {
					resultGraderSession = this.graderSessionWebService
							.finishGraderSession(graderSession);
				}
			} catch (OMRGraderWebServiceException e) {

				return (new Object[] { GRADER_SESSION_FAIL, resultGraderSession });
			}

			return (new Object[] {
					((resultGraderSession == null) ? GRADER_SESSION_OK
							: GRADER_SESSION_FAIL), resultGraderSession });
		}

		return (new Object[] { INVALID_PARAMETERS, null });
	}

	private boolean checkParameters(Object... parameters) {
		if ((parameters == null) || (parameters.length < 2)) {

			return (false);
		}

		String asyncTaskKey = null;
		GraderSession graderSession = null;

		try {
			asyncTaskKey = (String) parameters[0];
			if ((asyncTaskKey == null)
					|| (TextUtils.isEmpty(asyncTaskKey.trim()))) {

				return (false);
			}

			if ((!asyncTaskKey.equals(CREATE_GRADER_SESSION))
					&& (!asyncTaskKey.equals(FINISH_GRADER_SESSION))) {

				return (false);
			}

			graderSession = (GraderSession) parameters[1];
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

		if ((graderSession.getApprovalPercentage() <= 0.0F)
				|| (graderSession.getDecimalPrecision() <= 0)
				|| (graderSession.getMaximumGrade() <= 0.0F)) {

			return (false);
		}

		if ((asyncTaskKey.equals(FINISH_GRADER_SESSION))
				&& (graderSession.getRequestTimeStamp() == null)) {

			return (false);
		}

		return (true);
	}
}