package co.edu.udea.android.omrgrader2_0.business.directory;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import co.edu.udea.android.omrgrader2_0.R;
import co.edu.udea.android.omrgrader2_0.business.exception.OMRGraderBusinessException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class BaseStorageDirectory {

	private File baseStorageDirectoryFile;

	private Context context;

	private static BaseStorageDirectory baseStorageDirectory = null;

	private BaseStorageDirectory(Context context)
			throws OMRGraderBusinessException {
		this.context = context;

		this.creatBaseStorage();
	}

	public static synchronized BaseStorageDirectory getInstance(Context context)
			throws OMRGraderBusinessException {
		if (baseStorageDirectory == null) {
			baseStorageDirectory = new BaseStorageDirectory(context);
		}

		return (baseStorageDirectory);
	}

	public File[] createDirectoriesFilesForSession(
			String sessionNameCharSequence) throws OMRGraderBusinessException {
		StringBuilder pathStringBuilder = new StringBuilder();
		File sessionDirectoryFile = null;
		File studentsImagesDirectoryFile = null;

		pathStringBuilder
				.append(this.baseStorageDirectoryFile.getAbsolutePath())
				.append(File.separator).append(sessionNameCharSequence);
		sessionDirectoryFile = new File(pathStringBuilder.toString());
		boolean sessionDirectoryFileOk = sessionDirectoryFile.mkdirs();

		pathStringBuilder.append(File.separator).append(
				this.context.getResources().getString(
						R.string.base_album_students));
		studentsImagesDirectoryFile = new File(pathStringBuilder.toString());
		boolean studentImagesDirectoryFileOk = studentsImagesDirectoryFile
				.mkdirs();

		if ((!sessionDirectoryFileOk) || (!studentImagesDirectoryFileOk)) {
			throw new OMRGraderBusinessException(
					"The creation of the needed directories files for the storage the session's images was not successfully.");
		}

		return (new File[] { sessionDirectoryFile, studentsImagesDirectoryFile });
	}

	public boolean deleteFilesInSession(String sessionNameCharSequence) {
		boolean allWasDeleted = true;
		StringBuilder pathStringBuilder = new StringBuilder();
		File sessionDirectoryFile = null;
		File studentsImagesDirectoryFile = null;

		pathStringBuilder
				.append(this.baseStorageDirectoryFile.getAbsolutePath())
				.append(File.separator).append(sessionNameCharSequence);
		sessionDirectoryFile = new File(pathStringBuilder.toString());

		pathStringBuilder.append(File.separator).append(
				this.context.getResources().getString(
						R.string.base_album_students));
		studentsImagesDirectoryFile = new File(pathStringBuilder.toString());

		if (studentsImagesDirectoryFile.exists()) {
			File[] studentsExamsImagesFiles = studentsImagesDirectoryFile
					.listFiles();

			for (File studentExamImageFile : studentsExamsImagesFiles) {
				if (!studentExamImageFile.delete()) {
					allWasDeleted = false;
				}
			}
		}

		File[] examsImagesFiles = sessionDirectoryFile.listFiles();
		for (File examImageFile : examsImagesFiles) {
			if (!examImageFile.delete()) {
				allWasDeleted = false;
			}
		}

		return ((sessionDirectoryFile.delete()) && (allWasDeleted));
	}

	private File buildBaseStorageFile() {
		StringBuilder storageDirectoryPath = new StringBuilder(Environment
				.getExternalStorageDirectory().getAbsolutePath());

		storageDirectoryPath.append(File.separator)
				.append(this.context.getResources().getString(
						R.string.base_album_root));

		return (new File(storageDirectoryPath.toString()));
	}

	private void creatBaseStorage() throws OMRGraderBusinessException {
		File storageDirectoryFile = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			storageDirectoryFile = this.buildBaseStorageFile();

			if (storageDirectoryFile == null) {
				throw new OMRGraderBusinessException(
						"The Base Storage Directory File was not created successfully.");
			} else {
				storageDirectoryFile.mkdirs();

				this.baseStorageDirectoryFile = storageDirectoryFile;
			}
		}
	}
}