package co.edu.udea.web.omrgrader2_0.persistence.dao;

import co.edu.udea.web.omrgrader2_0.persistence.entities.IEntity;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IEntityDAO {

    public Long count(Class<IEntity> entityClass)
            throws OMRGraderPersistenceException;

    public IEntity delete(Class<IEntity> entityClass, Serializable primaryKey)
            throws OMRGraderPersistenceException;

    public IEntity find(Class<IEntity> entityClass, Serializable primaryKey)
            throws OMRGraderPersistenceException;

    public List<IEntity> findByNamedQuery(Class<IEntity> entityClass,
            String namedQuery, String attribute, Object value)
            throws OMRGraderPersistenceException;

    public Serializable save(IEntity entity)
            throws OMRGraderPersistenceException;

    public IEntity update(IEntity entity) throws OMRGraderPersistenceException;
}