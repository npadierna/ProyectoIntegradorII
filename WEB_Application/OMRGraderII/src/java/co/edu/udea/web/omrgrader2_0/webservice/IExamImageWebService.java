package co.edu.udea.web.omrgrader2_0.webservice;

import com.sun.jersey.core.header.FormDataContentDisposition;
import java.io.InputStream;
import javax.jws.WebService;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@WebService()
public interface IExamImageWebService {

    public Response uploadReferenceExamImageFile(
            InputStream imageFileInputStream,
            FormDataContentDisposition imageFileContentDisposition,
            String directoryStorageId);

    public Response uploadStudentExamImageFile(
            InputStream imageFileInputStream,
            FormDataContentDisposition imageFileContentDisposition,
            String imageFileId, String directoryStorageId);
}