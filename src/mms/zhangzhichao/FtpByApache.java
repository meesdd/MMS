package mms.zhangzhichao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.NoRouteToHostException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;


public class FtpByApache {
    private FTPClient f = null;
//    private static String host = "115.159.143.187";
//    private static String userName = "mmsdocuser";
//    private static String password = "mmsdocuser";
//    private static int port = 22;
    private Properties props; 
    private InputStream in;
    private BufferedReader bf; //解决中文乱码问题
    
    private static String host;
    private static String username;
    private static String password;
    private static int port;
    
    //默认构造函数
    public FtpByApache() throws IOException{
    	 f = new FTPClient();
         //得到连接
    	props = new Properties(); 
    	in = props.getClass().getResourceAsStream("/config/MMSconfig");
    	bf = new BufferedReader(new InputStreamReader(in)); //解决中文乱码问题
    	try {
    		props.load(bf);
    	} catch (IOException e) {
    	  e.printStackTrace();
    	}
    	host = props.getProperty("ftpserverhost");
    	username = props.getProperty("ftpadminuser");
    	password = props.getProperty("ftpadminpsw");
    	port = Integer.parseInt(props.getProperty("port"));
    	
    	this.get_connection(host, username, password, port);  
    }
    public FtpByApache(String url,String username,String password, int port) throws IOException {
        f = new FTPClient();
        //得到连接
        this.get_connection(url,username,password, port);    
    }
    
    public boolean deleteRemoteFile(String remoteFilePath) {
    	boolean flag = false;
    	try {
			flag = f.deleteFile(remoteFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
    }
    //连接服务器方法
    private void get_connection(String url, String username, String password, int port) throws IOException {
        try {
            //连接指定服务器，默认端口为21  
            f.connect(url);
            f.setDefaultPort(port);
            
            //设置链接编码，windows主机UTF-8会乱码，需要使用GBK或gb2312编码  
            f.setControlEncoding("GBK");
            //进入本地被动模式 
            f.enterLocalPassiveMode();           
            //登录
            boolean login=f.login(username, password);
                            
        } catch(NoRouteToHostException e1) {
        	JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "网络异常！", "失败", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void close_connection() {
    	boolean logout=false;
        try {
            logout = f.logout();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if(logout) {
             
         } else {
             
         }
        if(f.isConnected())
            try {
                
                f.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }    
    //获取所有文件和文件夹的名字
    public FTPFile[] getAllFile(){
        FTPFile[] files = null;
        try {
            files = f.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* for(FTPFile file:files)
        {
            if(file.isDirectory())
                
            if(file.isFile())
                
        }*/
        return files;
    }
  //生成InputStream用于上传本地文件  
   	public void upload(String localpath, String remotePath) throws IOException {// 输入远程保存路径是尽量用"/"而不是"\\"
   		FileInputStream input = null;
   		String[] fileName = null;

   		
   		String []splitString = remotePath.split("/");
   	    int changeTimes = splitString.length - 1;
   	    
   	    if(changeTimes != 0){
   	    	//如果是多级目录则切换多级工作路径
          	for(int i = 0; i < changeTimes; i++){
          		if(isExistent(splitString[i])){
          			if(f.changeWorkingDirectory(splitString[i])){
          				
          			}else{
          				
          			}
          		}else{
          			
          			if(makeDirectory(splitString[i])){
          				if(f.changeWorkingDirectory(splitString[i])){
          					
          				}else{
                  			
                  		}
          			}else{
          				
          			}
          		}
          	}
   	    }
   		try {
   			input = new FileInputStream(localpath);
   			fileName = localpath.split("\\\\");
   			
   		} catch (FileNotFoundException e) {
   			e.printStackTrace();
   		}

   		// 上传文件
   		// 设置传输模式
   		if (f.setFileType(FTP.BINARY_FILE_TYPE) == true) {
   			
   		} else {
   			
   		}
   		
   		String path = remotePath.substring(0, remotePath.lastIndexOf("/"));
   		
   		f.setBufferSize(1024);
   		
   		
   		boolean b = f.storeFile(remotePath.substring(remotePath.lastIndexOf("/") + 1), input);
   		if (b == true) {
   			
   		} else {
   			System.err.println("上传失败");
   		}
          if(changeTimes != 0){
          	//如果是多级目录则切换回原路径
           	if(remotePath.startsWith("./") == true){
           		changeTimes--;
           	}
           	for(int i = 0; i < changeTimes; i++){
           		f.changeToParentDirectory();
           	}
          }
   		if (input != null) input.close();
   	}
      //remotePath为要下载的远程文件的绝对路径
      //localPath为要保存到的本地文件的绝对路径
      public void download(String remotePath, String localPath) throws IOException{
          OutputStream output=null;
          String []splitString = remotePath.split("/");
          int changeTimes = splitString.length - 1;
          if(changeTimes != 0){
          	//如果是多级目录则切换多级工作路径
          	for(int i = 0; i < changeTimes; i++){
          		if(isExistent(splitString[i])){
          			f.changeWorkingDirectory(splitString[i]);
          			
          		}else{
          			
          		}
          	}
          }
          try {
              output = new FileOutputStream(localPath + splitString[splitString.length - 1]);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }
          f.retrieveFile(remotePath, output);
          if(output!=null) {
          	try {
                  if(output!=null)
                  output.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          if(changeTimes != 0){
          	//如果是多级目录则切换回原路径
           	if(remotePath.startsWith("./") == true){
           		changeTimes--;
           	}
           	for(int i = 0; i < changeTimes; i++){
           		f.changeToParentDirectory();
           	}
          }
      }
      public void newDownload(String remotePath,String localPath) throws IOException{
  		OutputStream output=null;
  		String []splitString = remotePath.split("/");
  		int changeTimes = splitString.length - 1;
  		
  		//设置linux环境
  		FTPClientConfig conf = new FTPClientConfig( FTPClientConfig.SYST_UNIX);
  		f.configure(conf);
  		//设置访问被动模式
  		f.setRemoteVerificationEnabled(false);
  		f.enterLocalPassiveMode();//本地被动
  		// 设置文件类型
  		if (f.setFileType(FTP.BINARY_FILE_TYPE) == true) {
  			
  		} else {
  			
  		}
  		if(changeTimes != 0){
  			//如果是多级目录则切换多级工作路径
  			for(int i = 0; i < changeTimes; i++){
  				if(isExistent(splitString[i])){
  					f.changeWorkingDirectory(splitString[i]);
  					
  				}else{
  					
  				}
  			}
  		}
  		try {
  			File file = new File(localPath + splitString[splitString.length - 1]);
  			output = new FileOutputStream(file);
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  		}
  		if(f.retrieveFile(splitString[splitString.length - 1], output) == false){
  			
  		}else{
  			
  		}
  		if(output!=null) {
  			try {
  				if(output!=null)
  					output.close();
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  		}
  		if(changeTimes != 0){
  			//如果是多级目录则切换回原路径
  			if(remotePath.startsWith("./") == true){
  				changeTimes--;
  			}
  			for(int i = 0; i < changeTimes; i++){
  				f.changeToParentDirectory();
  			}
  		}
  	}

    //改变目录路径
    public boolean changeWorkingDirectory(String directory) {
    	boolean flag = true;
    	try {
    		flag = f.changeWorkingDirectory(directory);
    		if (flag) {
    			
    		} else {
    			
    		}
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    	}
    	return flag;
    }
   //创建目录
   public boolean makeDirectory(String dir) {
	   boolean flag = true;
	   try {
		   flag = f.makeDirectory(dir);
		   if (flag) {
			   
		   } else {
			   
           }
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
       return flag;
   }
   //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
   public boolean createDirecroty(String remote) throws IOException {
	   boolean success = true;
	   String directory = remote + "/";
//           String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
	   // 如果远程目录不存在，则递归创建远程服务器目录
	   if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
		   int start = 0;
		   int end = 0;
		   if (directory.startsWith("/")) {
			   start = 1;
           } else {
               start = 0;
           }
		   end = directory.indexOf("/", start);
		   String path = "";
		   String paths = "";
		   while (true) {
			   String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
			   path = path + "/" + subDirectory;
			   if (!existFile(path)) {
				   if (makeDirectory(subDirectory)) {
					   changeWorkingDirectory(subDirectory);
                   } else {
                	   
                	   changeWorkingDirectory(subDirectory);
                   }
               } else {
                   changeWorkingDirectory(subDirectory);
               }
			   paths = paths + "/" + subDirectory;
			   start = end + 1;
			   end = directory.indexOf("/", start);
			   // 检查所有目录是否创建完毕
			   if (end <= start) {
				   break;
               }
		   }
	   }
	   return success;
   }
   //判断ftp当前路径下是否存在指定文件
   public boolean isExistent(String fileName){
	   boolean flag = false;
	   FTPFile[] files = getAllFile();
	   for(FTPFile f : files){
		   if(fileName.equals(".") || fileName.equals("..")){
			   return true;
		   }
		   if(f.getName().equals(fileName) == true){
			   flag = true;
			   break;
		   }
	   }
	   return flag;
   }
   //判断ftp服务器路径下是否有文件存在    
   public boolean existFile(String path) throws IOException {
	   boolean flag = false;
	   FTPFile[] ftpFileArr = f.listFiles(path);
	   if (ftpFileArr.length > 0) {
		   flag = true;
	   }
       return flag;
   }
   //上传单个文件
   public boolean uploadFile(File localFile, String remoteFile)	throws IOException {
	   boolean flag = false;
	   InputStream in = new FileInputStream(localFile);
	   String remote = new String(remoteFile.getBytes("GBK"), "iso-8859-1");
	   try {
		   if (f.storeFile(remote, in)) {
			   flag = true;
			   
		   } else {
			   
           }
           in.close();
       } catch (IOException e) {
    	   e.printStackTrace();
       }
       return flag;
   }
   //利用递归上传多个多目录文件
   public boolean uploadFiles(String localPathFileName, String remotePathFileName) throws IOException {
	   File local = new File(localPathFileName);
	   if (local.isDirectory()) {
		   try {
			   f.changeWorkingDirectory("/");
               createDirecroty(remotePathFileName);
               } catch (Exception e) {
            	   e.printStackTrace();
               }
	   } else {
		   File local1 = new File(localPathFileName);
		   uploadFile(local1, remotePathFileName);
	   }
	   File sourceFile[] = local.listFiles();
	   for (int i = 0; i < sourceFile.length; i++) {
		   if (sourceFile[i].exists()) {
			   String path = sourceFile[i].getAbsolutePath().substring(localPathFileName.length());
			   if (sourceFile[i].isDirectory()) {
				   this.uploadFiles(sourceFile[i].getAbsolutePath(), remotePathFileName + path);
				   } else {
					   if (!path.equals("/.DS_Store")) {
						   File file2 = new File(sourceFile[i].getAbsolutePath());
                           uploadFile(file2, remotePathFileName + path);
                       }
                   }
               }
           }
       return true;
    }
    
    /*public static void main(String []args) throws IOException {
		FtpByApache ftpByApache = new FtpByApache();
    	ftpByApache.upload("F:\\1.png", "./0919/1.png");
    	ftpByApache.upload("F:\\1.png", "./0919/2.png");
		ftpByApache.close_connection();
	}*/
}