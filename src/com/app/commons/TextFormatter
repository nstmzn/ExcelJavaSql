/**
 * 
 */
package com.app.commons;


public class TextFormatter {

	private static String tokens[] = 
		{"##filename", "##tablename", "##dbname","##recordcount","##exceptiontrace", "##nl", "##taskname"};
	
	public static String formatText(String str, TokensVO vo){
		
		for(String token : tokens){
			
			//Note that backslashes (\) and dollar signs ($) in the replacement string 
			//may cause the results to be different than if it were being treated as a literal replacement string.
			
			str = str.replaceAll(token, getValue(token, vo));
		}
//		System.out.println(str);
		return str;
		
	}
	
	private static String getValue(String token, TokensVO vo){
		
		if(token.equalsIgnoreCase("##filename")){
			return vo.getFileName();
			
		}else if(token.equalsIgnoreCase("##tablename")){
			return vo.getTableName();
			
		}else if(token.equalsIgnoreCase("##dbname")){
			return vo.getDatabaseName();
			
		}else if(token.equalsIgnoreCase("##recordcount")){
			return ""+ vo.getRecordCount();
			
		}else if(token.equalsIgnoreCase("##exceptiontrace")){
			return vo.getExceptionTrace();
			
		}else if(token.equalsIgnoreCase("##nl")){
			return "\n <br>";
			
		}else if(token.equalsIgnoreCase("##taskname")){
			return vo.getTaskName();
		}
		
		return "?";
	}

}
