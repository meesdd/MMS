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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import mms.common.ConnectionRegistrar;
import mms.zhangzhichao.FtpByApache;

public class PreorderConfirm extends JFrame {
	private JScrollPane mJPanel;
	private JLabel mJLabel;
	private List<JPanel> mRowsPanel;
	private JPanel preorderInfoPanel, mainJpanel, rowPanel;

	private JButton mJButtonSave, mJButtonCancel, mJButtonFile;
	private JFileChooser mJFileChooser;
	private File mFile;

	private ResultSet mResultSet;
	private java.sql.ResultSetMetaData metaData;
	
	private JTextField mJTFOrderID, mJTFRoomID, mJTFStartTime, mJTFEndTime;
	private JComboBox<String> mJComboBox;
	private JLabel mPreorderID, mJLabelRoomID, mJLabelStartTime, mJLabelEndTime;

	private java.sql.PreparedStatement mPreparedStatement;
	private String sql;

	private FtpByApache ftpByApache = null;

	private String fileName;

	private String remotePath;

	private String deviceName;

	private Map<String, Integer> deviceList;

	private ConnectionRegistrar cr;
	
	private int preorderID, deviceID;
	private String roomID,startTime,endTime;

	private static final String[] select = { "否","是"};

	private Map<JComboBox<String>, Integer> mCBMap;

	private MeetingCompleteManager mcm;
	
	private Dimension Size, mJpanelSize, mJLabelSize;
	private FlowLayout mFlowLayout;

	private static Font font;
	private static Color color;

	public PreorderConfirm(MeetingCompleteManager mcm,ConnectionRegistrar cr ,String startTime,String endTime, int preorderID, String roomID) {

		preorderInfoPanel = new JPanel();
		mainJpanel = new JPanel();
		mJPanel = new JScrollPane();
		mRowsPanel = new ArrayList<JPanel>();

		this.cr = cr;
		this.mcm = mcm;
		this.preorderID = preorderID;
		this.roomID = roomID;
		this.startTime = startTime;
		this.endTime = endTime;

		color = Color.WHITE;
		font = new Font("宋体", Font.BOLD, 15);

		deviceList = new HashMap<String, Integer>();
		mCBMap = new HashMap<JComboBox<String>, Integer>();

		mFlowLayout = new FlowLayout(FlowLayout.LEFT);

		mJButtonSave = new JButton("保存");
		mJButtonCancel = new JButton("取消");
		mJButtonFile = new JButton("上传文件");

		mJFileChooser = new JFileChooser();
		Size = new Dimension(160, 30);

		mJpanelSize = new Dimension(350, 50);
		mJLabelSize = new Dimension(100, 50);

		remotePath = "./" ;
		 

		this.init();

		this.fill();

	}

	public void init() {

		this.setSize(new Dimension(400, 550)); // 设置窗口大小
		this.setResizable(false); // 设置不可更改窗口大小
		this.setLocation(new Point(500, 100)); // 设置窗口位置
		// this.setUndecorated(true);

		mJFileChooser.setFileFilter(new DocFilter());

		mJButtonSave.setPreferredSize(Size);
		mJButtonCancel.setPreferredSize(Size);
		mJButtonFile.setPreferredSize(Size);

		preorderInfoPanel.setPreferredSize(new Dimension(350, 550)); // 设置容器大小
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
				try {
					// mConnectionRegistrar.getConnection().close();
				} catch (Exception exception) {
					
				}
				dispose();
			}
		});

		mJButtonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int state = 0;
				for (Map.Entry<JComboBox<String>, Integer> entry : mCBMap.entrySet()) {
					mJComboBox = entry.getKey();
					state = mJComboBox.getSelectedIndex();
					try {
						sql = "UPDATE device SET isBreakDown = ? WHERE deviceID = ?";
						mPreparedStatement = cr.getConnection().prepareStatement(sql);
						mPreparedStatement.setInt(1, state);
						mPreparedStatement.setInt(2, entry.getValue());
						mPreparedStatement.execute();

						sql = "UPDATE preorder SET isConfirmed = ? WHERE preorderID = ?";
						mPreparedStatement = cr.getConnection().prepareStatement(sql);
						mPreparedStatement.setInt(1, 1);
						mPreparedStatement.setInt(2, preorderID);
						mPreparedStatement.execute();
					} catch (SQLException exception) {
						
					}  finally {
						try {
							if(mPreparedStatement != null) mPreparedStatement.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
				JOptionPane.showMessageDialog(null, "保存成功！", "成功", JOptionPane.WARNING_MESSAGE);
				mcm.init();
				dispose();
			}
		});

		mJButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// mConnectionRegistrar.getConnection().close();
				} catch (Exception exception) {
					
					dispose();
				}
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
				mJFileChooser.showDialog(new JLabel(), "选择文件");
				mFile = mJFileChooser.getSelectedFile();
				if (mFile != null ) {
			
					fileName = "meeting_" + preorderID + "."
							+ mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
					try {
						ftpByApache.upload(mFile.getAbsolutePath(), remotePath + fileName);
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(null, "网络连接失败！", "失败", JOptionPane.WARNING_MESSAGE);
					} finally {
						if(ftpByApache != null) ftpByApache.close_connection();
					}
					sql = "UPDATE preorderrecorder SET filePath = ? WHERE preorderID = ?";
					try {
						mPreparedStatement = cr.getConnection().prepareStatement(sql);
						mPreparedStatement.setString(1, remotePath + fileName);
						mPreparedStatement.setInt(2, preorderID);
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
					JOptionPane.showMessageDialog(null, "文件上传成功！", "成功", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "未选择文件！", "注意", JOptionPane.WARNING_MESSAGE);
				}
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
		mRowsPanel.add(rowPanel);
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
		mRowsPanel.add(rowPanel);
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
		mRowsPanel.add(rowPanel);
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
		mRowsPanel.add(rowPanel);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabel = new JLabel(" 设备是否损坏：");
		mJLabel.setFont(font);
		mJLabel.setPreferredSize(mJpanelSize);
		rowPanel.add(mJLabel);
		mRowsPanel.add(rowPanel);
		preorderInfoPanel.add(rowPanel);

		addDevice();

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		mJLabel = new JLabel(" 会议文件：");
		mJLabel.setFont(font);
		mJLabel.setPreferredSize(new Dimension(160, 50));
		rowPanel.add(mJLabel);
		rowPanel.add(mJButtonFile);
		mRowsPanel.add(rowPanel);
		preorderInfoPanel.add(rowPanel);

		rowPanel = new JPanel();
		rowPanel.setLayout(mFlowLayout);
		rowPanel.setBackground(Color.WHITE);
		rowPanel.setPreferredSize(mJpanelSize);
		rowPanel.add(mJButtonSave);
		rowPanel.add(mJButtonCancel);
		mRowsPanel.add(rowPanel);
		preorderInfoPanel.add(rowPanel);

		mJPanel.updateUI();

	}

	private void addDevice() {

		try {
			sql = "SELECT device.* FROM device,belongtoroom where belongtoroom.deviceID = device.deviceID AND belongtoroom.roomID = ?";

			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setString(1, roomID);
			mResultSet = mPreparedStatement.executeQuery();

			metaData = mResultSet.getMetaData();

			while (mResultSet.next()) {
				deviceName = mResultSet.getString("deviceType") + " >> " + mResultSet.getString("deviceName");
				deviceID = mResultSet.getInt("deviceID");
				deviceList.put(deviceName, deviceID);
			}

			sql = "SELECT device.* FROM device,preorderdevice where preorderdevice.deviceID = device.deviceID AND preorderdevice.preorderID = ?";

			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setInt(1, preorderID);
			mResultSet = mPreparedStatement.executeQuery();

			metaData = mResultSet.getMetaData();

			while (mResultSet.next()) {
				deviceName = mResultSet.getString("deviceType") + " >> " + mResultSet.getString("deviceName");
				deviceID = mResultSet.getInt("deviceID");
				deviceList.put(deviceName, deviceID);
			}

			for (Map.Entry<String, Integer> entry : deviceList.entrySet()) {

				mJComboBox = new JComboBox<>(select);

				mCBMap.put(mJComboBox, entry.getValue());

				rowPanel = new JPanel();
				rowPanel.setLayout(mFlowLayout);
				rowPanel.setBackground(Color.WHITE);
				rowPanel.setPreferredSize(mJpanelSize);
				mJLabel = new JLabel(" " + entry.getKey());
				mJLabel.setFont(font);
				mJLabel.setPreferredSize(new Dimension(200, 50));
				rowPanel.add(mJLabel);
				rowPanel.add(mJComboBox);
				mRowsPanel.add(rowPanel);
				preorderInfoPanel.add(rowPanel);

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
	}

	private class DocFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = f.getName().toLowerCase();
			if (extension != null) {
				if (extension.endsWith("docx") || extension.endsWith("doc") || extension.endsWith("txt")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "图片文件(*.docx, *.doc, *.txt)";
		}
	}

	public void launch() {
		this.setVisible(true);
	}

}
