package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

abstract class AbstractContextWebService {

	private static final String TAG = AbstractContextWebService.class
			.getSimpleName();

	private static final String AMPERSAND = "&";
	private static final String EQUAL = "=";

	public AbstractContextWebService() {
		super();
	}

	private String buildQueriesForPath(Map<String, Object> parametersMap) {
		if ((parametersMap != null) && (!parametersMap.isEmpty())) {
			StringBuilder stringForQueries = new StringBuilder();
			Set<String> keySet = parametersMap.keySet();
			int counter = parametersMap.size();

			for (String key : keySet) {
				stringForQueries.append(key).append(EQUAL)
						.append(parametersMap.get(key).toString());
				counter--;

				if (counter >= 1) {
					stringForQueries.append(AMPERSAND);
				}
			}

			return (stringForQueries.toString());
		}

		return (null);
	}

	private URI buildURIForHTTPMethod(URL webServiceURL, String[] paths,
			Map<String, Object> parametersMap) throws URISyntaxException {
		StringBuilder stringForPaths = new StringBuilder();

		if ((paths != null) && (paths.length != 0)) {
			for (String s : paths) {
				stringForPaths.append(s);
			}
		}

		URI uri = new URI(webServiceURL.getProtocol(), null,
				webServiceURL.getHost(), webServiceURL.getPort(),
				webServiceURL.getFile().concat(stringForPaths.toString()),
				this.buildQueriesForPath(parametersMap), null);

		Log.d(TAG, String.format("URI Content: %s", uri.toString()));

		return (uri);
	}

	protected HttpEntity executeHTTPMethod(URL webServiceURL, String[] paths,
			Map<String, Object> parametersMap, HttpRequestBase httpRequestBase)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		httpRequestBase.setURI(this.buildURIForHTTPMethod(webServiceURL, paths,
				parametersMap));

		return (httpClient.execute(httpRequestBase).getEntity());
	}
}