package co.edu.udea.web.omrgrader2_0.process.image.model;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class SheetFileInfo {

    private GraderSession graderSession;
    private List<AnswerStudent> answerStudentList;
    private List<QuestionItem> correctAnswers;
    private int studentAmount;
    private int studentAmountPassed;
    private int questionAmount;
    private int minimumQuestionAmountToPass;
    private double minimumScoreToPass;

    public SheetFileInfo() {
        super();
    }

    public SheetFileInfo(GraderSession graderSession,
            List<AnswerStudent> answerStudentList,
            List<QuestionItem> correctAnswers) {
        this.graderSession = graderSession;
        this.answerStudentList = answerStudentList;
        this.correctAnswers = correctAnswers;
    }

    public GraderSession getGraderSession() {

        return graderSession;
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    public List<AnswerStudent> getAnswerStudentList() {

        return answerStudentList;
    }

    public void setAnswerStudentList(List<AnswerStudent> answerStudentList) {
        this.answerStudentList = answerStudentList;
    }

    public List<QuestionItem> getCorrectAnswers() {

        return correctAnswers;
    }

    public void setCorrectAnswers(List<QuestionItem> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getStudentAmount() {

        return studentAmount;
    }

    public void setStudentAmount(int studentAmount) {
        this.studentAmount = studentAmount;
    }

    public int getStudentAmountPassed() {

        return studentAmountPassed;
    }

    public void setStudentAmountPassed(int studentAmountPassed) {
        this.studentAmountPassed = studentAmountPassed;
    }

    public int getQuestionAmount() {

        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public int getMinimumQuestionAmountToPass() {

        return minimumQuestionAmountToPass;
    }

    public void setMinimumQuestionAmountToPass(int minimumQuestionsToPass) {
        this.minimumQuestionAmountToPass = minimumQuestionsToPass;
    }

    public double getMinimumScoreToPass() {

        return minimumScoreToPass;
    }

    public void setMinimumScoreToPass(double minimumScoreToPass) {
        this.minimumScoreToPass = minimumScoreToPass;
    }
}