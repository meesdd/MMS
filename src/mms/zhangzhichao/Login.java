package mms.zhangzhichao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.commons.lang3.RandomStringUtils;


import mms.common.ConnectionRegistrar;
import mms.common.Staff;
import mms.wangzhen.*;
import mms.common.*;

public class Login extends JFrame {
	
	private ConnectionRegistrar cr;//wangzhen
	
	private MainFrame mf;//wangzhen
	
	private JButton jButton_login, jButton_register;
	private JTextField jTextField_username, jTextField_validate;
	private JPasswordField jPasswordField_password;
	private String randomText;
	private JPanel jPanel_content;
	private JCheckBox jCheckBox;
	
	
	private static final long serialVersionUID = -4655235896173916415L;
	
	private class LoginActionListener implements ActionListener{

		java.sql.PreparedStatement sql = null;
		ResultSet resultSet = null;
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource()==(jButton_login)){
				String inputUsername = jTextField_username.getText();
				char[] pw = jPasswordField_password.getPassword();//wangzhen
				String inputPassword = new String(pw);
				String inputValidate = jTextField_validate.getText();
				try {
					//Connection connection = getConnection();
					if(inputUsername.length() == 0){
						JOptionPane.showInternalMessageDialog(getContentPane(), "请输入员工号","警告", JOptionPane.INFORMATION_MESSAGE); 
						dispose();
						Login l = new Login();
						l.launch();
						return;
					}
					if(inputPassword.length() == 0){
						JOptionPane.showInternalMessageDialog(getContentPane(), "请输入密码","警告", JOptionPane.INFORMATION_MESSAGE); 
						dispose();
						Login l = new Login();
						l.launch();
						return;
					}
					if(inputValidate.length() == 0){
						JOptionPane.showInternalMessageDialog(getContentPane(), "请输入验证码","警告", JOptionPane.INFORMATION_MESSAGE); 
						dispose();
						Login l = new Login();
						l.launch();
						return;
					}
					if(inputValidate.toUpperCase().equals(randomText.toUpperCase()) == false){
						JOptionPane.showInternalMessageDialog(getContentPane(), "验证码错误","警告", JOptionPane.INFORMATION_MESSAGE); 
						dispose();
						Login l = new Login();
						l.launch();
						return;
					}
					sql = cr.getConnection().prepareStatement("select * from staff where staffID = ? and password = ?");
					sql.setString(1, inputUsername);
					sql.setString(2, inputPassword);
					resultSet = sql.executeQuery();
					if(resultSet.next() == false){
						JOptionPane.showInternalMessageDialog(getContentPane(), "职工号或密码错误","警告", JOptionPane.INFORMATION_MESSAGE); 
						return;
					}else{
						resultSet.first();
						Staff staff = new Staff();
						staff.setStaffID(resultSet.getString(1));
						staff.setStaffName(resultSet.getString(2));
						staff.setDepartment(resultSet.getString(3));
						staff.setPhoneNumber(resultSet.getString(4));
						staff.setEmail(resultSet.getString(5));
						staff.setAuthority(resultSet.getInt(6));
						staff.setPassword(resultSet.getString(7));
						staff.setPhoto(resultSet.getString(8));
						
						cr.setStaff(staff);
						
						if(jCheckBox.isSelected()){
							//将用户名密码写入文件
							String path = 
							DirectoryMaker.makeDir(cr.getStaff().getStaffID()).getAbsolutePath();
							try {
								FileWriter fileWriter = new FileWriter(new File(path+ File.separator + "a.mms"),false);
								fileWriter.write(cr.getStaff().getPassword());
								fileWriter.flush();
								fileWriter.close();
								cr.setPath(path);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else{
							try{
								String path = 
								DirectoryMaker.makeDir(cr.getStaff().getStaffID()).getAbsolutePath();
								FileWriter fileWriter = new FileWriter(new File(path + File.separator + "a.mms"),false);
								fileWriter.write("");
								fileWriter.flush();
								fileWriter.close();
								cr.setPath(path);
							}catch(IOException e){
								e.printStackTrace();
							}
						}
						dispose();
						mf = new MainFrame(cr);
						mf.launch();
					}
					
				} catch (SQLException e2) {
				}
				finally {
					try {
						if(resultSet != null) resultSet.close();
						if(sql != null) sql.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else if(arg0.getSource()==(jButton_register)){
				Register register = new Register(cr);
				dispose();
				register.launch();
			}else{
				
			}
		}
		
	}
	//数据库连接
	private Properties props; 
    private InputStream in;
    private BufferedReader bf; //解决中文乱码问题
	private String url;  
	private String name;
	private String user;
	private String password;
	
	private Connection connection = null;
	
	private void buildConnection(){
		try {
			Class.forName(name);//指定连接类型 
			connection = DriverManager.getConnection(url, user, password);//获取连接 
			cr = new ConnectionRegistrar();
			cr.setConnection(connection);//wangzhen
		} catch (Exception e) {	
			JOptionPane.showMessageDialog(null, "数据库连接异常！", "失败", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
	
	public Login(){
		props = new Properties(); 
    	in = props.getClass().getResourceAsStream("/config/MMSconfig");
    	bf = new BufferedReader(new InputStreamReader(in)); //解决中文乱码问题
    	try {
    		props.load(bf);
    	} catch (IOException e) {
    	  e.printStackTrace();
    	}
    	
    	url = props.getProperty("MySQLhost");  
    	name = "com.mysql.jdbc.Driver";
    	user = props.getProperty("MySQLadminuser");
    	password = props.getProperty("MySQLadminpsw");
    	
		setTitle("登录");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(false);
		
		//内容容器面板
		jPanel_content = new JPanel();
		setContentPane(jPanel_content);
		jPanel_content.setLayout(new BoxLayout(jPanel_content, BoxLayout.PAGE_AXIS));
		
		//员工号
		JPanel jPanel_username = new JPanel();
		jPanel_content.add(jPanel_username);
		JLabel jLabel_username = new JLabel();
		jLabel_username.setText("职员ID：");
		jLabel_username.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_username.add(jLabel_username);
		jTextField_username = new JTextField(10);
		jTextField_username.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_username.setColumns(10);
		Document doc = jTextField_username.getDocument();
		doc.addDocumentListener(new DocumentListener() {
			//密码自动填入
			String userHomePath = System.getProperty("user.home") + File.separator + 
					"Documents" + File.separator +
					"MMSDoc";
			File file=new File(userHomePath);
			String pwd;
			private void function(){
				if(!file.exists() && !file .isDirectory()) {
		        	file.mkdirs();
		        }
		        File []files = file.listFiles();
		        ArrayList<String> filesName = new ArrayList<String>();
		        for(int i = 0; i < files.length; i++){
		        	if(files[i].isDirectory()){
		        		filesName.add(files[i].getName());
		        	}
		        }
		        String []fileName = new String[filesName.size()];
		        filesName.toArray(fileName);
		        String inputUsername = jTextField_username.getText();
		        for(int i = 0; i < fileName.length; ++i){
		        	if(fileName[i].equals(inputUsername)){
		        		try {
		        			File f = new File(userHomePath + File.separator + inputUsername + File.separator +"a.mms");
			        		if(!f.exists()){
			        			return;
			        		}
		        			FileInputStream file = new FileInputStream(f);
			        		InputStreamReader reader = new InputStreamReader(file);
			        		@SuppressWarnings("resource")
							BufferedReader bufferedReader = new BufferedReader(reader);
			        		String password=null;
							while((pwd = bufferedReader.readLine()) != null){
								password = pwd;
							}
							//
							jPasswordField_password.setText(password);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			        	
		        	}
		        }
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				function();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				function();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		jPanel_username.add(jTextField_username);
		
		//密码
		JPanel jPanel_password = new JPanel();
		jPanel_content.add(jPanel_password);
		JLabel jLabel_password = new JLabel("密   码：");
		jLabel_password.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_password.add(jLabel_password);
		jPasswordField_password = new JPasswordField(10);
		jPasswordField_password.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPasswordField_password.setColumns(10);
		jPanel_password.add(jPasswordField_password);
		
		//验证码
		JPanel jPanel_validate = new JPanel();
		jPanel_content.add(jPanel_validate);
		JLabel jLabel_validate = new JLabel("验证码：");
		jLabel_validate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_validate.add(jLabel_validate);
		jTextField_validate = new JTextField(10);
		jTextField_validate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_validate.setColumns(5);
		jPanel_validate.add(jTextField_validate);
		randomText = RandomStringUtils.randomAlphanumeric(4);//生成4位随机字符串
		ColorfulCAPTCHALabel colorfulCAPTCHALabel = new ColorfulCAPTCHALabel(randomText);
		jPanel_validate.add(colorfulCAPTCHALabel);
		
		//记住密码
		JPanel jPanel_remember = new JPanel();
		jPanel_content.add(jPanel_remember);
		jCheckBox = new JCheckBox("记住密码");
		jCheckBox.setSelected(true);
		jPanel_remember.add(jCheckBox);
		
		//按钮
		JPanel jPanel_button = new JPanel();
		jPanel_content.add(jPanel_button);
		jButton_login = new JButton("登录");
		jButton_login.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jButton_login.addActionListener(new LoginActionListener());
		jPanel_button.add(jButton_login);//添加监听器
		jButton_register = new JButton("注册");
		jButton_register.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jButton_register.addActionListener(new LoginActionListener());
		jPanel_button.add(jButton_register);//添加监听器
		buildConnection();//建立数据库连接
		
		setSize(400,250);//窗口大小
		setResizable(false);//禁止最大化
		setLocation(SwingUtil.centreContainer(getSize()));// 让窗体居中显示
	}
	public void launch() {
		this.setVisible(true);
	}
}


class SwingUtil {
/*
* 根据容器的大小，计算居中显示时左上角坐标
*
* @return 容器左上角坐标
*/
public static Point centreContainer(Dimension size) {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获得屏幕大小
		int x = (screenSize.width - size.width) / 2;// 计算左上角的x坐标
		int y = (screenSize.height - size.height) / 2;// 计算左上角的y坐标
		return new Point(x, y);// 返回左上角坐标
	}


}
