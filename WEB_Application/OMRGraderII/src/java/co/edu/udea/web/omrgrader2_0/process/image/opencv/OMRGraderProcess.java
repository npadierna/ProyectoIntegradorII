package co.edu.udea.web.omrgrader2_0.process.image.opencv;

import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.image.opencv.util.ImageProcessUtil;
import co.edu.udea.web.omrgrader2_0.process.image.model.Exam;
import co.edu.udea.web.omrgrader2_0.process.image.model.Student;
import co.edu.udea.web.omrgrader2_0.process.qr.QRManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn(value = {"openCVLibraryLoader"})
@Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public final class OMRGraderProcess {

    public static final String ONLY_LOGOS_TEMPLATE_IMAGE_NAME = "Only_Logos_Templage.png";
    private static final DescriptorExtractor DESCRIPTOR_EXTRACTOR;
    private static final DescriptorMatcher DESCRIPTOR_MATCHER;
    private static final FeatureDetector FEATURE_DETECTOR;

    static {
        DESCRIPTOR_EXTRACTOR = DescriptorExtractor
                .create(DescriptorExtractor.SURF);
        DESCRIPTOR_MATCHER = DescriptorMatcher
                .create(DescriptorMatcher.FLANNBASED);
        FEATURE_DETECTOR = FeatureDetector
                .create(FeatureDetector.SURF);
    }
    @Autowired()
    private QRManager qRManager;
    private ExamProcess examProcess;
    public Exam onlyLogosTemplateExam;

    public OMRGraderProcess() {
        this.examProcess = new ExamProcess();
    }

    @PostConstruct()
    public void initialize() {
        this.onlyLogosTemplateExam = this.extractFeatures(this.getClass().
                getResource(File.separator.concat(
                ONLY_LOGOS_TEMPLATE_IMAGE_NAME)).getPath());
    }

    public Exam getOnlyLogosTemplateExam() {

        return (this.onlyLogosTemplateExam);
    }

    public Exam extractFeatures(String examImageAbsolutePath) {
        Mat imageDescriptorsMat = new Mat();
        Mat grayScaledImageMat = Highgui.imread(examImageAbsolutePath,
                Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        MatOfKeyPoint imageMatOfKeyPoints = new MatOfKeyPoint();

        FEATURE_DETECTOR.detect(grayScaledImageMat, imageMatOfKeyPoints);
        DESCRIPTOR_EXTRACTOR.compute(grayScaledImageMat, imageMatOfKeyPoints,
                imageDescriptorsMat);

        return (new Exam(examImageAbsolutePath, grayScaledImageMat,
                imageMatOfKeyPoints, imageDescriptorsMat));
    }

    public Exam executeExamProcessing(Exam onlyLogosTemplate,
            Exam exam, int questionsItemsAmount,
            String processedImageDestinationDirectoryPath,
            String blackWhiteImageDestinationDirectoryPath,
            String imageProcessedName, String imageBlackWhiteName) {
        MatOfDMatch matOfDMatch = new MatOfDMatch();
        DESCRIPTOR_MATCHER.match(onlyLogosTemplate.getImageDescriptorsMat(),
                exam.getImageDescriptorsMat(), matOfDMatch);

        double dist;
        double max_dist = 0.0;
        double min_dist = 100.0;
        List<DMatch> matches = matOfDMatch.toList();
        for (int row = 0; row < onlyLogosTemplate.getImageDescriptorsMat().rows();
                row++) {
            dist = matches.get(row).distance;

            if (dist < min_dist) {
                min_dist = dist;
            }

            if (dist > max_dist) {
                max_dist = dist;
            }
        }

        List<DMatch> good_matches = new ArrayList<>();
        for (int row = 0; row < onlyLogosTemplate.getImageDescriptorsMat().rows();
                row++) {
            if (matches.get(row).distance < 2.0 * (min_dist + 0.001)) {
                good_matches.add(matches.get(row));
            }
        }

        MatOfDMatch matOfDMatches = new MatOfDMatch(
                good_matches.toArray(new DMatch[good_matches.size()]));

        List<Point> refer = new ArrayList<>();
        List<KeyPoint> keyPointsList_ref = onlyLogosTemplate.getImageMatOfKeyPoints().
                toList();

        List<Point> solu = new ArrayList<>();
        List<KeyPoint> keyPointList_solu = exam.getImageMatOfKeyPoints().
                toList();

        for (int i = 0; i < good_matches.size(); i++) {
            refer.add(keyPointsList_ref.get(good_matches.get(i).queryIdx).pt);
            solu.add(keyPointList_solu.get(good_matches.get(i).trainIdx).pt);
        }

        MatOfPoint2f matOfPoint2f_refer = new MatOfPoint2f(
                refer.toArray(new Point[refer.size()]));
        MatOfPoint2f matOfPoint2f_solu = new MatOfPoint2f(
                solu.toArray(new Point[solu.size()]));

        Mat H = Calib3d.findHomography(matOfPoint2f_refer, matOfPoint2f_solu,
                Calib3d.RANSAC, 3.0);

        List<Point> corners_template = new ArrayList<>();
        List<Point> corners_solu = new ArrayList<>();
        corners_template.add(new Point(0.0, 0.0));
        corners_template.add(new Point(onlyLogosTemplate.getGrayScaledImageMat()
                .cols(), 0.0));
        corners_template.add(new Point(onlyLogosTemplate.getGrayScaledImageMat()
                .cols(), onlyLogosTemplate.getGrayScaledImageMat().rows()));
        corners_template.add(new Point(0.0,
                onlyLogosTemplate.getGrayScaledImageMat().rows()));

        Mat corners_tem_mat = Converters
                .vector_Point2d_to_Mat(corners_template);
        Mat corners_sol_mat = new Mat();
        Core.perspectiveTransform(corners_tem_mat, corners_sol_mat, H);
        Converters.Mat_to_vector_Point2d(corners_sol_mat, corners_solu);
        Mat img_matches = new Mat();
        Features2d.drawMatches(onlyLogosTemplate.getGrayScaledImageMat(),
                onlyLogosTemplate.getImageMatOfKeyPoints(),
                exam.getGrayScaledImageMat(),
                exam.getImageMatOfKeyPoints(), matOfDMatches, img_matches,
                Scalar.all(-1.0), Scalar.all(-1.0), new MatOfByte(),
                Features2d.NOT_DRAW_SINGLE_POINTS);

        ImageProcessUtil.drawTransferredTemplateSquare(img_matches, new Scalar(0,
                255, 0), corners_solu, corners_template);

        List<Point> qr_corners_solu = new ArrayList<>();

        Mat qr_corners_tem_mat = Converters.vector_Point2d_to_Mat(
                ExamProcess.QR_CORNERS_POINTS);
        Mat qr_corners_sol_mat = new Mat();
        Core.perspectiveTransform(qr_corners_tem_mat, qr_corners_sol_mat, H);
        Converters.Mat_to_vector_Point2d(qr_corners_sol_mat, qr_corners_solu);

        ImageProcessUtil.drawTransferredQRSquere(img_matches, new Scalar(255,
                0, 0), qr_corners_solu, corners_template);

        List<Point> bubblesCentersPoints;
        if (questionsItemsAmount > 0) {
            bubblesCentersPoints = ExamProcess.BUBBLES_CENTERS_POINTS.subList(0,
                    questionsItemsAmount * ExamProcess.BUBBLE_OPTIONS_AMOUNT);
        } else {
            bubblesCentersPoints = ExamProcess.BUBBLES_CENTERS_POINTS;
        }

        Mat center_locations_mat = Converters
                .vector_Point2d_to_Mat(bubblesCentersPoints);
        Mat center_locations_transfered = new Mat();
        Core.perspectiveTransform(center_locations_mat,
                center_locations_transfered, H);
        List<Point> center_locations_t = new ArrayList<>();
        Converters.Mat_to_vector_Point2d(center_locations_transfered,
                center_locations_t);

        ImageProcessUtil.drawTransferredBubbles(img_matches,
                center_locations_t, corners_template, new Scalar(0, 0, 255), 2,
                ExamProcess.BUBBLE_OPTION_RADIUS_LENGTH);

        if (this.checkDirectoryPathName(processedImageDestinationDirectoryPath)
                && (this.checkImageName(imageProcessedName))) {
            ImageProcessUtil.writeImageFile(
                    imageProcessedName, img_matches, new File(
                    processedImageDestinationDirectoryPath));
        }

        Mat blackAndWhiteImage = ImageProcessUtil.convertImageToBlackWhite(
                exam.getGrayScaledImageMat(), false);

        if (this.checkDirectoryPathName(blackWhiteImageDestinationDirectoryPath)
                && (this.checkImageName(imageBlackWhiteName))) {
            ImageProcessUtil.writeImageFile(
                    imageBlackWhiteName, blackAndWhiteImage, new File(
                    blackWhiteImageDestinationDirectoryPath));
        }

        exam.setQuestionsItemsList(this.examProcess.findAnswers(
                blackAndWhiteImage, center_locations_t, questionsItemsAmount));

        if (questionsItemsAmount > 0) {
            exam.setStudent(this.executeQRCordeProcessing(exam,
                    qr_corners_solu));
        }

        return (exam);
    }

    public Exam executeExamProcessing(String refer_path,
            String solu_path, int questionsItemsAmount,
            String processedImageDestinationDirectoryPath,
            String blackWhiteImageDestinationDirectoryPath,
            String imageProcessedName, String imageBlackWhiteName) {
        Mat image_refer = Highgui.imread(refer_path,
                Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat image_solu = Highgui.imread(solu_path,
                Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        /* 1 Detectando los puntos */
        MatOfKeyPoint keyPoints_ref = new MatOfKeyPoint();
        MatOfKeyPoint keyPoints_solu = new MatOfKeyPoint();

        FEATURE_DETECTOR.detect(image_refer, keyPoints_ref);
        FEATURE_DETECTOR.detect(image_solu, keyPoints_solu);

        /* 2 Creando los descriptores */
        Mat descriptors_ref = new Mat();
        Mat descriptors_solu = new Mat();

        DESCRIPTOR_EXTRACTOR.compute(image_refer, keyPoints_ref,
                descriptors_ref);
        DESCRIPTOR_EXTRACTOR.compute(image_solu, keyPoints_solu,
                descriptors_solu);

        /* 3 Haciendo el Maching */
        MatOfDMatch matOfDMatch = new MatOfDMatch();
        DESCRIPTOR_MATCHER.match(descriptors_ref, descriptors_solu, matOfDMatch);

        double dist;
        double max_dist = 0.0;
        double min_dist = 100.0;
        List<DMatch> matches = matOfDMatch.toList();
        for (int row = 0; row < descriptors_ref.rows(); row++) {
            dist = matches.get(row).distance;

            if (dist < min_dist) {
                min_dist = dist;
            }

            if (dist > max_dist) {
                max_dist = dist;
            }
        }

        List<DMatch> good_matches = new ArrayList<>();
        for (int row = 0; row < descriptors_ref.rows(); row++) {
            if (matches.get(row).distance < 2.0 * (min_dist + 0.001)) {
                good_matches.add(matches.get(row));
            }
        }

        MatOfDMatch matOfDMatches = new MatOfDMatch(
                good_matches.toArray(new DMatch[good_matches.size()]));

        List<Point> refer = new ArrayList<>();
        List<KeyPoint> keyPointsList_ref = keyPoints_ref.toList();

        List<Point> solu = new ArrayList<>();
        List<KeyPoint> keyPointList_solu = keyPoints_solu.toList();

        for (int i = 0; i < good_matches.size(); i++) {
            refer.add(keyPointsList_ref.get(good_matches.get(i).queryIdx).pt);
            solu.add(keyPointList_solu.get(good_matches.get(i).trainIdx).pt);
        }

        MatOfPoint2f matOfPoint2f_refer = new MatOfPoint2f(
                refer.toArray(new Point[refer.size()]));
        MatOfPoint2f matOfPoint2f_solu = new MatOfPoint2f(
                solu.toArray(new Point[solu.size()]));

        Mat H = Calib3d.findHomography(matOfPoint2f_refer, matOfPoint2f_solu,
                Calib3d.RANSAC, 3.0);

        List<Point> corners_template = new ArrayList<>();
        List<Point> corners_solu = new ArrayList<>();
        corners_template.add(new Point(0.0, 0.0));
        corners_template.add(new Point(image_refer.cols(), 0.0));
        corners_template.add(new Point(image_refer.cols(), image_refer.rows()));
        corners_template.add(new Point(0.0, image_refer.rows()));

        Mat corners_tem_mat = Converters
                .vector_Point2d_to_Mat(corners_template);
        Mat corners_sol_mat = new Mat();
        Core.perspectiveTransform(corners_tem_mat, corners_sol_mat, H);
        Converters.Mat_to_vector_Point2d(corners_sol_mat, corners_solu);
        Mat img_matches = new Mat();
        Features2d.drawMatches(image_refer, keyPoints_ref, image_solu,
                keyPoints_solu, matOfDMatches, img_matches, Scalar.all(-1.0),
                Scalar.all(-1.0), new MatOfByte(),
                Features2d.NOT_DRAW_SINGLE_POINTS);

        ImageProcessUtil.drawTransferredTemplateSquare(img_matches, new Scalar(0,
                255, 0), corners_solu, corners_template);

        List<Point> qr_corners_solu = new ArrayList<>();

        Mat qr_corners_tem_mat = Converters.vector_Point2d_to_Mat(
                ExamProcess.QR_CORNERS_POINTS);
        Mat qr_corners_sol_mat = new Mat();
        Core.perspectiveTransform(qr_corners_tem_mat, qr_corners_sol_mat, H);
        Converters.Mat_to_vector_Point2d(qr_corners_sol_mat, qr_corners_solu);

        ImageProcessUtil.drawTransferredQRSquere(img_matches, new Scalar(255,
                0, 0), qr_corners_solu, corners_template);

        List<Point> bubblesCentersPoints;
        if (questionsItemsAmount > 0) {
            bubblesCentersPoints = ExamProcess.BUBBLES_CENTERS_POINTS.subList(0,
                    questionsItemsAmount * ExamProcess.BUBBLE_OPTIONS_AMOUNT);
        } else {
            bubblesCentersPoints = ExamProcess.BUBBLES_CENTERS_POINTS;
        }

        Mat center_locations_mat = Converters
                .vector_Point2d_to_Mat(bubblesCentersPoints);
        Mat center_locations_transfered = new Mat();
        Core.perspectiveTransform(center_locations_mat,
                center_locations_transfered, H);
        List<Point> center_locations_t = new ArrayList<>();
        Converters.Mat_to_vector_Point2d(center_locations_transfered,
                center_locations_t);

        ImageProcessUtil.drawTransferredBubbles(img_matches,
                center_locations_t, corners_template, new Scalar(0, 0, 255), 2,
                ExamProcess.BUBBLE_OPTION_RADIUS_LENGTH);
        if (this.checkDirectoryPathName(processedImageDestinationDirectoryPath)
                && (this.checkImageName(imageProcessedName))) {
            ImageProcessUtil.writeImageFile(
                    imageProcessedName, img_matches, new File(
                    processedImageDestinationDirectoryPath));
        }

        Mat blackAndWhiteImage = ImageProcessUtil.convertImageToBlackWhite(
                image_solu, false);

        if (this.checkDirectoryPathName(blackWhiteImageDestinationDirectoryPath)
                && (this.checkImageName(imageBlackWhiteName))) {
            ImageProcessUtil.writeImageFile(
                    imageBlackWhiteName, blackAndWhiteImage, new File(
                    blackWhiteImageDestinationDirectoryPath));
        }

        Exam exam = new Exam(solu_path, image_solu, keyPoints_solu,
                descriptors_solu);
        exam.setQuestionsItemsList(this.examProcess.findAnswers(
                blackAndWhiteImage, center_locations_t, questionsItemsAmount));

        if (questionsItemsAmount > 0) {
            exam.setStudent(this.executeQRCordeProcessing(exam,
                    qr_corners_solu));
        }

        return (exam);
    }

    /*
     * 0: Esquina superior izquierda.
     * 1: Esquina superior derecha.
     * 2: Esquina inferior izquierda.
     * 3: Esquina inferior derecha.
     */
    public Student executeQRCordeProcessing(Exam studentExam,
            List<Point> qrCordeCornersPoints) {
        try {
            Map<String, String> studentInformation = qRManager.readQRCode(
                    studentExam.getImageAbsolutePath(), QRManager.HINT_MAP,
                    (int) qrCordeCornersPoints.get(0).x,
                    (int) qrCordeCornersPoints.get(3).x,
                    (int) qrCordeCornersPoints.get(0).y,
                    (int) qrCordeCornersPoints.get(3).y);

            return ((studentInformation != null)
                    ? new Student(studentInformation) : new Student());
        } catch (OMRGraderProcessException ex) {

            return (new Student());
        }
    }

    private boolean checkDirectoryPathName(String directoryPathName) {

        return ((directoryPathName != null)
                && (!directoryPathName.trim().isEmpty()));
    }

    private boolean checkImageName(String imageName) {

        return ((imageName != null)
                && (!imageName.trim().isEmpty()));
    }
}