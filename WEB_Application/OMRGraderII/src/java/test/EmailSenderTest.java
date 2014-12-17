package test;

import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSenderTest {

    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();

        probarConAdjunto(emailSender);
    }

    public static void probarConAdjunto(EmailSender emailSender) {
        try {
            emailSender.sendEMail("/home/pivb/Escritorio/Temporales/OpenCV Linux.txt",
                    "Testing", "anderssongarciasotelo@gmail.com");
        } catch (OMRGraderProcessException ex) {
            Logger.getLogger(EmailSenderTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void probarSinAdjunto(EmailSender emailSender) {
        try {
            emailSender.sendEmail("anderssongs5@outlook.com", "Oelo", "Probando maricadas.");
        } catch (OMRGraderProcessException ex) {
            Logger.getLogger(EmailSenderTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}
