package co.edu.udea.web.omrgrader2_0.webservice.rest.impl;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import co.edu.udea.web.omrgrader2_0.webservice.IGraderSessionWebService;
import co.edu.udea.web.omrgrader2_0.webservice.rest.contract.WebServicePathContract;
import java.util.Date;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Path(value = WebServicePathContract.GraderSessionWebServiceContract.ROOT_PATH)
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Service()
@WebService(endpointInterface = WebServicePathContract.GraderSessionWebServiceContract.END_POINT_INTERFACE)
public class GraderSessionWebServiceImpl implements IGraderSessionWebService {

    private static final String TAG = GraderSessionWebServiceImpl.class.getSimpleName();
    @Autowired()
    private IGraderSessionDAO graderSessionDAO;
    @Autowired()
    private ImageFileManager imageFileManagement;

    public GraderSessionWebServiceImpl() {
        super();
    }

    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Override()
    @Path(value = WebServicePathContract.GraderSessionWebServiceContract.CREATE_GRADER_SESSION_PATH)
    @POST()
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response createGraderSession(GraderSession graderSession) {
        if (!this.preprocessGraderSession(graderSession, true)) {

            return (Response.status(Response.Status.BAD_REQUEST).build());
        }

        long storageDirectoryPathName = this.buildStorageDirectoryPathName(
                graderSession, true);

        try {
            this.imageFileManagement.createStorageDirectory(
                    String.valueOf(storageDirectoryPathName));
            this.graderSessionDAO.save(graderSession);
        } catch (OMRGraderProcessException | OMRGraderPersistenceException e) {

            return (Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build());
        }

        return (Response.ok(graderSession).build());
    }

    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Override()
    @Path(value = WebServicePathContract.GraderSessionWebServiceContract.FINISH_GRADER_SESSION_PATH)
    @POST()
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response finishGraderSession(GraderSession graderSession) {
        if (!this.preprocessGraderSession(graderSession, false)) {

            return (Response.status(Response.Status.BAD_REQUEST).build());
        }

        long storageDirectoryPathName = this.buildStorageDirectoryPathName(
                graderSession, false);

        try {
            boolean eliminationResult = this.imageFileManagement
                    .deleteStorageDirectory(String.valueOf(
                    storageDirectoryPathName));
        } catch (OMRGraderProcessException ex) {

            return (Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build());
        }

        return (Response.ok(graderSession).build());
    }

    private boolean preprocessGraderSession(GraderSession graderSession,
            boolean isCreation) {
        if ((graderSession == null)
                || (graderSession.getGraderSessionPK() == null)
                || (TextUtil.isEmpty(graderSession.getGraderSessionPK().getSessionName())
                || (TextUtil.isEmpty(graderSession.getGraderSessionPK().getElectronicMail())))) {

            return (false);
        }

        if ((!isCreation) && (graderSession.getRequest() == null)) {

            return (false);
        }

        graderSession.getGraderSessionPK().setElectronicMail(TextUtil.toLowerCase(
                graderSession.getGraderSessionPK().getElectronicMail()));
        graderSession.getGraderSessionPK().setSessionName(
                graderSession.getGraderSessionPK().getSessionName().trim());

        return (true);
    }

    private long buildStorageDirectoryPathName(GraderSession graderSession,
            boolean isCreation) {
        if (isCreation) {
            graderSession.setRequest(new Date());
        }

        return (Math.abs(graderSession.getGraderSessionPK().getElectronicMail().hashCode()
                + graderSession.getGraderSessionPK().getSessionName().hashCode()
                + graderSession.getRequest().hashCode()));
    }
}