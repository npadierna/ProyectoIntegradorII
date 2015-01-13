package co.edu.udea.web.omrgrader2_0.process.image.exception;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class OMRGraderImageException extends Throwable {

    private static final long serialVersionUID = 955598166997459193L;

    public OMRGraderImageException() {
        super();
    }

    public OMRGraderImageException(String message) {
        super(message);
    }

    public OMRGraderImageException(Throwable cause) {
        super(cause);
    }

    public OMRGraderImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public OMRGraderImageException(String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}