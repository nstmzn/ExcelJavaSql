package com.app.win;

import java.io.File;

import org.apache.log4j.Logger;



public class WinUtils {
	
	private static Logger log = Logger.getLogger(WinUtils.class.getName());

	public static void deleteFile(String path){
		try{
    		File file = new File(path);

    		if(file.delete()){
    			log.info(file.getName() + " is deleted!");
    		}else{
    			log.error("Could not delete "+file.getName());
    		}

    	}catch(Exception e){
    		log.error(e);
    		e.printStackTrace();
    	}
	}
	
	public static void renameFile(String oldPath, String newPath){
		File file = new File(oldPath);
		File file2 = new File(newPath);

		if (file2.exists()){
			log.error("New file "+ newPath+" already exists and so can not rename file "+oldPath);
		}
		
		boolean success = file.renameTo(file2);
		 
		if (success) {
			log.info(oldPath + " renamed to "+ newPath);
		}else{
			log.error("Could NOT rename " + oldPath + " to "+ newPath);
		}
	}
	
	public static void moveFile(String oldPath, String newPath){
		File file = new File(oldPath);
		
		boolean success = file.renameTo(new File(newPath));
		
		if (success) {
			log.info(oldPath + " moved to "+ newPath);
		}else{
			log.error("Could NOT move " + oldPath + " to "+ newPath);
		}
	}
	
}
