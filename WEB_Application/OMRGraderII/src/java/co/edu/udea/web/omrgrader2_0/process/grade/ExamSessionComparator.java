package co.edu.udea.web.omrgrader2_0.process.grade;

import co.edu.udea.web.omrgrader2_0.process.email.report.FileSheetReport;
import co.edu.udea.web.omrgrader2_0.process.image.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInformation;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.ExamProcess;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hssf.record.FileSharingRecord;
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

    public void score(SheetFileInformation sheetFileInformation) {
        DecimalFormat decimalFormat = new DecimalFormat(
                sheetFileInformation.getPrecisionPattern());

        int minimumQuestionAmountToPass = (int) ((Double.parseDouble(
                decimalFormat.format(sheetFileInformation.
                getGraderSession().getApprovalPercentage()).replace(',', '.')))
                * sheetFileInformation.getReferenceExam().getExam().
                getQuestionsItemsList().size());
        int questionAmount = sheetFileInformation.
                getReferenceExam().getExam().getQuestionsItemsList().size();

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

            double score = Double.parseDouble(decimalFormat.format(((Double.
                    parseDouble(decimalFormat.format(sheetFileInformation.
                    getGraderSession().getMaximumGrade()).replace(',', '.')))
                    * amount) / questionAmount).replace(',', '.'));

            if (amount >= minimumQuestionAmountToPass
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