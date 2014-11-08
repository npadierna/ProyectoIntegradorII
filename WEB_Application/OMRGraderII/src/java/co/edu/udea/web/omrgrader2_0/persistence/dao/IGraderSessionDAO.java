package co.edu.udea.web.omrgrader2_0.persistence.dao;

import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSession;
import co.edu.udea.web.omrgrader2_0.persistence.entities.GraderSessionPK;
import co.edu.udea.web.omrgrader2_0.persistence.exception.OMRGraderPersistenceException;
import java.util.List;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public interface IGraderSessionDAO {

    public Long count() throws OMRGraderPersistenceException;

    public GraderSession delete(GraderSessionPK graderSessionPK)
            throws OMRGraderPersistenceException;

    public GraderSession find(GraderSessionPK graderSessionPK)
            throws OMRGraderPersistenceException;

    public List<GraderSession> findByNamedQuery(String namedQuery,
            String attribute, Object value)
            throws OMRGraderPersistenceException;

    public GraderSessionPK save(GraderSession graderSession)
            throws OMRGraderPersistenceException;
}