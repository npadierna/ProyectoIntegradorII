package co.edu.udea.web.omrgrader2_0.process.dispatcher;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
final class GraderSessionThreadPool {

    public static final int THREAD_POOL_SIZE = 5;
    private static List<GraderSessionThread> threadPoolList;

    static {
        threadPoolList = new ArrayList<>(THREAD_POOL_SIZE);
    }

    public synchronized GraderSessionThread findFreeGraderSessionThread(
            GraderSession graderSession) {
        GraderSessionThread graderSessionThread = null;
        int index = threadPoolList.indexOf(null);

        if (index != -1) {
            graderSessionThread = new GraderSessionThread(graderSession);
            threadPoolList.add(index, graderSessionThread);
        }

        return (graderSessionThread);
    }
}