package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    public Test() {
        super();
    }

    public Properties readProperties() {
        Properties properties = new Properties();

        InputStream inputStream = getClass().getResourceAsStream("/co/edu/udea/web/omrgrader2_0/process/email/config/"
                + "emailsender.properties");
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (properties);
    }
}
