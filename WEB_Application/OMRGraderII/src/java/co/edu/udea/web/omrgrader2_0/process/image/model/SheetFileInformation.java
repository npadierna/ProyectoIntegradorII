// TODO: Refactorizar moviendo para algún lado.
package co.edu.udea.web.omrgrader2_0.process.image.model;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class SheetFileInformation {

    private GraderSession graderSession;
    private List<ExamResult> answerStudentList;
    // TODO: Cambiar por una referencia a un objeto Exam.
    private List<QuestionItem> correctAnswers;
    private int studentAmount;
    private int studentAmountPassed;
    private int questionAmount;
    private int minimumQuestionAmountToPass;
    private double minimumScoreToPass;

    public SheetFileInformation() {
        super();
    }

    public SheetFileInformation(GraderSession graderSession,
            List<ExamResult> answerStudentList,
            List<QuestionItem> correctAnswers) {
        this.graderSession = graderSession;
        this.answerStudentList = answerStudentList;
        this.correctAnswers = correctAnswers;
    }

    public GraderSession getGraderSession() {

        return (this.graderSession);
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    public List<ExamResult> getAnswerStudentList() {

        return (this.answerStudentList);
    }

    public void setAnswerStudentList(List<ExamResult> answerStudentList) {
        this.answerStudentList = answerStudentList;
    }

    public List<QuestionItem> getCorrectAnswers() {

        return (this.correctAnswers);
    }

    public void setCorrectAnswers(List<QuestionItem> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getStudentAmount() {

        return (this.studentAmount);
    }

    public void setStudentAmount(int studentAmount) {
        this.studentAmount = studentAmount;
    }

    public int getStudentAmountPassed() {

        return (this.studentAmountPassed);
    }

    public void setStudentAmountPassed(int studentAmountPassed) {
        this.studentAmountPassed = studentAmountPassed;
    }

    public int getQuestionAmount() {

        return (this.questionAmount);
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public int getMinimumQuestionAmountToPass() {

        return (this.minimumQuestionAmountToPass);
    }

    public void setMinimumQuestionAmountToPass(int minimumQuestionsToPass) {
        this.minimumQuestionAmountToPass = minimumQuestionsToPass;
    }

    public double getMinimumScoreToPass() {

        return (this.minimumScoreToPass);
    }

    public void setMinimumScoreToPass(double minimumScoreToPass) {
        this.minimumScoreToPass = minimumScoreToPass;
    }
}