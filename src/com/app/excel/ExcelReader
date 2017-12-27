package com.app.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private final Logger log = Logger.getLogger(getClass().getName());
	
	public List<ArrayList<Object>> getExcelData(String filePath, Map<String, Integer> columnsMap) throws Exception {

		log.info("Reading file:- " + filePath);

		FileInputStream fis = null;
		XSSFWorkbook workbook = null;

		List<ArrayList<Object>> finalList = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> rowList;

		try {

			fis = new FileInputStream(new File(filePath));
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			// to get which column exists at what index?
			if (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				int ii = 0;
				Cell cell;

				while (cellIterator.hasNext()) {
					cell = cellIterator.next();

					String cellValue = (String)getCellValue(cell);

					if (columnsMap.containsKey(cellValue)) {
						columnsMap.put(cellValue, ii);
					}
					ii++;
				}

			}// if

			//if could not get correct data
			for (Entry<String, Integer> entry : columnsMap.entrySet()) {
				if (entry.getValue() == -1) {
					log.error(entry.getKey()+" column is in xml and not in Excel sheet:- "+filePath);
					log.error("Incorrect column names configured in XML not present in excel.");
					
//					return null;    //commented and instead exception is thrown 
					throw new RuntimeException("Incorrect column names configured in XML not present in excel.");
				}
			}

			while (rowIterator.hasNext()) {
				rowList = new ArrayList<Object>();
				Row row = rowIterator.next();

				for (Entry<String, Integer> entry : columnsMap.entrySet()) {
					Cell cell = row.getCell(entry.getValue()); // get particular cell
					rowList.add(getCellValue(cell));
				}

				finalList.add(rowList);

			}// while- row iterator

		} catch (Exception e) {
			log.error("Exception occured while reading excel sheet. Ex- "+e.getMessage());
			
			if(log.isDebugEnabled()){
				for(StackTraceElement st : e.getStackTrace()){
					log.debug(st.toString());
				}
			}
//			return null;
			throw e;

		} finally {
			try {
				workbook.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("End reading file.");

		return finalList;
	}

	
	//this method need to improve for more types
	private Object getCellValue(Cell cell) {

		switch (cell.getCellType()) {

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue(); 
                return date;

            } else {
            	return "" + cell.getNumericCellValue();
            	
            }

		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();

			
		case Cell.CELL_TYPE_BLANK:
			log.warn("Blank cell found will return blank-string:- Cell type:- "+cell.getCellType());
			return "";
			
		default:
			log.error("Not expected:- Cell type:- "+cell.getCellType());
			////
		}
		
		log.error("This message is not supposed to be printed. Could not get correct cell type.");
		log.error("*******returning value blank string*******");
		return "";

	}

}
