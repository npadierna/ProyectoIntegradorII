package co.edu.udea.web.omrgrader2_0.util.text;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class TextUtil {

    private TextUtil() {
        super();
    }

    public static boolean isEmpty(String text) {

        return ((text == null) || (text.trim().isEmpty()));
    }

    public static String toLowerCase(String text) {

        return (text.trim().toLowerCase());
    }

    public static String toUpperCase(String text) {

        return (text.trim().toUpperCase());
    }
}