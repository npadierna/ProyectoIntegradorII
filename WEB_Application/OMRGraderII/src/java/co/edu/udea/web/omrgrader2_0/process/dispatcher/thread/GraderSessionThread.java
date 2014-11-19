package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import co.edu.udea.web.omrgrader2_0.process.email.EmailSender;
import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSessionThread extends Thread {

    private static final String TAG = GraderSessionThread.class.getSimpleName();
    private IGraderSessionDAO graderSessionDAO;
    private IThreadNotifier threadNotifier;
    private ImageFileManager imageFileManagement;
    private EmailSender emailSender;
    private GraderSession graderSession;

    public GraderSessionThread(long id, GraderSession graderSession,
            IGraderSessionDAO graderSessionDAO, ImageFileManager imageFileManagement) {
        this.graderSession = graderSession;
        this.graderSessionDAO = graderSessionDAO;
        this.imageFileManagement = imageFileManagement;
        this.emailSender = new EmailSender();
    }

    public GraderSession getGraderSession() {

        return (this.graderSession);
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    @Override()
    public void run() {
        // TODO: Invocar las funciones para efectuar la calificación de los exámenes.

        long storageDirectoryPathName = this.imageFileManagement.
                buildStorageDirectoryPathName(this.getGraderSession(), false);

        try {
            this.emailSender.sendEmail(this.getGraderSession().
                    getGraderSessionPK().getElectronicMail());

            boolean eliminationResult = this.imageFileManagement
                    .deleteStorageDirectory(String.valueOf(
                    storageDirectoryPathName));
            this.graderSessionDAO.delete(this.getGraderSession()
                    .getGraderSessionPK());

            System.out.format("%s: %d", "Directory Name", storageDirectoryPathName);

            this.executeNotification();

            // TODO: Borrar tanto los directorios como esta entidad de la Base de Datos.
        } catch (OMRGraderProcessException | OMRGraderPersistenceException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Grader Session Thread was trying to manage a Grader Session.",
                    e);
        }
    }

    private void executeNotification() {
    }
}