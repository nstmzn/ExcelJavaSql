package com.app.main;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.app.commons.AppConstants;
import com.app.commons.ExceptionFormatter;
import com.app.commons.MailManager;
import com.app.commons.ResponseVO;
import com.app.commons.TextFormatter;
import com.app.commons.TokensVO;
import com.app.db.dao.InsertDataDAO;
import com.app.db.dao.QueryGenerator;
import com.app.excel.ExcelReader;
import com.app.win.WinUtils;

public class AppMain {

	private final static Logger log = Logger.getLogger(AppMain.class.getName());
	private ExecuteTaskActionListener taskExecuter;
	
	public void registerExecuteTaskActionListener(ExecuteTaskActionListener taskExecuter){
		this.taskExecuter = taskExecuter;
	}
	
	private void updateUIStatus(Integer progressPercentage, String progressText){
		if(progressPercentage != null){
			taskExecuter.updateProgressUI(progressPercentage);
		}
		if(progressText != null){
			taskExecuter.updateupdateProgressText(progressText);
		}
	}

	public void init() {
		try{
			Properties props = new Properties();
			props.load(new FileInputStream( AppConstants.log4jPropertyFilePath ));
			PropertyConfigurator.configure(props);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
//			throw new Exception("Could not load property file: "+AppConstants.log4jPropertyFilePath, e);
			e.printStackTrace();
		}
	}


	private InsertDataDAO dao = new InsertDataDAO();
	private ExcelReader er = new ExcelReader();

	public void stepExcelToDB(TaskVO taskVo) {
		
		updateUIStatus(2, "Reading file...");
		
		TokensVO tokensVo = new TokensVO();
		ResponseVO responseVo = null;
		
		try{
			
			tokensVo.setDatabaseName(taskVo.getConnection4db());
			
			String[] split = taskVo.getExcelFilePath().split("\\\\");
			String excelFileName = split[split.length-1];
			
			tokensVo.setFileName(excelFileName);
			tokensVo.setTableName(taskVo.getDbTableName());
			tokensVo.setTaskName(taskVo.getTaskName());
			
			String excelFilePath = taskVo.getExcelFilePath();
	
			Map<String, Integer> map = new LinkedHashMap<String, Integer>();
	
			String excelColumns = taskVo.getExcelColumns();
			String[] columnsArray = excelColumns.split(",");
			for (String str : columnsArray) {
				map.put(str.trim(), -1);
			}
	
			//reading data from excel   :: throws Exception  
			List<ArrayList<Object>> excelData = er.getExcelData(excelFilePath, map);
	
			if (excelData != null) {
				
				updateUIStatus(10, "Inserting data...");
				
				String tableName = taskVo.getDbTableName();
				String[] colArray = taskVo.getDbTableColumns().split(",");
				
				String query = QueryGenerator.generateInsertQuery(tableName, colArray);
				log.debug(query);
				
				//inserting to database    :: throws Exception --removed
				responseVo = dao.insertData(taskVo.getConnection4db(), query, excelData);
				
				tokensVo.setRecordCount(responseVo.getInsertionCount());
				
				updateUIStatus(40, responseVo.getInsertionCount()+" Record inserted...");
				
				if( ! responseVo.isAnyException() ){
					//do post actions 
					doPostActions(taskVo);
					
					MailManager.send( taskVo.getMailTo(), 
									  taskVo.getMailTo(), 
									  taskVo.getMailCc(),
									  taskVo.getMailBcc(), 
									  taskVo.getMailSubject(),
									  TextFormatter.formatText(taskVo.getMailSuccessBody(), tokensVo )
									  );
					
				}else{
					log.info("Post-action skipped. Sending error mail.");
					throw responseVo.getExceptionObj(); 
					
				}
	
				log.info("done inserting one task.");
				
			}else{
				log.info(excelData + " :excelData");
			}
		
		}catch(Exception e){
			e.printStackTrace();
			
//			tokensVo.setExceptionTrace(ExceptionFormatter.getExceptionTrace( e ));
//			
//			MailManager.send( taskVo.getMailTo(), 
//					  taskVo.getMailTo(), 
//					  taskVo.getMailCc(),
//					  taskVo.getMailBcc(), 
//					  "***ERROR*** "+taskVo.getMailSubject(),
//					  TextFormatter.formatText(taskVo.getMailFailureBody(), tokensVo )
//					  );
			
		}
	}
	
	private void doPostActions(TaskVO vo){
		String action  = vo.getActionName();
		String originalFilePath = vo.getExcelFilePath();
		
		String[] split = vo.getExcelFilePath().split("\\\\");
		String excelFileName = split[split.length-1];
		
		if(action.equalsIgnoreCase(AppConstants.ACTION_DELETE)){
			WinUtils.deleteFile(originalFilePath);
			
		}else if(action.equalsIgnoreCase(AppConstants.ACTION_MOVE)){
			String newFilePath = vo.getActionPath() + getExtensionStr() + excelFileName ;
			WinUtils.moveFile(originalFilePath, newFilePath);
			
		}else if(action.equalsIgnoreCase(AppConstants.ACTION_RENAME)){
			String newFilePath = 
					originalFilePath.substring(0, originalFilePath.lastIndexOf(File.separator))
					+ File.separator
					+ getExtensionStr() 
					+ excelFileName;
			WinUtils.renameFile(originalFilePath, newFilePath);
			
		}else{
			log.error("Action "+action+"in invalid...");
		}
		
		
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");
	public String getExtensionStr(){
		return sdf.format(new Date()) + "_" ;
	}

}
