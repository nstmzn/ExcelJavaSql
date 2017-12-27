package com.app.commons;

public class TokensVO {
	private String fileName;
	private String databaseName;
	private String tableName;
	private int recordCount;
	private String exceptionTrace;
	private String taskName;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		if(databaseName != null && databaseName.contains("CONNECTION_")){
			this.databaseName = databaseName.replaceFirst("CONNECTION_", "");
		}else{
			this.databaseName = databaseName;
		}
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getExceptionTrace() {
		return exceptionTrace;
	}
	public void setExceptionTrace(String exceptionTrace) {
		this.exceptionTrace = exceptionTrace;
	}
	
	@Override
	public String toString() {
		return "TokensVO [fileName=" + fileName + ", databaseName="
				+ databaseName + ", tableName=" + tableName + ", recordCount="
				+ recordCount + ", exceptionTrace=" + exceptionTrace
				+ ", taskName=" + taskName + "]";
	}

}
