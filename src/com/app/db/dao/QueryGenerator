package com.app.db.dao;

public class QueryGenerator {
	
	public static String generateInsertQuery(String tableName, String[] columns){
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+ tableName +" (");
		
		for(String col : columns){
			sb.append(col.trim() +",");
		}
		
		sb.append("CREATED_ON, CREATED_BY");
//		sb.deleteCharAt(sb.length()-1);  //to remove last comma
		
		sb.append(") VALUES (");
		
		
		for(String col : columns){
			sb.append("?,");
		}
		
		sb.append("sysdate, 'automation'");
//		sb.deleteCharAt(sb.length()-1);  //to remove last comma
		
		sb.append(")");
		
		return sb.toString();
	}
	
}
