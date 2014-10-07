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

	public static final String APPROVAL_PERCENTAGE = "approvalPercentage";
	public static final String MAXIMUM_GRADE = "maximumGrade";
	public static final String DECIMAL_PRECISION = "decimalPrecision";
	public static final String REQUEST_TIME_STAMP = "requestTimeStamp";
	public static final String E_MAIL_ACCOUNT = "eMailAccount";
	public static final String GRADER_SESSION_NAME = "graderSessionName";

	private float approvalPercentage;
	private float maximumGrade;
	private int decimalPrecision;
	private Long requestTimeStamp;
	private String eMailAccount;
	private String graderSessionName;

	public GraderSession() {
		super();
	}

	public GraderSession(JSONObject jsonObject) throws JSONException {
		this.unpackJsonOjectToEntity(jsonObject);
	}

	public GraderSession(float approvalPercentage, float maximumGrade,
			int decimalPrecision, Long requestTimeStamp, String eMailAccount,
			String graderSessionName) {
		this.setApprovalPercentage(approvalPercentage);
		this.setMaximumGrade(maximumGrade);
		this.setDecimalPrecision(decimalPrecision);
		this.setRequestTimeStamp(requestTimeStamp);
		this.seteMailAccount(eMailAccount);
		this.setGraderSessionName(graderSessionName);
	}

	public float getApprovalPercentage() {

		return (this.approvalPercentage);
	}

	public void setApprovalPercentage(float approvalPercentage) {
		this.approvalPercentage = approvalPercentage;
	}

	public float getMaximumGrade() {

		return (this.maximumGrade);
	}

	public void setMaximumGrade(float maximumGrade) {
		this.maximumGrade = maximumGrade;
	}

	public int getDecimalPrecision() {

		return (this.decimalPrecision);
	}

	public void setDecimalPrecision(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public Long getRequestTimeStamp() {

		return (this.requestTimeStamp);
	}

	public void setRequestTimeStamp(Long requestTimeStamp) {
		this.requestTimeStamp = requestTimeStamp;
	}

	public String geteMailAccount() {

		return (this.eMailAccount);
	}

	public void seteMailAccount(String eMailAccount) {
		this.eMailAccount = eMailAccount;
	}

	public String getGraderSessionName() {

		return (this.graderSessionName);
	}

	public void setGraderSessionName(String graderSessionName) {
		this.graderSessionName = graderSessionName;
	}

	@Override()
	public JSONObject packEntityToJsonObject(IJSONContext jsonContext)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put(APPROVAL_PERCENTAGE, this.getApprovalPercentage());
		jsonObject.put(MAXIMUM_GRADE, this.getMaximumGrade());
		jsonObject.put(DECIMAL_PRECISION, this.getDecimalPrecision());
		jsonObject.put(REQUEST_TIME_STAMP, this.getRequestTimeStamp());
		jsonObject.put(E_MAIL_ACCOUNT, this.geteMailAccount());
		jsonObject.put(GRADER_SESSION_NAME, this.getGraderSessionName());

		return (jsonObject);
	}

	@Override()
	public IJSONContext unpackJsonOjectToEntity(JSONObject jsonObject)
			throws JSONException {
		this.seteMailAccount(jsonObject.getString(E_MAIL_ACCOUNT));
		this.setGraderSessionName(jsonObject.getString(GRADER_SESSION_NAME));

		return (this);
	}
}