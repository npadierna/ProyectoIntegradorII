package co.edu.udea.web.omrgrader2_0.process.image.opencv.util;

import java.io.File;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Component(value = OpenCVLibraryLoader.OPENCV_LIBRARY_LOADER_BEAN)
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public final class OpenCVLibraryLoader {

    public static final String OPENCV_LIBRARY_LOADER_BEAN = "openCVLibraryLoader";
    public static final String OPENCV_LIBRARY_NAME = "opencv_java248.so";

    static {
        System.load(OpenCVLibraryLoader.class.getResource(File.separator.concat(
                OPENCV_LIBRARY_NAME)).getPath());
    }

    public OpenCVLibraryLoader() {
        super();
    }
}