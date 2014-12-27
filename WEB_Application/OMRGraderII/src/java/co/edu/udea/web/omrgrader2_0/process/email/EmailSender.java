package co.edu.udea.web.omrgrader2_0.process.email;

import co.edu.udea.web.omrgrader2_0.process.email.exception.OMRGraderEmailException;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Component()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public final class EmailSender {

    private Properties properties;
    private String fromEmail;
    private String password;
    private String host;
    private String port;
    private Session session;

    public EmailSender() throws OMRGraderEmailException {
        this.properties = new Properties();
        this.configureEMailProperties();
    }

    public boolean sendEmail(String toEmail, String subject, String text)
            throws OMRGraderEmailException {
        try {
            Message message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new OMRGraderEmailException(
                    "Fatal error while the application was trying to send a "
                    + "E-Mail.", e.getCause());
        }

        return (true);
    }

    public boolean sendEMail(String toEmail, String fullFilePath,
            String examName) throws OMRGraderEmailException {
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
                    + "resultados del examen.</p><p>--<b><h4><font color "
                    + "=\"gray\">Calificación Examen - UdeA</h4></font></p></b>";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Agregando las múltiples partes al mensaje
            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new OMRGraderEmailException(
                    "Fatal error while the application was trying to send a "
                    + "E-Mail.", e.getCause());
        }

        return (true);
    }

    private void getEMailAccountProperties() throws OMRGraderEmailException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream(
                "/co/edu/udea/web/omrgrader2_0/process/email/config/"
                + "emailsender.properties");

        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new OMRGraderEmailException(
                    "Fatal error while application was trying to read E-Mail "
                    + "properties.", e.getCause());
        }

        this.fromEmail = properties.getProperty("EMAIL_ADDRESS");
        this.password = properties.getProperty("PASSWORD");
        this.host = properties.getProperty("HOST");
        this.port = properties.getProperty("PORT");
    }

    private void getSessionProperties(String host, String port) {
        this.properties.put("mail.smtp.host", host);
        this.properties.put("mail.smtp.socketFactory.port", port);
        this.properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.port", port);
    }

    private void configureEMailProperties() throws OMRGraderEmailException {
        this.getEMailAccountProperties();
        this.getSessionProperties(this.host, this.port);

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override()
            protected PasswordAuthentication getPasswordAuthentication() {

                return (new PasswordAuthentication(fromEmail, password));
            }
        });
    }
}