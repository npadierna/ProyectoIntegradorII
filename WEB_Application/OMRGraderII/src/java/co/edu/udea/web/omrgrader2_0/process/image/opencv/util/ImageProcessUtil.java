package co.edu.udea.web.omrgrader2_0.process.image.opencv.util;

import java.io.File;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class ImageProcessUtil {

    private ImageProcessUtil() {
        super();
    }

    public static Mat convertImageToBlackWhite(Mat imageMat,
            boolean applyGaussBlur) {
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

    public static void drawTransferredBubbles(Mat image,
            List<Point> transferredCenterLocationsPoints,
            List<Point> cornersTemplatePoints, Scalar bubbleColor,
            int innerCircleRadius, int outerCircleRadius) {
        for (int counter = 0; counter < transferredCenterLocationsPoints.size();
                counter++) {
            Core.circle(image,
                    new Point(transferredCenterLocationsPoints.get(counter).x
                    + cornersTemplatePoints.get(1).x,
                    transferredCenterLocationsPoints.get(counter).y),
                    innerCircleRadius, bubbleColor, -1);

            Core.circle(image,
                    new Point(transferredCenterLocationsPoints.get(counter).x
                    + cornersTemplatePoints.get(1).x,
                    transferredCenterLocationsPoints.get(counter).y),
                    outerCircleRadius, bubbleColor);
        }
    }

    public static void drawTransferredQRSquere(Mat image, Scalar lineColor,
            List<Point> transferredCornersPoints,
            List<Point> templateCornersPoints) {
        Core.line(image, new Point(transferredCornersPoints.get(0).x
                + templateCornersPoints.get(1).x, transferredCornersPoints.get(0).y),
                new Point(transferredCornersPoints.get(1).x
                + templateCornersPoints.get(1).x, transferredCornersPoints.get(1).y),
                lineColor, 4);
        Core.line(image, new Point(transferredCornersPoints.get(1).x
                + templateCornersPoints.get(1).x,
                transferredCornersPoints.get(1).y),
                new Point(transferredCornersPoints.get(3).x
                + templateCornersPoints.get(1).x, transferredCornersPoints.get(3).y),
                lineColor, 4);
        Core.line(image, new Point(transferredCornersPoints.get(3).x
                + templateCornersPoints.get(1).x,
                transferredCornersPoints.get(3).y),
                new Point(transferredCornersPoints.get(2).x
                + templateCornersPoints.get(1).x,
                transferredCornersPoints.get(2).y), lineColor, 4);
        Core.line(image, new Point(transferredCornersPoints.get(2).x
                + templateCornersPoints.get(1).x,
                transferredCornersPoints.get(2).y),
                new Point(transferredCornersPoints.get(0).x
                + templateCornersPoints.get(1).x,
                transferredCornersPoints.get(0).y), lineColor, 4);
    }

    public static void drawTransferredTemplateSquare(Mat image, 
            Scalar lineColor, List<Point> solutionCornersPoints,
            List<Point> templateCornersPoints) {
        Core.line(image, new Point(solutionCornersPoints.get(0).x
                + templateCornersPoints.get(1).x, solutionCornersPoints.get(0).y),
                new Point(solutionCornersPoints.get(1).x
                + templateCornersPoints.get(1).x,
                solutionCornersPoints.get(1).y), lineColor, 4);
        Core.line(image, new Point(solutionCornersPoints.get(1).x
                + templateCornersPoints.get(1).x, solutionCornersPoints.get(1).y),
                new Point(solutionCornersPoints.get(2).x
                + templateCornersPoints.get(1).x,
                solutionCornersPoints.get(2).y), lineColor, 4);
        Core.line(image, new Point(solutionCornersPoints.get(2).x
                + templateCornersPoints.get(1).x, solutionCornersPoints.get(2).y),
                new Point(solutionCornersPoints.get(3).x
                + templateCornersPoints.get(1).x,
                solutionCornersPoints.get(3).y), lineColor, 4);
        Core.line(image, new Point(solutionCornersPoints.get(3).x
                + templateCornersPoints.get(1).x, solutionCornersPoints.get(3).y),
                new Point(solutionCornersPoints.get(0).x
                + templateCornersPoints.get(1).x,
                solutionCornersPoints.get(0).y), lineColor, 4);
    }

    public static String writeImageFile(String fileImageName, Mat imageMat,
            File directoryFile) {
        File file = new File(directoryFile, fileImageName);
        fileImageName = file.toString();

        Highgui.imwrite(fileImageName, imageMat);

        return (fileImageName);
    }
}