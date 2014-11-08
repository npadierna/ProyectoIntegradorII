package co.edu.udea.web.omrgrader2_0.persistence.dao.hibernate.impl;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSessionPK;
import co.edu.udea.web.omrgrader2_0.persistence.entities.IEntity;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Repository()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class GraderSessionDAOImpl extends AbstractEntityDAO
        implements IGraderSessionDAO {

    public GraderSessionDAOImpl() {
        super();
    }

    @Override()
    public Long count() throws OMRGraderPersistenceException {

        return (super.count(GraderSession.class));
    }

    @Override()
    public GraderSession delete(GraderSessionPK graderSessionPK)
            throws OMRGraderPersistenceException {

        return ((GraderSession) super.delete(GraderSession.class,
                graderSessionPK));
    }

    @Override()
    public GraderSession find(GraderSessionPK graderSessionPK)
            throws OMRGraderPersistenceException {

        return (GraderSession) (super.find(GraderSession.class,
                graderSessionPK));
    }

    @Override()
    public List<GraderSession> findByNamedQuery(String namedQuery,
            String attribute, Object value) throws OMRGraderPersistenceException {
        List<GraderSession> graderSessionList = new ArrayList<>();
        List<IEntity> entityList = super.findByNamedQuery(GraderSession.class,
                namedQuery, attribute, value);

        for (IEntity entity : entityList) {
            graderSessionList.add((GraderSession) entity);
        }

        return (graderSessionList);
    }

    @Override()
    public GraderSessionPK save(GraderSession graderSession)
            throws OMRGraderPersistenceException {

        return ((GraderSessionPK) super.save(graderSession));
    }
}