package co.edu.udea.web.omrgrader2_0.webservice;


import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import javax.jws.WebService;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@WebService()
public interface IGraderSessionWebService {

    public Response createGraderSession(GraderSession graderSession);

    public Response finishGraderSession(GraderSession graderSession);
}