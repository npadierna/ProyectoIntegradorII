package co.edu.udea.web.omrgrader2_0.controller;

import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@ManagedBean
@SessionScoped
public class DocumentationManagedBean implements Serializable {

    private static final long serialVersionUID = 8128841604073795883L;
    private static String ANSWER_SHEET_TEMPLATE_DOCX_FILE_PATH =
            "/resources/docs/examtemplates/ANSWER_SHEET_TEMPLATE.docx";
    private static String DOCX_MIME_TYPE =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static String ANSWER_SHEET_TEMPLATE_DOCX_NAME =
            "ANSWER_SHEET_TEMPLATE.docx";
    private static String ANSWER_SHEET_TEMPLATE_PDF_FILE_PATH =
            "/resources/docs/examtemplates/ANSWER_SHEET_TEMPLATE.pdf";
    private static String PDF_MIME_TYPE = "application/pdf";
    private static String ANSWER_SHEET_TEMPLATE_PDF_NAME =
            "ANSWER_SHEET_TEMPLATE.pdf";
    private StreamedContent answerSheetTemplateDOCXFile;
    private StreamedContent answerSheetTemplatePDFFile;

    public DocumentationManagedBean() {
        super();
    }

    @PostConstruct
    public void init() {
        InputStream streamDOCXFile = ((ServletContext) FacesContext.getCurrentInstance().
                getExternalContext().getContext()).getResourceAsStream(
                ANSWER_SHEET_TEMPLATE_DOCX_FILE_PATH);
        this.answerSheetTemplateDOCXFile = new DefaultStreamedContent(streamDOCXFile,
                DOCX_MIME_TYPE, ANSWER_SHEET_TEMPLATE_DOCX_NAME);

        InputStream streamPDFFile = ((ServletContext) FacesContext.getCurrentInstance().
                getExternalContext().getContext()).getResourceAsStream(
                ANSWER_SHEET_TEMPLATE_PDF_FILE_PATH);
        this.answerSheetTemplatePDFFile = new DefaultStreamedContent(streamPDFFile,
                PDF_MIME_TYPE, ANSWER_SHEET_TEMPLATE_PDF_NAME);
    }

    public StreamedContent getAnswerSheetTemplateDOCXFile() {
        return (this.answerSheetTemplateDOCXFile);
    }

    public StreamedContent getAnswerSheetTemplatePDFFile() {
        return (this.answerSheetTemplatePDFFile);
    }
}