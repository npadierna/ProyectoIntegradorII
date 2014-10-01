package co.edu.udea.android.omrgrader2_0.activity.session.preference;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionPreferenceActivity extends Activity {

	private static final String PREFERENCE_FRAGMENT_KEY = "Preference Fragment for Main Session Actvity.";

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			PreferenceFragment preferenceFragment = new MainSessionPreferenceFragment();

			super.getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, preferenceFragment,
							PREFERENCE_FRAGMENT_KEY).commit();
		}
	}
}