package co.edu.udea.web.omrgrader2_0.process.email.exception;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderEmailException extends Throwable {

    private static final long serialVersionUID = -916431015095985596L;

    public OMRGraderEmailException() {
        super();
    }

    public OMRGraderEmailException(String message) {
        super(message);
    }

    public OMRGraderEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public OMRGraderEmailException(Throwable cause) {
        super(cause);
    }

    public OMRGraderEmailException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}