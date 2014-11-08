package co.edu.udea.android.omrgrader2_0.webservice.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IJSONContext {

	public JSONObject packEntityToJsonObject() throws JSONException;

	public IJSONContext unpackJsonOjectToEntity(JSONObject jsonObject)
			throws JSONException;
}