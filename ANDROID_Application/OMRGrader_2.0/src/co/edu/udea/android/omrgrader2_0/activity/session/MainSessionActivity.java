package co.edu.udea.android.omrgrader2_0.activity.session;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.activity.about.AboutUsActivity;
import co.edu.udea.android.omrgrader2_0.activity.session.preference.MainSessionPreferenceActivity;
import co.edu.udea.android.omrgrader2_0.business.pin.PingAsyncTask;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainSessionActivity extends Activity {

	private static final String TAG = MainSessionActivity.class.getSimpleName();

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main_session);
	}

	@Override()
	public boolean onCreateOptionsMenu(Menu menu) {
		super.getMenuInflater().inflate(R.menu.menu_main, menu);

		return (true);
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

		super.startActivity(new Intent(super.getApplicationContext(),
				ImageTakerActivity.class));
	}
}