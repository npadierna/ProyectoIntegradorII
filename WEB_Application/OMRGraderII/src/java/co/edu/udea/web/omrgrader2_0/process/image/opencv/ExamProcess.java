package co.edu.udea.web.omrgrader2_0.process.image.opencv;

import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamProcess {

    private static final List<Integer> QR_Y_COORDINATE = Arrays.asList(119,
            333);
    private static final List<Integer> QR_X_COORDINATE = Arrays.asList(76,
            290);
    private static final List<Integer> BUBBLE_Y_COORDINATE = Arrays.asList(362,
            389, 416, 443, 470, 497, 524, 551, 578, 605, 632, 659, 686, 713,
            740, 767, 794, 821, 848, 875);
    private static final List<Integer> BUBBLE_X_COORDINATE = Arrays.asList(137,
            166, 195, 224, 253, 376, 405, 434, 463, 492, 614, 643, 672, 701,
            730);
    public static final int BUBBLE_OPTIONS_AMOUNT = 5;
    public static final int BUBBLE_OPTION_RADIUS_LENGTH = 10;
    public static final int QUESTION_ITEMS_COLUMNS_AMOUNT = 3;
    public static final int TOTAL_QUESTION_ITEMS = 60;
    public static final int SELECTED_BUBBLE_THRESH = 290;
    public static final List<Point> BUBBLES_CENTERS_POINTS;
    public static final List<Point> QR_CORNERS_POINTS;

    static {
        BUBBLES_CENTERS_POINTS = new ArrayList<>();
        int length = BUBBLE_X_COORDINATE.size() / QUESTION_ITEMS_COLUMNS_AMOUNT;

        for (int columnCounter = 1; columnCounter <= QUESTION_ITEMS_COLUMNS_AMOUNT;
                columnCounter++) {
            for (int yCoordinate : BUBBLE_Y_COORDINATE) {
                for (int pos = ((columnCounter - 1) * length); pos
                        < (columnCounter * length); pos++) {
                    BUBBLES_CENTERS_POINTS.add(new Point(
                            BUBBLE_X_COORDINATE.get(pos), yCoordinate));
                }
            }
        }

        QR_CORNERS_POINTS = new ArrayList<>();
        for (int rowCounter = 0; rowCounter < QR_Y_COORDINATE.size();
                rowCounter++) {
            for (int columnCounter = 0; columnCounter < QR_X_COORDINATE.size();
                    columnCounter++) {
                QR_CORNERS_POINTS.add(new Point(
                        QR_X_COORDINATE.get(columnCounter),
                        QR_Y_COORDINATE.get(rowCounter)));
            }
        }
    }

    public ExamProcess() {
        super();
    }

    public List<QuestionItem> findAnswers(Mat examImageMat,
            List<Point> bubblesCentersPointsList) {
        List<QuestionItem> questionsItemsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        boolean[] answers;
        int[] pixelCounter;
        for (int i = 0; i < (bubblesCentersPointsList.size() / BUBBLE_OPTIONS_AMOUNT);
                i++) {
            answers = new boolean[BUBBLE_OPTIONS_AMOUNT];
            pixelCounter = new int[BUBBLE_OPTIONS_AMOUNT];
            stringBuilder.delete(0, stringBuilder.length());

            for (int j = 0; j < BUBBLE_OPTIONS_AMOUNT; j++) {
                int position = i * BUBBLE_OPTIONS_AMOUNT + j;
                Point point = bubblesCentersPointsList.get(position);

                pixelCounter[j] = this.countWhitePixelsInBubble(
                        examImageMat, point, BUBBLE_OPTION_RADIUS_LENGTH);
                stringBuilder.append(pixelCounter[j]).append(" ");
                answers[j] = pixelCounter[j] >= SELECTED_BUBBLE_THRESH;
            }

            questionsItemsList.add(new QuestionItem((short) (i + 1), answers));

            // TODO: Eliminar esta instrucción.
            System.out.println(stringBuilder);
        }

        return (questionsItemsList);
    }

    private int countWhitePixelsInBubble(Mat examImageMat,
            Point bubbleCenterPoint, int radius) {
        int centerAtX = (int) bubbleCenterPoint.x;
        int centerAtY = (int) bubbleCenterPoint.y;
        int countedWhitePixeles = 0;

        for (int column = (centerAtX - radius); column < (centerAtX + radius);
                column++) {
            for (int row = (centerAtY - radius); row < (centerAtY + radius);
                    row++) {
                if ((row < examImageMat.height())
                        && (column <= examImageMat.width())) {
                    double valueOfPixel = examImageMat.get(row, column)[0];

                    if (valueOfPixel == 255.0) {
                        countedWhitePixeles++;
                    }
                }
            }
        }

        return (countedWhitePixeles);
    }

//    private List<Point> buildBubblesCenterLocations() {
//        List<Point> centerLocationsPoints = new ArrayList<>();
//        int length = BUBBLE_X_COORDINATE.size() / QUESTION_ITEMS_COLUMNS_AMOUNT;
//
//        for (int columnCounter = 1; columnCounter <= QUESTION_ITEMS_COLUMNS_AMOUNT;
//                columnCounter++) {
//            for (int yCoordinate : BUBBLE_Y_COORDINATE) {
//                for (int pos = ((columnCounter - 1) * length); pos
//                        < (columnCounter * length); pos++) {
//                    centerLocationsPoints.add(new Point(
//                            BUBBLE_X_COORDINATE.get(pos), yCoordinate));
//                }
//            }
//        }
//
//        return (centerLocationsPoints);
//    }

//    private List<Point> buildQRCornersLocations() {
//        List<Point> qrCornersLocationsPoints = new ArrayList<>();
//
//        for (int rowCounter = 0; rowCounter < QR_Y_COORDINATE.size();
//                rowCounter++) {
//            for (int columnCounter = 0; columnCounter < QR_X_COORDINATE.size();
//                    columnCounter++) {
//                qrCornersLocationsPoints.add(new Point(
//                        QR_X_COORDINATE.get(columnCounter),
//                        QR_Y_COORDINATE.get(rowCounter)));
//            }
//        }
//
//        return (qrCornersLocationsPoints);
//    }
}