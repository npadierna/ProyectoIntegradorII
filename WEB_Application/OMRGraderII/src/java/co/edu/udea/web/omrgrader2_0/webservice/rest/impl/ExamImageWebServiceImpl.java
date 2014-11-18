package co.edu.udea.web.omrgrader2_0.webservice.rest.impl;

import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import co.edu.udea.web.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.web.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.web.omrgrader2_0.webservice.rest.contract.WebServicePathContract;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path(value = WebServicePathContract.ExamImageWebServiceContract.ROOT_PATH)
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Service()
@WebService(endpointInterface = WebServicePathContract.ExamImageWebServiceContract.END_POINT_INTERFACE)
public class ExamImageWebServiceImpl implements IExamImageWebService {

    private static final String TAG = ExamImageWebServiceImpl.class.getSimpleName();
    @Autowired()
    private ImageFileManager imageFileManagement;

    public ExamImageWebServiceImpl() {
        super();
    }

    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    @Override()
    @Path(value = WebServicePathContract.ExamImageWebServiceContract.UPLOAD_REFERENCE_IMAGE_FILE_PATH)
    @POST()
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response uploadReferenceExamImageFile(
            @FormDataParam(value = WebServicePathContract.ExamImageWebServiceContract.STREAM_REFERENCE_IMAGE_FILE_PARAMETER) InputStream imageFileInputStream,
            @FormDataParam(value = WebServicePathContract.ExamImageWebServiceContract.CONTENT_REFERENCE_IMAGE_FILE_PARAMETER) FormDataContentDisposition imageFileContentDisposition,
            @QueryParam(value = WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER) String directoryStorageId) {
        try {
            if (!this.receiveExamImageFile(imageFileInputStream,
                    this.imageFileManagement
                    .buildUploadedFileDirectoryPath(directoryStorageId, false),
                    this.imageFileManagement
                    .buildReferenceExamImageFileDefaultName())) {

                return (Response.status(Response.Status.BAD_REQUEST).build());
            }
        } catch (OMRGraderWebServiceException | OMRGraderProcessException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Web Service was trying to upload a new Reference Exam Image File.",
                    e);

            return (Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build());
        }

        return (Response.status(Response.Status.OK).build());
    }

    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    @Override()
    @Path(value = WebServicePathContract.ExamImageWebServiceContract.UPLOAD_STUDENT_IMAGE_FILE_PATH)
    @POST()
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response uploadStudentExamImageFile(
            @FormDataParam(value = WebServicePathContract.ExamImageWebServiceContract.STREAM_STUDENT_IMAGE_FILE_PARAMETER) InputStream imageFileInputStream,
            @FormDataParam(value = WebServicePathContract.ExamImageWebServiceContract.CONTENT_STUDENT_IMAGE_FILE_PARAMETER) FormDataContentDisposition imageFileContentDisposition,
            @QueryParam(value = WebServicePathContract.ExamImageWebServiceContract.STUDENT_EXAM_IMAGE_FILE_ID_PARAMETER) String imageFileId,
            @QueryParam(value = WebServicePathContract.ExamImageWebServiceContract.DIRECTORY_STORAGE_ID_PARAMETER) String directoryStorageId) {
        try {
            if (!this.receiveExamImageFile(imageFileInputStream,
                    this.imageFileManagement
                    .buildUploadedFileDirectoryPath(directoryStorageId, true),
                    this.imageFileManagement
                    .buildStudentExamImageFileName(imageFileId))) {

                return (Response.status(Response.Status.BAD_REQUEST).build());
            }
        } catch (OMRGraderWebServiceException | OMRGraderProcessException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Web Service was trying to upload a new Student Exam Image File.",
                    e);

            return (Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build());
        }

        return (Response.status(Response.Status.OK).build());
    }

    private boolean receiveExamImageFile(InputStream imageFileInputStream,
            String fullDirectoryStoragePath, String examImageFileName)
            throws OMRGraderWebServiceException {
        if ((TextUtil.isEmpty(fullDirectoryStoragePath))
                || (TextUtil.isEmpty(examImageFileName))
                || (imageFileInputStream == null)) {

            return (false);
        }

        File fullDirectoryStorageFile = new File(fullDirectoryStoragePath);
        if ((!fullDirectoryStorageFile.exists())
                || (!fullDirectoryStorageFile.canWrite())) {

            return (false);
        }

        try {
            this.imageFileManagement.saveExamImageFile(imageFileInputStream,
                    fullDirectoryStoragePath.concat(examImageFileName));
        } catch (OMRGraderProcessException e) {
            throw new OMRGraderWebServiceException(
                    "The application could not upload the Image.",
                    e.getCause());
        }

        return (true);
    }
}