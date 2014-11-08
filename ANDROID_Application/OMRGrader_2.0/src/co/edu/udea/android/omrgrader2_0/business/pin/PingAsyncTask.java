package co.edu.udea.android.omrgrader2_0.business.pin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.util.validator.InternetConnectionValidator;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class PingAsyncTask extends AsyncTask<Object, Void, Integer> {

	private static final String TAG = PingAsyncTask.class.getSimpleName();

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

		try {
			isResponse = this.isAvailableServer(null);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);

			return (MALFORMED_URL);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);

			return (IO_ERROR);
		}

		return ((isResponse) ? OK : NO_OK);
	}

	private boolean isAvailableServer(String url) throws MalformedURLException,
			IOException {
		if (InternetConnectionValidator.isNetworkConnecting(this.context)) {
			HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(
					context.getResources().getString(
							R.string.internet_connection_url_server))
					.openConnection());

			httpURLConnection.setRequestProperty(
					"User-Agent",
					context.getResources().getString(
							R.string.internet_connection_validator_user_agent));
			httpURLConnection.setConnectTimeout(context.getResources()
					.getInteger(R.integer.maximum_timeout));
			httpURLConnection.connect();

			return (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
		}

		return (false);
	}
}