package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;
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
	public GraderSession createGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException {
		try {
			HttpPost httpPost = new HttpPost();

			httpPost.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
			httpPost.setEntity(new StringEntity(graderSession
					.packEntityToJsonObject().toString()));

			HttpEntity httpEntity = super
					.executeHTTPMethod(
							new String[] {
									WebServicePathContract.GraderSessionWebServiceContract.ROOT_PATH,
									WebServicePathContract.GraderSessionWebServiceContract.CREATE_GRADER_SESSION_PATH },
							null, httpPost);

			if (httpEntity != null) {
				String entityResponse = EntityUtils.toString(httpEntity);

				GraderSession gs = (GraderSession) (new GraderSession()
						.unpackJsonOjectToEntity(new JSONObject(entityResponse)));
				Log.v(TAG, String.valueOf(ExamImageWebServiceImpl.getInstance()
						.buildStorageDirectoryPathName(gs)));

				return (gs);
			}
		} catch (Exception e) {
			throw new OMRGraderWebServiceException(
					"The application could not create a new Grader Session.", e);
		}

		return (null);
	}

	@Override()
	public GraderSession finishGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException {
		try {
			HttpPost httpPost = new HttpPost();

			httpPost.setHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
			httpPost.setEntity(new StringEntity(graderSession
					.packEntityToJsonObject().toString()));

			HttpEntity httpEntity = super
					.executeHTTPMethod(
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
}