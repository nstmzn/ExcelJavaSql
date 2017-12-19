package com.app.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.commons.AppConstants;
import com.app.commons.MailManager;
import com.app.commons.ResponseVO;
import com.app.db.dao.InsertDataDAO;
import com.app.db.dao.QueryGenerator;
import com.app.excel.ExcelReader;
import com.app.main.TaskVO;

public class ExcelDBService {
	
//
//	private final Logger log = Logger.getLogger(getClass().getName());
//	
//	public void stepExcelToDB(TaskVO vo){
//		
//		String excelFilePath = vo.getExcelPath()+vo.getExcelFileName();
//		
//		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
//
//		String excelColumns = vo.getExcelColumns();
//		String[] columnsArray = excelColumns.split(",");
//		for(String str : columnsArray){
//			map.put(str, -1);
//		}
//		
//		List<ArrayList<String>> excelData = er.getExcelData(excelFilePath, map);
//		
//		if(excelData != null){
//			String tableName = vo.getDbTableName();
//			String[] colArray = vo.getDbTableColumns().split(",");
//			String query = QueryGenerator.generateInsertQuery(tableName, colArray);
//			
//			log.debug(query);
////			log.info("Inserting task "+ vo.getTaskName());
//			ResponseVO responseVO = dao.insertData(vo.getConnection4db(), query, excelData);
//			
//			MailManager.send(vo.getMailTo(), 
//					vo.getMailTo(),
//					vo.getMailCc(),
//					vo.getMailBcc(),
//					vo.getMailSubject(),
//					String.format(vo.getMailBody(), responseVO.toString()) );
//			
//			log.info("done inserting one task.");
//		}
//	}
	
}
