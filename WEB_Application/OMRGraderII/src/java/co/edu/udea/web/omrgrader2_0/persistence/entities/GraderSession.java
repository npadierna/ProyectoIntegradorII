package co.edu.udea.web.omrgrader2_0.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Entity()
@NamedQueries({
    @NamedQuery(name = "GraderSession.findAll",
            query = "SELECT g FROM GraderSession g"),
    @NamedQuery(name = "GraderSession.findByElectronicMail",
            query = "SELECT g FROM GraderSession g WHERE g.graderSessionPK.electronicMail = :electronicMail"),
    @NamedQuery(name = "GraderSession.findBySessionName",
            query = "SELECT g FROM GraderSession g WHERE g.graderSessionPK.sessionName = :sessionName"),
    @NamedQuery(name = "GraderSession.findByRequest",
            query = "SELECT g FROM GraderSession g WHERE g.request = :request"),
    @NamedQuery(name = "GraderSession.findByAvailable",
            query = "SELECT g FROM GraderSession g WHERE g.available = :available"),
    @NamedQuery(name = "GraderSession.findByApprovalPercentage",
            query = "SELECT g FROM GraderSession g WHERE g.approvalPercentage = :approvalPercentage"),
    @NamedQuery(name = "GraderSession.findByMaximumGrade",
            query = "SELECT g FROM GraderSession g WHERE g.maximumGrade = :maximumGrade"),
    @NamedQuery(name = "GraderSession.findByDecimalPrecision",
            query = "SELECT g FROM GraderSession g WHERE g.decimalPrecision = :decimalPrecision")})
@Table(name = "GRADER_SESSION", catalog = "OMRGrader", schema = "")
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@XmlRootElement()
public class GraderSession implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId()
    protected GraderSessionPK graderSessionPK;
    @Basic(optional = false)
    @NotNull()
    @Column(nullable = false)
    private Long request;
    @Basic(optional = false)
    @NotNull()
    @Column(nullable = false)
    private boolean available;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "approval_percentage", precision = 5, scale = 2)
    private Float approvalPercentage;
    @Column(name = "maximum_grade", precision = 5, scale = 2)
    private Float maximumGrade;
    @Size(max = 1)
    @Column(name = "decimal_precision", length = 1)
    private String decimalPrecision;

    public GraderSession() {
        super();
    }

    public GraderSession(GraderSessionPK graderSessionPK) {
        this.graderSessionPK = graderSessionPK;
    }

    public GraderSession(GraderSessionPK graderSessionPK, Long request,
            boolean available) {
        this.graderSessionPK = graderSessionPK;
        this.request = request;
        this.available = available;
    }

    public GraderSession(String electronicMail, String sessionName) {
        this.graderSessionPK = new GraderSessionPK(electronicMail, sessionName);
    }

    public GraderSessionPK getGraderSessionPK() {

        return (this.graderSessionPK);
    }

    public void setGraderSessionPK(GraderSessionPK graderSessionPK) {
        this.graderSessionPK = graderSessionPK;
    }

    public Long getRequest() {

        return (this.request);
    }

    public void setRequest(Long request) {
        this.request = request;
    }

    public boolean getAvailable() {

        return (this.available);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Float getApprovalPercentage() {

        return (this.approvalPercentage);
    }

    public void setApprovalPercentage(Float approvalPercentage) {
        this.approvalPercentage = approvalPercentage;
    }

    public Float getMaximumGrade() {

        return (this.maximumGrade);
    }

    public void setMaximumGrade(Float maximumGrade) {
        this.maximumGrade = maximumGrade;
    }

    public String getDecimalPrecision() {

        return (this.decimalPrecision);
    }

    public void setDecimalPrecision(String decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    @Override()
    public Serializable getPrimaryKey() {

        return (this.getGraderSessionPK());
    }

    @Override()
    public int hashCode() {
        int hash = 0;

        hash += (this.getGraderSessionPK() != null
                ? this.getGraderSessionPK().hashCode() : 0);

        return (hash);
    }

    @Override()
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GraderSession)) {

            return (false);
        }

        GraderSession other = (GraderSession) object;
        if (((this.getGraderSessionPK() == null)
                && (other.getGraderSessionPK() != null))
                || ((this.getGraderSessionPK() != null)
                && !(this.getGraderSessionPK().equals(other.getGraderSessionPK())))) {

            return (false);
        }

        return (true);
    }

    @Override()
    public String toString() {

        return ("co.edu.udea.web.omrgrader2_0.persistence.entity.GraderSession[ graderSessionPK="
                + this.getGraderSessionPK() + " ]");
    }
}