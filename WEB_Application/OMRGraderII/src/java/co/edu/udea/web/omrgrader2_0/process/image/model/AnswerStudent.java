package co.edu.udea.web.omrgrader2_0.process.image.model;

import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class AnswerStudent {

    private List<QuestionItem> answerList;
    private Student student;
    private List<Boolean> answersScore;
    private int correctAnswersAmount;
    private double score;
    private boolean passed = false;

    public AnswerStudent(List<QuestionItem> answerList, Student student) {
        super();
        this.answerList = answerList;
        this.student = student;
    }

    public AnswerStudent() {
        super();
    }

    public List<QuestionItem> getAnswerList() {

        return answerList;
    }

    public void setAnswerList(List<QuestionItem> answerList) {
        this.answerList = answerList;
    }

    public Student getStudent() {

        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getCorrectAnswersAmount() {

        return correctAnswersAmount;
    }

    public void setCorrectAnswersAmount(int correctAnswersAmount) {
        this.correctAnswersAmount = correctAnswersAmount;
    }

    public List<Boolean> getAnswersScore() {

        return answersScore;
    }

    public void setAnswersScore(List<Boolean> answersScore) {
        this.answersScore = answersScore;
    }

    public double getScore() {

        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean getPassed() {

        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
