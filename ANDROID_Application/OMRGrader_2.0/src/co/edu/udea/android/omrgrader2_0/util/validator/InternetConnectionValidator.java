package co.edu.udea.android.omrgrader2_0.util.validator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class InternetConnectionValidator {

	private InternetConnectionValidator() {
		super();
	}

	public static boolean isNetworkConnecting(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager != null) {
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();

			return ((networkInfo != null) && (networkInfo
					.isConnectedOrConnecting()));
		}

		return (false);
	}
}