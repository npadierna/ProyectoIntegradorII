package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
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
    private GraderSession graderSession;
    private long key;

    public GraderSessionThread(long key, GraderSession graderSession,
            IGraderSessionDAO graderSessionDAO,
            ImageFileManager imageFileManagement,
            IThreadNotifier threadNotifier) {
        this.key = key;
        this.graderSession = graderSession;
        this.graderSessionDAO = graderSessionDAO;
        this.imageFileManagement = imageFileManagement;
        this.threadNotifier = threadNotifier;
    }

    public GraderSession getGraderSession() {

        return (this.graderSession);
    }

    public void setGraderSession(GraderSession graderSession) {
        this.graderSession = graderSession;
    }

    public long getKey() {

        return (this.key);
    }

    public void setKey(long key) {
        this.key = key;
    }

    @Override()
    public void run() {
        // TODO: Invocar las funciones para efectuar la calificación de los exámenes.
        long storageDirectoryPathName = this.imageFileManagement.
                buildStorageDirectoryPathName(this.getGraderSession(), false);

        try {
            boolean eliminationResult = this.imageFileManagement
                    .deleteStorageDirectory(String.valueOf(
                    storageDirectoryPathName));
            this.graderSessionDAO.delete(this.getGraderSession()
                    .getGraderSessionPK());

            System.out.format("%s: %d", "Directory Name", storageDirectoryPathName);

            this.executeNotification();
        } catch (OMRGraderProcessException | OMRGraderPersistenceException e) {
            Logger.getLogger(TAG).log(Level.SEVERE,
                    "Error while the Grader Session Thread was trying to manage a Grader Session.",
                    e);
        }
    }

    private void executeNotification() {
        this.threadNotifier.notifyEvent(new Object[]{this});
    }
}