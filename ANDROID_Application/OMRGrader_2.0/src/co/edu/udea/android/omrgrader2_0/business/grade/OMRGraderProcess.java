package co.edu.udea.android.omrgrader2_0.business.grade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.business.directory.BaseStorageDirectory;
import co.edu.udea.android.omrgrader2_0.business.exception.OMRGraderBusinessException;
import co.edu.udea.android.omrgrader2_0.business.grade.asynctask.ExamImageUploaderAsyncTask;
import co.edu.udea.android.omrgrader2_0.business.grade.asynctask.GraderSessionAsyncTask;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class OMRGraderProcess {

	private BaseStorageDirectory baseStorageDirectory;
	private GraderSession graderSession;

	private File referenceExamImageFile;
	private List<File> studentsExamsImagesFilesList;

	private AsyncTask<Object, Void, Integer> examImageUploaderAsyncTask;
	private AsyncTask<Object, Void, Object[]> graderSessionAsyncTask;

	private SharedPreferences sharedPreferences;

	public OMRGraderProcess(Context context, String graderSessionName)
			throws OMRGraderBusinessException {
		this.createComponents(context, graderSessionName);
	}

	public void createGraderSession() {
		this.graderSessionAsyncTask = new GraderSessionAsyncTask();
		this.graderSessionAsyncTask.execute(new Object[] {
				GraderSessionAsyncTask.CREATE_GRADER_SESSION,
				this.graderSession });

		try {
			Object[] returnedValues = this.graderSessionAsyncTask.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishGraderSession() {
		this.graderSessionAsyncTask = new GraderSessionAsyncTask();
		this.graderSessionAsyncTask.execute(new Object[] {
				GraderSessionAsyncTask.FINISH_GRADER_SESSION,
				this.graderSession });

		try {
			Object[] returnedValues = this.graderSessionAsyncTask.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadReferenceExamImage() {
		this.examImageUploaderAsyncTask = new ExamImageUploaderAsyncTask();
		this.examImageUploaderAsyncTask.execute(new Object[] {
				ExamImageUploaderAsyncTask.UPLOAD_REFERENCE_EXAM_IMAGE,
				this.graderSession, null });

		try {
			Integer returnedValue = this.examImageUploaderAsyncTask.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadStudentExamImage() {
		this.examImageUploaderAsyncTask = new ExamImageUploaderAsyncTask();
		this.examImageUploaderAsyncTask.execute(new Object[] {
				ExamImageUploaderAsyncTask.UPLOAD_STUDENT_EXAM_IMAGE,
				this.graderSession, null, null });

		try {
			Integer returnedValue = this.examImageUploaderAsyncTask.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createComponents(Context context, String graderSessionName)
			throws OMRGraderBusinessException {
		if ((context == null) || (TextUtils.isEmpty(graderSessionName))) {
			throw new OMRGraderBusinessException(
					"The instance was not created dued the parameters are null, empty or invalid");
		}

		graderSessionName = graderSessionName.trim();
		if (TextUtils.isEmpty(graderSessionName)) {
			throw new OMRGraderBusinessException(
					"The instance was not created dued the parameters are null, empty or invalid");
		}

		this.studentsExamsImagesFilesList = new ArrayList<File>();

		this.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		this.baseStorageDirectory = BaseStorageDirectory.getInstance(context);

		this.graderSession = new GraderSession();
		this.graderSession.setGraderSessionName(graderSessionName);

		this.graderSession
				.setApprovalPercentage(Float.valueOf(this.sharedPreferences.getString(
						context.getString(R.string.percentage_shared_preference_key),
						context.getString(R.string.percentage_default_shared_preference))));
		this.graderSession
				.setDecimalPrecision(Integer.valueOf(this.sharedPreferences.getString(
						context.getString(R.string.grade_precision_shared_preference_key),
						String.valueOf(context
								.getResources()
								.getInteger(
										R.integer.grade_precision_default_shared_preference)))));
		// FIXME: What's about the Email?
		this.graderSession.seteMailAccount("");
		this.graderSession
				.setMaximumGrade(Float.valueOf(this.sharedPreferences.getString(
						context.getString(R.string.grader_values_shared_preference_key),
						context.getString(R.string.grader_values_default_shared_preference))));
	}
}