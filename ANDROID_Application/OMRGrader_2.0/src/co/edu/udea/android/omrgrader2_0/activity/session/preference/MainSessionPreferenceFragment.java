package co.edu.udea.android.omrgrader2_0.activity.session.preference;

import java.util.List;

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
			if (key.equals(super
					.getString(R.string.email_shared_preference_key))) {
				String newEMailValue = sharedPreferences.getString(key, null);

				if (!RegexValidator.isValidEMail(newEMailValue)) {
					this.errorAlertDialogBuilder
							.setMessage(R.string.invalid_email_alert_dialog_message);
					this.errorAlertDialogBuilder
							.setTitle(R.string.invalid_email_alert_dialog_title);
					this.errorAlertDialogBuilder.create().show();

					this.onSetDefaultEMail();
				}
			} else if (key.equals(super
					.getString(R.string.percentage_shared_preference_key))) {
				float maximumPercentageValue = Float
						.valueOf(super
								.getString(R.string.percentage_maximum_shared_preference));
				float minimumPercentageValue = Float
						.valueOf(super
								.getString(R.string.percentage_minimum_shared_preference));

				this.errorAlertDialogBuilder
						.setMessage(R.string.invalid_percentage_alert_dialog_message);
				this.errorAlertDialogBuilder
						.setTitle(R.string.invalid_percentage_alert_dialog_title);

				try {
					float newPercentageValue = Float.valueOf(sharedPreferences
							.getString(key, null));
					if ((newPercentageValue < minimumPercentageValue)
							|| (newPercentageValue > maximumPercentageValue)) {
						this.errorAlertDialogBuilder.create().show();

						this.onSetDefaultPercentage();
					}
				} catch (NumberFormatException ex) {
					this.errorAlertDialogBuilder.create().show();

					this.onSetDefaultPercentage();
				}
			} else if (key.equals(super
					.getString(R.string.grade_precision_shared_preference_key))) {
				int maximumPrecisionvalue = super.getResources().getInteger(
						R.integer.grade_precision_maximum_shared_preference);
				int minimumPrecisionValue = super.getResources().getInteger(
						R.integer.grade_precision_minimum_shared_preference);

				this.errorAlertDialogBuilder
						.setMessage(R.string.invalid_grade_precision_dialog_message);
				this.errorAlertDialogBuilder
						.setTitle(R.string.invalid_grade_precision_dialog_title);

				try {
					int newGradePrecisionValue = Integer
							.valueOf(sharedPreferences.getString(key, null));

					if ((newGradePrecisionValue < minimumPrecisionValue)
							|| (newGradePrecisionValue > maximumPrecisionvalue)) {
						this.errorAlertDialogBuilder.create().show();

						this.onSetDefaultGradePrecision();
					}
				} catch (NumberFormatException ex) {
					this.errorAlertDialogBuilder.create().show();

					this.onSetDefaultGradePrecision();
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
		List<CharSequence> electronicsMailList = EMailAccountManager
				.findAllEMailsAccount(super.getActivity()
						.getApplicationContext());
		CharSequence defaultElectronicMail = new String();

		if (!electronicsMailList.isEmpty()) {
			defaultElectronicMail = electronicsMailList.get(0);
		}

		sharedPreferencesEditor.putString(
				super.getString(R.string.email_shared_preference_key),
				defaultElectronicMail.toString());

		sharedPreferencesEditor.commit();
	}

	private void onSetDefaultPercentage() {
		SharedPreferences.Editor sharedPreferencesEditor = this.sharedPreferences
				.edit();

		sharedPreferencesEditor.putString(
				super.getString(R.string.percentage_shared_preference_key),
				super.getString(R.string.percentage_default_shared_preference));

		sharedPreferencesEditor.commit();
	}

	private void onSetDefaultGradePrecision() {
		SharedPreferences.Editor sharedPreferencesEditor = this.sharedPreferences
				.edit();

		sharedPreferencesEditor
				.putString(
						super.getString(R.string.grade_precision_shared_preference_key),
						String.valueOf(super
								.getResources()
								.getInteger(
										R.integer.grade_precision_default_shared_preference)));

		sharedPreferencesEditor.commit();
	}
}