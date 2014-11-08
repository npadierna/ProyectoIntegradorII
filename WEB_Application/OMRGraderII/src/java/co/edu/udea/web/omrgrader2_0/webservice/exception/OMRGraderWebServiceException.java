package co.edu.udea.web.omrgrader2_0.webservice.exception;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderWebServiceException extends Throwable {

    private static final long serialVersionUID = -4468696866004400516L;

    public OMRGraderWebServiceException() {
        super();
    }

    public OMRGraderWebServiceException(String message) {
        super(message);
    }

    public OMRGraderWebServiceException(Throwable cause) {
        super(cause);
    }

    public OMRGraderWebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OMRGraderWebServiceException(String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}