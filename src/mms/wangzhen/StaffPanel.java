package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mms.common.ConnectionRegistrar;
import mms.common.DirectoryMaker;
import mms.lbb.DeviceManagementFrame;
import mms.lbb.RoomManagementFrame;
import mms.xuanruiwei.PersonalInfoFrame;
import mms.zhangzhichao.FtpByApache;

public class StaffPanel extends JPanel {
	private static final long serialVersionUID = -5495958309657691268L;
	//连结数据库的对象
	private ConnectionRegistrar cr;
	private MeetingManagementFrame mmf;
	private DeviceManagementFrame dmf;
	private RoomManagementFrame rmf;
	private RecentMeetingPanel rmp;
	private PreorderMeetingPanel pmp;
	private SuperAdminFrame saf;
	private PersonalInfoFrame pif;
	//上一个调用页面的对象
	//无...
	
	//可能会调用的下一个页面的对象的引用
	//个人详细资料界面
	
	/*************组件***************/
	private JLabel photoContent,
					idTitle, //ID标题
					nameTitle, //名字标题
					dptTitle, //部门标题
					phoneTitle, //电话号码标题
					emailTitle, //邮箱标题
					idContent,//ID显示
					nameContent, //名字显示
					dptContent, //部门显示
					phoneContent, //电话号码显示
					emailContent; //邮箱显示
	private JButton roomInfoButton, //会议室信息维护按钮
					meetingManagementButton,//会议管理按钮 
					meetingResourceButton, //会议资源管理按钮
					personalInfoButton,//个信息按钮
					staffManagementButton;//人员管理
	private JPanel[] jps;
	private ImageIcon mImageIcon; 
	
	private StaffPanel sp = this;
	/****************************/
	
	private class MyButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == meetingManagementButton) {
				mmf = new MeetingManagementFrame(cr, rmp, pmp);
				mmf.launch();
			} else if(e.getSource() == meetingResourceButton) {
				dmf = new DeviceManagementFrame(cr);
				dmf.launch();
			} else if(e.getSource() == personalInfoButton) {
				pif = new PersonalInfoFrame(cr, sp);
				pif.launch();
			} else if(e.getSource() == roomInfoButton) {
				rmf = new RoomManagementFrame(cr);
				rmf.launch();
			} else if(e.getSource() == staffManagementButton) {
				saf = new SuperAdminFrame(cr);
				saf.launch();
			}
		}
	}
	
	public StaffPanel(ConnectionRegistrar cr, RecentMeetingPanel rmp, PreorderMeetingPanel pmp) {
		
		this.cr = cr;
		this.rmp = rmp;
		this.pmp = pmp;
		this.setPreferredSize(new Dimension(200, 600));
		//setLayout(new GridLayout(10, 1));
		this.setLayout(new FlowLayout());
/*****************************组件组合区**********************************/
		jps = new JPanel[11];
		//0-4：放个人信息
		//5-8：放按钮
		//9：空白
		//10：把0-9放进去
		for(int i = 0; i < jps.length ; i++) {
			jps[i] = new JPanel();
jps[i].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		}
		
/***************************头像区**************************************/		
		photoContent = new JLabel();
		photoContent.setPreferredSize(new Dimension(100, 100));
photoContent.setBorder(BorderFactory.createLineBorder(Color.BLUE));
/*****************************个人信息区************************************/		
		
		idTitle = new JLabel("职员ID");
		idContent =new JLabel();
		nameTitle = new JLabel("姓名");
		nameContent = new JLabel();
		dptTitle = new JLabel("部门");
		dptContent = new JLabel();
		phoneTitle = new JLabel("电话");
		phoneContent = new JLabel();
		emailTitle = new JLabel("邮箱");
		emailContent = new JLabel();
		
		setDefaultIcon();
		Thread t = new Thread(new IconSetor());
		t.start();
		setLabelInfo();
		
		int height = 48;
		int widthTitle = 40;
		int widthContent = 140;
		idTitle.setPreferredSize(new Dimension(widthTitle, height));
		idContent.setPreferredSize(new Dimension(widthContent, height));
		jps[0].add(idTitle, BorderLayout.WEST);
		jps[0].add(idContent, BorderLayout.CENTER);
		
		
		nameTitle.setPreferredSize(new Dimension(widthTitle, height));
		nameContent.setPreferredSize(new Dimension(widthContent, height));
		jps[1].add(nameTitle, BorderLayout.WEST);
		jps[1].add(nameContent, BorderLayout.CENTER);
		
		
		dptTitle.setPreferredSize(new Dimension(widthTitle, height));
		dptContent.setPreferredSize(new Dimension(widthContent, height));
		jps[2].add(dptTitle, BorderLayout.WEST);
		jps[2].add(dptContent, BorderLayout.CENTER);
		
		
		phoneTitle.setPreferredSize(new Dimension(widthTitle, height));
		phoneContent.setPreferredSize(new Dimension(widthContent, height));
		jps[3].add(phoneTitle, BorderLayout.WEST);
		jps[3].add(phoneContent, BorderLayout.CENTER);
		
		
		emailTitle.setPreferredSize(new Dimension(widthTitle, height));
		emailContent.setPreferredSize(new Dimension(widthContent, height));
		jps[4].add(emailTitle, BorderLayout.WEST);
		jps[4].add(emailContent, BorderLayout.CENTER);
/*******************************按钮区**********************************/
		MyButtonActionListener mbal = new MyButtonActionListener();
		meetingManagementButton = new JButton("会议管理");
		meetingManagementButton.addActionListener(mbal);
		jps[5].add(meetingManagementButton);
		
		personalInfoButton = new JButton("个人中心");
		personalInfoButton.addActionListener(mbal);
		jps[6].add(personalInfoButton);
		
		meetingResourceButton = new JButton("设备管理");
		meetingResourceButton.addActionListener(mbal);
		jps[7].add(meetingResourceButton);
		
		roomInfoButton = new JButton("会议室信息管理");
		roomInfoButton.addActionListener(mbal);
		jps[8].add(roomInfoButton);
		
		staffManagementButton = new JButton("人员管理");
		staffManagementButton.addActionListener(mbal);
		jps[9].add(staffManagementButton);
		if(cr.getStaff().getAuthority() < 1) {
			meetingManagementButton.setEnabled(false);
			personalInfoButton.setEnabled(false);
			meetingResourceButton.setEnabled(true);
			roomInfoButton.setEnabled(true);
			staffManagementButton.setEnabled(true);
		} else if(cr.getStaff().getAuthority() > 1) {
			meetingManagementButton.setEnabled(true);
			personalInfoButton.setEnabled(true);
			meetingResourceButton.setEnabled(false);
			roomInfoButton.setEnabled(false);
			staffManagementButton.setEnabled(false);
		}
		
/****************************布局叠加*************************************/			
		jps[10].setPreferredSize(new Dimension(200, 480));
		jps[10].setLayout(new GridLayout(10, 1));
jps[10].setBorder(BorderFactory.createLineBorder(Color.blue));

		for(int i = 0; i < 10; i++) {
			jps[10].add(jps[i]);
		}
		this.add(photoContent);
		this.add(jps[10]);
	}
	
	public void setLabelInfo() {	
	    mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95,95, Image.SCALE_DEFAULT));
		idContent.setText(cr.getStaff().getStaffID());
		nameContent.setText(cr.getStaff().getStaffName()); //名字显示
		dptContent.setText(cr.getStaff().getDepartment());//部门显示
		phoneContent.setText(cr.getStaff().getPhoneNumber()); //电话号码显示
		emailContent.setText(cr.getStaff().getEmail()); //邮箱显示
	}
	
	public void setDefaultIcon() {
		String defaultIconURL = System.getProperty("user.home") + File.separator +
				"Documents" + File.separator + 
				"MMSDoc" + File.separator +
				"Default" + File.separator + 
				"Default.jpg";
		mImageIcon = new ImageIcon(defaultIconURL);
		mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
		photoContent.setIcon(mImageIcon);
		photoContent.setHorizontalAlignment(SwingConstants.CENTER);
		photoContent.setVerticalAlignment(SwingConstants.CENTER);
	}
	
	public void setUserIconFromLocal(String userURL, String iconName) {
		String userIconURL = userURL + File.separator + iconName;
		mImageIcon = new ImageIcon(userIconURL);
		mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
		photoContent.setIcon(mImageIcon);
		photoContent.setHorizontalAlignment(SwingConstants.CENTER);
		photoContent.setVerticalAlignment(SwingConstants.CENTER);
	}
	
	public void updatePhoto(ImageIcon imageIcon) {
		mImageIcon =  imageIcon;
		photoContent.setIcon(mImageIcon); 
	    mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95,95, Image.SCALE_DEFAULT));
	    mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
		photoContent.setIcon(mImageIcon);
		photoContent.setHorizontalAlignment(SwingConstants.CENTER);
		photoContent.setVerticalAlignment(SwingConstants.CENTER);
	    photoContent.updateUI();
	}
	
	private class IconSetor implements Runnable {
		String userURL = System.getProperty("user.home") + File.separator +
				"Documents" + File.separator + 
				"MMSDoc" + File.separator +
				cr.getStaff().getStaffID();
		String localAvatarName = DirectoryMaker.getLocalAvatarName(userURL);
		String remoteAvatarName = "";
		String remotePhotoPath = "" + cr.getStaff().getPhoto();
		
		public IconSetor() {
			String temp = remotePhotoPath;
			if(!"null".equals(temp)||!"".equals(temp)||!"NULL".equals(temp)) {
				remoteAvatarName = temp.substring(temp.lastIndexOf("/") + 1);
			}
		}
		@Override
		public void run() {
			if("".equals(remoteAvatarName)) {
				DirectoryMaker.deleteFile(userURL + File.separator + localAvatarName);
			} else {
				if(localAvatarName.equals(remoteAvatarName)) {
					mImageIcon = new ImageIcon(userURL + File.separator + localAvatarName);
					mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
					photoContent.setIcon(mImageIcon);
					photoContent.setHorizontalAlignment(SwingConstants.CENTER);
					photoContent.setVerticalAlignment(SwingConstants.CENTER);
				
				} else {
					DirectoryMaker.deleteFile(userURL + File.separator + localAvatarName);
					if(!"null".equals(remoteAvatarName) && !"".equals(remoteAvatarName)) {
						FtpByApache fba = null;
						try {
							fba = new FtpByApache();
							fba.newDownload(remotePhotoPath, userURL + File.separator);
							mImageIcon = new ImageIcon(userURL + File.separator + DirectoryMaker.getLocalAvatarName(userURL));
							mImageIcon.setImage(mImageIcon.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT));
							photoContent.setIcon(mImageIcon);
							photoContent.setHorizontalAlignment(SwingConstants.CENTER);
							photoContent.setVerticalAlignment(SwingConstants.CENTER);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
						} finally {
							if(fba != null) fba.close_connection();
						}
					}
				}
			}
		}
	}
	
	public ImageIcon getmImageIcon() {
		return mImageIcon;
	}

	public void setmImageIcon(ImageIcon mImageIcon) {
		this.mImageIcon = mImageIcon;
	}
	
	public void lauch() {
		this.setVisible(true);
	}
}

