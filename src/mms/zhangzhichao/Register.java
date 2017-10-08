package mms.zhangzhichao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.RandomStringUtils;

import mms.common.ConnectionRegistrar;
import mms.common.Staff;

public class Register extends JFrame{
	
	private ConnectionRegistrar cr;
	private Login lg;
	/*********************/
	//ftp服务器信息
	private String ftpHost = "115.159.143.187";  //Host
	private int ftpPort = 22;  				  //Port
	private String ftpUsername = "mmsdocuser";  		  //UserName
	private String ftpPassword = "mmsdocuser";    //Password
	/***********************/
	
	private JTextField jTextField_staffID,
				jTextField_name, 
				jTextField_phoneNumber, 
				jTextField_department, 
				jTextField_email,
				jTextField_validate,
				jTextField_avatar;
	private JPasswordField jPasswordField_password, 
					jPasswordField_confirmPassword;
	private JButton jButton_register, 
			jButton_reset,
			jButton_select;
	private JPanel jPanel_content;
	private String randomText;
	private JFileChooser jFileChooser;
	private JLabel jLabel_avatarImage;
	
	private static final long serialVersionUID = -4655235896173916415L;
	
	//数据库连接
	static final String url = "jdbc:mysql://127.0.0.1/meeting_management_system?characterEncoding=utf-8";  
	static final String name = "com.mysql.jdbc.Driver";
	static final String user = "root";
	static final String password = "";
	
//	private void buildConnection(){
//		try {
//				Class.forName(name);//指定连接类型 
//				cr = new ConnectionRegistrar();
//				cr.setConnection(DriverManager.getConnection(url, user, password));//获取连接 
//				
//				//cr.setConnetcion(connection);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	}
	
	public Register(ConnectionRegistrar cr) {
		this.cr = cr;
		createPanel();
	}
	public Register(){
		//buildConnection();
		createPanel();
	}
	//创建布局
	private void createPanel() {
		setTitle("注册");
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		validate();
		setVisible(false);
		//内容容器面板
		jPanel_content = new JPanel();
		setContentPane(jPanel_content);
		jPanel_content.setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanel_content.setBackground(Color.WHITE);
		
		
		//标题
		JPanel jPanel_title = new JPanel();
		jPanel_title.setBackground(Color.white);
		//jPanel_title.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));//添加边框
		jPanel_title.setSize(500, 30);
		jPanel_content.add(jPanel_title, BorderLayout.CENTER);
		JLabel jLabel_title = new JLabel("                     注册                         ");
		jLabel_title.setFont(new Font("微软雅黑", Font.BOLD, 30));
		jPanel_title.add(jLabel_title);
		
		//副标题
		JPanel jPanel_title2 = new JPanel();
		jPanel_title2.setBackground(Color.white);
		jPanel_title2.setSize(500, 30);
		jPanel_content.add(jPanel_title2, BorderLayout.CENTER);
		JLabel jLabel_title2 = new JLabel("                                            (带*号项为必填)");
		jLabel_title2.setFont(new Font("微软雅黑", Font.BOLD, 13));
		jLabel_title2.setForeground(Color.red);
		jPanel_title2.add(jLabel_title2);
		
		
		//员工号
		JPanel jPanel_staffID = new JPanel();
		jPanel_content.add(jPanel_staffID);
		jPanel_staffID.setBackground(Color.WHITE);
		JLabel jLabel_staffID = new JLabel("    *职员ID：");
		jLabel_staffID.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_staffID.add(jLabel_staffID);
		jTextField_staffID = new JTextField();
		jTextField_staffID.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_staffID.setColumns(15);
		jPanel_staffID.add(jTextField_staffID);
		JLabel jLabel_staffIDNote = new JLabel("(请输入正确的员工号)");
		jLabel_staffIDNote.setBackground(Color.WHITE);
		jLabel_staffIDNote.setVisible(false);
		jPanel_staffID.add(jLabel_staffIDNote);
		jPanel_staffID.add(jLabel_staffIDNote);
		//姓名
		JPanel jPanel_name = new JPanel();
		jPanel_content.add(jPanel_name);
		jPanel_name.setBackground(Color.WHITE);
		JLabel jLabel_name = new JLabel("        *姓名：");
		jLabel_name.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_name.add(jLabel_name);
		jTextField_name = new JTextField();
		jTextField_name.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_name.setColumns(15);
		jPanel_name.add(jTextField_name);
		JLabel jLabel_nameNote = new JLabel("(请输入正确的姓名)");
		jLabel_nameNote.setVisible(false);
		jPanel_name.add(jLabel_nameNote);
		//密码
		JPanel jPanel_password = new JPanel();
		jPanel_content.add(jPanel_password);
		jPanel_password.setBackground(Color.WHITE);
		JLabel jLabel_password = new JLabel("        *密码：");
		jLabel_password.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_password.add(jLabel_password);
		jPasswordField_password = new JPasswordField();
		jPasswordField_password.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPasswordField_password.setColumns(15);
		jPanel_password.add(jPasswordField_password);
		JLabel jLabel_passwordNote = new JLabel("(请输入6-20位密码)");
		jPanel_password.add(jLabel_passwordNote);
		jPasswordField_password.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		//确认密码
		JPanel jPanel_confirmPassword = new JPanel();
		jPanel_content.add(jPanel_confirmPassword);
		jPanel_confirmPassword.setBackground(Color.white);
		JLabel jLabel_confirmPassword = new JLabel("*确认密码：");
		jLabel_confirmPassword.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_confirmPassword.add(jLabel_confirmPassword);
		jPasswordField_confirmPassword = new JPasswordField();
		jPasswordField_confirmPassword.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPasswordField_confirmPassword.setColumns(15);
		jPanel_confirmPassword.add(jPasswordField_confirmPassword);
		JLabel jLabel_confirmPasswordNote = new JLabel("两次密码输入不一致");
		//jLabel_confirmPasswordNote.setFont(new Font("黑体", Font.BOLD, size));
		jLabel_confirmPasswordNote.setForeground(Color.red);
		jLabel_confirmPasswordNote.setVisible(false);
		jLabel_confirmPasswordNote.setBackground(Color.WHITE);
		jPanel_confirmPassword.add(jLabel_confirmPasswordNote);
		jPasswordField_confirmPassword.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				char[] pw = jPasswordField_password.getPassword();
				String string_password = new String(pw);
				char[] icpw = jPasswordField_confirmPassword.getPassword();
				String string_confirmPassword = new String(icpw);
				if(string_confirmPassword.equals(string_password) == false){
					jLabel_confirmPasswordNote.setVisible(true);
					jLabel_confirmPasswordNote.setOpaque(true);
				}else{
					jLabel_confirmPasswordNote.setVisible(false);
					jLabel_confirmPasswordNote.setOpaque(false);
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
		});
		
		//部门
		JPanel jPanel_department = new JPanel();
		jPanel_department.setBackground(Color.white);
		jPanel_content.add(jPanel_department);
		JLabel jLabel_department = new JLabel("        *部门：");
		jLabel_department.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_department.add(jLabel_department);
		jTextField_department = new JTextField();
		jTextField_department.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_department.setColumns(15);
		jPanel_department.add(jTextField_department);
		//电话
		JPanel jPanel_phoneNumber = new JPanel();
		jPanel_phoneNumber.setBackground(Color.white);
		jPanel_content.add(jPanel_phoneNumber);
		JLabel jLabel_phoneNumber = new JLabel("        *电话：");
		jLabel_phoneNumber.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_phoneNumber.add(jLabel_phoneNumber);
		jTextField_phoneNumber = new JTextField();
		jTextField_phoneNumber.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_phoneNumber.setColumns(15);
		jTextField_phoneNumber.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() > '9' || e.getKeyChar() < '0'){//电话号文本框中如果输入非数字键则不显示
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		jPanel_phoneNumber.add(jTextField_phoneNumber);
		//邮箱
		JPanel jPanel_email = new JPanel();
		jPanel_email.setBackground(Color.white);
		jPanel_content.add(jPanel_email);
		JLabel jLabel_email = new JLabel("    *E-mail：");
		jLabel_email.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_email.add(jLabel_email);
		jTextField_email = new JTextField();
		jTextField_email.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_email.setColumns(15);
		jPanel_email.add(jTextField_email);
		//头像
		JPanel jPanel_avatar = new JPanel();
		//jPanel_avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));//添加边框
		jPanel_avatar.setBackground(Color.white);
		jPanel_content.add(jPanel_avatar);
		JLabel jLabel_avatar = new JLabel("         头像：");
		jLabel_avatar.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_avatar.add(jLabel_avatar);
		jLabel_avatarImage = new JLabel();
		jPanel_avatar.add(jLabel_avatarImage);
		jLabel_avatarImage.setVisible(true);
		jTextField_avatar = new JTextField();
		jTextField_avatar.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_avatar.setColumns(10);
		jPanel_avatar.add(jTextField_avatar);
		jButton_select = new JButton("浏览");
		jButton_select.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jButton_select.addActionListener(new RegisterActionListener());
		jPanel_avatar.add(jButton_select);
		//验证码
		JPanel jPanel_validate = new JPanel();
		jPanel_validate.setBackground(Color.white);
		jPanel_content.add(jPanel_validate);
		JLabel jLabel_validate = new JLabel("    *验证码：");
		jLabel_validate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_validate.add(jLabel_validate);
		jTextField_validate = new JTextField(10);
		jTextField_validate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jTextField_validate.setColumns(10);
		jTextField_validate.addKeyListener(new ValidateKeyListener());
		jPanel_validate.add(jTextField_validate);
		randomText = RandomStringUtils.randomAlphanumeric(4);//生成4位随机字符串
		ColorfulCAPTCHALabel colorfulCAPTCHALabel = new ColorfulCAPTCHALabel(randomText);
		colorfulCAPTCHALabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel_validate.add(colorfulCAPTCHALabel);
		//空白行
		JPanel jPanel_blankLine = new JPanel();
		jPanel_blankLine.setBackground(Color.white);
		jPanel_blankLine.add(new JLabel("                                          "));
		jPanel_content.add(jPanel_blankLine);
		//按钮
		JPanel jPanel_button = new JPanel();
		jPanel_button.setBackground(Color.white);
		jPanel_content.add(jPanel_button);
		jButton_register = new JButton("注册");
		jButton_register.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jButton_register.addActionListener(new RegisterActionListener());
		jPanel_button.add(jButton_register);
		jButton_register.setEnabled(false);
		jButton_reset = new JButton("重置");
		jButton_reset.addActionListener(new RegisterActionListener());
		jButton_reset.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		jPanel_button.add(jButton_reset);
		setSize(450, 570);
		setResizable(false);
		setLocation(SwingUtil.centreContainer(getSize()));// 让窗体居中显示
	}
	public void launch() {
		this.setVisible(true);
	}
	private class RegisterActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String inputStaffID = jTextField_staffID.getText();
			String inputStaffName = jTextField_name.getText();
			String inputDepartment = jTextField_department.getText();
			String inputPhoneNumber = jTextField_phoneNumber.getText();
			String inputEmail = jTextField_email.getText();
			String inputPhoto = jTextField_avatar.getText();
			char[] pw = jPasswordField_password.getPassword();
			String inputPassword = new String(pw);//本地路径
			String remotePath = "";//远程路径
			Staff staff = new Staff();
			
			if(inputPhoto.length() != 0){
				FtpByApache ftpByApache = null;
				try {
					ftpByApache = new FtpByApache();
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
				}//建立ftp连接
				remotePath = "./" + inputStaffID + "/avatar" + System.currentTimeMillis()
							+ inputPhoto.substring(inputPhoto.lastIndexOf("."));
				try {
					ftpByApache.upload(inputPhoto, remotePath);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
				} finally {
					if(ftpByApache != null) ftpByApache.close_connection();
				}
			}else{
				inputPhoto = "";
				remotePath = "";
			}
			staff.setAuthority(2);
			staff.setDepartment(inputDepartment);
			staff.setEmail(inputEmail);
			staff.setPassword(inputPassword);
			staff.setPhoneNumber(inputPhoneNumber);
			staff.setStaffID(inputStaffID);
			staff.setStaffName(inputStaffName);
			staff.setPhoto(remotePath);
			
			if(e.getSource().equals(jButton_register)){
				PreparedStatement sql = null;
				try {
					sql = cr.getConnection().prepareStatement("INSERT INTO staff "
															+ "(staffID,staffName,department,"
															+ "phoneNumber,email,"
															+ "authority,password, photo) "
															+ "values(?, ?, ?, ?, ?, ?, ?, ?)");
					//jTextField_validate的监视器已经确保此按钮监视器中的数据都符合要求
					sql.setString(1, inputStaffID);
					sql.setString(2, inputStaffName);
					sql.setString(3, inputDepartment);
					sql.setString(4, inputPhoneNumber);
					sql.setString(5, inputEmail);
					sql.setInt(6, 2);//数字2表示最低权限
					sql.setString(7, inputPassword);
					sql.setString(8, remotePath);
					sql.executeUpdate();
					/******/
					
					JOptionPane.showInternalMessageDialog(getContentPane(), 
							"注册成功","", JOptionPane.INFORMATION_MESSAGE); 					
					//cr.setStaff(staff);
					dispose();
					lg = new Login();
					lg.launch();
				} catch (SQLException e1) {
					String sub = "PRIMARY";
					if(e1.getMessage().indexOf(sub) != -1){//如果是职员ID已存在则提示
						JOptionPane.showInternalMessageDialog(getContentPane(), 
								"员工ID已存在","警告", JOptionPane.INFORMATION_MESSAGE); 
						dispose();
						Register register = new Register(cr);
						register.launch();
						e1.printStackTrace();
						return;
					}
					JOptionPane.showInternalMessageDialog(getContentPane(), 
							"数据插入失败","警告", JOptionPane.INFORMATION_MESSAGE); 
					dispose();
					Register register = new Register(cr);
					register.launch();
					e1.printStackTrace();
					return;
				}
			} else if(e.getSource().equals(jButton_reset)){
				dispose();
				Register register = new Register(cr);
				register.launch();
			} else if(e.getSource().equals(jButton_select)){
				jFileChooser = new JFileChooser();
				FileSystemView fileSystemView = FileSystemView.getFileSystemView();
				jFileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
				int result = jFileChooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					String name = jFileChooser.getSelectedFile().getAbsolutePath();
					jTextField_avatar.setText(name);//选择好文件后将文件绝对路径显示在文本框中
					ImageIcon imageIcon2 = new ImageIcon(name);
					imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(60, 70, Image.SCALE_DEFAULT));
					jLabel_avatarImage.setIcon(imageIcon2);//选择好文件后将文件显示在jLabel中
					jLabel_avatarImage.setVisible(true);
					jTextField_avatar.setColumns(5);
				}
			} else {
				
			}
		}
	}
	private class ValidateKeyListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
		@Override
		public void keyTyped(KeyEvent e) {
			String inputStaffID = jTextField_staffID.getText();
			String inputStaffName = jTextField_name.getText();
			String inputDepartment = jTextField_department.getText();
			String inputPhoneNumber = jTextField_phoneNumber.getText();
			String inputEmail = jTextField_email.getText();
			char[] pw = jPasswordField_password.getPassword();
			String inputPassword = new String(pw);
			char[] icpw = jPasswordField_confirmPassword.getPassword();
			String inputConfirmPassword = new String(icpw);
			if(inputStaffID.length() == 0 || inputStaffName.length() ==0 || inputDepartment.length() == 0 || 
					inputEmail.length() ==0 || inputPhoneNumber.length() == 0 || 
					inputPassword.length() == 0 || inputConfirmPassword.length() ==0){
				jButton_register.setEnabled(false);
				return;
			}
			if(inputConfirmPassword.length() < 6 || inputPassword.length() < 6 ||
					inputConfirmPassword.length() > 20 || inputPassword.length() > 20){
				jButton_register.setEnabled(false);
				return;
			}
			if(inputConfirmPassword.equals(inputPassword) == false){
				jButton_register.setEnabled(false);
				return;
			}
			jButton_register.setEnabled(true);
		}
	}
}
