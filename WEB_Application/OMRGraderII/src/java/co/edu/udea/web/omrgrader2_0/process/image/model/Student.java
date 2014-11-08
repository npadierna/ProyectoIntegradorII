package co.edu.udea.web.omrgrader2_0.process.image.model;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class Student {
    
    private String eMail;
    private String fullName;
    private String idNumber;
    
    public Student(String eMail, String fullName, String idNumber){
        this.fullName = fullName;
        this.eMail = eMail;
        this.idNumber = idNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}