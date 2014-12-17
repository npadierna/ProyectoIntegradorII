package co.edu.udea.web.omrgrader2_0.process.email.report;

import co.edu.udea.web.omrgrader2_0.process.exception.OMRGraderProcessException;
import co.edu.udea.web.omrgrader2_0.process.image.model.AnswerStudent;
import co.edu.udea.web.omrgrader2_0.process.image.model.SheetFileInfo;
import co.edu.udea.web.omrgrader2_0.util.text.TextUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public class FileSheetReport {

    private final String RESULT_SHEET_NAME = "Resumen de Resultados";
    private final int EXAM_ANSWER_ROW_START = 10;
    private final String ARIAL_FONT = "Arial";
    private final int HEADERS_FONT_SIZE = 12;
    private final int TEXT_FONT_SIZE = 11;
    private final int COLUMN_WIDTH_50 = 50 * 256;
    private final int COLUMN_WIDTH_40 = 40 * 256;
    private final int COLUMN_WIDTH_30 = 30 * 256;
    private final int COLUMN_WIDTH_20 = 20 * 256;
    private final String FILE_EXTENSION = ".xlsx";

    public FileSheetReport() {
        super();
    }

    public String createDataSheet(String path, SheetFileInfo info)
            throws OMRGraderProcessException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        this.setPropertiesToWorkbook(workbook, info.getGraderSession().
                getGraderSessionPK().getSessionName());
        Sheet sheet = workbook.createSheet(this.RESULT_SHEET_NAME);

        this.createExamAnswerHeaders(workbook, sheet,
                this.EXAM_ANSWER_ROW_START);

        this.createStudentInfoCells(workbook, sheet, info.getAnswerStudentList(),
                this.EXAM_ANSWER_ROW_START + 1,
                info.getGraderSession().getDecimalPrecision());

        this.createExamInfoHeaders(info, sheet, workbook);

        path = path + TextUtil.removeSpaces(info.getGraderSession().
                getGraderSessionPK().getSessionName()) + this.FILE_EXTENSION;
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            throw new OMRGraderProcessException(
                    "Fatal error while the application was trying to create "
                    + "grades file " + this.FILE_EXTENSION + ".", e.getCause());
        }

        return (path);
    }

    private void createExamAnswerHeaders(XSSFWorkbook workbook, Sheet sheet,
            int rowNumber) {
        Row row = sheet.createRow((short) rowNumber);
        Cell cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createExamAnswerHeaderCellStyle(workbook));
        cell.setCellValue("Nombre Estudiante");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createExamAnswerHeaderCellStyle(workbook));
        cell.setCellValue("Número de Identificación");
        cell = row.createCell((short) 2);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createExamAnswerHeaderCellStyle(workbook));
        cell.setCellValue("Correo Electrónico");
        cell = row.createCell((short) 3);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createExamAnswerHeaderCellStyle(workbook));
        cell.setCellValue("Número de Preguntas Correctas");
        cell = row.createCell((short) 4);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createExamAnswerHeaderCellStyle(workbook));
        cell.setCellValue("Nota");

        sheet.setColumnWidth(0, this.COLUMN_WIDTH_50);
        sheet.setColumnWidth(1, this.COLUMN_WIDTH_30);
        sheet.setColumnWidth(2, this.COLUMN_WIDTH_40);
        sheet.setColumnWidth(3, this.COLUMN_WIDTH_40);
        sheet.setColumnWidth(4, this.COLUMN_WIDTH_20);
    }

    private void createExamInfoHeaders(SheetFileInfo info, Sheet sheet,
            XSSFWorkbook workbook) {
        Row row = sheet.createRow((short) 0);
        Cell cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Nombre del Examen");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), false));
        cell.setCellValue(info.getGraderSession().getGraderSessionPK().
                getSessionName());

        row = sheet.createRow((short) 1);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Nota Máxima");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), true));
        cell.setCellValue(info.getGraderSession().getMaximumGrade());

        row = sheet.createRow((short) 2);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Número Total de Estudiantes");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), false));
        cell.setCellValue(info.getStudentAmount());

        row = sheet.createRow((short) 3);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Número de Estudiantes que Aprobaron");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), false));
        cell.setCellValue(info.getStudentAmountPassed());

        row = sheet.createRow((short) 4);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Cantidad Total de Preguntas");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), false));
        cell.setCellValue(info.getQuestionAmount());

        row = sheet.createRow((short) 5);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Cantidad Mínima de Preguntas para Ganar");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), false));
        cell.setCellValue(info.getMinimumQuestionAmountToPass());

        row = sheet.createRow((short) 6);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Nota Mínima para Ganar");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), true));
        cell.setCellValue(info.getMinimumScoreToPass());

        row = sheet.createRow((short) 7);
        cell = row.createCell((short) 0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(this.createInfoExamHeaderCellStyle(workbook));
        cell.setCellValue("Porcentaje para Ganar");
        cell = row.createCell((short) 1);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellStyle(this.createInfoExamCellStyle(workbook,
                info.getGraderSession().getDecimalPrecision(), true));
        cell.setCellValue(info.getGraderSession().getApprovalPercentage());
    }

    private void setPropertiesToWorkbook(XSSFWorkbook workbook, String examName) {
        if (examName == null || examName.length() == 0) {
            examName = "Exámen UdeA";
        }

        POIXMLProperties properties = workbook.getProperties();

        POIXMLProperties.ExtendedProperties extendedProperties = properties.
                getExtendedProperties();
        extendedProperties.getUnderlyingProperties().setCompany(
                "Departamento Ingeniería de Sistemas - UdeA");
        extendedProperties.getUnderlyingProperties().setTemplate("XSSF");
        extendedProperties.getUnderlyingProperties().setAppVersion("1.0");
        extendedProperties.getUnderlyingProperties().setManager(
                "Calificación de Exámenes UdeA");

        POIXMLProperties.CoreProperties coreProperties = properties.
                getCoreProperties();
        coreProperties.setCreator("Calificación de Exámenes UdeA");
        coreProperties.setTitle(examName);
    }

    private void createStudentInfoCells(XSSFWorkbook workbook, Sheet sheet,
            List<AnswerStudent> studentList, int rowNumberStart, String precision) {
        DataFormat format = workbook.createDataFormat();
        String prec = this.constructPrecision(precision);

        int rowNumber = rowNumberStart;
        for (AnswerStudent as : studentList) {
            Row row = sheet.createRow((short) rowNumber);
            Cell cell = row.createCell((short) 0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(this.createInfoStudentCellStyle(workbook, 0, false, false));
            cell.setCellValue(as.getStudent().getFullName());

            cell = row.createCell((short) 1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(this.createInfoStudentCellStyle(workbook, 0, true, false));
            cell.setCellValue(as.getStudent().getIdNumber());

            cell = row.createCell((short) 2);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(this.createInfoStudentCellStyle(workbook, 0, true, false));
            cell.setCellValue(as.getStudent().geteMail());

            cell = row.createCell((short) 3);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(this.createInfoStudentCellStyle(workbook, 0, true, false));
            cell.setCellValue(as.getCorrectAnswersAmount());

            cell = row.createCell((short) 4);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            int color = 2;
            if (as.getPassed()) {
                color = 1;
            }
            CellStyle cellStyle = this.createInfoStudentCellStyle(workbook, color, true, true);
            cellStyle.setDataFormat(format.getFormat(prec));
            cell.setCellStyle(cellStyle);
            cell.setCellValue(as.getScore());

            rowNumber++;
        }
    }

    private String constructPrecision(String precision) {
        int decimalPrecision = Integer.parseInt(precision);
        int i = 1;
        String decimalPrecisionFormat = "0.0";
        while (i < decimalPrecision) {
            decimalPrecisionFormat = decimalPrecisionFormat + "0";
            i++;
        }

        return (decimalPrecisionFormat);
    }

    private CellStyle createInfoExamCellStyle(XSSFWorkbook workbook,
            String precision, boolean prec) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        DataFormat format = workbook.createDataFormat();
        String precisionFormat = this.constructPrecision(precision);

        if (prec) {
            cellStyle.setDataFormat(format.getFormat(precisionFormat));
        }

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        this.createCommonCellStyle(cellStyle, font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        font.setFontHeightInPoints((short) this.TEXT_FONT_SIZE);
        cellStyle.setFont(font);

        return (cellStyle);
    }

    private CellStyle createExamAnswerHeaderCellStyle(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        this.createCommonCellStyle(cellStyle, font);
        cellStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) this.HEADERS_FONT_SIZE);
        cellStyle.setFont(font);

        return cellStyle;
    }

    private CellStyle createInfoExamHeaderCellStyle(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        this.createCommonCellStyle(cellStyle, font);
        cellStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) this.HEADERS_FONT_SIZE);
        cellStyle.setFont(font);

        return cellStyle;
    }

    private CellStyle createInfoStudentCellStyle(XSSFWorkbook workbook,
            int color, boolean alignCenter, boolean changeFont) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        if (alignCenter) {
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        } else {
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        }

        this.createCommonCellStyle(cellStyle, font);

        switch (color) {
            case 1:
                cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
                break;
            case 2:
                cellStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
                break;
            default:
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }

        if (changeFont) {
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }

        font.setFontHeightInPoints((short) this.TEXT_FONT_SIZE);
        cellStyle.setFont(font);

        return (cellStyle);
    }

    private void createCommonCellStyle(CellStyle cellStyle, Font font) {
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        font.setFontName(this.ARIAL_FONT);
    }
}
