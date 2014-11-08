package co.edu.udea.web.omrgrader2_0.webservice.rest.contract;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class WebServicePathContract {

    public static final String ROOT_CONTEXT_PATH = "/rest";

    private WebServicePathContract() {
        super();
    }

    public static final class ExamImageWebServiceContract {

        /*
         * Paths
         */
        public static final String END_POINT_INTERFACE = "co.edu.udea.web.omrgrader2_0.webservice.IExamImageWebService";
        public static final String ROOT_PATH = "/examimage";
        public static final String UPLOAD_REFERENCE_IMAGE_FILE_PATH = "/upload/reference";
        public static final String UPLOAD_STUDENT_IMAGE_FILE_PATH = "/upload/student";
        /*
         * Parameteres
         */
        public static final String CONTENT_REFERENCE_IMAGE_FILE_PARAMETER = "referenceExamImage";
        public static final String CONTENT_STUDENT_IMAGE_FILE_PARAMETER = "studentExamImage";
        public static final String DIRECTORY_STORAGE_ID_PARAMETER = "directoryStorageId";
        public static final String STREAM_REFERENCE_IMAGE_FILE_PARAMETER = "referenceExamImage";
        public static final String STREAM_STUDENT_IMAGE_FILE_PARAMETER = "studentExamImage";
        public static final String STUDENT_EXAM_IMAGE_FILE_ID_PARAMETER = "imageFileId";

        private ExamImageWebServiceContract() {
            super();
        }
    }

    public static final class GraderSessionWebServiceContract {

        /*
         * Paths
         */
        public static final String CREATE_GRADER_SESSION_PATH = "/create";
        public static final String END_POINT_INTERFACE = "co.edu.udea.web.omrgrader2_0.webservice.IGraderSessionWebService";
        public static final String FINISH_GRADER_SESSION_PATH = "/finish";
        public static final String ROOT_PATH = "/gradersession";
        /*
         * Parameteres
         */
//        public static final String E_MAIL_ACCOUNT_PARAMETER = "emailAccount";
//        public static final String GRADER_SESSION_NAME_PARAMETER = "graderSessionName";

        private GraderSessionWebServiceContract() {
            super();
        }
    }
}