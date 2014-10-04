package co.edu.udea.android.omrgrader2_0.activity.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.activity.session.MainSessionActivity;
import co.edu.udea.android.omrgrader2_0.util.validator.InternetConnectionValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ConfigurationStarterActivity extends Activity {

	private static final String TAG = ConfigurationStarterActivity.class
			.getSimpleName();

	private AlertDialog.Builder errorAlertDialogBuilder;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.getActionBar().hide();
		super.setContentView(R.layout.activity_configuration_starter);

		this.createViewComponents();
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if (!this.isCameraApplication()) {
			this.errorAlertDialogBuilder
					.setMessage(R.string.no_backcamera_alert_dialog_message);
			this.errorAlertDialogBuilder
					.setTitle(R.string.no_backcamera_alert_dialog_title);
			this.errorAlertDialogBuilder.create().show();

			return;
		}

//		if (!InternetConnectionValidator.isNetworkConnecting(super
//				.getApplicationContext())) {
//			this.errorAlertDialogBuilder
//					.setMessage(R.string.no_internet_connection_alert_dialog_message);
//			this.errorAlertDialogBuilder
//					.setTitle(R.string.no_internet_connection_alert_dialog_title);
//			this.errorAlertDialogBuilder.create().show();
//
//			return;
//		}

		this.onStartNextActivity();
	}

	private void createViewComponents() {
		Log.v(TAG, "Creating the View Components for the Activity.");

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder.setPositiveButton(
				R.string.accept_button_label,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						ConfigurationStarterActivity.this.finish();
					}
				});
	}

	private boolean isCameraApplication() {
		Log.v(TAG, "Checking if there is any Camera Application installed.");

		return (super.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA));
	}

	private void onStartNextActivity() {
		Log.v(TAG, "Starting new next Activity.");

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getApplicationContext());
		String eMailSetted = sharedPreferences.getString(
				super.getString(R.string.email_shared_preference_key), null);

		Intent intent = null;
		if (TextUtils.isEmpty(eMailSetted)) {
			intent = new Intent(super.getApplicationContext(),
					InitialActivity.class);
		} else {
			intent = new Intent(super.getApplicationContext(),
					MainSessionActivity.class);
		}

		super.startActivity(intent);
	}
}