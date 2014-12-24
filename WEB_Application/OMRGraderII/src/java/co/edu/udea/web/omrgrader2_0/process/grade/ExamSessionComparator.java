package co.edu.udea.web.omrgrader2_0.process.grade;

import co.edu.udea.web.omrgrader2_0.process.image.model.ExamResult;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInformation;
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

    public void score(SheetFileInformation sheetFileInfo) {
        sheetFileInfo.setQuestionAmount(sheetFileInfo.getCorrectAnswers().size());
        double s = sheetFileInfo.getGraderSession().getMaximumGrade()
                * sheetFileInfo.getGraderSession().getApprovalPercentage();
        sheetFileInfo.setMinimumScoreToPass(s);
        double t = sheetFileInfo.getGraderSession().getApprovalPercentage()
                * sheetFileInfo.getCorrectAnswers().size();
        sheetFileInfo.setMinimumQuestionAmountToPass((int) t);

        int amountStudentPassed = 0;

        List<ExamResult> answerStudentList = new ArrayList<>();
        for (ExamResult answerStudent : sheetFileInfo.getAnswerStudentList()) {
            List<Boolean> scoreList = this.compareAnswers(sheetFileInfo.
                    getCorrectAnswers(), answerStudent.getAnswerList());
            ExamResult temp = new ExamResult();
            temp.setAnswerList(answerStudent.getAnswerList());
            temp.setAnswersScore(scoreList);
            temp.setStudent(answerStudent.getStudent());

            int amount = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                if (scoreList.get(i).booleanValue()) {
                    amount++;
                }
            }

            double score = ((sheetFileInfo.getGraderSession().getMaximumGrade()
                    * amount)
                    / sheetFileInfo.getQuestionAmount());

            if (amount >= sheetFileInfo.getMinimumQuestionAmountToPass()
                    && score >= sheetFileInfo.getMinimumScoreToPass()) {
                amountStudentPassed++;
                temp.setPassed(true);
            }

            temp.setCorrectAnswersAmount(amount);
            temp.setScore(score);

            answerStudentList.add(temp);
        }

        sheetFileInfo.setAnswerStudentList(answerStudentList);
        sheetFileInfo.setStudentAmount(answerStudentList.size());
        sheetFileInfo.setStudentAmountPassed(amountStudentPassed);
    }

    private List<Boolean> compareAnswers(List<QuestionItem> correctAnswers,
            List<QuestionItem> studentAnswers) {
        if (correctAnswers.size() != studentAnswers.size()) {

            return (null);
        }

        List<Boolean> scoreList = new ArrayList<>();
        for (int i = 0; i < correctAnswers.size(); i++) {
            scoreList.add(Arrays.equals(correctAnswers.get(i).getChoises(), studentAnswers.
                    get(i).getChoises()));
        }

        return scoreList;
    }
}