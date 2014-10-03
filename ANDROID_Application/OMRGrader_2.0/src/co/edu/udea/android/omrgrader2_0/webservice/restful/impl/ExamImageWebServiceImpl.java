package co.edu.udea.android.omrgrader2_0.webservice.restful.impl;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import co.edu.udea.android.omrgrader2_0.webservice.IExamImageWebService;
import co.edu.udea.android.omrgrader2_0.webservice.exception.OMRGraderWebServiceException;

public class ExamImageWebServiceImpl implements IExamImageWebService {

	private static final String TAG = ExamImageWebServiceImpl.class
			.getSimpleName();

	public ExamImageWebServiceImpl() {
		super();
	}

	@Override()
	public boolean uploadReferenceExamImageFile(Bitmap referenceExamImageBitmap)
			throws OMRGraderWebServiceException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		referenceExamImageBitmap.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = null;
		ByteArrayBody baba = new ByteArrayBody(data, "test.jpg");
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		reqEntity.addPart("referenceExamImage", baba);

		try {
			postRequest = new HttpPost(
					new URI(
							"http",
							"",
							"192.168.0.195",
							8084,
							"/UploadImageWebApplication/rest/examimage/upload/reference",
							null, null));
			postRequest.setEntity(reqEntity);

			String entityResponseString = EntityUtils.toString(httpClient
					.execute(postRequest).getEntity());

			Log.v(TAG, entityResponseString);
		} catch (Exception ex) {
			throw new OMRGraderWebServiceException(
					"An error has happened while the application was trying to POSTING the Reference Exam Image File.",
					ex);
		}

		return (true);
	}

	@Override()
	public boolean uploadStudentExamImageFile(Bitmap studentExamImageBitmap)
			throws OMRGraderWebServiceException {

		return false;
	}

}