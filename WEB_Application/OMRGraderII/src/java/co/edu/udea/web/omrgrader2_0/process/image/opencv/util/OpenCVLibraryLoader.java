/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.web.omrgrader2_0.process.image.opencv.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
@Component()
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public final class OpenCVLibraryLoader {

    static {
        System.load("/home/pivb/Software/Libraries/OpenCV2.4.8/opencv_java248.so");
//        System.load(OpenCVLibraryLoader.class.getResource(File.separator
//                .concat("opencv_java248.so")).getPath());

//        System.load(Core.NATIVE_LIBRARY_NAME);
//        System.loadLibrary("/home/pivb/Software/Libraries/OpenCV2.4.8/opencv_java248.so");
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.loadLibrary("opencv_java248.so");
//        System.loadLibrary("./opencv_java248.so");
    }

    public OpenCVLibraryLoader() {
        super();
    }
}