package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.email.exception.OMRGraderEmailException;
import co.edu.udea.web.omrgrader2_0.process.email.report.FileSheetReport;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.grade.ExamSessionComparator;
import co.edu.udea.web.omrgrader2_0.process.image.model.Exam;
import co.edu.udea.web.omrgrader2_0.process.email.report.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.email.report.model.FileSheetInformation;
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
    private EmailSender emailSender;
    private ExamSessionComparator examSessionComparator;
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
            EmailSender emailSender,
            ExamSessionComparator examSessionComparator,
            IThreadNotifier threadNotifier) {
        this.key = key;
        this.graderSession = graderSession;
        this.oMRGraderProcess = oMRGraderProcess;
        this.graderSessionDAO = graderSessionDAO;
        this.imageFileManagement = imageFileManagement;
        this.emailSender = emailSender;
        this.examSessionComparator = examSessionComparator;
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
        FileSheetReport fileSheetReport = new FileSheetReport();

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
                        0, null, null, null, null);
                exam.setGrayScaledImageMat(null);
                exam.setImageDescriptorsMat(null);
                exam.setImageMatOfKeyPoints(null);

                referenceExamResult = new ExamResult(exam);
                studentsExamsResultsList = new ArrayList<>(studentExamsImagesFiles.length);

                for (File studentExamImageFile : studentExamsImagesFiles) {
                    exam = this.oMRGraderProcess.extractFeatures(
                            studentExamImageFile.getAbsolutePath());
                    exam = this.oMRGraderProcess.executeExamProcessing(
                            this.oMRGraderProcess.getOnlyLogosTemplateExam(),
                            exam,
                            referenceExamResult.getExam().getQuestionsItemsList().size(),
                            null, null, null, null);
                    exam.setGrayScaledImageMat(null);
                    exam.setImageDescriptorsMat(null);
                    exam.setImageMatOfKeyPoints(null);

                    studentsExamsResultsList.add(new ExamResult(exam));
                }

                FileSheetInformation fileSheetInformation = new FileSheetInformation(
                        this.graderSession, referenceExamResult,
                        studentsExamsResultsList);
                String fileXSLXPath;

                this.examSessionComparator.score(fileSheetInformation);

                try {
                    fileXSLXPath = fileSheetReport.createDataSheet(
                            this.imageFileManagement.
                            buildUploadedFileDirectoryPath(String.valueOf(
                            storageDirectoryPathName), false), fileSheetInformation);
                } catch (OMRGraderProcessException e) {
                    try {
                        this.emailSender.sendEmail(this.graderSession.getGraderSessionPK().getElectronicMail(),
                                "Error", "Ha surgido un error mientras se estaba generando el Reporte de Calificaciones.");
                    } catch (OMRGraderEmailException ex) {
                        Logger.getLogger(TAG).log(Level.SEVERE,
                                "Error while the applications was sending the Error Reporting Email.",
                                e);
                    }
                    this.resumeGranderSession(storageDirectoryPathName);
                    this.executeNotification();

                    return;
                }

                try {
                    this.emailSender.sendEMail(this.graderSession.
                            getGraderSessionPK().getElectronicMail(),
                            fileXSLXPath,
                            fileSheetInformation.getGraderSession().
                            getGraderSessionPK().getSessionName());
                } catch (OMRGraderEmailException e) {
                    Logger.getLogger(TAG).log(Level.SEVERE,
                            "Error while the applications was sending the Reporting Email.",
                            e);
                }

                this.resumeGranderSession(storageDirectoryPathName);
                this.executeNotification();
            }
        } catch (OMRGraderProcessException | OMRGraderPersistenceException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Grader Session Thread was trying to manage a Grader Session.",
                    e);
        }
    }

    private void resumeGranderSession(long storageDirectoryPathName)
            throws OMRGraderPersistenceException {
        try {
            this.imageFileManagement.deleteStorageDirectory(String.valueOf(
                    storageDirectoryPathName));
        } catch (OMRGraderProcessException ex) {
        } finally {
            this.graderSessionDAO.delete(this.getGraderSession()
                    .getGraderSessionPK());
        }
    }

    private void executeNotification() {
        this.threadNotifier.notifyEvent(new Object[]{this});
    }
}