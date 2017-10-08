package mms.xuanruiwei;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import mms.common.ConnectionRegistrar;
import mms.common.DirectoryMaker;
import mms.common.Staff;
import mms.wangzhen.StaffPanel;
import mms.zhangzhichao.FtpByApache;

public class PersonalInfoFrame extends JFrame {

	private JPanel personalInfoPanel, photoInfoPanel, mJPanel;
	private JPanel[] mRowsPanel;

	private JLabel photoView;
	private JButton mJButtonSave, mJButtonCancel, mJButtonPassword, mJButtonSelPhoto;
	private ImageIcon mImageIcon;
	private JFileChooser mJFileChooser;
	private File mFile;

	private JTextField mJTextFieldStaID, mJTextFieldStaName, mJTextFieldPhoNumber, mJTextFieldEmail,
			mJTextFieldAuthority;
	private JTextArea mJTextAreaDepartment;
	private JPasswordField mJPasswordFiePassword;
	private JLabel mJLabelStaID, mJLabelStaName, mJLabelPhoNumber, mJLabelEmail, mJLabelAuthority, mJLabelDepartment,
			mJLabelPassword;

	private ConnectionRegistrar mConnectionRegistrar;
	private Staff staff;
	private StaffPanel sp;

	private java.sql.PreparedStatement mPreparedStatement;
	private String sql;

	private FtpByApache ftpByApache;

	private String filePath;
	private String fileName;

	private String remotePath;

	private Dimension Size;
	private FlowLayout mFlowLayout;

	private static Font font;
	private static Color color;

	public PersonalInfoFrame(ConnectionRegistrar connectionRegistrar, StaffPanel sp) {

		this.mConnectionRegistrar = connectionRegistrar;
		this.sp = sp;
		this.setTitle("个人中心");
		personalInfoPanel = new JPanel();
		photoInfoPanel = new JPanel();
		photoView = new JLabel();
		mJPanel = new JPanel();
		mRowsPanel = new JPanel[8];

		color = Color.WHITE;
		font = new Font("宋体", Font.BOLD, 15);

		mFlowLayout = new FlowLayout(FlowLayout.LEFT);

		mJButtonSave = new JButton("保存");
		mJButtonCancel = new JButton("取消");
		mJButtonPassword = new JButton("修改密码");
		mJButtonSelPhoto = new JButton("更换头像");

		mJFileChooser = new JFileChooser();
		Size = new Dimension(160, 30);

		staff = mConnectionRegistrar.getStaff();

		filePath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "MMSDoc"
				+ File.separator + staff.getStaffID() + File.separator;

		remotePath = "./" + staff.getStaffID() + "/";

		this.init();
		
		this.fill();
	}

	public void init() {

		this.setSize(new Dimension(400, 550)); // 设置窗口大小
		this.setResizable(false); // 设置不可更改窗口大小
		this.setLocation(new Point(500, 100)); // 设置窗口位置
		// this.setUndecorated(true);

		mJFileChooser.setFileFilter(new ImageFilter());

		mJButtonSave.setPreferredSize(Size);
		mJButtonCancel.setPreferredSize(Size);
		mJButtonSelPhoto.setPreferredSize(Size);

		personalInfoPanel.setPreferredSize(new Dimension(350, 350)); // 设置容器大小
		personalInfoPanel.setLayout(new FlowLayout()); // 设置布局格式
		personalInfoPanel.setBackground(Color.WHITE);

		photoInfoPanel.setPreferredSize(new Dimension(350, 150)); // 设置容器大小
		photoInfoPanel.setLayout(new BorderLayout()); // 设置布局格式
		photoInfoPanel.setBackground(new Color(0, 229, 238));

		mJPanel.setPreferredSize(new Dimension(350, 550)); // 设置容器大小
		mJPanel.setLayout(new BorderLayout()); // 设置布局格式
		mJPanel.setBackground(Color.WHITE);

		photoInfoPanel.add(photoView, BorderLayout.CENTER);
		photoInfoPanel.add(mJButtonSelPhoto, BorderLayout.SOUTH);

		mJPanel.add(photoInfoPanel, BorderLayout.NORTH);
		mJPanel.add(personalInfoPanel, BorderLayout.SOUTH);

		this.setContentPane(mJPanel); // 将容器添加到窗口中间，位置居中
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					dispose();
				} catch (Exception exception) {
					
				}
			}
		});
		mJButtonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String updateInfoSql = "UPDATE staff SET"
										+ " staffName = ?,"
										+ " department = ?,"
										+ " phoneNumber = ?,"
										+ " email = ?,"
										+ " authority = ?" 
										+ " WHERE staffID = ?";
				try {
					mPreparedStatement = mConnectionRegistrar.getConnection().prepareStatement(updateInfoSql);
					mPreparedStatement.setString(1, mJTextFieldStaName.getText());
					mPreparedStatement.setString(2, mJTextAreaDepartment.getText());
					mPreparedStatement.setString(3, mJTextFieldPhoNumber.getText());
					mPreparedStatement.setString(4, mJTextFieldEmail.getText());
					mPreparedStatement.setString(5, mJTextFieldAuthority.getText());
					mPreparedStatement.setString(6, staff.getStaffID());
					mPreparedStatement.execute();
					JOptionPane.showMessageDialog(null, "保存成功！", "成功", JOptionPane.WARNING_MESSAGE);
					mConnectionRegistrar.getStaff().setStaffName(mJTextFieldStaName.getText());
					mConnectionRegistrar.getStaff().setDepartment(mJTextAreaDepartment.getText());
					mConnectionRegistrar.getStaff().setPhoneNumber(mJTextFieldPhoNumber.getText());
					mConnectionRegistrar.getStaff().setEmail(mJLabelEmail.getText());
					//sp.setLabelInfo();
					dispose();
				} catch (SQLException exception) {
					
				} finally {
					try {
						if(mPreparedStatement != null) mPreparedStatement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		mJButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mConnectionRegistrar.getConnection().close();
				} catch (Exception exception) {
					
				}
			}
		});
		mJButtonPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new PasswordFrame(mConnectionRegistrar);
			}
		});
		mJButtonSelPhoto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mJFileChooser.showDialog(new JLabel(), "选择图片");
				mFile = mJFileChooser.getSelectedFile();
				if (mFile != null ) {
					String localPath = System.getProperty("user.home") + File.separator +
							"Documents" + File.separator + 
							"MMSDoc" + File.separator +
							mConnectionRegistrar.getStaff().getStaffID();
					String oldLocalCompletePath = localPath 
													+ File.separator 
													+ DirectoryMaker.getLocalAvatarName(localPath);
					DirectoryMaker.deleteFile(oldLocalCompletePath);
					
					fileName = "avatar" + Calendar.getInstance().getTimeInMillis() + "."
							+ mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
					DirectoryMaker.copyFile(mFile.getAbsolutePath(), filePath + fileName);	
					try {
						ftpByApache = new FtpByApache();

						ftpByApache.deleteRemoteFile(staff.getPhoto());

						String newLocalCompletePath = localPath 
													+ File.separator 
													+ DirectoryMaker.getLocalAvatarName(localPath);
						ftpByApache.upload(newLocalCompletePath, remotePath + fileName);
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
					} finally {
						if(ftpByApache != null) ftpByApache.close_connection();
					}
					mImageIcon = new ImageIcon(filePath + fileName);
					photoView.setIcon(mImageIcon);
					mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
					photoInfoPanel.repaint();
					String updatePhotoSql = "UPDATE staff SET photo = ? WHERE staffID = ?";
					try {
						mPreparedStatement = mConnectionRegistrar.getConnection().prepareStatement(updatePhotoSql);
						mPreparedStatement.setString(1, remotePath + fileName);
						mPreparedStatement.setString(2, staff.getStaffID());
						staff.setPhoto(remotePath + fileName);
						mPreparedStatement.execute();
					} catch (Exception exception) {
						 exception.printStackTrace();
					} finally {
						try {
							if(mPreparedStatement != null) mPreparedStatement.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null, "图像上传成功！", "成功", JOptionPane.WARNING_MESSAGE);
					sp.updatePhoto(mImageIcon);
				} else {
					JOptionPane.showMessageDialog(null, "未选择图片！", "注意", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
	
	public void fill() {
		mImageIcon = sp.getmImageIcon();
		photoView.setIcon(mImageIcon);
		mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
		photoView.setHorizontalAlignment(SwingConstants.CENTER);
		photoView.setVerticalAlignment(SwingConstants.CENTER);

		mRowsPanel[0] = new JPanel();
		mRowsPanel[0].setLayout(mFlowLayout);
		mRowsPanel[0].setBackground(Color.WHITE);
		mJLabelStaID = new JLabel(" 职员 ID：");
		mJLabelStaID.setFont(font);
		mRowsPanel[0].add(mJLabelStaID);
		mJTextFieldStaID = new JTextField();
		mJTextFieldStaID.setEditable(false);
		mJTextFieldStaID.setBackground(color);
		mJTextFieldStaID.setFont(font);
		mJTextFieldStaID.setColumns(25);
		mJTextFieldStaID.setText(staff.getStaffID());
		mJTextFieldStaID.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[0].add(mJTextFieldStaID);
		personalInfoPanel.add(mRowsPanel[0]);

		mRowsPanel[1] = new JPanel();
		mRowsPanel[1].setLayout(mFlowLayout);
		mRowsPanel[1].setBackground(Color.WHITE);
		mJLabelStaName = new JLabel(" 职员名称：");
		mJLabelStaName.setFont(font);
		mRowsPanel[1].add(mJLabelStaName);
		mJTextFieldStaName = new JTextField();
		mJTextFieldStaName.setBackground(color);
		mJTextFieldStaName.setFont(font);
		mJTextFieldStaName.setColumns(25);
		mJTextFieldStaName.setText(staff.getStaffName());
		mJTextFieldStaName.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[1].add(mJTextFieldStaName);
		personalInfoPanel.add(mRowsPanel[1]);

		mRowsPanel[2] = new JPanel();
		mRowsPanel[2].setLayout(mFlowLayout);
		mRowsPanel[2].setBackground(Color.WHITE);
		mJLabelPhoNumber = new JLabel(" 电话号码：");
		mJLabelPhoNumber.setFont(font);
		mRowsPanel[2].add(mJLabelPhoNumber);
		mJTextFieldPhoNumber = new JTextField();
		mJTextFieldPhoNumber.setBackground(color);
		mJTextFieldPhoNumber.setFont(font);
		mJTextFieldPhoNumber.setColumns(25);
		mJTextFieldPhoNumber.setText(staff.getPhoneNumber());
		mJTextFieldPhoNumber.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[2].add(mJTextFieldPhoNumber);
		personalInfoPanel.add(mRowsPanel[2]);

		mRowsPanel[3] = new JPanel();
		mRowsPanel[3].setLayout(mFlowLayout);
		mRowsPanel[3].setBackground(Color.WHITE);
		mJLabelDepartment = new JLabel(" 职员部门：");
		mJLabelDepartment.setFont(font);
		mRowsPanel[3].add(mJLabelDepartment);
		mJTextAreaDepartment = new JTextArea();
		mJTextAreaDepartment.setBackground(color);
		mJTextAreaDepartment.setFont(font);
		mJTextAreaDepartment.setColumns(25);
		mJTextAreaDepartment.setLineWrap(true);
		mJTextAreaDepartment.setRows(3);
		mJTextAreaDepartment.setText(staff.getDepartment());
		mJTextAreaDepartment.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[3].add(mJTextAreaDepartment);
		personalInfoPanel.add(mRowsPanel[3]);

		mRowsPanel[4] = new JPanel();
		mRowsPanel[4].setLayout(mFlowLayout);
		mRowsPanel[4].setBackground(Color.WHITE);
		mJLabelEmail = new JLabel(" 电子邮件：");
		mJLabelEmail.setFont(font);
		mRowsPanel[4].add(mJLabelEmail);
		mJTextFieldEmail = new JTextField();
		mJTextFieldEmail.setBackground(color);
		mJTextFieldEmail.setFont(font);
		mJTextFieldEmail.setColumns(25);
		mJTextFieldEmail.setText(staff.getEmail());
		mJTextFieldEmail.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[4].add(mJTextFieldEmail);
		personalInfoPanel.add(mRowsPanel[4]);

		mRowsPanel[5] = new JPanel();
		mRowsPanel[5].setLayout(mFlowLayout);
		mRowsPanel[5].setBackground(Color.WHITE);
		mJLabelAuthority = new JLabel(" 职员权限：");
		mJLabelAuthority.setFont(font);
		mRowsPanel[5].add(mJLabelAuthority);
		mJTextFieldAuthority = new JTextField();
		mJTextFieldAuthority.setBackground(color);
		mJTextFieldAuthority.setFont(font);
		mJTextFieldAuthority.setColumns(25);
		mJTextFieldAuthority.setEditable(false);
		mJTextFieldAuthority.setText(staff.getAuthority() + "");
		mJTextFieldAuthority.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[5].add(mJTextFieldAuthority);
		personalInfoPanel.add(mRowsPanel[5]);

		mRowsPanel[6] = new JPanel();
		mRowsPanel[6].setLayout(mFlowLayout);
		mRowsPanel[6].setBackground(Color.WHITE);
		mJLabelPassword = new JLabel(" 用户密码：");
		mJLabelPassword.setFont(font);
		mRowsPanel[6].add(mJLabelPassword);
		mJPasswordFiePassword = new JPasswordField();
		mJPasswordFiePassword.setBackground(color);
		mJPasswordFiePassword.setFont(font);
		mJPasswordFiePassword.setColumns(15);
		mJPasswordFiePassword.setEditable(false);
		mJPasswordFiePassword.setText(staff.getPassword());
		mJPasswordFiePassword.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[6].add(mJPasswordFiePassword);
		mRowsPanel[6].add(mJButtonPassword);
		personalInfoPanel.add(mRowsPanel[6]);

		mRowsPanel[7] = new JPanel();
		mRowsPanel[7].setLayout(mFlowLayout);
		mRowsPanel[7].setBackground(Color.WHITE);
		mRowsPanel[7].add(mJButtonSave);
		mRowsPanel[7].add(mJButtonCancel);
		personalInfoPanel.add(mRowsPanel[7]);

		mJPanel.updateUI();

	}

	class ImageFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = f.getName().toLowerCase();
			if (extension != null) {
				if (extension.endsWith("jpg") || extension.endsWith("jpeg") || extension.endsWith("jpg")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
		@Override
		public String getDescription() {
			return "图片文件(*.jpg, *.jpeg, *.png)";
		}
	}

	public void launch() {
		this.setVisible(true);
	}
}
