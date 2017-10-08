package mms.xuanruiwei.confirm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import mms.common.ConnectionRegistrar;
import mms.zhangzhichao.FtpByApache;

public class PreorderDetail extends JFrame {
	private JScrollPane mJPanel;
	private JPanel preorderInfoPanel, mainJpanel, rowPanel;

	private JButton mJButtonFile;
	private ResultSet mResultSet;
	private java.sql.ResultSetMetaData metaData;

	private JTextField mJTFOrderID, mJTFRoomID, mJTFStartTime, mJTFEndTime;
	private JTextArea mJTADevice, mJTAParticipants;
	private JLabel mPreorderID, mJLabelRoomID, mJLabelStartTime, mJLabelEndTime, mJLabelDevice, mJLabelParticipants,
			mJLabelFile;

	private java.sql.PreparedStatement mPreparedStatement;
	private String sql;

	private FtpByApache ftpByApache;

	private String remotePath;
	private static	String localPath = System.getProperty("user.home") + File.separator +
			"Documents" + File.separator + 
			"MMSDoc" + File.separator;		
	
	private String deviceName;

	private String deviceList, participantList;

	private ConnectionRegistrar cr;

	private int preorderID;

	private Dimension Size, mJpanelSize, mJLabelSize;
	private FlowLayout mFlowLayout;
	private MeetingCompleteManager mcm;
	private String roomID, startTime, endTime;

	private static Font font;
	private static Color color;

	public PreorderDetail(MeetingCompleteManager mcm, ConnectionRegistrar cr, String startTime, String endTime,
			int preorderID, String roomID) {

		preorderInfoPanel = new JPanel();
		mainJpanel = new JPanel();
		mJPanel = new JScrollPane();

		this.cr = cr;
		this.mcm = mcm;
		this.preorderID = preorderID;
		this.roomID = roomID;
		this.startTime = startTime;
		this.endTime = endTime;

		color = Color.WHITE;
		font = new Font("宋体", Font.BOLD, 15);

		mFlowLayout = new FlowLayout(FlowLayout.LEFT);

		mJButtonFile = new JButton("下载文件");

		Size = new Dimension(160, 30);

		mJpanelSize = new Dimension(350, 50);
		mJLabelSize = new Dimension(100, 50);

		deviceList = "";
		participantList = "";

		this.init();

		this.fill();

	}

	public void init() {

		this.setSize(new Dimension(400, 550)); // 设置窗口大小
		this.setResizable(false); // 设置不可更改窗口大小
		this.setLocation(new Point(500, 100)); // 设置窗口位置
		// this.setUndecorated(true);

		mJButtonFile.setPreferredSize(Size);

		preorderInfoPanel.setPreferredSize(new Dimension(350, 630)); // 设置容器大小
		preorderInfoPanel.setLayout(new FlowLayout()); // 设置布局格式
		preorderInfoPanel.setBackground(Color.WHITE);

		mainJpanel.setPreferredSize(new Dimension(350, 550)); // 设置容器大小
		mainJpanel.setLayout(new BorderLayout()); // 设置布局格式
		mainJpanel.setBackground(Color.WHITE);

		mJPanel.setPreferredSize(new Dimension(350, 550)); // 设置容器大小
		mJPanel.setBackground(Color.WHITE);

		mJPanel.setViewportView(preorderInfoPanel);

		mainJpanel.add(mJPanel, BorderLayout.CENTER);

		this.setContentPane(mainJpanel); // 将容器添加到窗口中间，位置居中

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					// mConnectionRegistrar.getConnection().close();
				} catch (Exception exception) {
					
				}
				dispose();
			}
		});
		mJButtonFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
						
				try {
					ftpByApache = new FtpByApache();
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
				}
				try {
					sql = "SELECT filePath FROM preorderrecorder WHERE preorderID = ?";
					mPreparedStatement = cr.getConnection().prepareStatement(sql);
					mPreparedStatement.setInt(1, preorderID);
					mResultSet = mPreparedStatement.executeQuery();
					while (mResultSet.next()) {
						remotePath = mResultSet.getString("filePath");
					}
					ftpByApache.download(remotePath, localPath);;
				} catch (Exception exception) {
					exception.printStackTrace();
				} finally {
					try {
						if(ftpByApache != null) ftpByApache.close_connection();
						if(mPreparedStatement != null) mPreparedStatement.close();
						if(mResultSet != null) mResultSet.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "文件下载成功，路径为" + localPath, "成功", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	public void fill() {
		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mPreorderID = new JLabel(" 会议 ID: ");
		mPreorderID.setFont(font);
		mPreorderID.setPreferredSize(mJLabelSize);
		rowPanel.add(mPreorderID);
		mJTFOrderID = new JTextField();
		mJTFOrderID.setEditable(false);
		mJTFOrderID.setBackground(color);
		mJTFOrderID.setFont(font);
		mJTFOrderID.setColumns(25);
		mJTFOrderID.setText(preorderID + "");
		mJTFOrderID.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTFOrderID);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabelRoomID = new JLabel(" 会议室ID：");
		mJLabelRoomID.setFont(font);
		mJLabelRoomID.setPreferredSize(mJLabelSize);
		rowPanel.add(mJLabelRoomID);
		mJTFRoomID = new JTextField();
		mJTFRoomID.setEditable(false);
		mJTFRoomID.setBackground(color);
		mJTFRoomID.setFont(font);
		mJTFRoomID.setColumns(25);
		mJTFRoomID.setText(roomID);
		mJTFRoomID.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTFRoomID);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabelStartTime = new JLabel(" 开始时间：");
		mJLabelStartTime.setFont(font);
		mJLabelStartTime.setPreferredSize(mJLabelSize);
		rowPanel.add(mJLabelStartTime);
		mJTFStartTime = new JTextField();
		mJTFStartTime.setBackground(color);
		mJTFStartTime.setEditable(false);
		mJTFStartTime.setFont(font);
		mJTFStartTime.setColumns(25);
		mJTFStartTime.setText(startTime);
		mJTFStartTime.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTFStartTime);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabelEndTime = new JLabel(" 结束时间：");
		mJLabelEndTime.setFont(font);
		mJLabelEndTime.setPreferredSize(mJLabelSize);
		rowPanel.add(mJLabelEndTime);
		mJTFEndTime = new JTextField();
		mJTFEndTime.setEditable(false);
		mJTFEndTime.setBackground(color);
		mJTFEndTime.setFont(font);
		mJTFEndTime.setColumns(25);
		mJTFEndTime.setText(endTime);
		mJTFEndTime.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTFEndTime);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(new Dimension(350, 160));
		mJLabelDevice = new JLabel(" 使用设备：");
		mJLabelDevice.setFont(font);
		mJLabelDevice.setPreferredSize(mJLabelSize);
		rowPanel.add(mJLabelDevice);
		mJTADevice = new JTextArea();
		mJTADevice.setEditable(false);
		mJTADevice.setBackground(color);
		mJTADevice.setFont(font);
		mJTADevice.setPreferredSize(new Dimension(250, 100));
		mJTADevice.setLineWrap(true);
		mJTADevice.setText(getDevice());
		mJTADevice.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTADevice);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(new Dimension(350, 160));
		mJLabelParticipants = new JLabel(" 参与人员：");
		mJLabelParticipants.setFont(font);
		mJLabelParticipants.setPreferredSize(mJLabelSize);
		rowPanel.add(mJLabelParticipants);
		mJTAParticipants = new JTextArea();
		mJTAParticipants.setEditable(false);
		mJTAParticipants.setBackground(color);
		mJTAParticipants.setFont(font);
		mJTAParticipants.setPreferredSize(new Dimension(250, 100));
		mJTAParticipants.setLineWrap(true);
		mJTAParticipants.setText(getParticipants());
		mJTAParticipants.setBorder(BorderFactory.createLineBorder(Color.gray));
		rowPanel.add(mJTAParticipants);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabelFile = new JLabel(" 会议文件：");
		mJLabelFile.setFont(font);
		mJLabelFile.setPreferredSize(new Dimension(160, 50));
		rowPanel.add(mJLabelFile);
		rowPanel.add(mJButtonFile);
		preorderInfoPanel.add(rowPanel);

		mJPanel.updateUI();

	}

	private String getDevice() {

		try {
			sql = "SELECT device.* FROM device,belongtoroom where belongtoroom.deviceID = device.deviceID AND belongtoroom.roomID = ?";

			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setString(1, roomID);
			mResultSet = mPreparedStatement.executeQuery();

			while (mResultSet.next()) {
				deviceName = "(" + mResultSet.getString("deviceID") + ")" + mResultSet.getString("deviceType") + ">>"
						+ mResultSet.getString("deviceName") + ";";
				deviceList += deviceName;
			}

			sql = "SELECT device.* FROM device,preorderdevice where preorderdevice.deviceID = device.deviceID AND preorderdevice.preorderID = ?";

			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setInt(1, preorderID);
			mResultSet = mPreparedStatement.executeQuery();

			while (mResultSet.next()) {
				deviceName = "(" + mResultSet.getString("deviceID") + ")" + mResultSet.getString("deviceType") + ">>"
						+ mResultSet.getString("deviceName") + ";";
				deviceList += deviceName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(mPreparedStatement != null) mPreparedStatement.close();
				if(mResultSet != null) mResultSet.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return deviceList;
	}

	private String getParticipants() {

		try {
			sql = "SELECT staff.* FROM staff,preorderparticipant WHERE preorderparticipant.participant = staff.staffID AND preorderparticipant.preorderID = ?";

			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setInt(1, preorderID);
			mResultSet = mPreparedStatement.executeQuery();

			metaData = mResultSet.getMetaData();

			while (mResultSet.next()) {
				participantList += "(" + mResultSet.getString("staffID") + ")" + mResultSet.getString("staffName")
						+ ";";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(mPreparedStatement != null) mPreparedStatement.close();
				if(mResultSet != null) mResultSet.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return participantList;
	}

	public void launch() {
		this.setVisible(true);
	}
}
