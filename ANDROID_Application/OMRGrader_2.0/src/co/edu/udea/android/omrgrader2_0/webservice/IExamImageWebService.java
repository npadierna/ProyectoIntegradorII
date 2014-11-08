package co.edu.udea.android.omrgrader2_0.webservice;

import android.graphics.Bitmap;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IExamImageWebService {

	public int buildStorageDirectoryPathName(GraderSession graderSession);

	public boolean uploadReferenceExamImageFile(
			Bitmap referenceExamImageBitmap, GraderSession graderSession)
			throws OMRGraderWebServiceException;

	public boolean uploadStudentExamImageFile(Bitmap studentExamImageBitmap,
			Integer imageFileId, GraderSession graderSession)
			throws OMRGraderWebServiceException;
}