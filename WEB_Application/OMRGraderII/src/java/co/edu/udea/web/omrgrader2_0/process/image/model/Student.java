package co.edu.udea.web.omrgrader2_0.process.image.model;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class Student {

    private String electronicMail;
    private String fullName;
    private String idNumber;

    public Student(String electronicMail, String fullName, String idNumber) {
        this.fullName = fullName;
        this.electronicMail = electronicMail;
        this.idNumber = idNumber;
    }

    public String getElectronicMail() {

        return (this.electronicMail);
    }

    public void setElectronicMail(String electronicMail) {
        this.electronicMail = electronicMail;
    }

    public String getFullName() {

        return (this.fullName);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {

        return (this.idNumber);
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}