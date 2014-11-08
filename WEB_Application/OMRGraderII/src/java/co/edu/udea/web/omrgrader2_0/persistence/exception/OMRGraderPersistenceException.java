package co.edu.udea.web.omrgrader2_0.persistence.exception;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderPersistenceException extends Throwable {

    private static final long serialVersionUID = -4468696866004400516L;

    public OMRGraderPersistenceException() {
        super();
    }

    public OMRGraderPersistenceException(String message) {
        super(message);
    }

    public OMRGraderPersistenceException(Throwable cause) {
        super(cause);
    }

    public OMRGraderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OMRGraderPersistenceException(String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}