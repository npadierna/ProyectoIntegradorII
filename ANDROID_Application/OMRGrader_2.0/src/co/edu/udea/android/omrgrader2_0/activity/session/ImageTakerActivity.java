package co.edu.udea.android.omrgrader2_0.activity.session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import co.edu.udea.android.omrgrader2_0.R;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ImageTakerActivity extends Activity {

	private static final String TAG = ImageTakerActivity.class.getSimpleName();

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_image_taker);
	}

	public void onTakeReferenceExamImage(View view) {
		Log.v(TAG, "Taking the Reference Exam Image.");
	}
}