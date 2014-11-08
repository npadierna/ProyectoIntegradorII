package co.edu.udea.web.omrgrader2_0.process.email;

import co.edu.udea.web.omrgrader2_0.process.email.config.EMailPropertiesReader;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class EmailSender {

    public EmailSender() {
        super();
    }

    public boolean sendMail(String filePath, String examName, String toEmail)
            throws OMRGraderProcessException {
        toEmail = TextUtil.toLowerCase(toEmail);
        List<String> propertyNameList = new ArrayList<>();
        propertyNameList.add("EMAIL_ADDRESS");
        propertyNameList.add("PASSWORD");
        propertyNameList.add("HOST");
        propertyNameList.add("PORT");
        List<String> propertyValueList;

        try {
            propertyValueList = EMailPropertiesReader.readProperties(propertyNameList,
                    "co/edu/udea/web/omrgrader2_0/process/email/config/"
                    + "emailsender.properties");


            if (propertyValueList.isEmpty()) {

                return (false);
            }

            final String fromEmail = propertyValueList.get(0);
            final String password = propertyValueList.get(1);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", propertyValueList.get(2));
            properties.put("mail.smtp.socketFactory.port", propertyValueList.get(3));
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", propertyValueList.get(3));

            Session session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                @Override()
                protected PasswordAuthentication getPasswordAuthentication() {

                    return (new PasswordAuthentication(fromEmail, password));
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            // Asignando el receptor
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            // Asignando asunto
            String subject = "Calificación Examen: " + examName;
            message.setSubject(subject);

//------------------------ Mensaje con múltiples partes ------------------------
            Multipart multipart = new MimeMultipart();

            // Cuerpo del mensaje
            // Agregando el archivo adjunto
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName(dataSource.getName());
            multipart.addBodyPart(messageBodyPart);

            // HTML
            messageBodyPart = new MimeBodyPart();
            String htmlText = "<h2>Calificación de Examen: " + examName
                    + "</h2><p>Adjunto se encuentra la hoja de cálculo con los "
                    + "resultados del examen.</p><p>--<b><h4><font color =\"gray\">"
                    + "Calificación Examen - UdeA</h4></font></p></b>";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Agregando las múltiples partes al mensaje
            message.setContent(multipart);

            Transport.send(message);
        } catch (IOException | MessagingException e) {
            throw new OMRGraderProcessException(
                    "Fatal error while the application was trying to send a E-Mail.",
                    e.getCause());
        }

        return (true);
    }
}
