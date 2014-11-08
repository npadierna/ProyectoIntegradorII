package co.edu.udea.web.omrgrader2_0.persistence.dao.hibernate.impl;

import co.edu.udea.web.omrgrader2_0.persistence.dao.IEntityDAO;
import co.edu.udea.web.omrgrader2_0.persistence.entities.IEntity;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
abstract class AbstractEntityDAO implements IEntityDAO {

    @PersistenceContext(unitName = "OMRGraderIIPU")
    protected EntityManager entityManager;

    public AbstractEntityDAO() {
        super();
    }

    public EntityManager getEntityManager() {

        return (this.entityManager);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override()
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public Long count(Class entityClass)
            throws OMRGraderPersistenceException {
        try {
            CriteriaQuery criteriaQuery = this.getEntityManager().
                    getCriteriaBuilder().createQuery(entityClass);
            Root<IEntity> root = criteriaQuery.from(entityClass);

            criteriaQuery.select(this.getEntityManager().getCriteriaBuilder()
                    .count(root));

            return ((Long) this.getEntityManager().createQuery(criteriaQuery)
                    .getSingleResult());
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to count entities.",
                    e.getCause());
        }
    }

    @Override()
    @SuppressWarnings(value = {"rawtypes"})
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public IEntity delete(Class entityClass, Serializable primaryKey)
            throws OMRGraderPersistenceException {
        IEntity entity = null;

        try {
            entity = this.find(entityClass, primaryKey);

            this.getEntityManager().remove(entity);
            this.getEntityManager().flush();
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to delete an entity.",
                    e.getCause());
        }

        return (entity);
    }

    @Override()
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public IEntity find(Class entityClass, Serializable primaryKey)
            throws OMRGraderPersistenceException {
        IEntity entity = null;

        try {
            entity = (IEntity) this.getEntityManager().find(entityClass,
                    primaryKey);
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to find or search an entity.",
                    e.getCause());
        }

        return (entity);
    }

    @Override()
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public List<IEntity> findByNamedQuery(Class entityClass, String namedQuery,
            String attribute, Object value)
            throws OMRGraderPersistenceException {
        List<IEntity> entitesList = null;

        try {
            Query query = this.getEntityManager().createNamedQuery(namedQuery,
                    entityClass);
            query.setParameter(attribute, value);

            entitesList = query.getResultList();
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to execute the Named Query",
                    e.getCause());
        }

        return (entitesList);
    }

    @Override()
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Serializable save(IEntity entity)
            throws OMRGraderPersistenceException {
        try {
            this.getEntityManager().persist(entity);
            this.getEntityManager().flush();
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to persist or save an entity.",
                    e.getCause());
        }

        return (entity.getPrimaryKey());
    }

    @Override()
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public IEntity update(IEntity entity) throws OMRGraderPersistenceException {
        try {
            this.getEntityManager().merge(entity);
            this.getEntityManager().flush();
        } catch (Exception e) {
            throw new OMRGraderPersistenceException(
                    "Fatal error while the DAO was trying to update or refresh an entity.",
                    e.getCause());
        }

        return (entity);
    }
}