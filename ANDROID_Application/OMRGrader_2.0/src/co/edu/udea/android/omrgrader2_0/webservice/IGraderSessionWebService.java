package co.edu.udea.android.omrgrader2_0.webservice;

import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IGraderSessionWebService {

	public GraderSession createGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException;

	public GraderSession finishGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException;
}