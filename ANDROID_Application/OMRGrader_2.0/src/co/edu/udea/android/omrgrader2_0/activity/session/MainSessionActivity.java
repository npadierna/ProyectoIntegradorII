package co.edu.udea.android.omrgrader2_0.activity.session;

import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
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
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.activity.about.AboutUsActivity;
import co.edu.udea.android.omrgrader2_0.activity.session.dialog.SessionNameDialogFragment;
import co.edu.udea.android.omrgrader2_0.activity.session.preference.MainSessionPreferenceActivity;
import co.edu.udea.android.omrgrader2_0.business.pin.PingAsyncTask;

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

	private AlertDialog.Builder errorAlertDialogBuilder;
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

		return (false);
	}

	public void onDoPing(View view) {
		Log.v(TAG, "PIN to Server.");

		AsyncTask<Object, Void, Integer> pinAsyncTask = new PingAsyncTask(
				super.getApplicationContext());
		pinAsyncTask.execute(new Object());

		try {
			pinAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void onStartGradeSession(View view) {
		Log.v(TAG, "Starting new Grade Session.");

		this.sessionNameDialogFragment.show(super.getSupportFragmentManager(),
				SESSION_NAME_DIALOG_TAG);
	}

	private void createViewComponents() {
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