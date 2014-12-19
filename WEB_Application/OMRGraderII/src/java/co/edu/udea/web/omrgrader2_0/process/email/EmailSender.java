package co.edu.udea.web.omrgrader2_0.process.email;

import co.edu.udea.web.omrgrader2_0.process.email.config.EMailPropertiesReader;
import co.edu.udea.web.omrgrader2_0.process.email.exception.EmailSenderException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
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

    private Properties properties;
    private String fromEmail;
    private String password;
    private String host;
    private String port;
    private Session session;

    public EmailSender() throws EmailSenderException {
        this.properties = new Properties();
        this.configEMailProperties();
    }

    public boolean sendEmail(String toEmail, String subject, String text)
            throws EmailSenderException {
        try {
            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailSenderException(
                    "Fatal error while the application was trying to send a E-Mail.",
                    e.getCause());
        }

        return (true);
    }

    public boolean sendEMail(String toEmail, String fullFilePath, String examName)
            throws EmailSenderException {
        toEmail = TextUtil.toLowerCase(toEmail);

        try {
            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.fromEmail));

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
            DataSource dataSource = new FileDataSource(fullFilePath);
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

        } catch (MessagingException e) {
            throw new EmailSenderException(
                    "Fatal error while the application was trying to send a E-Mail.",
                    e.getCause());
        }

        return (true);
    }

    private void getPropertiesValueList() throws EmailSenderException {
        List<String> propertyNameList = new ArrayList<>();
        propertyNameList.add("EMAIL_ADDRESS");
        propertyNameList.add("PASSWORD");
        propertyNameList.add("HOST");
        propertyNameList.add("PORT");
        List<String> propertyValueList;

        propertyValueList = EMailPropertiesReader.readProperties(propertyNameList,
                "co/edu/udea/web/omrgrader2_0/process/email/config/"
                + "emailsender.properties");

        this.fromEmail = propertyValueList.get(0);
        this.password = propertyValueList.get(1);
        this.host = propertyValueList.get(2);
        this.port = propertyValueList.get(3);
    }

    private void getSessionProperties(String host, String port) {
        this.properties.put("mail.smtp.host", host);
        this.properties.put("mail.smtp.socketFactory.port", port);
        this.properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.port", port);
    }

    private void configEMailProperties() throws EmailSenderException {
        this.getPropertiesValueList();

        this.getSessionProperties(this.host, this.port);

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override()
            protected PasswordAuthentication getPasswordAuthentication() {

                return (new PasswordAuthentication(fromEmail, password));
            }
        });
    }
}