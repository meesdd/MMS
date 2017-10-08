package mms.wangzhen.meetingpreorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;
import mms.common.Device;
import mms.common.MeetingPreorderInfo;
import mms.common.TimeComparetor;
import mms.common.TinyStaff;
import mms.wangzhen.PreorderMeetingPanel;
import mms.wangzhen.RecentMeetingPanel;
import mms.zhangleiyu.MyMail;

public class MeetingPreorderFrame extends JFrame{
	
	/**************主要组件区*********************/
	protected JLabel enableRoomsLabel;
	protected DefaultTableModel enableRoomsDTM;
	protected JTable enableRoomsTable;
	protected JScrollPane enableRoomsJSP;
	
	protected JLabel timeTitleLabel;
	protected JComboBox<Integer> startYear,
								startMonth,
								startDay,
								startHour,
								startMinute,
								endYear,
								endMonth,
								endDay,
								endHour,
								endMinute;
	
	protected JLabel peopleNumTitleLabel;
	protected JTextField peopleNumTextField;
	protected JButton confirmButton;
	
	protected JLabel extDeviceTitleLabel;
	protected JLabel extDeviceSelectLabel;
	protected JButton extDeviceSelectButton;
	
	protected JLabel participantTitleLabel;
	protected DefaultTableModel partitcipantSelectDTM;
	protected JTable participantSelectTable;
	protected JScrollPane participantJSP;
	protected JButton recorderSelectButton;
	protected JButton participantSelectButton;
	protected JButton cancelPreorderButton, submitButton, modifyButton;
	/****************辅助组件区********************/
	protected JPanel jp1, jp2, jp3, jp4, jp5, jp6;
	
	
	
	/**************内容区*************************/
	protected ConnectionRegistrar cr;
	protected RecentMeetingPanel rmp;
	protected PreorderMeetingPanel pmp;
	protected MeetingPreorderInfo mpi;
	public MeetingPreorderInfo getMpi() {
		return mpi;
	}
	public void setMpi(MeetingPreorderInfo mpi) {
		this.mpi = mpi;
	}
	protected MeetingPreorderFrame mpf = this;
	protected OtherDeviceSelectFrame odsf;
	protected ParticipantsSelectFrame psf;
	protected List<String> enableRooms;
	protected String[] enableRoomsTableHead;
	protected String[][] enableRoomsTableContent;
	protected String roomID;
	
	protected Timestamp startTime;
	protected Timestamp endTime;
	
	protected int peopleNum;
	
	protected List<Device> selectedDevices;
	protected List<TinyStaff> selectedStaffs;
	protected List<String> recorders;
	protected String[] participantTableHead;
	protected String[][] participantTableContent;
	protected String organizer;
	protected boolean isConfirmed = false;
	protected String status;
	
	
	/******************************************************/
	public MeetingPreorderFrame(String status, ConnectionRegistrar cr, RecentMeetingPanel rmp, PreorderMeetingPanel pmp) {
		this.cr = cr;
		this.rmp = rmp;
		this.pmp = pmp;
		this.status = status;
		this.setTitle("预约会议");
		this.setSize(new Dimension(700, 680));
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null); 
		enableRoomsLabel = new JLabel("当前可选会议室", JLabel.CENTER);
		enableRoomsLabel.setPreferredSize(new Dimension(600, 20));
		enableRoomsLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
		enableRoomsDTM = new DefaultTableModel();
		enableRoomsTable = new JTable(enableRoomsDTM){
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		enableRoomsJSP = new JScrollPane(enableRoomsTable);
		enableRoomsJSP.setPreferredSize(new Dimension(600, 100));
		enableRoomsJSP.setBorder(BorderFactory.createLineBorder(Color.RED));

		timeTitleLabel = new JLabel("时间选择", JLabel.CENTER);
		timeTitleLabel.setPreferredSize(new Dimension(600, 30));
		timeTitleLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
		startYear = new JComboBox<Integer>();
		startMonth = new JComboBox<Integer>();
		startDay = new JComboBox<Integer>();
		startHour = new JComboBox<Integer>();
		startMinute = new JComboBox<Integer>();
		endYear = new JComboBox<Integer>();
		endMonth = new JComboBox<Integer>();
		endDay = new JComboBox<Integer>();
		endHour = new JComboBox<Integer>();
		endMinute = new JComboBox<Integer>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		for(int i = 0; i < 20; i++) {
			startYear.addItem(c.get(Calendar.YEAR) + i);
			endYear.addItem(c.get(Calendar.YEAR) + i);
		}
		for(int i = 0; i < 12; i++) {
			startMonth.addItem(i + 1);
			endMonth.addItem(i + 1);
		}
		int smonthDays = getMonthNumber
				((Integer)startYear.getSelectedItem(), 
				(Integer)startMonth.getSelectedItem());
		for(int i = 0; i < smonthDays; i++) {
			startDay.addItem(i + 1);
		}
		int emonthDays = getMonthNumber
				((Integer)endYear.getSelectedItem(), 
				(Integer)endMonth.getSelectedItem());
		for(int i = 0; i < emonthDays; i++) {
			endDay.addItem(i + 1);
		}
		for(int i = 0; i < 24; i++) {
			startHour.addItem(i);
			endHour.addItem(i);
		}
		for(int i = 0; i < 60; i++) {
			startMinute.addItem(i);
			endMinute.addItem(i);
		}
		jp1 = new JPanel();
		jp1.setPreferredSize(new Dimension(600, 40));
		jp1.setBorder(BorderFactory.createLineBorder(Color.RED));
		jp1.add(startYear);
		jp1.add(startMonth);
		jp1.add(startDay);
		jp1.add(startHour);
		jp1.add(startMinute);
		jp1.add(new JLabel("到"));
		jp1.add(endYear);
		jp1.add(endMonth);
		jp1.add(endDay);
		jp1.add(endHour);
		jp1.add(endMinute);
		
		peopleNumTitleLabel = new JLabel("人数范围设定", JLabel.CENTER);
		peopleNumTitleLabel.setPreferredSize(new Dimension(600, 25));
		peopleNumTitleLabel.setBorder(BorderFactory.createLineBorder(Color.red));
		peopleNumTextField = new JTextField();
		peopleNumTextField.setPreferredSize(new Dimension(200, 30));
		confirmButton = new JButton("确认");
		confirmButton.setPreferredSize(new Dimension(100, 30));
		jp2 = new JPanel();
		jp2.setPreferredSize(new Dimension(600, 40));
		jp2.setBorder(BorderFactory.createLineBorder(Color.red));
		jp2.add(peopleNumTextField);
		jp2.add(confirmButton);
		
		extDeviceTitleLabel = new JLabel("额外设备选择", JLabel.CENTER);
		extDeviceTitleLabel.setPreferredSize(new Dimension(600, 25));
		extDeviceTitleLabel.setBorder(BorderFactory.createLineBorder(Color.red));
		extDeviceSelectLabel = new JLabel();
		extDeviceSelectLabel.setBorder(BorderFactory.createLineBorder(Color.blue));
		extDeviceSelectLabel.setPreferredSize(new Dimension(350, 30));
		extDeviceSelectButton = new JButton("额外设备选择");
		extDeviceSelectButton.setPreferredSize(new Dimension(120, 30));
		jp3 = new JPanel();
		jp3.setPreferredSize(new Dimension(600, 40));
		jp3.setBorder(BorderFactory.createLineBorder(Color.red));
		jp3.add(extDeviceSelectLabel);
		jp3.add(extDeviceSelectButton);
		
		participantTitleLabel = new JLabel("与会人员选择", JLabel.CENTER);
		participantTitleLabel.setPreferredSize(new Dimension(600, 25));
		participantTitleLabel.setBorder(BorderFactory.createLineBorder(Color.red));
		partitcipantSelectDTM = new DefaultTableModel();
		participantSelectTable = new JTable(partitcipantSelectDTM){
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		participantJSP = new JScrollPane(participantSelectTable);
		participantJSP.setPreferredSize(new Dimension(300, 80));
		recorderSelectButton = new JButton("设置为会议记录人");
		recorderSelectButton.setEnabled(false);
		recorderSelectButton.setPreferredSize(new Dimension(140, 35));
		participantSelectButton = new JButton("选择与会人");
		participantSelectButton.setPreferredSize(new Dimension(140, 35));
		jp4 = new JPanel();
		jp4.setPreferredSize(new Dimension(150, 90));
		jp4.setBorder(BorderFactory.createLineBorder(Color.blue));
		jp4.add(recorderSelectButton);
		jp4.add(participantSelectButton);
		jp5 = new JPanel();
		jp5.setPreferredSize(new Dimension(600, 100));
		jp5.setBorder(BorderFactory.createLineBorder(Color.red));
		jp5.add(participantJSP);
		jp5.add(jp4);
		
		cancelPreorderButton = new JButton("取消预约");
		cancelPreorderButton.setPreferredSize(new Dimension(150, 45));
		submitButton = new JButton("请在上方选择一个会议室");
		submitButton.setEnabled(false);
		submitButton.setPreferredSize(new Dimension(200, 45));
		modifyButton = new JButton("修改预约");
		modifyButton.setPreferredSize(new Dimension(150, 45));
		if("edit".equals(status)) {
			cancelPreorderButton.setEnabled(true);
			modifyButton.setEnabled(true);
		} else {
			cancelPreorderButton.setVisible(false);
			modifyButton.setVisible(false);
		}
		jp6 = new JPanel();
		jp6.setPreferredSize(new Dimension(600, 60));
		jp6.setBorder(BorderFactory.createLineBorder(Color.red));
		jp6.add(cancelPreorderButton);
		jp6.add(submitButton);
		jp6.add(modifyButton);
		
		this.add(enableRoomsLabel);
		this.add(enableRoomsJSP);
		this.add(timeTitleLabel);
		this.add(jp1);
		this.add(peopleNumTitleLabel);
		this.add(jp2);
		this.add(extDeviceTitleLabel);
		this.add(jp3);
		this.add(participantTitleLabel);
		this.add(jp5);
		this.add(jp6);
		
	}
	
	protected class EnableTableClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getComponent() == enableRoomsTable) {
				int row = enableRoomsTable.getSelectedRow();
				roomID = (String) enableRoomsTable.getValueAt(row, 0);
				submitButton.setText("提交预约");
				submitButton.setEnabled(true);
			} else {
				submitButton.setText("请在上方选择一个会议室");
				submitButton.setEnabled(false);
			}
		}
	}
	
	protected class ComboxChangedListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				isConfirmed = false;
				roomID = null;
				String[] head = getParticipantTableHead();
				partitcipantSelectDTM.setDataVector(null, head);
			}
		}
	}
	
	protected class StartComboxActionListener implements ItemListener {	
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				int syear = (Integer)startYear.getSelectedItem();
				int smonth = (Integer)startMonth.getSelectedItem();
				int totalDays = getMonthNumber(syear, smonth);
				startDay.removeAllItems();
				for(int i = 0; i < totalDays; i++) {
					startDay.addItem(i + 1);
				}
			}
		}
	}
	
	protected class EndComboxActionListener implements ItemListener {	
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				int eyear = (Integer)endYear.getSelectedItem();
				int emonth = (Integer)endMonth.getSelectedItem();
				int totalDays = getMonthNumber(eyear, emonth);
				endDay.removeAllItems();
				for(int i = 0; i < totalDays; i++) {
					endDay.addItem(i + 1);
				}
			}
		}
	}
	
	protected class ParticipantTableClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getComponent() == participantSelectTable) {
				String note = (String)participantSelectTable.getValueAt(participantSelectTable.getSelectedRow(), 2);
				if("".equals(note)) {
					recorderSelectButton.setText("设为记录人");
					recorderSelectButton.setActionCommand("set");
					recorderSelectButton.setEnabled(true);
				} else if("记录人".equals(note)) {
					recorderSelectButton.setText("移除记录人");
					recorderSelectButton.setActionCommand("remove");
					recorderSelectButton.setEnabled(true);
				}
			}
		}
	}
	
	protected class MyButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == confirmButton) {
				clickConfirmButton();
			} else {
				if(isConfirmed) {
					if(e.getSource() == extDeviceSelectButton) {
						odsf = new OtherDeviceSelectFrame(cr, mpf, "new", null, startTime, endTime);
						odsf.launch();
					} else if(e.getSource() == recorderSelectButton) {
						if(e.getActionCommand().equals("set")) {
							int row = participantSelectTable.getSelectedRow();
							recorders.add((String)participantSelectTable.getValueAt(row, 0));
							participantSelectTable.setValueAt("记录人", row, 2);
							partitcipantSelectDTM.fireTableDataChanged();
						} else if(e.getActionCommand().equals("remove")) {
							int row = participantSelectTable.getSelectedRow();
							recorders.remove(participantSelectTable.getValueAt(row, 0));
							participantSelectTable.setValueAt("", row, 2);
							partitcipantSelectDTM.fireTableDataChanged();
						}
					} else if(e.getSource() == participantSelectButton) {
						psf = new ParticipantsSelectFrame(cr, mpf, startTime, endTime, peopleNum, "new", null);
						psf.launch(); 
					} else if(e.getSource() == submitButton) {
						submit();
					}
				} else {
					JOptionPane.showMessageDialog(null, 
							"请在上方确认有符合条件的会议室", 
							"alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	protected void clickConfirmButton() {
		if("".equals(peopleNumTextField.getText())) {
			JOptionPane.showMessageDialog(null, 
					"请输入大致人数", 
					"alert", JOptionPane.ERROR_MESSAGE);
		} else {
			String st = startYear.getSelectedItem() + "-" 
						+ startMonth.getSelectedItem() + "-"
						+ startDay.getSelectedItem() + " "
						+ startHour.getSelectedItem() + ":"
						+ startMinute.getSelectedItem() + ":"
						+ "30";
			String et = endYear.getSelectedItem() + "-" 
					+ endMonth.getSelectedItem() + "-"
					+ endDay.getSelectedItem() + " "
					+ endHour.getSelectedItem() + ":"
					+ endMinute.getSelectedItem() + ":"
					+ "00";
			startTime = Timestamp.valueOf(st);
			endTime = Timestamp.valueOf(et);
			
			if(moreThanCurrentTime(startTime)) {
				if(startTime.compareTo(endTime) == -1 ) {
					List<String> afterFiltedRooms 
					= removeRoomByTimeCapacity(enableRooms, startTime, endTime);
					String[] head = getEnableTableHead();
					String[][] content = getEnableTableContent(head, afterFiltedRooms);
					setEnableTable(content, head);
					if(afterFiltedRooms.size() <= 0) {
						JOptionPane.showMessageDialog(null, 
								"抱歉,没有适合所选时间段或容纳人数的会议室可用了", 
								"alert", JOptionPane.ERROR_MESSAGE);
						isConfirmed = false;
					} else {
						JOptionPane.showMessageDialog(null, 
								"当前时间段有会议室可用", 
								"alert", JOptionPane.ERROR_MESSAGE);
						peopleNum = Integer.parseInt(peopleNumTextField.getText());
						
						mpi.setStartTime(startTime);
						mpi.setEndTime(endTime);
						
						isConfirmed = true;
						submitButton.setText("请在上方选择一个会议室");
						submitButton.setEnabled(false);
					}	
				} else {
					JOptionPane.showMessageDialog(null, 
							"所选开始时间超过结束时间", 
							"alert", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, 
						"选择的时间已过", 
						"alert", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected boolean moreThanCurrentTime(Timestamp startDate) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(now.compareTo(startDate) == -1) {
			return true;
		}
		return false;
	}
	
	
	
	protected List<String> removeRoomByTimeCapacity(List<String> allRooms , Timestamp sd, Timestamp ed) {
		List<String> temp = new ArrayList<String>();
		int inputNumber = Integer.parseInt(peopleNumTextField.getText());
		for(String s : allRooms) {
			temp.add(s);
		}
		String sql = "SELECT * FROM preorder, room WHERE room.roomID = preorder.roomID";
		String sql2 = "SELECT roomID, roomSize FROM room";
		Timestamp recordSd = null;
		Timestamp recordEd = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		try {
			ps1 = cr.getConnection().prepareStatement(sql);
			rs = ps1.executeQuery();
			while(rs.next()) {
				recordSd = rs.getTimestamp("startTime");
				recordEd = rs.getTimestamp("endTime");
				int size = Integer.parseInt(rs.getString("roomSize"));
				if(TimeComparetor.timeOverlap(sd, ed, recordSd, recordEd) || (size < inputNumber)) {
					temp.remove(rs.getString("roomID"));
				}
			}
			try {
				if(ps1 != null) ps1.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps1 = cr.getConnection().prepareStatement(sql2);
			rs = ps1.executeQuery();
			while(rs.next()) {
				int size = Integer.parseInt(rs.getString("roomSize"));
				if(size<inputNumber) {
					temp.remove(rs.getString("roomID"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps1 != null) ps1.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	
	public void initalContent() {
		mpi = new MeetingPreorderInfo();
		roomID = null;
		startTime = Timestamp.valueOf("1111-1-1 1:1:1");
		endTime = Timestamp.valueOf("1111-1-1 1:1:1");	
		organizer = cr.getStaff().getStaffID();
		selectedDevices = new ArrayList<Device>();
		selectedStaffs = new ArrayList<TinyStaff>();
		recorders = new ArrayList<String>();
		enableRooms = getAllRoomsID();
		enableRoomsTableHead = getEnableTableHead();
		enableRoomsTableContent = getEnableTableContent(enableRoomsTableHead, enableRooms);
		enableRoomsDTM.setDataVector(enableRoomsTableContent, enableRoomsTableHead);
		
	}
	
	public void addMyComponentListener() {
		enableRoomsTable.addMouseListener(new EnableTableClickListener());
		ComboxChangedListener ccl = new ComboxChangedListener();
		startYear.addItemListener(ccl);
		startMonth.addItemListener(ccl);
		startDay.addItemListener(ccl);
		startHour.addItemListener(ccl);
		startMinute.addItemListener(ccl);
		endYear.addItemListener(ccl);
		endMonth.addItemListener(ccl);
		endDay.addItemListener(ccl);
		endHour.addItemListener(ccl);
		endMinute.addItemListener(ccl);
		StartComboxActionListener scal = new StartComboxActionListener();
		startYear.addItemListener(scal);
		startMonth.addItemListener(scal);
		EndComboxActionListener ecal = new EndComboxActionListener();
		endYear.addItemListener(ecal);
		endMonth.addItemListener(ecal);
		peopleNumTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() > '9' || e.getKeyChar() < '0'){
					e.consume();
					return;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		MyButtonListener mbl = new MyButtonListener();
		confirmButton.addActionListener(mbl);
		extDeviceSelectButton.addActionListener(mbl);
		participantSelectTable.addMouseListener(new ParticipantTableClickListener());
		recorderSelectButton.addActionListener(mbl);
		participantSelectButton.addActionListener(mbl);
		submitButton.addActionListener(mbl);
	}
	
	protected String[] getEnableTableHead() {
		String[] temp = {"会议室", "容纳人数", "已拥有设备"};
		return temp;
	}
	
	protected String[][] getEnableTableContent(String[] head, List<String> enableRooms) {
		int row = enableRooms.size();
		int col = head.length;
		String[][] temp = new String[row][col];
		for(int i = 0; i < row; i++) {
			String sql1 = "SELECT * FROM room WHERE roomID = ?";	
			String sql2 = "SELECT device.deviceType, device.deviceName"
						+ " FROM belongtoroom, device"
						+ " WHERE belongtoroom.deviceID = device.deviceID"
						+ " AND belongtoroom.roomID = ?";	
			String enableRoomID = enableRooms.get(i);
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			PreparedStatement ps2 = null;
			ResultSet rs2 = null;
			try {
				ps1 = cr.getConnection().prepareStatement(sql1);
				ps1.setString(1, enableRoomID);
				rs1 = ps1.executeQuery();
				rs1.first();
				temp[i][0] = rs1.getString("roomID");
				temp[i][1] = rs1.getString("roomSize");
				ps2 = cr.getConnection().prepareStatement(sql2);
				ps2.setString(1, enableRoomID);
				rs2 = ps2.executeQuery();
				while(rs2.next()) {
					String content = "(" + rs2.getString("deviceType") + ")" 
									+ rs2.getString("deviceName") + " ";
					if(temp[i][2] == null)
						temp[i][2] = content;
					else
						temp[i][2] += content;
				}
				if(temp[i][2] == null) temp[i][2] = "无";
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(ps1 != null) ps1.close();
					if(rs1 != null) rs1.close();
					if(ps2 != null) ps1.close();
					if(rs2 != null) rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return temp;
	}
	
	protected void setEnableTable(String[][] content, String[] head) {
		enableRoomsDTM.setDataVector(content, head);
	}
	
	protected void setSelectedStaff(List<TinyStaff> staffs) {
		selectedStaffs.clear();
		for(TinyStaff s : staffs) {
			selectedStaffs.add(s);
		}
		participantTableHead = getParticipantTableHead();
		participantTableContent = getParticipantTableContent(participantTableHead, selectedStaffs);
		partitcipantSelectDTM.setDataVector(participantTableContent, participantTableHead);
	}
	
	protected String[] getParticipantTableHead() {
		String[] temp= {"与会人ID","姓名","备注"};
		return temp;
	}
	
	protected String[][] getParticipantTableContent(String[] head, List<TinyStaff> staffs) {
		int row = staffs.size();
		int col = head.length;
		String[][] temp = new String[row][col];
		for(int i = 0; i < row; i++) {
			temp[i][0] = staffs.get(i).getStaffID();
			temp[i][1] = staffs.get(i).getStaffName();
			temp[i][2] = "";
		}
		return temp;
	}
	
	protected List<String> getAllRoomsID() {
		String sql = "SELECT * FROM room";
		List<String> allrooms = new ArrayList<String>();
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		try {
			ps1 = cr.getConnection().prepareStatement(sql);
			rs = ps1.executeQuery();
			while (rs.next()) {
				allrooms.add(rs.getString("roomID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps1 != null)
					ps1.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allrooms;
	}

	
	protected void setSelectedDevice(List<Device> selectedContent) {
		String content = "";
		selectedDevices.clear();
		for(Device d : selectedContent) {
			selectedDevices.add(d);
			content += "(" + d.getDeviceType() + ")" + d.getDeviceName() +";   ";
		}
		if(!"".equals(content)) {
			extDeviceSelectLabel.setText(content);	
		} else {
			extDeviceSelectLabel.setText("无");
		}
	}

	protected void listSelected() {
		String str = "当前所选时间:" + startTime + "~~~" + endTime + "\n" 
					+ "所选与会人数" + peopleNum + "\n"
					+ "所选设备有" + deviceList(selectedDevices) + "\n" 
					+ "组织者" + organizer + "\n"
					+ "与会人" + staffList(selectedStaffs) + "\n" 
					+ "记录人" + recorderList(recorders) + "\n"
					+ "会议室" + roomID + "\n";
	}

	protected String deviceList(List<Device> devices) {
		String temp = "";
		for (Device d : devices) {
			temp += d.getDeviceName() + " ";
		}
		if("".equals(temp)) {
			return "没有额外设备";
		}
		return temp;
	}

	protected String staffList(List<TinyStaff> staffs) {
		String temp = "";
		for (TinyStaff s : staffs) {
			temp += s.getStaffName() + " ";
		}
		return temp;
	}

	protected String recorderList(List<String> recorders) {
		String temp = "";
		for (String r : recorders) {
			temp += r + " ";
		}
		if("".equals(temp)) {
			return "没有设置记录人员";
		}
		return temp;
	}
	
	protected void submit() {
		if (selectedStaffs.size() > peopleNum) {
			JOptionPane.showMessageDialog(null, 
					"所选与会人数大于设定人数", "alert", 
					JOptionPane.ERROR_MESSAGE);
		} else if (isConfirmed == false) {
			JOptionPane.showMessageDialog(null, 
					"请确认所选时间段与设定人数有可用会议室", "alert", 
					JOptionPane.ERROR_MESSAGE);
		} else if (selectedStaffs.size() == 0) {
			JOptionPane.showMessageDialog(null, 
					"请至少选择一位与会人", "alert", 
					JOptionPane.ERROR_MESSAGE);
		} else if (roomID == null) {
			JOptionPane.showMessageDialog(null, 
					"请在上方选择一个会议室", "alert", 
					JOptionPane.ERROR_MESSAGE);
		} else if(true) {
			
			submitNewData();
			emailNewMeeting();
		}
	}
	
	protected void submitNewData() {
		listSelected();
		String sql = "INSERT INTO preorder "
					+ "(roomID, startTime, endTime, organizer, isConfirmed)"
					+ " VALUE"
					+ " (?, ?, ?, ?, 0)";
		String getThiePreorderIDSQL = "SELECT MAX(preorderID) FROM preorder";
		String insertDeviceSQL = "INSERT INTO"
				+ " preorderdevice"
				+ " (preorderID, deviceID)"
				+ " VALUES"
				+ " (?, ?)";
		String insertParticipantSQL = "INSERT INTO"
				+ " preorderparticipant"
				+ " (preorderID, participant)"
				+ " VALUES"
				+ " (?, ?)";
		String insertRecorderSQL = "INSERT INTO"
				+ " preorderrecorder"
				+ " (preorderID, recorder)"
				+ " VALUES"
				+ " (?, ?)";
		String thisPreorderID = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, roomID);
			ps.setString(2, startTime.toString());
			ps.setString(3, endTime.toString());
			ps.setString(4, organizer);
			int thisRowCount  = ps.executeUpdate();
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ps = cr.getConnection().prepareStatement(getThiePreorderIDSQL);
			rs = ps.executeQuery();
			while(rs.next()) {
				thisPreorderID = rs.getString(1);
			}
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ps = cr.getConnection().prepareStatement(insertDeviceSQL);
			for(Device d : selectedDevices) {
				ps.setString(1, thisPreorderID);
				ps.setString(2, d.getDeviceID());
				int rowCount = ps.executeUpdate();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ps = cr.getConnection().prepareStatement(insertParticipantSQL);
			for(TinyStaff ts : selectedStaffs) {
				ps.setString(1, thisPreorderID);
				ps.setString(2, ts.getStaffID());
				int rowCount = ps.executeUpdate();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ps = cr.getConnection().prepareStatement(insertRecorderSQL);
			for(String s : recorders) {
				ps.setString(1, thisPreorderID);
				ps.setString(2, s);
				int rowCount = ps.executeUpdate();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, 
					"会议预约成功", "alert", 
					JOptionPane.ERROR_MESSAGE);
			isConfirmed = false;
			rmp.flushTable();
			pmp.flushTable();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void emailNewMeeting() {
		int len = selectedStaffs.size();
		String[] emails = new String[len];
		String sql = "SELECT email FROM staff WHERE staffID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			for(int i = 0; i < len; i++) {
				ps.setString(1, selectedStaffs.get(i).getStaffID());
				rs = ps.executeQuery();
				while(rs.next()) {
					emails[i] = rs.getString("email");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			MyMail.sendMail( emails, "新的会议需要参加", emailContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected String emailContent() {
		String str = "<b>当前所选时间: " + startTime + "~~~" + endTime + "</br>" 
					+ "所选与会人数: " + peopleNum + "\n"
					+ "所选设备有: " + deviceList(selectedDevices) + "</br>" 
					+ "组织者: " + organizer + "\n"
					+ "与会人: " + staffList(selectedStaffs) + "</br>" 
					+ "记录人: " + recorderList(recorders) + "</br>"
					+ "会议室: " + roomID + "</br>";
		return str;
	} 
	
	protected int getMonthNumber(int y, int m) {
		switch(m) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		default :
			if(y % 400 == 0 || (y % 4 == 0 && y % 100 != 0)) {
				return 29;
			} else {
				return 28;
			}
		}
	}
	
	public void launch() {
		this.setVisible(true);
	}

}
