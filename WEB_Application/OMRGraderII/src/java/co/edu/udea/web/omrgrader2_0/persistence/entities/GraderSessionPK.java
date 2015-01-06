package co.edu.udea.web.omrgrader2_0.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Embeddable()
public class GraderSessionPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 55)
    @Column(name = "electronic_mail", nullable = false, length = 55)
    private String electronicMail;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 55)
    @Column(name = "session_name", nullable = false, length = 55)
    private String sessionName;

    public GraderSessionPK() {
        super();
    }

    public GraderSessionPK(String electronicMail, String sessionName) {
        this.electronicMail = electronicMail;
        this.sessionName = sessionName;
    }

    public String getElectronicMail() {

        return (this.electronicMail);
    }

    public void setElectronicMail(String electronicMail) {
        this.electronicMail = electronicMail;
    }

    public String getSessionName() {

        return (this.sessionName);
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override()
    public int hashCode() {
        int hash = 0;

        hash += (this.getElectronicMail() != null
                ? this.getElectronicMail().hashCode() : 0);
        hash += (this.getSessionName() != null
                ? this.getSessionName().hashCode() : 0);

        return (hash);
    }

    @Override()
    public boolean equals(Object object) {
        if (!(object instanceof GraderSessionPK)) {

            return (false);
        }

        GraderSessionPK other = (GraderSessionPK) object;
        if (((this.getElectronicMail() == null)
                && (other.getElectronicMail() != null))
                || ((this.getElectronicMail() != null)
                && !(this.getElectronicMail().equals(other.getElectronicMail())))) {

            return (false);
        }

        if (((this.getSessionName() == null) && (other.getSessionName() != null))
                || ((this.getSessionName() != null)
                && !(this.getSessionName().equals(other.getSessionName())))) {

            return (false);
        }

        return (true);
    }

    @Override()
    public String toString() {

        return ("co.edu.udea.web.omrgrader2_0.persistence.entity.GraderSessionPK[ electronicMail="
                + this.getElectronicMail() + ", sessionName="
                + this.getSessionName() + " ]");
    }
}
