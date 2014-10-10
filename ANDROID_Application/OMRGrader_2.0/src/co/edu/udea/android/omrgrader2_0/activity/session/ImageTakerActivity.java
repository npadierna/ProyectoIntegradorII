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
import co.edu.udea.android.omrgrader2_0.business.exception.OMRGraderBusinessException;
import co.edu.udea.android.omrgrader2_0.business.grade.OMRGraderProcess;
import co.edu.udea.android.omrgrader2_0.business.grade.asynctask.ExamImageUploaderAsyncTask;
import co.edu.udea.android.omrgrader2_0.business.grade.asynctask.GraderSessionAsyncTask;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ImageTakerActivity extends Activity {

	private static final String TAG = ImageTakerActivity.class.getSimpleName();

	private OMRGraderProcess omrGraderProcess;

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_image_taker);

		this.createComponents();
	}

	public void onGradeExams(View view) {
		Log.v(TAG, "Starting The Grade for Exams.");
	}

	public void onStartTakingStudentsExamsImages(View view) {
		Log.v(TAG, "Start Taking Students Exams Images.");
	}

	public void onTakeReferenceExamImage(View view) {
		Log.v(TAG, "Taking the Reference Exam Image.");

		GraderSession graderSession = new GraderSession(60.0F, 100.0F, 3, null,
				"npadierna@gmail.com", "Session");
		Bitmap imageBitmap = BitmapFactory
				.decodeFile("/storage/sdcard0/DCIM/Camera/1398827742194.jpg");

		AsyncTask<Object, Void, Integer> examImageUploaderAsyncTask = new ExamImageUploaderAsyncTask();
		// examImageUploaderAsyncTask.execute(new Object[] { imageBitmap });

		AsyncTask<Object, Void, Object[]> graderSessionAsyncTask = new GraderSessionAsyncTask();
		graderSessionAsyncTask.execute(new Object[] {
				GraderSessionAsyncTask.CREATE_GRADER_SESSION, graderSession });

		try {
			// examImageUploaderAsyncTask.get();
			Object[] returns = graderSessionAsyncTask.get();

			Log.v(TAG, String.valueOf(returns.length));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void createComponents() {
		// FIXME: Think more about how to handle this exception.

		try {
			this.omrGraderProcess = new OMRGraderProcess(
					super.getApplicationContext(), "");
		} catch (OMRGraderBusinessException e) {
			e.printStackTrace();
		}
	}
}