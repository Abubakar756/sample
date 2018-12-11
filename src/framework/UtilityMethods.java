package framework;

import java.io.File;

public class UtilityMethods {
	
	private static final ThreadLocal<UtilityMethods> T=new ThreadLocal<UtilityMethods>();
	public static UtilityMethods get() {
		return T.get();
	}
	public static void  set(UtilityMethods utilityMethods) {
		T.set(utilityMethods);
	}
	
//********************************************************************************************************
	/**
	 * Creating a folder
	 * @param foldPath
	 * @author Abubakar
	 */
	public static void createFolder(String foldPath) {
		
		File f=new File(foldPath);
		if(!f.exists()) {
			f.mkdir();
		}
	}
//**********************************************************************************************************
	/**
	 * Deleting folder
	 * @param foldPath
	 * @author Abubakar
	 */
	public static void deleteFolder(String foldPath) {
		File f=new File(foldPath);
		if(f.exists()) {
			File[] allfiles=f.listFiles();
			for(File file:allfiles) {
				try {
					file.delete();
				}catch(Exception e) {
					System.out.println("Unable to delete file"+file);
				}
			}
		}
	}
//***********************************************************************************************************
	/**
	 * making file path
	 * @param path
	 */
	public void makePath(String path) {
		File f=new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
	}
//************************************************************************************************************
	
}
