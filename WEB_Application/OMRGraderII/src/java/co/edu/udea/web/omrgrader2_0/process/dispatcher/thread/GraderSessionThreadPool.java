package co.edu.udea.web.omrgrader2_0.process.dispatcher.thread;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.process.directory.ImageFileManager;
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

    public static final int THREAD_POOL_SIZE = 5;
    private static List<GraderSessionThread> threadPoolList;
    @Autowired()
    private IGraderSessionDAO graderSessionDAO;
    @Autowired()
    private ImageFileManager imageFileManagement;

    static {
        threadPoolList = Arrays.asList(null, null, null, null, null);
//        for (int counter = 0; counter < THREAD_POOL_SIZE; counter++) {
//            threadPoolList.add(null);
//        }
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
                    this.imageFileManagement);

            threadPoolList.set(index, graderSessionThread);
        }

        return (graderSessionThread);
    }

    @Override()
    public void notifyEvent(Object... arguments) {
        // TODO: Implementar.
    }
}