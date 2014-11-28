package co.edu.udea.web.omrgrader2_0.process.image.opencv;

import co.edu.udea.web.omrgrader2_0.process.image.model.QuestionItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class ExamProcess {

    private static final List<Integer> Y_COR = new ArrayList<>(Arrays.asList(340,
            367, 394, 421, 448, 475, 502, 529, 556, 583, 610, 637, 664, 691, 718,
            745, 772, 799, 826, 853));
    private static final List<Integer> X_COR = new ArrayList<>(Arrays.asList(116,
            145, 174, 203, 232, 355, 384, 413, 442, 471, 593, 622, 651, 680,
            709));
    public static final int TOTAL_QUESTIONS_ITEMS = 60;
    public static final int QUESTION_ITEMS_COLUMNS_AMOUNT = 3;
    public static final int OPTION_AMOUNT = 5;

    public ExamProcess() {
        super();
    }

    public List<QuestionItem> getAnswers(Mat imageToProcessMat,
            List<Point> pointsList, int radius) {
        int thresh = 75;
        List<QuestionItem> answersList = new ArrayList<>();
        for (int i = 0; i < (pointsList.size() / OPTION_AMOUNT); i++) {
            boolean[] answers = new boolean[OPTION_AMOUNT];
            int[] pixelCounter = new int[OPTION_AMOUNT];
            StringBuilder stringBuilder = new StringBuilder();

            for (int j = 0; j < OPTION_AMOUNT; j++) {
                int position = i * OPTION_AMOUNT + j;
                Point point = pointsList.get(position);
                pixelCounter[j] = this.getWhitePixelsInCircle(
                        imageToProcessMat, point, radius);
                stringBuilder.append(pixelCounter[j]).append(" ");
                answers[j] = pixelCounter[j] > thresh;
            }

            answersList.add(new QuestionItem((short) (i + 1), answers));

            ImageProcess imageProcess = new ImageProcess();
        }

        return (answersList);
    }

    public int getWhitePixelsInCircle(Mat imageToProcessMat,
            Point currentPoint, int radius) {
        int centerAtX = (int) currentPoint.x;
        int centerAtY = (int) currentPoint.y;
        int amoutOfWhitePixeles = 0;

        for (int column = (centerAtX - radius); column < (centerAtX + radius);
                column++) {
            for (int row = (centerAtY - radius); row < (centerAtY + radius);
                    row++) {
                if ((row < imageToProcessMat.height())
                        && (column <= imageToProcessMat.width())) {
                    double valueOfPixel = imageToProcessMat.get(row, column)[0];

                    if (valueOfPixel == 255.0) {
                        amoutOfWhitePixeles++;
                    }
                }
            }
        }

        return (amoutOfWhitePixeles);
    }

    public List<Point> buildBubblesCenterLocations() {
        List<Point> centerLocations = new ArrayList<>();
        int length = (int) (X_COR.size() / QUESTION_ITEMS_COLUMNS_AMOUNT);

        for (int columnCounter = 1; columnCounter <= QUESTION_ITEMS_COLUMNS_AMOUNT;
                columnCounter++) {
            for (int yCoordinate : Y_COR) {
                for (int pos = ((columnCounter - 1) * length); pos < (columnCounter
                        * length); pos++) {
                    centerLocations.add(new Point(X_COR.get(pos),
                            yCoordinate));
                }
            }
        }

        return (centerLocations);
    }

    public void drawTransferredSquare(Mat imgMatches, Scalar lineColor,
            List<Point> corners_solu, List<Point> corners_template) {
        Core.line(imgMatches, new Point(corners_solu.get(0).x
                + corners_template.get(1).x, corners_solu.get(0).y),
                new Point(corners_solu.get(1).x + corners_template.get(1).x,
                corners_solu.get(1).y), lineColor, 4);
        Core.line(imgMatches, new Point(corners_solu.get(1).x
                + corners_template.get(1).x, corners_solu.get(1).y),
                new Point(corners_solu.get(2).x + corners_template.get(1).x,
                corners_solu.get(2).y), lineColor, 4);
        Core.line(imgMatches, new Point(corners_solu.get(2).x
                + corners_template.get(1).x, corners_solu.get(2).y),
                new Point(corners_solu.get(3).x + corners_template.get(1).x,
                corners_solu.get(3).y), lineColor, 4);
        Core.line(imgMatches, new Point(corners_solu.get(3).x
                + corners_template.get(1).x, corners_solu.get(3).y),
                new Point(corners_solu.get(0).x + corners_template.get(1).x,
                corners_solu.get(0).y), lineColor, 4);
    }

    public void drawTransferredBubbles(Mat image,
            List<Point> centerLocationsTransfered, List<Point> cornersTemplate,
            Scalar bubbleColor, int innerCircleRadius, int outerCircleRadius) {
        for (int counter = 0; counter < centerLocationsTransfered.size();
                counter++) {
            Core.circle(image,
                    new Point(centerLocationsTransfered.get(counter).x
                    + cornersTemplate.get(1).x,
                    centerLocationsTransfered.get(counter).y),
                    innerCircleRadius, bubbleColor, -1);

            Core.circle(image,
                    new Point(centerLocationsTransfered.get(counter).x
                    + cornersTemplate.get(1).x,
                    centerLocationsTransfered.get(counter).y),
                    outerCircleRadius, bubbleColor);
        }
    }
}