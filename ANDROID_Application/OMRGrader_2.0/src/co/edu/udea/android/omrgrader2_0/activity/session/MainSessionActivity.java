package co.edu.udea.android.omrgrader2_0.activity.session;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.activity.about.AboutUsActivity;
import co.edu.udea.android.omrgrader2_0.activity.session.dialog.SessionNameDialogFragment;
import co.edu.udea.android.omrgrader2_0.activity.session.preference.MainSessionPreferenceActivity;
import co.edu.udea.android.omrgrader2_0.business.ping.PingAsyncTask;
import co.edu.udea.android.omrgrader2_0.util.validator.InternetConnectionValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionActivity extends FragmentActivity implements
		SessionNameDialogFragment.NoticeDialogListener {

	private static final String TAG = MainSessionActivity.class.getSimpleName();

	private static final String SESSION_NAME_DIALOG_TAG = "Session Name Dialog Fragment";

	private URL applicationServerURL;

	private AlertDialog.Builder errorAlertDialogBuilder;
	private Button newGraderSessionButton;
	private DialogFragment sessionNameDialogFragment;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main_session);

		this.createViewComponents();
	}

	@Override()
	public boolean onCreateOptionsMenu(Menu menu) {
		super.getMenuInflater().inflate(R.menu.menu_main, menu);

		return (true);
	}

	@Override()
	public void onDialogFragmentNegativeClick(DialogFragment dialogFragment) {
		Log.v(TAG, "Session Name Dialog Fragment Negative Button");

		this.sessionNameDialogFragment.getDialog().cancel();
	}

	@Override()
	public void onDialogFragmentPositiveClick(DialogFragment dialogFragment) {
		Log.v(TAG, "Session Name Dialog Fragment Positive Button");

		String sessionName = ((SessionNameDialogFragment) dialogFragment)
				.getSessionNameEditText().getText().toString();

		if (TextUtils.isEmpty(sessionName)
				|| (TextUtils.isEmpty(sessionName.trim()))) {
			this.errorAlertDialogBuilder.create().show();
		} else {
			Intent intent = new Intent(super.getApplicationContext(),
					ImageTakerActivity.class);
			intent.putExtra(ImageTakerActivity.SESSION_NAME_KEY,
					sessionName.trim());

			super.startActivity(intent);
		}
	}

	@Override()
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_settings_menu_item:
			super.startActivity(new Intent(super.getApplicationContext(),
					MainSessionPreferenceActivity.class));

			return (true);

		case R.id.main_about_us_menu_item:
			super.startActivity(new Intent(super.getApplicationContext(),
					AboutUsActivity.class));

			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if (!InternetConnectionValidator.isNetworkConnecting(super
				.getApplicationContext())) {
			this.errorAlertDialogBuilder
					.setMessage(R.string.no_internet_connection_alert_dialog_message);
			this.errorAlertDialogBuilder
					.setTitle(R.string.no_internet_connection_alert_dialog_title);
			this.errorAlertDialogBuilder.setPositiveButton(
					R.string.accept_button_label,
					new DialogInterface.OnClickListener() {

						@Override()
						public void onClick(DialogInterface dialog, int which) {
							MainSessionActivity.this.finish();
						}
					});
			this.errorAlertDialogBuilder.create().show();
		} else {
			this.createApplicationServerURL();
		}
	}

	public void onDoPing(View view) {
		Log.v(TAG, "PIN to Server.");

		AsyncTask<Object, Void, Integer> pingAsyncTask = new PingAsyncTask(
				super.getApplicationContext());
		pingAsyncTask.execute(new Object[] { this.applicationServerURL });

		Integer pingResult = null;
		try {
			pingResult = pingAsyncTask.get();
		} catch (InterruptedException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ExecutionException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		if ((pingResult == null) || !(pingResult.equals(PingAsyncTask.OK))) {
			this.newGraderSessionButton.setEnabled(false);

			this.errorAlertDialogBuilder
					.setMessage(R.string.no_application_server_available_alert_dialog_message);
			this.errorAlertDialogBuilder
					.setTitle(R.string.no_application_server_available_alert_dialog_title);
			this.errorAlertDialogBuilder.create().show();
		} else {
			this.newGraderSessionButton.setEnabled(true);
		}
	}

	public void onStartGradeSession(View view) {
		Log.v(TAG, "Starting new Grade Session.");

		this.sessionNameDialogFragment.show(super.getSupportFragmentManager(),
				SESSION_NAME_DIALOG_TAG);
	}

	private void createApplicationServerURL() {
		try {
			this.applicationServerURL = new URL(
					super.getString(R.string.application_server_protocol),
					super.getString(R.string.application_server_ip),
					Integer.valueOf(super
							.getString(R.string.application_server_port)),
					super.getString(R.string.application_server_context));
		} catch (NumberFormatException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void createViewComponents() {
		this.newGraderSessionButton = (Button) super
				.findViewById(R.id.new_grader_session_button);

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder
				.setMessage(R.string.invalid_session_name_alert_dialog_message);
		this.errorAlertDialogBuilder
				.setTitle(R.string.invalid_session_name_alert_dialog_title);
		this.errorAlertDialogBuilder.setPositiveButton(
				R.string.accept_button_label, null);

		this.sessionNameDialogFragment = new SessionNameDialogFragment();
	}
}