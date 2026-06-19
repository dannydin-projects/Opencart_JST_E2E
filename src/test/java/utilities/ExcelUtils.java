package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    public FileInputStream fi;
    public FileOutputStream fo;
    public XSSFWorkbook workbook;
    public XSSFSheet sheet;
    public XSSFRow row;
    public XSSFCell cell;
    public CellStyle style;

    String path;

    public ExcelUtils(String path) {
        this.path = path;
    }

    // Returns total number of rows
    public int getRowCount(String sheetName) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        int rowcount = sheet.getLastRowNum();

        workbook.close();
        fi.close();

        return rowcount;
    }

    // Returns total number of cells in a row
    public int getCellCount(String sheetName, int rownum) throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);

        int cellcount = row.getLastCellNum();

        workbook.close();
        fi.close();

        return cellcount;
    }

    // Reads cell data
    public String getCellData(String sheetName, int rownum, int column)
            throws IOException {

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = row.getCell(column);

        DataFormatter formatter = new DataFormatter();

        String data;

        try {
            data = formatter.formatCellValue(cell);
        } catch (Exception e) {
            data = "";
        }

        workbook.close();
        fi.close();

        return data;
    }

    // Writes data into Excel
    public void setCellData(String sheetName,
                            int rownum,
                            int column,
                            String data) throws IOException {

        File xlfile = new File(path);

        // If file does not exist, create it
        if (!xlfile.exists()) {

            workbook = new XSSFWorkbook();
            fo = new FileOutputStream(path);
            workbook.write(fo);

            workbook.close();
            fo.close();
        }

        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);

        // If sheet does not exist, create it
        if (workbook.getSheetIndex(sheetName) == -1)
            workbook.createSheet(sheetName);

        sheet = workbook.getSheet(sheetName);

        // If row does not exist, create it
        if (sheet.getRow(rownum) == null)
            sheet.createRow(rownum);

        row = sheet.getRow(rownum);

        cell = row.createCell(column);
        cell.setCellValue(data);

        fo = new FileOutputStream(path);
        workbook.write(fo);

        workbook.close();
        fi.close();
        fo.close();
    }
}