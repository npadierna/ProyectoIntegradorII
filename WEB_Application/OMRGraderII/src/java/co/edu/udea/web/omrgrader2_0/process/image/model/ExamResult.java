// TODO: Refactorizar moviendo para el paquete donde quede SheetFilInfo.
package co.edu.udea.web.omrgrader2_0.process.image.model;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamResult {

    private Exam exam;
    private int correctAnswersAmount;
    private double score;
    private boolean passed;

    public ExamResult(Exam exam) {
        this.exam = exam;
    }

    public Exam getExam() {

        return (this.exam);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public int getCorrectAnswersAmount() {

        return (this.correctAnswersAmount);
    }

    public void setCorrectAnswersAmount(int correctAnswersAmount) {
        this.correctAnswersAmount = correctAnswersAmount;
    }

    public double getScore() {

        return (this.score);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isPassed() {

        return (this.passed);
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}