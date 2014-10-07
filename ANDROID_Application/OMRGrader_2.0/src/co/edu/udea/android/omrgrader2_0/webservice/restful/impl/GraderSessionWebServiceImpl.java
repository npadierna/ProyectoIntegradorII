package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import co.edu.udea.android.omrgrader2_0.webservice.IGraderSessionWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.model.GraderSession;

public class GraderSessionWebServiceImpl extends AbstractContextWebService
		implements IGraderSessionWebService {

	private static final String TAG = GraderSessionWebServiceImpl.class
			.getSimpleName();

	public GraderSessionWebServiceImpl() {
		super();
	}

	@Override()
	public boolean createGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException {

		return (false);
	}

	@Override()
	public boolean finishGraderSession(GraderSession graderSession)
			throws OMRGraderWebServiceException {

		return (false);
	}

	@Override()
	public int buildStorageDirectoryPathName(GraderSession graderSession) {

		return (0);
	}
}