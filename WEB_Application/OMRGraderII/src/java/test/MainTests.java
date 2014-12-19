package test;

import co.edu.udea.web.omrgrader2_0.process.image.opencv.OMRProcess;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class MainTest {

    public static void main(String[] args) {
        Properties constants = new Properties();
        long timeStart;
        long timeEnd;
        long fullTime;
        timeStart = System.currentTimeMillis();

        try {
            constants.load(MainTest.class.getClassLoader().getResourceAsStream(
                    "constants.properties"));
        } catch (IOException e) {
            System.out.println("Error al leer archivo de propiedades.");
            e.printStackTrace();
        }

        if (!constants.isEmpty()) {
            OMRProcess
                    .getInstance()
                    .executeProcessing(
                    constants.getProperty("refer_path"),
                    constants.getProperty("solu_path"),
                    constants.getProperty("processedImageDirectory"),
                    constants.getProperty("blackWhiteImageDirectory"),
                    constants
                    .getProperty("examForProcessingName-Processed"),
                    constants
                    .getProperty("examForProcessingName-BlackAndWhite"));
        } else {
            System.out.println("Propiedades vacías");
        }

        timeEnd = System.currentTimeMillis();
        fullTime = timeEnd - timeStart;
        System.out.println("Time Processing: " + fullTime + "milliseconds.");
    }
}