package co.edu.udea.web.omrgrader2_0.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@ManagedBean
@RequestScoped
public class MenuManagedBean implements Serializable {

    private static final long serialVersionUID = -4531468843516045318L;
    private static final String ABOUT_US = "ABOUT_US";
    private static final String HOME = "HOME";
    private static final String ABOUT_APPLICATION = "ABOUT_APPLICATION";
    private static final String ABOUT_PROJECT = "ABOUT_PROJECT";
    private static final String USER_DOCUMENTATION = "USER_DOCUMENTATION";
    private static final String ACKNOWLEDGMENTS = "ACKNOWLEDGMENTS";
    private static final String EXAM_TEMPLATE = "EXAM_TEMPLATE";

    public MenuManagedBean() {
        super();
    }

    public String navigateToAboutUs() {
        return (ABOUT_US);
    }

    public String navigateToHome() {
        return (HOME);
    }

    public String navigateToAboutApplication() {
        return (ABOUT_APPLICATION);
    }

    public String navigateToAboutProject() {
        return (ABOUT_PROJECT);
    }

    public String navigateToUserDocumentation() {
        return (USER_DOCUMENTATION);
    }

    public String navigateToAcknowledgments() {
        return (ACKNOWLEDGMENTS);
    }

    public String navigateToExamTemplate() {
        return (EXAM_TEMPLATE);
    }
}