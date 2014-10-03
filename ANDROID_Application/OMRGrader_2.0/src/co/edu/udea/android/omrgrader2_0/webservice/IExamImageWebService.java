package co.edu.udea.android.omrgrader2_0.webservice;

import android.graphics.Bitmap;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IExamImageWebService {

	public boolean uploadReferenceExamImageFile(Bitmap referenceExamImageBitmap)
			throws OMRGraderWebServiceException;

	public boolean uploadStudentExamImageFile(Bitmap studentExamImageBitmap)
			throws OMRGraderWebServiceException;
}