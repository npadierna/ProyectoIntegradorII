package co.edu.udea.web.omrgrader2_0.process.dispatcher;

import co.edu.udea.web.omrgrader2_0.process.dispatcher.thread.GraderSessionThread;
import co.edu.udea.web.omrgrader2_0.process.dispatcher.thread.GraderSessionThreadPool;
import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Service()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public final class GraderSessionDispatcherThread extends Thread {

    private static final String TAG = GraderSessionDispatcherThread.class.getSimpleName();
    private static final long TIME_FOR_SLEEPING = 60000L;
    @Autowired()
    private IGraderSessionDAO graderSessionDAO;
    @Autowired()
    private GraderSessionThreadPool graderSessionThreadPool;
    private boolean isRunning;

    public GraderSessionDispatcherThread() {
        super(TAG);

        this.isRunning = true;
    }

    @Override()
    public void run() {
        GraderSession graderSession;
        GraderSessionThread graderSessionThread;

        while (this.isRunning) {
            try {
                graderSession = this.graderSessionDAO.findFirstByRequest();

                if (graderSession != null) {
                    graderSessionThread = this.graderSessionThreadPool.
                            findFreeGraderSessionThread(graderSession);

                    if (graderSessionThread != null) {
                        graderSession.setAvailable(false);
                        graderSession = this.graderSessionDAO.update(graderSession);

                        graderSessionThread.setGraderSession(graderSession);

                        this.execute(graderSessionThread);

                        continue;
                    }
                }

                Thread.sleep(TIME_FOR_SLEEPING);
            } catch (OMRGraderPersistenceException | InterruptedException e) {
                Logger.getLogger(TAG).log(Level.SEVERE,
                        "Error while the OMRGrader Dispatcher was trying to manage a Grader Session.",
                        e);
            }
        }
    }

    public void finish() {
        this.isRunning = false;
    }

    private void execute(GraderSessionThread graderSessionThread) {
        if (graderSessionThread != null) {
            graderSessionThread.start();
        }
    }
}