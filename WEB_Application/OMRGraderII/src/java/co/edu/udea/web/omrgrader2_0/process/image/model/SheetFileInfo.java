package co.edu.udea.web.omrgrader2_0.process.image.model;

import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class SheetFileInfo {

    private String examName;
    private double maximumScore;
    private List<AnswerStudent> answerStudentList;
    private List<QuestionItem> correctAnswers;
    private int studentAmount;
    private int studentAmountPassed;
    private int questionAmount;
    private int minimumQuestionAmountToPass;
    private double minimumScoreToPass;
    private double percentageToPass;
    private int precision;

    public SheetFileInfo(List<AnswerStudent> answerStudentList, List<QuestionItem> 
            correctAnswers, double maximumScore, double percentageToPass, int precision, String examName) {
        this.answerStudentList = answerStudentList;
        this.correctAnswers = correctAnswers;
        this.maximumScore = maximumScore;
        this.percentageToPass = percentageToPass;
        this.precision = precision;
        this.examName = examName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public double getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(double maximumScore) {
        this.maximumScore = maximumScore;
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

    public double getPercentageToPass() {
        return percentageToPass;
    }

    public void setPercentageToPass(double percentageToPass) {
        this.percentageToPass = percentageToPass;
    }

    public double getMinimumScoreToPass() {
        return minimumScoreToPass;
    }

    public void setMinimumScoreToPass(double minimumScoreToPass) {
        this.minimumScoreToPass = minimumScoreToPass;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}