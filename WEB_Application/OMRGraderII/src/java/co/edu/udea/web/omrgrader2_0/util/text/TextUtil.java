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

    public static String removeSpaces(String text) {
        StringBuilder formattedText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                formattedText.append(text.charAt(i));
            }
        }

        return (formattedText.toString());
    }

    public static boolean hasOnlyNumbers(String text) {
        if (isEmpty(text)) {

            return (true);
        }

        char c;
        for (int i = 0; i < text.length(); i++) {
            c = text.charAt(i);

            if (c < '0' || c > '9') {

                return (true);
            }
        }

        return (false);
    }
}