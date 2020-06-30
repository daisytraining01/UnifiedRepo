package com.maveric.core.utils.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelData {

    private static final Logger logger = LogManager.getLogger();

    private Workbook workbook;
    private ArrayList<String> columns;
    private Sheet sheet = null;
    private File excelFile = null;

    public ExcelData(String excelPath) {
        this.excelFile = new File(excelPath);
    }

    public ExcelData setSheet(int sheetNo) {
        try {
            columns = new ArrayList<>();
            this.sheet = workbook.getSheetAt(sheetNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ExcelData setSheet(String sheetName) {
        try {
            columns = new ArrayList<>();
            this.sheet = workbook.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public int getRows() {
        int count = 0;
        final Iterator<Row> rowsIterator = sheet.iterator();
        Row row;
        while (rowsIterator.hasNext()) {
            row = rowsIterator.next();
            if (!readCell(row.getCell(0)).isEmpty()) {
                count++;
            }
        }
        return count;
    }


    public ExcelData writeData(String column, int line, String value) {
        if (this.columns.isEmpty()) {
            this.columns = getColumns();
        }
        final int colIndex = columns.indexOf(column);
        final Row row = sheet.getRow(line);
        Cell cell = row.getCell(colIndex);
        if (cell != null) {
            row.removeCell(cell);
        }
        cell = row.createCell(colIndex);
        cell.setCellValue(value);
        return this;
    }

    public ExcelData writeData(int column, int line, String value) {
        final Row row = sheet.getRow(line);
        Cell cell = row.getCell(column);
        if (cell != null) {
            row.removeCell(cell);
        }
        cell = row.createCell(column);
        cell.setCellValue(value);
        return this;
    }


    public String readData(String column, int line) {
        if (this.columns.isEmpty()) {
            this.columns = getColumns();
        }
        final int colIndex = columns.indexOf(column);
        if (colIndex == -1) {
            throw new RuntimeException("Invalid Column Provided : " + column);
        }
        final Row row = sheet.getRow(line);
        final Cell cell = row.getCell(colIndex);
        if (cell == null) {
            return "";
        } else {
            return readCell(cell);
        }
    }

    public String readData(int column, int line) {

        final Row row = sheet.getRow(line);
        final Cell cell = row.getCell(column);
        if (cell == null) {
            return "";
        } else {
            return readCell(cell);
        }
    }

    public ArrayList<String> readLine(int line) {
        ArrayList<String> data = new ArrayList<>();
        if (this.columns.isEmpty()) {
            this.columns = getColumns();
        }
        try {
            final Row row = sheet.getRow(line);
            if (row == null || readCell(row.getCell(0)).isEmpty()) {
                return null;
            } else {
                Cell cell;
                for (int i = 0; i < columns.size(); i++) {
                    if ((cell = row.getCell(i)) == null) {
                        data.add("");
                    } else {
                        data.add(readCell(cell));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Data Not Found");
            e.printStackTrace();
        }
        return data;
    }


    public ArrayList<String> getColumns() {

        ArrayList<String> columns = new ArrayList<>();
        try {
            Row row = sheet.getRow(0);
            Cell cell;
            for (int i = 0; (cell = row.getCell(i)) != null; i++) {
                columns.add(cell.getStringCellValue());
            }
            if (columns.size() < 2) {
                throw new RuntimeException("Empty File");
            }
        } catch (Exception e) {
            logger.error("Columns not found");
            e.printStackTrace();
        }
        return columns;

    }

    public ExcelData open() {
        try (FileInputStream fileIn = new FileInputStream(excelFile)) {
            workbook = new XSSFWorkbook(fileIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    private String readCell(Cell cell) {
        String txt = "";
        if (cell != null) {
            txt = readCellByType(cell, cell.getCellType());
        }
        return txt.trim();
    }


    private String readCellByType(Cell cell, CellType type) {
        String txt = "";
        if (cell != null) {
            switch (type) {
                case NUMERIC:
                    txt = dateOrNumberProcessing(cell);
                    break;
                case STRING:
                    txt = String.valueOf(cell.getRichStringCellValue());
                    break;
                case FORMULA:
                    txt = readCellByType(cell, cell.getCachedFormulaResultType());
                    break;
                case BLANK:
                    break;
                default:
                    logger.error("Invalid Cell Type");
                    break;
            }
        }
        return txt;
    }


    private String dateOrNumberProcessing(Cell cell) {
        String txt;
        if (DateUtil.isCellDateFormatted(cell)) {
            final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            txt = formatter.format(cell.getDateCellValue());
        } else {
            txt = String.valueOf(cell.getNumericCellValue());
        }
        return txt;
    }

    public ExcelData save() {
        try (FileOutputStream fileOut = new FileOutputStream(excelFile)) {
            workbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


}
