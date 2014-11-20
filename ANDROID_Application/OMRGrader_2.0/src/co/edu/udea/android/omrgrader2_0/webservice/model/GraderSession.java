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
public class GraderSession implements IJSONContext, Serializable {

	private static final long serialVersionUID = -1921474439112952926L;

	public static final String GRADER_SESSION_PK = "graderSessionPK";
	public static final String REQUEST = "request";
	public static final String AVAILABLE = "available";
	public static final String APPROVAL_PERCENTAGE = "approvalPercentage";
	public static final String MAXIMUM_GRADE = "maximumGrade";
	public static final String DECIMAL_PRECISION = "decimalPrecision";

	protected GraderSessionPK graderSessionPK;
	private Long request;
	private boolean available;
	private Float approvalPercentage;
	private Float maximumGrade;
	private String decimalPrecision;

	public GraderSession() {
		this.setGraderSessionPK(new GraderSessionPK());
	}

	public GraderSession(JSONObject jsonObject) throws JSONException {
		this.unpackJsonOjectToEntity(jsonObject);
	}

	public GraderSession(String electronicMail, String sessionName) {
		this.setGraderSessionPK(new GraderSessionPK(electronicMail, sessionName));
	}

	public GraderSession(GraderSessionPK graderSessionPK, Long request,
			Float approvalPercentage, Float maximumGrade,
			String decimalPrecision) {
		this.setGraderSessionPK(graderSessionPK);
		this.setApprovalPercentage(approvalPercentage);
		this.setMaximumGrade(maximumGrade);
		this.setDecimalPrecision(decimalPrecision);
		this.setRequest(request);
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
	public JSONObject packEntityToJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(GRADER_SESSION_PK, this.getGraderSessionPK());
		jsonObject.put(REQUEST, this.getRequest());
		jsonObject.put(AVAILABLE, this.getAvailable());
		jsonObject.put(APPROVAL_PERCENTAGE, this.getApprovalPercentage());
		jsonObject.put(MAXIMUM_GRADE, this.getMaximumGrade());
		jsonObject.put(DECIMAL_PRECISION, this.getDecimalPrecision());

		return (jsonObject);
	}

	@Override()
	public IJSONContext unpackJsonOjectToEntity(JSONObject jsonObject)
			throws JSONException {
		this.setGraderSessionPK(new GraderSessionPK(jsonObject
				.getJSONObject(GRADER_SESSION_PK)));

		if (jsonObject.has(REQUEST)) {
			this.setRequest(jsonObject.getLong(REQUEST));
		}

		if (jsonObject.has(AVAILABLE)) {
			this.setAvailable(jsonObject.getBoolean(AVAILABLE));
		}

		if (jsonObject.has(APPROVAL_PERCENTAGE)) {
			this.setApprovalPercentage(Float.valueOf((float) jsonObject
					.getDouble(APPROVAL_PERCENTAGE)));
		}

		if (jsonObject.has(MAXIMUM_GRADE)) {
			this.setMaximumGrade(Float.valueOf((float) jsonObject
					.getDouble(MAXIMUM_GRADE)));
		}

		if (jsonObject.has(DECIMAL_PRECISION)) {
			this.setDecimalPrecision(jsonObject.getString(DECIMAL_PRECISION));
		}

		return (this);
	}

	@Override()
	public int hashCode() {
		int hash = 0;

		hash += (this.getGraderSessionPK() != null ? this.getGraderSessionPK()
				.hashCode() : 0);

		return (hash);
	}

	@Override()
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof GraderSession)) {

			return (false);
		}

		GraderSession other = (GraderSession) object;
		if (((this.getGraderSessionPK() == null) && (other.getGraderSessionPK() != null))
				|| ((this.getGraderSessionPK() != null) && !(this
						.getGraderSessionPK()
						.equals(other.getGraderSessionPK())))) {

			return (false);
		}

		return (true);
	}

	@Override()
	public String toString() {

		return ("co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession[ graderSessionPK="
				+ this.getGraderSessionPK() + " ]");
	}
}