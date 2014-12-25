package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.image.model.Exam;
import co.edu.udea.web.omrgrader2_0.process.image.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInformation;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.OMRGraderProcess;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSessionThread extends Thread {

    private static final String TAG = GraderSessionThread.class.getSimpleName();
    private IGraderSessionDAO graderSessionDAO;
    private IThreadNotifier threadNotifier;
    private ImageFileManager imageFileManagement;
    private GraderSession graderSession;
    private OMRGraderProcess oMRGraderProcess;
    private long key;

    public GraderSessionThread(long key, GraderSession graderSession,
            OMRGraderProcess oMRGraderProcess,
            IGraderSessionDAO graderSessionDAO,
            ImageFileManager imageFileManagement,
            IThreadNotifier threadNotifier) {
        this.key = key;
        this.graderSession = graderSession;
        this.oMRGraderProcess = oMRGraderProcess;
        this.graderSessionDAO = graderSessionDAO;
        this.imageFileManagement = imageFileManagement;
        this.threadNotifier = threadNotifier;
    }

    public GraderSession getGraderSession() {

        return (this.graderSession);
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    public long getKey() {

        return (this.key);
    }

    public void setKey(long key) {
        this.key = key;
    }

    @Override()
    public void run() {
        ExamResult referenceExamResult;
        List<ExamResult> studentsExamsResultsList;

        long storageDirectoryPathName = this.imageFileManagement.
                buildStorageDirectoryPathName(this.getGraderSession());

        try {
            File[] referenceExamImageFiles = new File(this.imageFileManagement.
                    buildUploadedFileDirectoryPath(String.valueOf(
                    storageDirectoryPathName), false)).listFiles(
                    this.imageFileManagement);
            File[] studentExamsImagesFiles = new File(this.imageFileManagement.
                    buildUploadedFileDirectoryPath(String.valueOf(
                    storageDirectoryPathName), true)).listFiles(
                    this.imageFileManagement);

            if ((referenceExamImageFiles != null)
                    && (referenceExamImageFiles.length > 0)
                    && (studentExamsImagesFiles != null)
                    && (studentExamsImagesFiles.length > 0)) {
                Exam exam = this.oMRGraderProcess.extractFeatures(
                        referenceExamImageFiles[0].getAbsolutePath());
                exam = this.oMRGraderProcess.executeExamProcessing(
                        this.oMRGraderProcess.getOnlyLogosTemplateExam(), exam,
                        true, null, null, null, null);
                exam.setGrayScaledImageMat(null);
                exam.setImageDescriptorsMat(null);
                exam.setImageMatOfKeyPoints(null);

                referenceExamResult = new ExamResult(exam);
                studentsExamsResultsList = new ArrayList<>(studentExamsImagesFiles.length);

                ExamResult examResult;
                for (File studentExamImageFile : studentExamsImagesFiles) {
                    exam = this.oMRGraderProcess.extractFeatures(
                            studentExamImageFile.getAbsolutePath());
                    exam = this.oMRGraderProcess.executeExamProcessing(
                            this.oMRGraderProcess.getOnlyLogosTemplateExam(),
                            exam, false, null, null, null, null);
                    exam.setGrayScaledImageMat(null);
                    exam.setImageDescriptorsMat(null);
                    exam.setImageMatOfKeyPoints(null);

                    examResult = new ExamResult(exam);

                    studentsExamsResultsList.add(examResult);
                }

                SheetFileInformation sheetFileInformation = new SheetFileInformation(
                        this.graderSession, referenceExamResult,
                        studentsExamsResultsList);
                // TODO: Proceder a crear el archivo con la información y a
                // enviar el correo electrónico.
            }

            boolean eliminationResult = this.imageFileManagement
                    .deleteStorageDirectory(String.valueOf(
                    storageDirectoryPathName));
            this.graderSessionDAO.delete(this.getGraderSession()
                    .getGraderSessionPK());

            this.executeNotification();
        } catch (OMRGraderProcessException | OMRGraderPersistenceException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Grader Session Thread was trying to manage a Grader Session.",
                    e);
        }
    }

    private void executeNotification() {
        this.threadNotifier.notifyEvent(new Object[]{this});
    }
}