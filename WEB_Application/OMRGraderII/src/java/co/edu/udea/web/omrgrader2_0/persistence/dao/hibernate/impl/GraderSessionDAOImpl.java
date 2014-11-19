package co.edu.udea.web.omrgrader2_0.persistence.dao.hibernate.impl;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IGraderSessionDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSessionPK;
import co.edu.udea.web.omrgrader2_0.persistence.entities.IEntity;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    /*
     * SELECT * FROM GRADER_SESSION AS gs WHERE gs.is_available = true ORDER BY gs.request ASC LIMIT 1
     */
    @Override()
    @SuppressWarnings(value = {"rawtypes"})
    public GraderSession findFirstByRequest()
            throws OMRGraderPersistenceException {
        try {
            CriteriaBuilder criteriaBuilder = super.getEntityManager().
                    getCriteriaBuilder();
            CriteriaQuery<GraderSession> criteriaQuery = criteriaBuilder.
                    createQuery(GraderSession.class);
            Root<GraderSession> root = criteriaQuery.from(GraderSession.class);

            criteriaQuery.where(criteriaBuilder.equal(root.get("available"),
                    Boolean.TRUE));
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("request")));

            Query query = super.getEntityManager().createQuery(criteriaQuery).
                    setMaxResults(1);
            List resultsList = query.getResultList();

            return (((resultsList != null) && (!resultsList.isEmpty()))
                    ? (GraderSession) resultsList.get(0) : null);
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to execute the Query for finding by Request.",
                    e.getCause());
        }
    }

    @Override()
    public GraderSessionPK save(GraderSession graderSession)
            throws OMRGraderPersistenceException {

        return ((GraderSessionPK) super.save(graderSession));
    }

    @Override()
    public GraderSession update(GraderSession graderSession)
            throws OMRGraderPersistenceException {

        return ((GraderSession) super.update(graderSession));
    }
}