package co.edu.udea.android.omrgrader2_0.activity.session.preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.util.EMailAccountManager;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionPreferenceFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private AlertDialog.Builder errorAlertDialogBuilder;

	private SharedPreferences sharedPreferences;

	@Override()
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.addPreferencesFromResource(R.xml.shared_preference_grader_setting);

		this.createViewComponents();
	}

	@Override()
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (super.getActivity() != null) {
			if (key.equals(super.getActivity().getString(
					R.string.email_shared_preference_key))) {
				String newEMailValue = sharedPreferences.getString(key, null);

				if (!RegexValidator.isValidEMail(newEMailValue)) {
					this.errorAlertDialogBuilder.create().show();

					this.onSetDefaultEMail();
				}
			}
		}
	}

	@Override()
	public void onStart() {
		super.onStart();

		this.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(super.getActivity()
						.getApplicationContext());
		this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	private void createViewComponents() {
		this.errorAlertDialogBuilder = new AlertDialog.Builder(
				super.getActivity());
		this.errorAlertDialogBuilder.setMessage(super.getActivity().getString(
				R.string.invalid_email_alert_dialog_message));
		this.errorAlertDialogBuilder.setTitle(super.getActivity().getString(
				R.string.invalid_email_alert_dialog_title));
		this.errorAlertDialogBuilder.setPositiveButton(
				R.string.accept_button_label,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						getActivity().finish();
					}
				});
	}

	private void onSetDefaultEMail() {
		SharedPreferences.Editor sharedPreferencesEditor = this.sharedPreferences
				.edit();

		sharedPreferencesEditor.putString(
				super.getActivity().getString(
						R.string.email_shared_preference_key),
				EMailAccountManager
						.findAllEMailsAccount(
								super.getActivity().getApplicationContext())
						.get(0).toString());

		sharedPreferencesEditor.commit();
	}
}