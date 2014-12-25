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
    private List<ExamResult> studentsExamsResultsList;
    private ExamResult referenceExam;
    private int studentAmountPassed;
    // TODO: Creo que estos atributos al ser derivados es mejor calcularlos.
    private int questionAmount;
    private int minimumQuestionAmountToPass;

    public SheetFileInformation(GraderSession graderSession,
            ExamResult refereExamResult,
            List<ExamResult> studentsExamsResultsList) {
        this.graderSession = graderSession;
        this.referenceExam = refereExamResult;
        this.studentsExamsResultsList = studentsExamsResultsList;
    }

    public GraderSession getGraderSession() {

        return (this.graderSession);
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    public List<ExamResult> getStudentsExamsResultsList() {

        return (this.studentsExamsResultsList);
    }

    public void setStudentsExamsResultsList(
            List<ExamResult> studentsExamsResultsList) {
        this.studentsExamsResultsList = studentsExamsResultsList;
    }

    public ExamResult getReferenceExam() {

        return (this.referenceExam);
    }

    public void setReferenceExam(ExamResult referenceExam) {
        this.referenceExam = referenceExam;
    }

    public int getStudentsAmount() {

        return ((this.getStudentsExamsResultsList() != null)
                ? this.getStudentsExamsResultsList().size() : -1);
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

    public void setMinimumQuestionAmountToPass(int minimumQuestionAmountToPass) {
        this.minimumQuestionAmountToPass = minimumQuestionAmountToPass;
    }

    public double getMinimumScoreToPass() {
        if (this.getGraderSession() != null) {

            return (this.getGraderSession().getMaximumGrade()
                    * this.getGraderSession().getApprovalPercentage());
        }

        return (-1.0);
    }
}