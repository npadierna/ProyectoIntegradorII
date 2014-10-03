package co.edu.udea.android.omrgrader2_0.business.grade;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import co.edu.udea.android.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;
import co.edu.udea.android.omrgrader2_0.webservice.restful.impl.ExamImageWebServiceImpl;

public class ExamImageSenderAsyncTask extends AsyncTask<Object, Void, Integer> {

	private IExamImageWebService examImageWebService;

	public ExamImageSenderAsyncTask() {
		this.examImageWebService = new ExamImageWebServiceImpl();
	}

	@Override()
	protected Integer doInBackground(Object... parameters) {
		Bitmap examImageBitmap = (Bitmap) parameters[0];
		boolean b;

		try {
			b = this.examImageWebService
					.uploadReferenceExamImageFile(examImageBitmap);
		} catch (OMRGraderWebServiceException e) {
			e.printStackTrace();
		}

		return (null);
	}
}