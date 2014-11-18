package co.edu.udea.web.omrgrader2_0.config;

import co.edu.udea.web.omrgrader2_0.process.dispatcher.GraderSessionDispatcherThread;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class DeployServletContextListener implements ServletContextListener {

    private GraderSessionDispatcherThread graderSessionDispatcherThread;

    @Override()
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext applicationContext = WebApplicationContextUtils.
                getWebApplicationContext(servletContextEvent.getServletContext());

        this.graderSessionDispatcherThread = applicationContext.
                getBean(GraderSessionDispatcherThread.class);
        this.graderSessionDispatcherThread.start();
    }

    @Override()
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.graderSessionDispatcherThread.finish();
    }
}