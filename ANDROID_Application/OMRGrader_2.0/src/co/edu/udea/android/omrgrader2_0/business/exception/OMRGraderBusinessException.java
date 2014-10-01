package co.edu.udea.android.omrgrader2_0.business.exception;

import java.io.Serializable;

import android.util.Log;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderBusinessException extends Throwable implements
		Serializable {

	private static final long serialVersionUID = -8174178328694856461L;

	private static final String TAG = OMRGraderBusinessException.class
			.getSimpleName();

	public OMRGraderBusinessException() {
		super();

		Log.e(TAG, this.getMessage(), this);
	}

	public OMRGraderBusinessException(String detailMessage) {
		super(detailMessage);

		Log.e(TAG, detailMessage, this);
	}

	public OMRGraderBusinessException(Throwable throwable) {
		super(throwable);

		Log.e(TAG, throwable.getMessage(), throwable);
	}

	public OMRGraderBusinessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);

		Log.e(TAG, detailMessage, throwable);
	}
}