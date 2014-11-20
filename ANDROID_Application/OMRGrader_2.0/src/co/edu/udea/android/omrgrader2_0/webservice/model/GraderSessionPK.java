package co.edu.udea.android.omrgrader2_0.webservice.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSessionPK implements IJSONContext, Serializable {

	// jsonObject.put(E_MAIL_ACCOUNT, this.geteMailAccount());
	// jsonObject.put(GRADER_SESSION_NAME, this.getGraderSessionName());
	private static final long serialVersionUID = 2045541944837331846L;

	public static final String ELECTRONIC_MAIL = "electronicMail";
	public static final String SESSION_NAME = "sessionName";

	private String electronicMail;
	private String sessionName;

	public GraderSessionPK() {
		super();
	}

	public GraderSessionPK(JSONObject jsonObject) throws JSONException {
		this.unpackJsonOjectToEntity(jsonObject);
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
	public JSONObject packEntityToJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(ELECTRONIC_MAIL, this.getElectronicMail());
		jsonObject.put(SESSION_NAME, this.getSessionName());

		return (jsonObject);
	}

	@Override()
	public IJSONContext unpackJsonOjectToEntity(JSONObject jsonObject)
			throws JSONException {
		this.setElectronicMail(jsonObject.getString(ELECTRONIC_MAIL));
		this.setSessionName(jsonObject.getString(SESSION_NAME));

		return (this);
	}

	@Override()
	public int hashCode() {
		int hash = 0;

		hash += (this.getElectronicMail() != null ? this.getElectronicMail()
				.hashCode() : 0);
		hash += (this.getSessionName() != null ? this.getSessionName()
				.hashCode() : 0);

		return (hash);
	}

	@Override()
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof GraderSessionPK)) {

			return (false);
		}

		GraderSessionPK other = (GraderSessionPK) object;
		if (((this.getElectronicMail() == null) && (other.getElectronicMail() != null))
				|| ((this.getElectronicMail() != null) && !(this
						.getElectronicMail().equals(other.getElectronicMail())))) {

			return (false);
		}

		if (((this.getSessionName() == null) && (other.getSessionName() != null))
				|| ((this.getSessionName() != null) && !(this.getSessionName()
						.equals(other.getSessionName())))) {

			return (false);
		}

		return (true);
	}

	@Override()
	public String toString() {

		return ("co.edu.udea.android.omrgrader2_0.webservice.model.GraderSessionPK[ electronicMail="
				+ this.getElectronicMail()
				+ ", sessionName="
				+ this.getSessionName() + " ]");
	}
}