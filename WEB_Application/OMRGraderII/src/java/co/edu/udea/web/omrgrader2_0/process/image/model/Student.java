package co.edu.udea.web.omrgrader2_0.process.image.model;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class Student {

    public static final String ID_NUMBER_KEY = "idNumber";
    public static final String FULL_NAMES_KEY = "fullNames";
    public static final String E_MAIL_KEY = "eMail";
    private String eMail;
    private String fullNames;
    private String idNumber;

    public Student(String eMail, String fullNames, String idNumber) {
        this.fullNames = fullNames;
        this.eMail = eMail;
        this.idNumber = idNumber;
    }

    public String geteMail() {

        return (this.eMail);
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFullNames() {

        return (this.fullNames);
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getIdNumber() {

        return (this.idNumber);
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}