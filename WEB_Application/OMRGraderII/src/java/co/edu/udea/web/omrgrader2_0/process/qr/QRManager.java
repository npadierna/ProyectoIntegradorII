package co.edu.udea.web.omrgrader2_0.process.qr;

import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class QRManager {

    public QRManager() {
        super();
    }

    public void createQRCode(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws OMRGraderProcessException {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            MatrixToImageWriter.writeToPath(matrix, filePath.substring(filePath
                    .lastIndexOf('.') + 1), Paths.get(filePath));
        } catch (IOException | WriterException e) {
            throw new OMRGraderProcessException(
                    "Fatal error while the application was trying to create a QR Code.",
                    e.getCause());
        }
    }

    public String readQRCode(String filePath, String charset, Map hintMap)
            throws OMRGraderProcessException {
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                    ImageIO.read(new FileInputStream(filePath)))));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                    hintMap);

            return (qrCodeResult.getText());
        } catch (IOException | NotFoundException e) {
            throw new OMRGraderProcessException(
                    "Fatal error while the application was trying to read a QR Code.",
                    e.getCause());
        }
    }
}