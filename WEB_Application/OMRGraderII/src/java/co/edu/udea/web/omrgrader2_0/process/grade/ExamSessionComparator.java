package co.edu.udea.web.omrgrader2_0.process.grade;

import co.edu.udea.web.omrgrader2_0.process.email.report.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.email.report.model.FileSheetInformation;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.ExamProcess;
import java.text.DecimalFormat;
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

    public void score(FileSheetInformation fileSheetInformation) {
        DecimalFormat decimalFormat = new DecimalFormat(
                fileSheetInformation.getPrecisionPattern());

        int minimumQuestionAmountToPass = (int) ((Double.parseDouble(
                decimalFormat.format(fileSheetInformation.
                getGraderSession().getApprovalPercentage() / 100.0F).
                replace(',', '.')))
                * fileSheetInformation.getReferenceExam().getExam().
                getQuestionsItemsList().size());
        int questionAmount = fileSheetInformation.
                getReferenceExam().getExam().getQuestionsItemsList().size();

        int amountStudentPassed = 0;
        for (ExamResult examResult : fileSheetInformation.
                getStudentsExamsResultsList()) {
            List<Boolean> scoreList = this.compareAnswers(fileSheetInformation.
                    getReferenceExam().getExam().getQuestionsItemsList(),
                    examResult.getExam().getQuestionsItemsList());

            int amount = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                if (scoreList.get(i).booleanValue()) {
                    amount++;
                }
            }

            double score = Double.parseDouble(decimalFormat.format(((Double.
                    parseDouble(decimalFormat.format(fileSheetInformation.
                    getGraderSession().getMaximumGrade()).replace(',', '.')))
                    * amount) / questionAmount).replace(',', '.'));

            if (amount >= minimumQuestionAmountToPass
                    && score >= fileSheetInformation.getMinimumScoreToPass()) {
                amountStudentPassed++;
                examResult.setPassed(true);
            }

            examResult.setCorrectAnswersAmount(amount);
            examResult.setScore(score);
        }

        fileSheetInformation.setStudentAmountPassed(amountStudentPassed);
    }

    private List<Boolean> compareAnswers(List<QuestionItem> correctAnswers,
            List<QuestionItem> studentAnswers) {
        if ((correctAnswers == null) || (studentAnswers == null)
                || (correctAnswers.isEmpty()) || (studentAnswers.isEmpty())
                || (correctAnswers.size() != studentAnswers.size())) {

            return (null);
        }

        List<Boolean> scoreList = new ArrayList<>();
        for (int i = 0; i < correctAnswers.size(); i++) {
            scoreList.add(correctAnswers.get(i).equals(studentAnswers.get(i)));
        }

        return (scoreList);
    }
}