package co.edu.udea.android.omrgrader2_0.activity.session;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.business.grade.ExamImageSenderAsyncTask;

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

		Bitmap imageBitmap = BitmapFactory.decodeFile("/storage/sdcard0/DCIM/Camera/1398827742194.jpg");
		AsyncTask<Object, Void, Integer> webServiceAsyncTask = new ExamImageSenderAsyncTask();

		webServiceAsyncTask.execute(new Object[] { imageBitmap });
		try {
			webServiceAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}