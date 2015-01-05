package co.edu.udea.android.omrgrader2_0.activity.session;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.business.exception.OMRGraderBusinessException;
import co.edu.udea.android.omrgrader2_0.business.grade.OMRGraderProcess;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ImageTakerActivity extends Activity {

	private static final String TAG = ImageTakerActivity.class.getSimpleName();

	private static final int REQUEST_FOR_TAKING_REFERENCE_EXAM = 1;
	private static final int REQUEST_FOR_TAKING_STUDENT_EXAM = 2;

	public static final String SESSION_NAME_KEY = "Key for the Session Name";

	private OMRGraderProcess omrGraderProcess;

	private File newExamPictureFile;
	private URL webServiceURL;

	private AlertDialog.Builder alertDialogBuilder;
	private Button startTakingStudentExamsImagesButton;
	private Button gradeExamsButton;
	private ProgressDialog uploaderProgressDialog;

	@Override()
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_FOR_TAKING_REFERENCE_EXAM:
			if (resultCode == Activity.RESULT_OK) {
				this.omrGraderProcess
						.setReferenceExamImageFile(this.newExamPictureFile);

				this.addFileToGalleryImages(this.newExamPictureFile);

				this.startTakingStudentExamsImagesButton.setEnabled(true);
			}
			break;

		case REQUEST_FOR_TAKING_STUDENT_EXAM:
			if (resultCode == Activity.RESULT_OK) {
				this.addFileToGalleryImages(this.newExamPictureFile);

				this.gradeExamsButton.setEnabled(true);
			}
			break;
		}
	}

	@Override()
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_image_taker);

		this.createViewComponents();
		this.createWebServiceURL();
	}

	@Override()
	protected void onStart() {
		super.onStart();

		if (this.omrGraderProcess == null) {
			Bundle bundle = super.getIntent().getExtras();

			try {
				this.omrGraderProcess = new OMRGraderProcess(
						super.getApplicationContext(), this.webServiceURL,
						(bundle.containsKey(SESSION_NAME_KEY)) ? bundle
								.getString(SESSION_NAME_KEY) : null);
			} catch (OMRGraderBusinessException e) {
				this.alertDialogBuilder.setPositiveButton(
						R.string.accept_button_label,
						new DialogInterface.OnClickListener() {

							@Override()
							public void onClick(DialogInterface dialog,
									int which) {
								ImageTakerActivity.this.finish();
							}
						});
				this.alertDialogBuilder.create().show();

				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	public void onGradeExams(View view) {
		Log.v(TAG, "Starting The Grade for Exams.");

		boolean[] uploadedStudentsExams = null;
		UploaderProgressDialogThread uploaderProgressDialogThread = new UploaderProgressDialogThread(
				this.uploaderProgressDialog);

		this.uploaderProgressDialog.show();
		uploaderProgressDialogThread.setRunning(true);
		uploaderProgressDialogThread.start();

		this.alertDialogBuilder.setPositiveButton(R.string.accept_button_label,
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int which) {
						ImageTakerActivity.this.finish();
					}
				});

		try {
			if (!this.omrGraderProcess.createGraderSession()) {
				this.alertDialogBuilder
						.setMessage(R.string.grader_session_no_created_alert_dialog_message);
				this.alertDialogBuilder
						.setTitle(R.string.grader_session_no_created_alert_dialog_title);
				this.alertDialogBuilder.create().show();
			}

			if (!this.omrGraderProcess.uploadReferenceExamImage()) {
				this.alertDialogBuilder
						.setMessage(R.string.reference_exam_image_no_uploaded_alert_dialog_message);
				this.alertDialogBuilder
						.setTitle(R.string.reference_exam_image_no_uploaded_alert_dialog_title);
				this.alertDialogBuilder.create().show();
			}

			uploadedStudentsExams = this.omrGraderProcess
					.uploadStudentExamImage();

			if (!this.omrGraderProcess.finishGraderSession()) {
				this.alertDialogBuilder
						.setMessage(R.string.grader_session_no_finished_alert_dialog_message);
				this.alertDialogBuilder
						.setTitle(R.string.grader_session_no_finished_alert_dialog_title);
				this.alertDialogBuilder.create().show();
			}
		} catch (OMRGraderBusinessException e) {
			this.alertDialogBuilder
					.setMessage(R.string.grader_session_uploading_error_alert_dialog_message);
			this.alertDialogBuilder
					.setTitle(R.string.grader_session_uploading_error_alert_dialog_title);
			this.alertDialogBuilder.create().show();

			Log.e(TAG, e.getMessage(), e);
		}

		this.omrGraderProcess.deleteExamsImagesFiles();

		uploaderProgressDialogThread.setRunning(false);

		this.resumeGraderSession(uploadedStudentsExams);
	}

	public void onStartTakingStudentsExamsImages(View view) {
		Log.v(TAG, "Start Taking Students Exams Images.");

		try {
			this.newExamPictureFile = this.createIntentForTakingPicture(
					super.getString(R.string.student_exam_picture_file_name),
					this.omrGraderProcess.getSessionStudentDirectoryFile(),
					REQUEST_FOR_TAKING_STUDENT_EXAM);
		} catch (IOException e) {
			this.alertDialogBuilder
					.setMessage(R.string.student_exam_image_no_taken_alert_dialog_message);
			this.alertDialogBuilder
					.setTitle(R.string.student_exam_image_no_taken_alert_dialog_title);
			this.alertDialogBuilder.create().show();

			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void onTakeReferenceExamImage(View view) {
		Log.v(TAG, "Taking the Reference Exam Image.");

		try {
			this.newExamPictureFile = this.createIntentForTakingPicture(
					super.getString(R.string.reference_exam_picture_file_name),
					this.omrGraderProcess.getSessionBaseDirectoryFile(),
					REQUEST_FOR_TAKING_REFERENCE_EXAM);
		} catch (IOException e) {
			this.alertDialogBuilder
					.setMessage(R.string.reference_exam_image_no_taken_alert_dialog_message);
			this.alertDialogBuilder
					.setTitle(R.string.reference_exam_image_no_taken_alert_dialog_title);
			this.alertDialogBuilder.create().show();

			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void addFileToGalleryImages(File pictureFile) {
		Uri uri = Uri.fromFile(pictureFile);

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(uri);

		super.sendBroadcast(intent);
	}

	private void createViewComponents() {
		this.startTakingStudentExamsImagesButton = (Button) super
				.findViewById(R.id.start_taking_student_exams_images_button);
		this.gradeExamsButton = (Button) super
				.findViewById(R.id.grade_exams_button);

		this.alertDialogBuilder = new AlertDialog.Builder(this);
		this.alertDialogBuilder
				.setMessage(R.string.grader_session_creation_failed_alert_dialog_message);
		this.alertDialogBuilder
				.setTitle(R.string.grader_session_creation_failed_alert_dialog_title);
		this.alertDialogBuilder.setPositiveButton(R.string.accept_button_label,
				null);

		this.uploaderProgressDialog = new ProgressDialog(this);
		this.uploaderProgressDialog.setMessage(super
				.getString(R.string.progress_uploader_message));
		this.uploaderProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.uploaderProgressDialog.setTitle(super
				.getString(R.string.progress_uploader_title));
		this.uploaderProgressDialog.setCancelable(false);
	}

	private File createIntentForTakingPicture(String pictureName,
			File pictureDestinationDirectoryFile, int requestCode)
			throws IOException {
		File takenPictureFile = File.createTempFile(pictureName,
				super.getString(R.string.extension_picture_sufix),
				pictureDestinationDirectoryFile);

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(takenPictureFile));

		super.startActivityForResult(takePictureIntent, requestCode);

		return (takenPictureFile);
	}

	private void createWebServiceURL() {
		try {
			this.webServiceURL = new URL(
					super.getString(R.string.application_server_protocol),
					super.getString(R.string.application_server_ip),
					Integer.valueOf(super
							.getString(R.string.application_server_port)),
					super.getString(R.string.application_server_context)
							.concat(File.separator)
							.concat(super
									.getString(R.string.application_server_web_services_context)));
		} catch (NumberFormatException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void resumeGraderSession(boolean[] uploadedStudentsExams) {
		int counter = 0;

		if (uploadedStudentsExams != null) {
			for (boolean b : uploadedStudentsExams) {
				if (b) {
					counter++;
				}
			}
		}

		this.alertDialogBuilder
				.setMessage(String.format(
						"%s %d.",
						super.getString(R.string.grader_session_results_alert_dialog_message),
						counter));
		this.alertDialogBuilder
				.setTitle(R.string.grader_session_results_alert_dialog_title);
		this.alertDialogBuilder.create().show();
	}
}