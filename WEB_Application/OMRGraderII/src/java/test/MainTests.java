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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
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

    public static void main(String[] args) throws OMRGraderProcessException,
            OMRGraderEmailException {

        long timeStart;
        long timeEnd;
        long fullTime;
        timeStart = System.currentTimeMillis();
        System.load("/home/pivb/Software/Libraries/OpenCV2.4.8/opencv_java248.so");

        testDataSheetGeneratorAndEmaiSender();

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

    public static void testReadEmailProperties() throws OMRGraderEmailException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "co.edu.udea.web.omrgrader2_0.process.email.config.emailsender");
        System.out.println("EMAIL_ADDRESS_BUNDLE: " + resourceBundle.getString("EMAIL_ADDRESS"));
        System.out.println("PASSWORD_BUNDLE: " + resourceBundle.getString("PASSWORD"));
        System.out.println("HOST_BUNDLE: " + resourceBundle.getString("HOST"));
        System.out.println("PORT_BUNDLE: " + resourceBundle.getString("PORT"));

        System.out.println("\n");

        Properties properties = new Test().readProperties();
        System.out.println("EMAIL_ADDRESS_InSt: " + properties.getProperty("EMAIL_ADDRESS"));
        System.out.println("PASSWORD_InSt: " + properties.getProperty("PASSWORD"));
        System.out.println("HOST_InSt: " + properties.getProperty("HOST"));
        System.out.println("PORT_InSt: " + properties.getProperty("PORT"));
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
            emailSender.sendEmail("anderssongarciasotelo@gmail.com",
                    "Testing Without Attached", "Probando maricadas.");
        } catch (OMRGraderEmailException ex) {
            Logger.getLogger(MainTests.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void testDataSheetGeneratorAndEmaiSender() throws OMRGraderEmailException {
        String path = "/home/pivb/Escritorio/Temporales/";

        List<QuestionItem> correctAnswers = new ArrayList<>();
        boolean[] choises = {true, false, false, false, false};
        QuestionItem qi = new QuestionItem((short) 1, choises);
        correctAnswers.add(qi);
        choises = new boolean[]{false, true, false, false, false};
        qi = new QuestionItem((short) 2, choises);
        correctAnswers.add(qi);
        choises = new boolean[]{false, false, false, false, true};
        qi = new QuestionItem((short) 3, choises);
        correctAnswers.add(qi);
        choises = new boolean[]{false, false, false, true, false};
        qi = new QuestionItem((short) 4, choises);
        correctAnswers.add(qi);
        choises = new boolean[]{false, false, true, false, false};
        qi = new QuestionItem((short) 5, choises);
        correctAnswers.add(qi);
        Exam exam = new Exam(null, null, null, null);
        exam.setQuestionsItemsList(correctAnswers);

        ExamResult referenceExam = new ExamResult(exam);
        List<ExamResult> answerStudents = new ArrayList<>();
        List<QuestionItem> answers = new ArrayList<>();

        choises = new boolean[]{true, false, false, false, false};
        qi = new QuestionItem((short) 1, choises);
        answers.add(qi);
        choises = new boolean[]{false, true, false, false, false};
        qi = new QuestionItem((short) 2, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, false, true};
        qi = new QuestionItem((short) 3, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, true, false};
        qi = new QuestionItem((short) 4, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, true, false, false};
        qi = new QuestionItem((short) 5, choises);
        answers.add(qi);
        Student student = new Student("1037622083", "Andersson García Sotelo", "anderssongs5@outlook.com");
        exam = new Exam(null, null, null, null);
        exam.setStudent(student);
        exam.setQuestionsItemsList(answers);
        answerStudents.add(new ExamResult(exam));

        answers = new ArrayList<>();
        choises = new boolean[]{false, true, false, false, false};
        qi = new QuestionItem((short) 1, choises);
        answers.add(qi);
        choises = new boolean[]{false, true, false, false, false};
        qi = new QuestionItem((short) 2, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, false, true};
        qi = new QuestionItem((short) 3, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, false, true};
        qi = new QuestionItem((short) 4, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, true, false, false};
        qi = new QuestionItem((short) 5, choises);
        answers.add(qi);
        student = new Student("1022095657", "Neiber de Jesús Padierna Pérez", "npadierna@gmail.com");
        exam = new Exam(null, null, null, null);
        exam.setStudent(student);
        exam.setQuestionsItemsList(answers);
        answerStudents.add(new ExamResult(exam));

        answers = new ArrayList<>();
        choises = new boolean[]{true, false, false, false, false};
        qi = new QuestionItem((short) 1, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, true, false};
        qi = new QuestionItem((short) 2, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, false, true};
        qi = new QuestionItem((short) 3, choises);
        answers.add(qi);
        choises = new boolean[]{false, true, false, false, false};
        qi = new QuestionItem((short) 4, choises);
        answers.add(qi);
        choises = new boolean[]{false, false, false, false, false};
        qi = new QuestionItem((short) 5, choises);
        answers.add(qi);
        student = new Student("1035859551", "Miguel Angel Ossa Ruiz", "miguelcold8@gmail.com");
        exam = new Exam(null, null, null, null);
        exam.setStudent(student);
        exam.setQuestionsItemsList(answers);
        answerStudents.add(new ExamResult(exam));

        float percentage = 60f / 100f;
        GraderSession gs = new GraderSession(
                new GraderSessionPK("anderssongarciasotelo@gmail.com", "Examen I Prueba"));
        gs.setApprovalPercentage(percentage);
        gs.setDecimalPrecision("2");
        gs.setMaximumGrade(5f);
        SheetFileInformation sfi = new SheetFileInformation(gs, referenceExam, answerStudents);
        ExamSessionComparator esc = new ExamSessionComparator();
        esc.score(sfi);

        DecimalFormat df = new DecimalFormat(sfi.getPrecisionPattern());

        System.out.println("Exam Name: " + sfi.getGraderSession().
                getGraderSessionPK().getSessionName());
        System.out.println("Maximum Score: " + (Double.parseDouble(df.format(
                sfi.getGraderSession().getMaximumGrade()).
                replace(',', '.'))));
        System.out.println("Student Amount: " + sfi.getStudentsAmount());
        System.out.println("Passed Student Amount: " + sfi.getStudentAmountPassed());
        System.out.println("Question Amount: " + sfi.getQuestionAmount());
        System.out.println("Minimum Question Amount to Pass: "
                + sfi.getMinimumQuestionAmountToPass());
        System.out.println("Minimum Score to Pass: " + sfi.getMinimumScoreToPass());
        System.out.println("Percentage to Pass: "
                + (Double.parseDouble(df.format(sfi.getGraderSession().
                getApprovalPercentage()).replace(',', '.'))));

        FileSheetReport fileSheetReport = new FileSheetReport();
        String fileXSLXPath = "";
        try {
            fileXSLXPath = fileSheetReport.createDataSheet(path, sfi);
            System.out.println("\nFile Full Path: " + fileXSLXPath);
        } catch (OMRGraderProcessException ex) {
            ex.printStackTrace();
        }

        EmailSender emailSender = new EmailSender();
        emailSender.sendEMail(sfi.getGraderSession().getGraderSessionPK().
                getElectronicMail(), fileXSLXPath,
                sfi.getGraderSession().getGraderSessionPK().getSessionName());
    }
}