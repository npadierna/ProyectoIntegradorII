package test;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSessionPK;
import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.email.exception.OMRGraderEmailException;
import co.edu.udea.web.omrgrader2_0.process.email.report.FileSheetReport;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.grade.ExamSessionComparator;
import co.edu.udea.web.omrgrader2_0.process.image.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.Exam;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInformation;
import co.edu.udea.web.omrgrader2_0.process.image.model.Student;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.OMRGraderProcess;
import static co.edu.udea.web.omrgrader2_0.process.image.opencv.OMRGraderProcess.ONLY_LOGOS_TEMPLATE_IMAGE_NAME;
import co.edu.udea.web.omrgrader2_0.process.qr.QRManager;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainTests {

    public static void main(String[] args) throws OMRGraderProcessException {

        long timeStart;
        long timeEnd;
        long fullTime;
        timeStart = System.currentTimeMillis();
        System.load("/home/pivb/Software/Libraries/OpenCV2.4.8/opencv_java248.so");

        testOMR();

        timeEnd = System.currentTimeMillis();
        fullTime = timeEnd - timeStart;
        System.out.println("Time Processing: " + fullTime + "milliseconds.");
    }

    /* 
     * Este método lo que hace es recortar una imagen usando OpenCV y la guarda.
     * No está en ninguna clase pero se deja acá por si en algún momento se
     * necesita algo similar.
     */
    public static void testCropImage() {
        String imagePath = "/home/pivb/Escritorio/Temporales/Foto_Nueva_(1).jpg";
        Mat image = Highgui.imread(imagePath);

        Rect rect = new Rect(442, 95, 143, 143);
        Mat croppedImage = new Mat(image, rect);

        File file = new File("/home/pivb/Escritorio/Temporales/", "croppedImage.jpg");

        String filePhotoName = file.toString();
        Highgui.imwrite(filePhotoName, croppedImage);

        System.out.println("Guardado");
    }

    public static void testQRReader() {
        String imagePath = "/home/pivb/Escritorio/Temporales/Foto_Nueva_(1).jpg";
        QRManager qrManager = new QRManager();
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        try {
            Map<String, String> studentInformationMap = qrManager.readQRCode(
                    imagePath, hintMap, 447, 576, 100, 233);

            System.out.println(studentInformationMap);
        } catch (OMRGraderProcessException ex) {
            Logger.getLogger(MainTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testOMR() {
        OMRGraderProcess oMRGraderProcess = new OMRGraderProcess();
        oMRGraderProcess.initialize();

        String examAbsolutePath = "/home/pivb/Imágenes/UdeA/Foto_(1).jpg";

        oMRGraderProcess
                .executeExamProcessing(MainTests.class.getResource(File.separator.concat(
                ONLY_LOGOS_TEMPLATE_IMAGE_NAME)).getPath(),
                examAbsolutePath, false,
                "/home/pivb/Imágenes/UdeA/",
                "/home/pivb/Imágenes/UdeA/",
                "examForProcessing-Processed.png",
                "examForProcessing-BlackAndWhite.png");

        Exam referenceExam = oMRGraderProcess.getOnlyLogosTemplateExam();
        Exam studentExam = oMRGraderProcess.extractFeatures(
                examAbsolutePath);
        oMRGraderProcess.executeExamProcessing(referenceExam, studentExam, false,
                "/home/pivb/Imágenes/UdeA/",
                "/home/pivb/Imágenes/UdeA/",
                "examForProcessing-Processed_(1).png",
                "examForProcessing-BlackAndWhite_(1).png");
    }

    public static void testEMailSenderWithAttached() throws OMRGraderEmailException {
        EmailSender emailSender = new EmailSender();

        try {
            emailSender.sendEMail("anderssongarciasotelo@gmail.com",
                    "/home/pivb/Escritorio/Temporales/OpenCV Linux.txt",
                    "Testing With Attached");
        } catch (OMRGraderEmailException ex) {
            Logger.getLogger(MainTests.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void testEMailSenderWithoutAttached() throws OMRGraderEmailException {
        EmailSender emailSender = new EmailSender();

        try {
            emailSender.sendEmail("anderssongarciasotelo@gmail.com", "Oelo 1.1",
                    "Probando maricadas.");
        } catch (OMRGraderEmailException ex) {
            Logger.getLogger(MainTests.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    // TODO: Comenté esta "prueba" para que Andersson la refactorice como es.
//    public static void testDataSheetGeneratorAndEmaiSender() {
//        try {
//            String path = "/home/pivb/Escritorio/Temporales/";
//
//            List<QuestionItem> correctAnswers = new ArrayList<>();
//            boolean[] choises = {true, false, false, false, false};
//            QuestionItem qi = new QuestionItem((short) 1, choises);
//            correctAnswers.add(qi);
//            choises = new boolean[]{false, true, false, false, false};
//            qi = new QuestionItem((short) 2, choises);
//            correctAnswers.add(qi);
//            choises = new boolean[]{false, false, false, false, true};
//            qi = new QuestionItem((short) 3, choises);
//            correctAnswers.add(qi);
//            choises = new boolean[]{false, false, false, true, false};
//            qi = new QuestionItem((short) 4, choises);
//            correctAnswers.add(qi);
//            choises = new boolean[]{false, false, true, false, false};
//            qi = new QuestionItem((short) 5, choises);
//            correctAnswers.add(qi);
//
//            List<ExamResult> answerStudents = new ArrayList<>();
//            List<QuestionItem> answers = new ArrayList<>();
//
//            choises = new boolean[]{true, false, false, false, false};
//            qi = new QuestionItem((short) 1, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, true, false, false, false};
//            qi = new QuestionItem((short) 2, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, false, true};
//            qi = new QuestionItem((short) 3, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, true, false};
//            qi = new QuestionItem((short) 4, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, true, false, false};
//            qi = new QuestionItem((short) 5, choises);
//            answers.add(qi);
//            Student student = new Student("anderssongs5@outlook.com", "Andersson García Sotelo", "1037622083");
//            answerStudents.add(new ExamResult(answers, student));
//
//            answers = new ArrayList<>();
//            choises = new boolean[]{false, true, false, false, false};
//            qi = new QuestionItem((short) 1, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, true, false, false, false};
//            qi = new QuestionItem((short) 2, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, false, true};
//            qi = new QuestionItem((short) 3, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, false, true};
//            qi = new QuestionItem((short) 4, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, true, false, false};
//            qi = new QuestionItem((short) 5, choises);
//            answers.add(qi);
//            student = new Student("npadierna@gmail.com", "Neiber de Jesús Padierna Pérez", "1022095657");
//            answerStudents.add(new ExamResult(answers, student));
//
//            answers = new ArrayList<>();
//            choises = new boolean[]{true, false, false, false, false};
//            qi = new QuestionItem((short) 1, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, true, false};
//            qi = new QuestionItem((short) 2, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, false, true};
//            qi = new QuestionItem((short) 3, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, true, false, false, false};
//            qi = new QuestionItem((short) 4, choises);
//            answers.add(qi);
//            choises = new boolean[]{false, false, false, false, false};
//            qi = new QuestionItem((short) 5, choises);
//            answers.add(qi);
//            student = new Student("miguelcold8@gmail.com", "Miguel Angel Ossa Ruiz", "1035859551");
//            answerStudents.add(new ExamResult(answers, student));
//
//            float percentage = 60f / 100f;
//            GraderSession gs = new GraderSession(
//                    new GraderSessionPK("anderssongarciasotelo@gmail.com", "Examen I Prueba"));
//            gs.setApprovalPercentage(percentage);
//            gs.setDecimalPrecision("3");
//            gs.setMaximumGrade(5f);
//            SheetFileInformation sfi = new SheetFileInformation(gs, answerStudents, correctAnswers);
//            ExamSessionComparator esc = new ExamSessionComparator();
//            esc.score(sfi);
//
//            System.out.println("Exam Name: " + sfi.getGraderSession().
//                    getGraderSessionPK().getSessionName());
//            System.out.println("Maximum Score: " + sfi.getGraderSession().
//                    getMaximumGrade());
//            System.out.println("Student Amount: " + sfi.getStudentAmount());
//            System.out.println("Passed Student Amount: " + sfi.getStudentAmountPassed());
//            System.out.println("Question Amount: " + sfi.getQuestionAmount());
//            System.out.println("Minimum Question Amount to Pass: "
//                    + sfi.getMinimumQuestionAmountToPass());
//            System.out.println("Minimum Score to Pass: " + sfi.getMinimumScoreToPass());
//            System.out.println("Percentage to Pass: "
//                    + sfi.getGraderSession().getApprovalPercentage());
//
//            FileSheetReport fileSheetReport = new FileSheetReport();
//            String fileXSLXPath = "";
//            try {
//                fileXSLXPath = fileSheetReport.createDataSheet(path, sfi);
//                System.out.println("\nFile Full Path: " + fileXSLXPath);
//            } catch (OMRGraderProcessException ex) {
//                ex.printStackTrace();
//            }
//
//            EmailSender emailSender = new EmailSender();
//            emailSender.sendEMail(sfi.getGraderSession().getGraderSessionPK().
//                    getElectronicMail(), fileXSLXPath,
//                    sfi.getGraderSession().getGraderSessionPK().getSessionName());
//        } catch (OMRGraderEmailException ex) {
//            Logger.getLogger(MainTests.class.getName()).
//                    log(Level.SEVERE, null, ex);
//        }
//    }
}