package mms.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DirectoryMaker {
	public static File makeDir(String staffID) {
		String usrHome = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "MMSDoc"
				+ File.separator + staffID;
		File file = new File(usrHome);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		return file;
	}

	public static void copyFile(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(oldPath);// 可替换为任何路径何和文件名
			output = new FileOutputStream(newPath);// 可替换为任何路径何和文件名
			int in = input.read();
			while (in != -1) {
				output.write(in);
				in = input.read();
			}
		} catch (IOException e) {
		} finally {
			try {
				if(input != null) input.close();
				if(output != null) output.close();
			} catch (IOException e) {
			}
		}
	}

	public static boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	public static String getLocalAvatarName(String localUserURL) {
		String localAvatarName = "";
		File file = new File(localUserURL);
		if(file.isDirectory()) {
			String[] files = file.list();
			for(String s : files) {
				if(s.startsWith("avatar")) {
					localAvatarName = s;
				}
			}
		}
		return localAvatarName;
	}

}
