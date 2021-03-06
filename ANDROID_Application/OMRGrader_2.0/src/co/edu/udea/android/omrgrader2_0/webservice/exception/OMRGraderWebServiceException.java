package co.edu.udea.android.omrgrader2_0.webservice.exception;

import java.io.Serializable;

import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderWebServiceException extends Throwable implements
		Serializable {

	private static final long serialVersionUID = -8174178328694856461L;

	private static final String TAG = OMRGraderWebServiceException.class
			.getSimpleName();

	public OMRGraderWebServiceException() {
		super();

		Log.e(TAG, this.getMessage(), this);
	}

	public OMRGraderWebServiceException(String detailMessage) {
		super(detailMessage);

		Log.e(TAG, detailMessage, this);
	}

	public OMRGraderWebServiceException(Throwable throwable) {
		super(throwable);

		Log.e(TAG, throwable.getMessage(), throwable);
	}

	public OMRGraderWebServiceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);

		Log.e(TAG, detailMessage, throwable);
	}
}