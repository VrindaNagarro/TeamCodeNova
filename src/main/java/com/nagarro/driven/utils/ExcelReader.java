package com.nagarro.driven.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {

    private final String filePath;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the entire sheet and returns data as a List of Map<String, String>.
     * Each row becomes a Map with column headers as keys.
     */
    public List<Map<String, String>> getData(String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet \"" + sheetName + "\" not found in file: " + filePath);
            }

            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row missing in sheet: " + sheetName);
            }

            int lastRowNum = sheet.getLastRowNum();
            int totalCols = headerRow.getLastCellNum();

            // Loop through rows (skip header)
            for (int i = 1; i <= lastRowNum; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < totalCols; j++) {
                    String header = getCellValue(headerRow.getCell(j));
                    String value = getCellValue(currentRow.getCell(j));
                    rowData.put(header, value);
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Read data from the first sheet of the workbook. Useful when sheet name is unknown.
     */
    public List<Map<String, String>> getDataFirstSheet() {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new RuntimeException("No sheets found in file: " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row missing in first sheet of file: " + filePath);
            }

            int lastRowNum = sheet.getLastRowNum();
            int totalCols = headerRow.getLastCellNum();

            for (int i = 1; i <= lastRowNum; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < totalCols; j++) {
                    String header = getCellValue(headerRow.getCell(j));
                    String value = getCellValue(currentRow.getCell(j));
                    rowData.put(header, value);
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file (first sheet): " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Get a single cell value by sheet, row, and column index.
     */
    public String getCellData(String sheetName, int rowNum, int colNum) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row row = sheet.getRow(rowNum);
            if (row == null) return "";

            Cell cell = row.getCell(colNum);
            return getCellValue(cell);

        } catch (IOException e) {
            throw new RuntimeException("Failed to get cell data: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}
