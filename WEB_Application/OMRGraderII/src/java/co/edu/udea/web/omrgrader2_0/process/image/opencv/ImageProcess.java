package co.edu.udea.web.omrgrader2_0.process.image.opencv;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ImageProcess {

    public ImageProcess() {
        super();
    }

    public Mat convertImageToBlackWhite(Mat imageMat, boolean applyGaussBlur) {
        Mat imageInGrayMat = imageMat.clone();
        if (applyGaussBlur) {
            Imgproc.GaussianBlur(imageInGrayMat, imageInGrayMat,
                    new Size(3, 3), 0, 0);
        }
        double thresh = Imgproc.threshold(imageInGrayMat, imageInGrayMat, 0,
                255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgproc.threshold(imageInGrayMat, imageInGrayMat, thresh, 255,
                Imgproc.THRESH_BINARY_INV);

        return (imageInGrayMat);
    }

    public String writePhotoFile(String filePhotoName, Mat imageMat,
            File directoryFile) {
        File file = new File(directoryFile, filePhotoName);
        filePhotoName = file.toString();
        Highgui.imwrite(filePhotoName, imageMat);

        return (filePhotoName);
    }
}