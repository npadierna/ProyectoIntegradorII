package co.edu.udea.web.omrgrader2_0.process.grade;

import co.edu.udea.web.omrgrader2_0.process.image.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInformation;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.ExamProcess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Component()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class ExamSessionComparator {

    public ExamSessionComparator() {
        super();
    }

    // TODO: Me tocó refactorizar este método, no sé si esté bueno, así que toca
    // ya que al usar ExamResult, y al mover las relaciones entre las clases,
    // algunos atributos se movieron.
    public void score(SheetFileInformation sheetFileInformation) {
        sheetFileInformation.setQuestionAmount(sheetFileInformation.
                getReferenceExam().getExam().getQuestionsItemsList().size());

        double t = sheetFileInformation.getGraderSession().getApprovalPercentage()
                * sheetFileInformation.getReferenceExam().getExam().
                getQuestionsItemsList().size();
        sheetFileInformation.setMinimumQuestionAmountToPass((int) t);

        int amountStudentPassed = 0;
        for (ExamResult examResult : sheetFileInformation.
                getStudentsExamsResultsList()) {
            List<Boolean> scoreList = this.compareAnswers(sheetFileInformation.
                    getReferenceExam().getExam().getQuestionsItemsList(),
                    examResult.getExam().getQuestionsItemsList());

            int amount = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                if (scoreList.get(i).booleanValue()) {
                    amount++;
                }
            }

            double score = ((sheetFileInformation.getGraderSession().
                    getMaximumGrade() * amount)
                    / sheetFileInformation.getQuestionAmount());

            if (amount >= sheetFileInformation.getMinimumQuestionAmountToPass()
                    && score >= sheetFileInformation.getMinimumScoreToPass()) {
                amountStudentPassed++;
                examResult.setPassed(true);
            }

            examResult.setCorrectAnswersAmount(amount);
            examResult.setScore(score);
        }

        sheetFileInformation.setStudentAmountPassed(amountStudentPassed);
    }

    private List<Boolean> compareAnswers(List<QuestionItem> correctAnswers,
            List<QuestionItem> studentAnswers) {
        if ((correctAnswers == null) || (studentAnswers == null)
                || (correctAnswers.isEmpty()) || (studentAnswers.isEmpty())
                || (correctAnswers.size() != studentAnswers.size())) {

            return (null);
        }

        // TODO: No sé sí esto es un machetazo. ATT: Fry
        boolean[] invalidAnswer = new boolean[ExamProcess.BUBBLE_OPTIONS_AMOUNT];
        List<Boolean> scoreList = new ArrayList<>();
        for (int i = 0; i < correctAnswers.size(); i++) {
            if (Arrays.equals(correctAnswers.get(i).getChoises(), invalidAnswer)) {
                break;
            }
            
            scoreList.add(correctAnswers.get(i).equals(studentAnswers.get(i)));
        }

        return (scoreList);
    }
}