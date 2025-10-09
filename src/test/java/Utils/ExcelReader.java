package Utils;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    private Sheet sheet;

    public ExcelReader(String path, String sheetName) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        Workbook wb = new XSSFWorkbook(fis);
        sheet = wb.getSheet(sheetName);
    }

    // ✅ Get cell data
    public String getData(int row, int col) {
        try {
            Cell cell = sheet.getRow(row).getCell(col);

            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();

                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    }
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue); // integers
                    } else {
                        return String.valueOf(numericValue); // decimals
                    }

                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());

                case FORMULA:
                    return cell.getCellFormula();

                case BLANK:
                    return "";

                default:
                    return "UNKNOWN_TYPE";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    // ✅ Total row count (excluding header row)
    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows() - 1;
    }

    // ✅ Column count (based on header row)
    public int getColumnCount() {
        return sheet.getRow(0).getPhysicalNumberOfCells();
    }

    // ✅ Get header name at given column
    public String getHeader(int col) {
        return sheet.getRow(0).getCell(col).toString().trim();
    }
}
