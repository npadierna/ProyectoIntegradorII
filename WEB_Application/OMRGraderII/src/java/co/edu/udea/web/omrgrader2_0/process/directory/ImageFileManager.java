package co.edu.udea.web.omrgrader2_0.process.directory;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Repository()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class ImageFileManager {

    public static final String IMAGE_FILE_SUFFIX = ".jpeg";
    public static final String REFERENCE_EXAM_IMAGE_FILE_DEFAULT_NAME = "referenceExamImageFile";
    public static final String STUDENT_EXAM_IMAGE_FILE_DEFAULT_NAME = "studentExamImageFile_";
    public static final String STUDENT_EXAM_IMAGE_FILE_DIRECTORY_PATH = "Students_Exams_Images";
    public static final String UPLOADED_FILE_DIRECTORY_PATH = "/tmp/OMRGrader/";

    public ImageFileManager() {
        super();
    }

    public String buildReferenceExamImageFileDefaultName()
            throws OMRGraderProcessException {

        return (this.buildExamImageFileName(
                REFERENCE_EXAM_IMAGE_FILE_DEFAULT_NAME));
    }

    public String buildStudentExamImageFileName(String imageFileId)
            throws OMRGraderProcessException {
        if (TextUtil.isEmpty(imageFileId)) {
            throw new OMRGraderProcessException(
                    "The Exam Image File Name can not be null, empty or too short.");
        }

        return (this.buildExamImageFileName(
                STUDENT_EXAM_IMAGE_FILE_DEFAULT_NAME.concat(imageFileId)));
    }

    public String buildUploadedFileDirectoryPath(String directoryStorageId,
            boolean isStudentExamImageFileDirectoryPath)
            throws OMRGraderProcessException {
        if (TextUtil.isEmpty(directoryStorageId)) {
            throw new OMRGraderProcessException(
                    "The Directory Storage Id can not be null or empty.");
        }

        StringBuilder uploadedFileDirectoryPath =
                new StringBuilder(UPLOADED_FILE_DIRECTORY_PATH);
        uploadedFileDirectoryPath.append(directoryStorageId)
                .append(File.separator);

        if (isStudentExamImageFileDirectoryPath) {
            uploadedFileDirectoryPath = uploadedFileDirectoryPath
                    .append(STUDENT_EXAM_IMAGE_FILE_DIRECTORY_PATH)
                    .append(File.separator);
        }

        return (uploadedFileDirectoryPath.toString());
    }

    public void createStorageDirectory(String storageDirectoryPathName)
            throws OMRGraderProcessException {
        if (TextUtil.isEmpty(storageDirectoryPathName)) {
            throw new OMRGraderProcessException(
                    "The Storage Directory Path Name is empty or invalid.");
        }

        StringBuilder absoluteStorageDirectoryPath =
                new StringBuilder(UPLOADED_FILE_DIRECTORY_PATH);
        absoluteStorageDirectoryPath.append(storageDirectoryPathName.trim())
                .append(File.separator)
                .append(STUDENT_EXAM_IMAGE_FILE_DIRECTORY_PATH);

        File storageDirectoryPathFile = new File(
                absoluteStorageDirectoryPath.toString());
        if ((storageDirectoryPathFile.exists())
                || (!storageDirectoryPathFile.mkdirs())) {
            throw new OMRGraderProcessException(
                    "The Storage Directory Path Name could not be created.");
        }
    }

    public boolean deleteStorageDirectory(String storageDirectoryPathName)
            throws OMRGraderProcessException {
        if (TextUtil.isEmpty(storageDirectoryPathName)) {
            throw new OMRGraderProcessException(
                    "The Storage Directory Path Name is empty or invalid.");
        }

        File rootStorageDirectoryPathFile = new File(UPLOADED_FILE_DIRECTORY_PATH
                .concat(storageDirectoryPathName.trim()));
        File studentsExamsImagesStorageDirectoryPathFile = new File(
                rootStorageDirectoryPathFile,
                STUDENT_EXAM_IMAGE_FILE_DIRECTORY_PATH);

        if ((!rootStorageDirectoryPathFile.exists())
                || (!rootStorageDirectoryPathFile.canWrite())) {
            throw new OMRGraderProcessException(
                    "The Storage Directory Path Name could not be deleted.");
        }

        boolean allWasDeleted = true;
        if (studentsExamsImagesStorageDirectoryPathFile.exists()) {
            File[] studentsExamsImagesFiles =
                    studentsExamsImagesStorageDirectoryPathFile.listFiles();
            for (File studentExamImageFile : studentsExamsImagesFiles) {
                if (!studentExamImageFile.delete()) {
                    allWasDeleted = false;
                }
            }
        }

        File[] examsImagesFiles = rootStorageDirectoryPathFile.listFiles();
        for (File examImageFile : examsImagesFiles) {
            if (!examImageFile.delete()) {
                allWasDeleted = false;
            }
        }

        return ((rootStorageDirectoryPathFile.delete()) && (allWasDeleted));
    }

    public void saveExamImageFile(
            InputStream examImageFileInputStream, String fileDirectoryPath)
            throws OMRGraderProcessException {
        if ((TextUtil.isEmpty(fileDirectoryPath))
                || (!fileDirectoryPath.endsWith(IMAGE_FILE_SUFFIX))) {
            throw new OMRGraderProcessException(
                    "The Full File Directory Path is empty or invalid.");
        }

        if (examImageFileInputStream == null) {
            throw new OMRGraderProcessException(
                    "The Image's Stream is null or invalid.");
        }

        int readBytes;
        byte[] examImageBytes = new byte[1024];
        OutputStream examImageFileOutputStream = null;

        try {
            examImageFileOutputStream = new FileOutputStream(
                    new File(fileDirectoryPath));

            while ((readBytes = examImageFileInputStream.read(examImageBytes))
                    != -1) {
                examImageFileOutputStream.write(examImageBytes, 0, readBytes);
            }

            examImageFileOutputStream.flush();
            examImageFileOutputStream.close();
        } catch (Exception ex1) {
            if (examImageFileOutputStream != null) {
                try {
                    examImageFileOutputStream.close();
                } catch (Exception ex2) {
                    throw new OMRGraderProcessException(
                            "The application could not close the OutputStream buffer.",
                            ex2);
                }
            }

            throw new OMRGraderProcessException(
                    "The application could not copy the Image to disk.",
                    ex1);
        }
    }

    private String buildExamImageFileName(String rawExamImageFileName)
            throws OMRGraderProcessException {
        if ((TextUtil.isEmpty(rawExamImageFileName))
                || (rawExamImageFileName.length() <= IMAGE_FILE_SUFFIX.length())) {
            throw new OMRGraderProcessException(
                    "The Exam Image File Name can not be null, empty or too short.");
        }

        return (rawExamImageFileName.concat(IMAGE_FILE_SUFFIX));
    }

    public long buildStorageDirectoryPathName(GraderSession graderSession,
            boolean isCreation) {
        if (isCreation) {
            graderSession.setRequest(Long.valueOf(new Date().getTime()));
        }

        return (Math.abs(graderSession.getGraderSessionPK().getElectronicMail()
                .hashCode()
                + graderSession.getGraderSessionPK().getSessionName().hashCode()
                + graderSession.getRequest().hashCode()));
    }
}