package co.edu.udea.web.omrgrader2_0.process.email.report.model;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class FileSheetInformation {

    private GraderSession graderSession;
    private List<ExamResult> studentsExamsResultsList;
    private ExamResult referenceExam;
    private int studentAmountPassed;
    private String precisionPattern;

    public FileSheetInformation(GraderSession graderSession,
            ExamResult refereExamResult,
            List<ExamResult> studentsExamsResultsList) {
        this.graderSession = graderSession;
        this.referenceExam = refereExamResult;
        this.studentsExamsResultsList = studentsExamsResultsList;

        this.constructPrecision(this.graderSession.getDecimalPrecision().
                toString());
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

    public String getPrecisionPattern() {

        return (this.precisionPattern);
    }

    public void setPrecisionPattern(String precisionPattern) {
        this.precisionPattern = precisionPattern;
    }

    public double getMinimumScoreToPass() {
        if (this.getGraderSession() != null) {

            DecimalFormat df = new DecimalFormat(this.getPrecisionPattern());
            String num = df.format((Double.parseDouble(df.format(
                    this.getGraderSession().getMaximumGrade()).
                    replace(',', '.'))) * (Double.parseDouble(df.format(
                    this.getGraderSession().getApprovalPercentage() / 100.0).
                    replace(',', '.')))).replace(',', '.');

            return (Double.parseDouble(num));
        }

        return (-1.0);
    }

    public int getQuestionAmount() {
        if (this.getGraderSession() != null
                && this.getReferenceExam().getExam().getQuestionsItemsList() != null
                && !this.getReferenceExam().getExam().getQuestionsItemsList().isEmpty()) {

            return (this.getReferenceExam().getExam().getQuestionsItemsList().size());
        }

        return (-1);
    }

    public int getMinimumQuestionAmountToPass() {
        if (this.getGraderSession() != null
                && this.getReferenceExam().getExam().getQuestionsItemsList() != null
                && !this.getReferenceExam().getExam().getQuestionsItemsList().isEmpty()) {
            DecimalFormat df = new DecimalFormat(this.getPrecisionPattern());

            return (int) ((Double.parseDouble(df.format(this.getGraderSession().
                    getApprovalPercentage() / 100.0).replace(',', '.')))
                    * this.getReferenceExam().getExam().getQuestionsItemsList().
                    size());
        }

        return (-1);
    }

    private void constructPrecision(String precision) {
        String decimalPrecisionFormat = "0.0";
        if (TextUtil.hasOnlyNumbers(precision)) {
            this.setPrecisionPattern(decimalPrecisionFormat);

            return;
        }

        int decimalPrecision = Integer.parseInt(precision);
        int i = 1;

        while (i < decimalPrecision) {
            decimalPrecisionFormat = decimalPrecisionFormat + "0";
            i++;
        }

        this.setPrecisionPattern(decimalPrecisionFormat);
    }
}