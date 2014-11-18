package co.edu.udea.web.omrgrader2_0.process.dispatcher;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class GraderSessionThread extends Thread {

    @Autowired()
    private ImageFileManager imageFileManagement;
    private GraderSession graderSession;

    public GraderSessionThread(GraderSession graderSession) {
        this.graderSession = graderSession;
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

        System.out.format("%s: %d", "Directory Name", storageDirectoryPathName);
    }
}