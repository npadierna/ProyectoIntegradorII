package co.edu.udea.web.omrgrader2_0.util.uitext;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class UIText {

    private static final Logger logger = Logger.getLogger(UIText.class.getName());
    private static final String RESOURCE_PACKAGE_NAME =
            "co.edu.udea.test.uimessages.config.UIMessages";
    private static ResourceBundle currentResourcePackage =
            ResourceBundle.getBundle(RESOURCE_PACKAGE_NAME);

    private UIText() {
    }

    public static void loadResourcePackage(Locale locale) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (locale != null) {
            currentResourcePackage = ResourceBundle.getBundle(
                    RESOURCE_PACKAGE_NAME, locale);
        } else {
            currentResourcePackage = ResourceBundle.getBundle(
                    RESOURCE_PACKAGE_NAME, facesContext.getApplication().
                    getDefaultLocale());
        }
    }

    public static String getText(String key) {
        try {

            return (currentResourcePackage.getString(key));
        } catch (MissingResourceException excepcion) {
            logger.error("Exception trying to get text from properties: ",
                    excepcion);

            return "Invalid Key: [" + key + "]";
        }
    }
}
