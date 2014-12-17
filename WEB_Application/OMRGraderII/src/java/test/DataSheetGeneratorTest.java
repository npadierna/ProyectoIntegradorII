package test;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSessionPK;
import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.email.report.FileSheetReport;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.grade.ExamSessionComparator;
import co.edu.udea.web.omrgrader2_0.process.image.model.AnswerStudent;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInfo;
import co.edu.udea.web.omrgrader2_0.process.image.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSheetGeneratorTest {

    public static void main(String[] args) {
        try {
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

            List<AnswerStudent> answerStudents = new ArrayList<>();
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
            Student student = new Student("anderssongs5@outlook.com", "Andersson García Sotelo", "1037622083");
            answerStudents.add(new AnswerStudent(answers, student));

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
            student = new Student("npadierna@gmail.com", "Neiber de Jesús Padierna Pérez", "1022095657");
            answerStudents.add(new AnswerStudent(answers, student));

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
            student = new Student("miguelcold8@gmail.com", "Miguel Angel Ossa Ruiz", "1035859551");
            answerStudents.add(new AnswerStudent(answers, student));

            float percentage = 60f / 100f;
            GraderSession gs = new GraderSession(
                    new GraderSessionPK("anderssongarciasotelo@gmail.com", "Examen I Prueba"));
            gs.setApprovalPercentage(percentage);
            gs.setDecimalPrecision("3");
            gs.setMaximumGrade(5f);
            SheetFileInfo sfi = new SheetFileInfo(gs, answerStudents, correctAnswers);
            ExamSessionComparator esc = new ExamSessionComparator();
            esc.score(sfi);

            System.out.println("Exam Name: " + sfi.getGraderSession().
                    getGraderSessionPK().getSessionName());
            System.out.println("Maximum Score: " + sfi.getGraderSession().
                    getMaximumGrade());
            System.out.println("Student Amount: " + sfi.getStudentAmount());
            System.out.println("Passed Student Amount: " + sfi.getStudentAmountPassed());
            System.out.println("Question Amount: " + sfi.getQuestionAmount());
            System.out.println("Minimum Question Amount to Pass: "
                    + sfi.getMinimumQuestionAmountToPass());
            System.out.println("Minimum Score to Pass: " + sfi.getMinimumScoreToPass());
            System.out.println("Percentage to Pass: "
                    + sfi.getGraderSession().getApprovalPercentage());

            FileSheetReport fileSheetReport = new FileSheetReport();
            String fileXSLXPath = "";
            try {
                fileXSLXPath = fileSheetReport.createDataSheet(path, sfi);
                System.out.println("\nFile Full Path: " + fileXSLXPath);
            } catch (OMRGraderProcessException ex) {
                ex.printStackTrace();
            }

            EmailSender emailSender = new EmailSender();
            emailSender.sendEMail(fileXSLXPath,
                    sfi.getGraderSession().getGraderSessionPK().getSessionName(),
                    sfi.getGraderSession().getGraderSessionPK().getElectronicMail());
        } catch (OMRGraderProcessException ex) {
            Logger.getLogger(DataSheetGeneratorTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}
