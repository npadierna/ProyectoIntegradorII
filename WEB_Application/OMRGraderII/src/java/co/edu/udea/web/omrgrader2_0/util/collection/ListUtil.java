package co.edu.udea.web.omrgrader2_0.util.collection;

import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class ListUtil {

    private ListUtil() {
        super();
    }

    public static boolean isEmptyStringList(List<String> list) {

        return ((list == null) || (list.isEmpty()));
    }
}