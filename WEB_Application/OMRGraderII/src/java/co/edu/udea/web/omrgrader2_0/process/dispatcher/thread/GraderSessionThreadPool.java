package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public final class GraderSessionThreadPool implements IThreadNotifier {

    public static final int THREAD_POOL_SIZE = 10;
    private static List<GraderSessionThread> threadPoolList;
    @Autowired()
    private IGraderSessionDAO graderSessionDAO;
    @Autowired()
    private ImageFileManager imageFileManagement;

    static {
        threadPoolList = new ArrayList<>(THREAD_POOL_SIZE);
        for (int index = 0; index < THREAD_POOL_SIZE; index++) {
            threadPoolList.add(null);
        }
    }

    public GraderSessionThreadPool() {
        super();
    }

    public synchronized GraderSessionThread findFreeGraderSessionThread(
            GraderSession graderSession) {
        GraderSessionThread graderSessionThread = null;
        int index = threadPoolList.indexOf(null);

        if (index != -1) {
            graderSessionThread = new GraderSessionThread((long) index,
                    graderSession, this.graderSessionDAO,
                    this.imageFileManagement, this);

            threadPoolList.set(index, graderSessionThread);
        }

        return (graderSessionThread);
    }

    @Override()
    public void notifyEvent(Object... arguments) {
        if ((arguments != null) && (arguments.length != 0)
                && (arguments[0] instanceof GraderSessionThread)) {
            GraderSessionThread graderSessionThread = (GraderSessionThread) arguments[0];
            int key = (int) graderSessionThread.getKey();

            if ((key >= 0) && (key < (THREAD_POOL_SIZE - 1))
                    && (threadPoolList.get(key).equals(graderSessionThread))) {
                threadPoolList.set(key, null);
            }
        }
    }
}