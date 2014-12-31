package co.edu.udea.android.omrgrader2_0.business.ping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.util.validator.InternetConnectionValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class PingAsyncTask extends AsyncTask<Object, Void, Integer> {

	private static final String TAG = PingAsyncTask.class.getSimpleName();

	private static final String USER_AGENT = "User-Agent";

	public static final int OK = 0;
	public static final int MALFORMED_URL = -1;
	public static final int IO_ERROR = -2;
	public static final int NO_OK = -3;

	private Context context;

	public PingAsyncTask(Context context) {
		this.context = context;
	}

	@Override()
	protected Integer doInBackground(Object... parameters) {
		boolean isResponse = false;

		if (this.checkParameters(parameters)) {
			try {
				isResponse = this.isAvailableServer((URL) parameters[0]);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);

				return (IO_ERROR);
			}

			return ((isResponse) ? OK : NO_OK);
		}

		return (MALFORMED_URL);
	}

	private boolean checkParameters(Object... parameters) {
		if ((parameters == null) || (parameters.length != 1)
				|| !(parameters[0] instanceof URL)) {

			return (false);
		}

		URL url = (URL) parameters[0];

		if ((TextUtils.isEmpty(url.getProtocol()))
				|| (TextUtils.isEmpty(url.getHost())) || (url.getPort() != -1)
				|| (TextUtils.isEmpty(url.getFile()))) {

			return (false);
		}

		return (true);
	}

	private boolean isAvailableServer(URL url) throws IOException {
		if (InternetConnectionValidator.isNetworkConnecting(this.context)) {
			HttpURLConnection httpURLConnection = (HttpURLConnection) (url
					.openConnection());

			httpURLConnection.setRequestProperty(
					USER_AGENT,
					this.context.getResources().getString(
							R.string.internet_connection_validator_user_agent));
			httpURLConnection.setConnectTimeout(this.context.getResources()
					.getInteger(R.integer.maximum_timeout));
			httpURLConnection.connect();

			return (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
		}

		return (false);
	}
}