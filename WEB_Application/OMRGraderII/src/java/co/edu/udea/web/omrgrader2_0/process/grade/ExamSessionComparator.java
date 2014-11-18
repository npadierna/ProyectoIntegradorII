package co.edu.udea.web.omrgrader2_0.process.grade;

import co.edu.udea.web.omrgrader2_0.process.image.model.AnswerStudent;
import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamSessionComparator {

    public ExamSessionComparator() {
        super();
    }

    public void score(SheetFileInfo sheetFileInfo) {
        sheetFileInfo.setQuestionAmount(sheetFileInfo.getCorrectAnswers().size());
        double s = sheetFileInfo.getMaximumScore() * sheetFileInfo.getPercentageToPass();
        sheetFileInfo.setMinimumScoreToPass(s);
        double t = sheetFileInfo.getPercentageToPass() * sheetFileInfo.getCorrectAnswers().size();
        sheetFileInfo.setMinimumQuestionAmountToPass((int) t);

        int amountStudentPassed = 0;

        List<AnswerStudent> answerStudentList = new ArrayList<>();
        for (AnswerStudent answerStudent : sheetFileInfo.getAnswerStudentList()) {
            List<Boolean> scoreList = this.compareAnswers(sheetFileInfo.
                    getCorrectAnswers(), answerStudent.getAnswerList());
            AnswerStudent temp = new AnswerStudent();
            temp.setAnswerList(answerStudent.getAnswerList());
            temp.setAnswersScore(scoreList);
            temp.setStudent(answerStudent.getStudent());

            int amount = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                if (scoreList.get(i).booleanValue()) {
                    amount++;
                }
            }

            double score = ((sheetFileInfo.getMaximumScore() * amount)
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