package co.edu.udea.web.omrgrader2_0.process.exception;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderProcessException extends Throwable {

    private static final long serialVersionUID = -4468696866004400516L;

    public OMRGraderProcessException() {
        super();
    }

    public OMRGraderProcessException(String message) {
        super(message);
    }

    public OMRGraderProcessException(Throwable cause) {
        super(cause);
    }

    public OMRGraderProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public OMRGraderProcessException(String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}