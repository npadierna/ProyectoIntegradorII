package co.edu.udea.android.omrgrader2_0.activity.configuration;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.activity.session.MainSessionActivity;
import co.edu.udea.android.omrgrader2_0.util.EMailAccountManager;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class InitialActivity extends Activity {

	private static final String TAG = InitialActivity.class.getSimpleName();

	private List<CharSequence> eMailAccountsList;
	private String eMailAccountSelected;
	private String maximumGradeSelected;

	private AlertDialog.Builder errorAlertDialogBuilder;
	private CheckBox deleteImagesCheckBox;
	private EditText eMailEditText;
	private Spinner eMailListSpinner;
	private Spinner maximumGradeSpinner;

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_initial);

		this.createViewComponents();
	}

	public void onSetInitialConfiguration(View view) {
		if (this.eMailAccountsList.isEmpty()) {
			this.eMailAccountSelected = this.eMailEditText.getText().toString();

			if (!RegexValidator.isValidEMail(this.eMailAccountSelected)) {
				this.errorAlertDialogBuilder.create().show();

				return;
			}
		}

		this.onPersistInitialConfiguration();

		super.startActivity(new Intent(super.getApplicationContext(),
				MainSessionActivity.class));
	}

	private void createViewComponents() {
		Log.v(TAG, "Creating the View Components for the Activity.");

		this.errorAlertDialogBuilder = new AlertDialog.Builder(this);
		this.errorAlertDialogBuilder
				.setMessage(R.string.invalid_email_account_alert_dialog_message);
		this.errorAlertDialogBuilder
				.setTitle(R.string.invalid_email_account_alert_dialog_title);
		this.errorAlertDialogBuilder.setPositiveButton(
				R.string.accept_button_label, null);

		this.eMailEditText = (EditText) super
				.findViewById(R.id.email_edit_text);

		this.deleteImagesCheckBox = (CheckBox) super
				.findViewById(R.id.delete_images_check_box);

		this.eMailAccountsList = EMailAccountManager.findAllEMailsAccount(super
				.getApplicationContext());

		this.eMailListSpinner = (Spinner) super
				.findViewById(R.id.email_list_spinner);

		if (!this.eMailAccountsList.isEmpty()) {
			SpinnerAdapter eMailSpinnerAdapter = new ArrayAdapter<CharSequence>(
					super.getApplicationContext(),
					android.R.layout.simple_spinner_dropdown_item,
					this.eMailAccountsList);

			this.eMailListSpinner.setAdapter(eMailSpinnerAdapter);
			this.eMailListSpinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override()
						public void onItemSelected(AdapterView<?> parente,
								View view, int position, long id) {
							eMailAccountSelected = eMailAccountsList.get(
									position).toString();
						}

						@Override()
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
			this.eMailListSpinner.setVisibility(View.VISIBLE);
		} else {
			this.eMailEditText.setVisibility(View.VISIBLE);
		}

		SpinnerAdapter maximumGradeSpinnerAdapter = new ArrayAdapter<CharSequence>(
				super.getApplicationContext(),
				android.R.layout.simple_spinner_dropdown_item, super
						.getResources().getStringArray(
								R.array.grader_values_shared_preference_array));

		this.maximumGradeSpinner = (Spinner) super
				.findViewById(R.id.maximum_grade_spinner);
		this.maximumGradeSpinner.setAdapter(maximumGradeSpinnerAdapter);
		this.maximumGradeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override()
					public void onItemSelected(AdapterView<?> parente,
							View view, int position, long id) {
						maximumGradeSelected = getResources().getStringArray(
								R.array.grader_values_shared_preference_array)[position];
					}

					@Override()
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
	}

	private void onPersistInitialConfiguration() {
		Log.v(TAG, "Saving the Initial Configuration for the Application.");

		SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager
				.getDefaultSharedPreferences(super.getApplicationContext())
				.edit();

		sharedPreferencesEditor.putBoolean(
				super.getString(R.string.delete_images_shared_preference_key),
				this.deleteImagesCheckBox.isChecked());
		sharedPreferencesEditor.putString(
				super.getString(R.string.grader_values_shared_preference_key),
				this.maximumGradeSelected);
		sharedPreferencesEditor.putString(
				super.getString(R.string.email_shared_preference_key),
				this.eMailAccountSelected);

		sharedPreferencesEditor.commit();
	}
}