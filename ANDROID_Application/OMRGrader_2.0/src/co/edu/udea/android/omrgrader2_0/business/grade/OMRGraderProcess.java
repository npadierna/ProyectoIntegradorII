package co.edu.udea.android.omrgrader2_0.business.grade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	private File sessionDirectoryFile;
	private File referenceExamImageFile;
	private List<File> studentsExamsImagesFilesList;

	private AsyncTask<Object, Void, Integer> examImageUploaderAsyncTask;
	private AsyncTask<Object, Void, Object[]> graderSessionAsyncTask;

	private SharedPreferences sharedPreferences;

	public OMRGraderProcess(Context context, String graderSessionName)
			throws OMRGraderBusinessException {
		this.createComponents(context, graderSessionName);
	}

	public File getReferenceExamImageFile() {

		return (this.referenceExamImageFile);
	}

	public void setReferenceExamImageFile(File referenceExamImageFile) {
		this.referenceExamImageFile = referenceExamImageFile;
	}

	public List<File> getStudentsExamsImagesFilesList() {

		return (this.studentsExamsImagesFilesList);
	}

	public void setStudentsExamsImagesFilesList(
			List<File> studentsExamsImagesFilesList) {
		this.studentsExamsImagesFilesList = studentsExamsImagesFilesList;
	}

	public boolean createGraderSession() throws OMRGraderBusinessException {
		Integer returnedCodeResult = null;
		Object[] returnedValues = null;

		GraderSession returnedGraderSession = null;

		this.graderSessionAsyncTask = new GraderSessionAsyncTask();
		this.graderSessionAsyncTask.execute(new Object[] {
				GraderSessionAsyncTask.CREATE_GRADER_SESSION,
				this.graderSession });

		try {
			returnedValues = this.graderSessionAsyncTask.get();

			returnedCodeResult = (Integer) returnedValues[0];
			returnedGraderSession = (GraderSession) returnedValues[1];
		} catch (Exception e) {
			throw new OMRGraderBusinessException(
					"The application could not create the Grader Session with the Server",
					e);
		}

		if (returnedGraderSession != null) {
			this.graderSession = returnedGraderSession;
		}

		return ((returnedCodeResult == GraderSessionAsyncTask.GRADER_SESSION_OK) && (returnedGraderSession != null));
	}

	public boolean finishGraderSession() throws OMRGraderBusinessException {
		Integer returnedCodeResult = null;
		Object[] returnedValues = null;

		GraderSession returnedGraderSession = null;

		this.graderSessionAsyncTask = new GraderSessionAsyncTask();
		this.graderSessionAsyncTask.execute(new Object[] {
				GraderSessionAsyncTask.FINISH_GRADER_SESSION,
				this.graderSession });

		try {
			returnedValues = this.graderSessionAsyncTask.get();

			returnedCodeResult = (Integer) returnedValues[0];
			returnedGraderSession = (GraderSession) returnedValues[1];
		} catch (Exception e) {
			throw new OMRGraderBusinessException(
					"The application could not finish the Grader Session with the Server",
					e);
		}

		return ((returnedCodeResult == GraderSessionAsyncTask.GRADER_SESSION_OK) && (returnedGraderSession != null));
	}

	public boolean uploadReferenceExamImage() throws OMRGraderBusinessException {
		Integer returnedValue = null;

		Bitmap referenceExamImageBitMap = BitmapFactory.decodeFile(this
				.getReferenceExamImageFile().getAbsolutePath());

		this.examImageUploaderAsyncTask = new ExamImageUploaderAsyncTask();
		this.examImageUploaderAsyncTask.execute(new Object[] {
				ExamImageUploaderAsyncTask.UPLOAD_REFERENCE_EXAM_IMAGE,
				this.graderSession, referenceExamImageBitMap });

		try {
			returnedValue = this.examImageUploaderAsyncTask.get();
		} catch (Exception e) {
			throw new OMRGraderBusinessException(
					String.format(
							"The application could not upload the Reference Exam Image: %s",
							this.getReferenceExamImageFile().getAbsoluteFile()),
					e);
		}

		return (returnedValue == ExamImageUploaderAsyncTask.SUCCESS_UPLOADING);
	}

	public boolean[] uploadStudentExamImage() throws OMRGraderBusinessException {
		boolean[] successfulUploadings = new boolean[this
				.getStudentsExamsImagesFilesList().size()];

		File studentExamImageFile = null;
		Integer returnedValue = null;

		Bitmap studentExamImageBitMap = null;

		for (int position = 0; position < this
				.getStudentsExamsImagesFilesList().size(); position++) {
			studentExamImageFile = this.getStudentsExamsImagesFilesList().get(
					position);
			studentExamImageBitMap = BitmapFactory
					.decodeFile(studentExamImageFile.getAbsolutePath());

			this.examImageUploaderAsyncTask = new ExamImageUploaderAsyncTask();
			this.examImageUploaderAsyncTask.execute(new Object[] {
					ExamImageUploaderAsyncTask.UPLOAD_STUDENT_EXAM_IMAGE,
					this.graderSession, studentExamImageBitMap,
					Integer.valueOf(position) });

			try {
				returnedValue = this.examImageUploaderAsyncTask.get();

				successfulUploadings[position] = (returnedValue == ExamImageUploaderAsyncTask.SUCCESS_UPLOADING);
			} catch (Exception e) {
				// throw new OMRGraderBusinessException(
				// String.format(
				// "The application could not upload the Student Exam Image: %s",
				// studentExamImageFile.getAbsoluteFile()), e);
			}
		}

		return (successfulUploadings);
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
		this.graderSession.getGraderSessionPK().setSessionName(
				graderSessionName);

		this.graderSession
				.setApprovalPercentage(Float.valueOf(this.sharedPreferences.getString(
						context.getString(R.string.percentage_shared_preference_key),
						context.getString(R.string.percentage_default_shared_preference))));
		this.graderSession
				.setDecimalPrecision(this.sharedPreferences.getString(
						context.getString(R.string.grade_precision_shared_preference_key),
						String.valueOf(context
								.getResources()
								.getInteger(
										R.integer.grade_precision_default_shared_preference))));
		// FIXME: What's about the Email?
		this.graderSession
				.getGraderSessionPK()
				.setElectronicMail(
						this.sharedPreferences.getString(
								context.getString(R.string.email_shared_preference_key),
								null));
		this.graderSession
				.setMaximumGrade(Float.valueOf(this.sharedPreferences.getString(
						context.getString(R.string.grader_values_shared_preference_key),
						context.getString(R.string.grader_values_default_shared_preference))));
	}
}