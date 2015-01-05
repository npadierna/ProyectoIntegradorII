package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;
import co.edu.udea.android.omrgrader2_0.webservice.IGraderSessionWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;
import co.edu.udea.android.omrgrader2_0.webservice.restful.contract.WebServicePathContract;

public class GraderSessionWebServiceImpl extends AbstractContextWebService
		implements IGraderSessionWebService {

	private static final String TAG = GraderSessionWebServiceImpl.class
			.getSimpleName();

	public static final String CONTENT_TYPE_KEY = "content-type";
	public static final String CONTENT_TYPE_VALUE = "application/json";

	private static GraderSessionWebServiceImpl instance;

	private GraderSessionWebServiceImpl() {
		super();
	}

	public static synchronized GraderSessionWebServiceImpl getInstance() {
		if (instance == null) {
			instance = new GraderSessionWebServiceImpl();
		}

		return (instance);
	}

	@Override()
	public GraderSession createGraderSession(URL webServiceURL,
			GraderSession graderSession) throws OMRGraderWebServiceException {
		if (!this.checkParameters(webServiceURL, graderSession, true)) {

			return (null);
		}

		try {
			HttpPost httpPost = new HttpPost();

			httpPost.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
			httpPost.setEntity(new StringEntity(graderSession
					.packEntityToJsonObject().toString()));

			HttpEntity httpEntity = super
					.executeHTTPMethod(
							webServiceURL,
							new String[] {
									WebServicePathContract.GraderSessionWebServiceContract.ROOT_PATH,
									WebServicePathContract.GraderSessionWebServiceContract.CREATE_GRADER_SESSION_PATH },
							null, httpPost);

			if (httpEntity != null) {
				Header header = httpEntity.getContentType();
				Log.d(TAG, header.toString());

				String entityResponse = EntityUtils.toString(httpEntity);

				GraderSession responsedGraderSession = (GraderSession) (new GraderSession()
						.unpackJsonOjectToEntity(new JSONObject(entityResponse)));

				Log.v(TAG, String.valueOf(ExamImageWebServiceImpl.getInstance()
						.buildStorageDirectoryPathName(responsedGraderSession)));

				return (responsedGraderSession);
			}
		} catch (Exception e) {
			throw new OMRGraderWebServiceException(
					"The application could not create a new Grader Session.", e);
		}

		return (null);
	}

	@Override()
	public GraderSession finishGraderSession(URL webServiceURL,
			GraderSession graderSession) throws OMRGraderWebServiceException {
		if (!this.checkParameters(webServiceURL, graderSession, false)) {

			return (null);
		}

		try {
			HttpPost httpPost = new HttpPost();

			httpPost.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
			httpPost.setEntity(new StringEntity(graderSession
					.packEntityToJsonObject().toString()));

			HttpEntity httpEntity = super
					.executeHTTPMethod(
							webServiceURL,
							new String[] {
									WebServicePathContract.GraderSessionWebServiceContract.ROOT_PATH,
									WebServicePathContract.GraderSessionWebServiceContract.FINISH_GRADER_SESSION_PATH },
							null, httpPost);

			if (httpEntity != null) {
				String entityResponse = EntityUtils.toString(httpEntity);

				Log.v(TAG, entityResponse);

				return ((GraderSession) (new GraderSession()
						.unpackJsonOjectToEntity(new JSONObject(entityResponse))));
			}
		} catch (Exception e) {
			throw new OMRGraderWebServiceException(
					"The application could not finish the created Grader Session.",
					e);
		}

		return (null);
	}

	private boolean checkParameters(URL webServiceURL,
			GraderSession graderSession, boolean isCreation) {
		if ((webServiceURL == null)
				|| (TextUtils.isEmpty(webServiceURL.getProtocol()))
				|| (TextUtils.isEmpty(webServiceURL.getHost()))
				|| (webServiceURL.getPort() == -1)
				|| (TextUtils.isEmpty(webServiceURL.getFile()))) {

			return (false);
		}

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

		if ((graderSession.getApprovalPercentage() <= 0.0F)
				|| (TextUtils.isEmpty(graderSession.getDecimalPrecision()))
				|| (Integer.valueOf(graderSession.getDecimalPrecision()) <= 0)
				|| (graderSession.getMaximumGrade() <= 0.0F)) {

			return (false);
		}

		if ((!isCreation) && (graderSession.getRequest() == null)) {

			return (false);
		}

		return (true);
	}
}