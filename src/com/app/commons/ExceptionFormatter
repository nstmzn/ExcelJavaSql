package com.app.commons;

public class ExceptionFormatter {
	public static String getExceptionTrace(Exception e){
		
		if(e == null){
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(e.toString() + " <br> ");
		
			
		for(StackTraceElement st : e.getStackTrace()){
//			Note  \n not working in mail properly (mail body support html), <br> is working
			sb.append(st.toString() + " <br> ");  
//			sb.append("\n");
		}
		
		return sb.toString();
	}
}
