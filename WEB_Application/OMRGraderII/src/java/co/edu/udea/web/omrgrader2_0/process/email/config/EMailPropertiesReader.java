package co.edu.udea.web.omrgrader2_0.process.email.config;

import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.email.exception.OMRGraderEmailException;
import co.edu.udea.web.omrgrader2_0.util.collection.ListUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class EMailPropertiesReader {

    private EMailPropertiesReader() {
        super();
    }

    public static List<String> readProperties(List<String> propertyNames,
            String path) throws OMRGraderEmailException {

        if (ListUtil.isEmptyStringList(propertyNames)) {

            return (null);
        }

        List<String> propertyValueList = new ArrayList<>();
        Properties properties = new Properties();
        try {
            properties.load(EmailSender.class.getClassLoader().getResourceAsStream(
                    path));

            if (!properties.isEmpty()) {
                for (String s : propertyNames) {
                    propertyValueList.add(properties.getProperty(s));
                }
            }
        } catch (IOException e) {
            throw new OMRGraderEmailException(
                    "Fatal error while application was trying to read email "
                    + "properties.",
                    e.getCause());
        }

        return (propertyValueList);
    }
}