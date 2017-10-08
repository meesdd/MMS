package mms.wangzhen.meetingpreorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import mms.common.ConnectionRegistrar;
import mms.common.Device;
import mms.common.MeetingPreorderInfo;
import mms.common.TimeComparetor;
import mms.common.TinyStaff;
import mms.lbb.MonthChart;
import mms.wangzhen.PreorderMeetingPanel;
import mms.wangzhen.RecentMeetingPanel;
import mms.zhangleiyu.MyMail;

public class MeetingPreorderManagementFrame extends MeetingPreorderFrame {
	private MeetingPreorderInfo mpi;
	private MonthChart mc;
	private MeetingPreorderFrame mpf = this;
	public MeetingPreorderManagementFrame(String status, ConnectionRegistrar cr, RecentMeetingPanel rmp,
			PreorderMeetingPanel pmp, MonthChart mc, MeetingPreorderInfo mpi) {
		
		super(status, cr, rmp, pmp);
		this.setTitle("该会议相关信息");
		this.mpi = mpi;
		this.mc = mc;
	}
	
	public void initalContentAgain() {
		roomID = mpi.getRoomID();
		startTime = mpi.getStartTime();
		endTime = mpi.getEndTime();	
		organizer = cr.getStaff().getStaffID();
		selectedDevices = mpi.getDevices();
		selectedStaffs = mpi.getParticipants();
		recorders = mpi.getRecorders();
		enableRooms = getAllRoomsID();
		enableRoomsTableHead = getEnableTableHead();
		enableRoomsTableContent = getEnableTableContent(enableRoomsTableHead, enableRooms);
		enableRoomsDTM.setDataVector(enableRoomsTableContent, enableRoomsTableHead);
		
		Date d = new Date();
		startYear.setSelectedIndex(startTime.getYear() - d.getYear());
		startMonth.setSelectedIndex(startTime.getMonth());
		startDay.setSelectedIndex(startTime.getDate() - 1);
		startHour.setSelectedIndex(startTime.getHours());
		startMinute.setSelectedIndex(startTime.getMinutes());
		endYear.setSelectedIndex(endTime.getYear() - d.getYear());
		endMonth.setSelectedIndex(endTime.getMonth());
		endDay.setSelectedIndex(endTime.getDate() - 1);
		endHour.setSelectedIndex(endTime.getHours());
		endMinute.setSelectedIndex(endTime.getMinutes());
		
		peopleNumTextField.setText(""+selectedStaffs.size());
		
		
		String content = "";
		for (Device dv : selectedDevices) {
			content += "(" + dv.getDeviceType() + ")" + dv.getDeviceName() +";   ";
		}
		if (!"".equals(content)) {
			extDeviceSelectLabel.setText(content);	
		} else {
			extDeviceSelectLabel.setText("无");
		}
		
		participantTableHead = getParticipantTableHead();
		participantTableContent = getParticipantTableContent(participantTableHead, selectedStaffs);
		partitcipantSelectDTM.setDataVector(participantTableContent, participantTableHead);
		setAllPanelEnable(false);
		
	}
	@Override
	protected String[][] getParticipantTableContent(String[] head, List<TinyStaff> staffs) {
		int row = staffs.size();
		int col = head.length;
		String[][] temp = new String[row][col];
		for(int i = 0; i < row; i++) {
			temp[i][0] = staffs.get(i).getStaffID();
			temp[i][1] = staffs.get(i).getStaffName();
			if(recorders.contains(staffs.get(i).getStaffID())) {
				temp[i][2] = "记录者";
			} else {
				temp[i][2] = "";
			}
		}
		return temp;
	}
	
	protected void setAllPanelEnable(boolean flag) {
		enableRoomsTable.setEnabled(flag);
		startYear.setEnabled(flag);
		startMonth.setEnabled(flag);
		startDay.setEnabled(flag);
		startHour.setEnabled(flag);
		startMinute.setEnabled(flag);
		endYear.setEnabled(flag);
		endMonth.setEnabled(flag);
		endDay.setEnabled(flag);
		endHour.setEnabled(flag);
		endMinute.setEnabled(flag);
		peopleNumTextField.setEnabled(flag);
		confirmButton.setEnabled(flag);
		extDeviceSelectButton.setEnabled(flag);
		participantSelectTable.setEnabled(flag);
		recorderSelectButton.setEnabled(flag);
		participantSelectButton.setEnabled(flag);
	}
	
	
	
	private void submitEdit() {
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
			
			listSelected();
			submitNewEditData();
			emailEditedMeeting("edit");
		}
		
	}
	
	private void submitNewEditData() {
		String thisPreorderID = mpi.getPreorderID();
		String sql = "UPDATE preorder"
				+ " SET roomID = ?,"
				+ " startTime = ?,"
				+ " endTime = ?,"
				+ " organizer = ?,"
				+ " isConfirmed = 0"
				+ " WHERE preorderID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, roomID);
			ps.setString(2, startTime.toString());
			ps.setString(3, endTime.toString());
			ps.setString(4, organizer);
			ps.setString(5, thisPreorderID);
			ps.executeUpdate();
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			String delSQL1 = "DELETE FROM preorderdevice WHERE preorderID = ?";
			ps = cr.getConnection().prepareStatement(delSQL1);
			ps.setString(1, thisPreorderID);
			ps.execute();
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String insertDeviceSQL = "INSERT INTO"
					+ " preorderdevice"
					+ " (preorderID, deviceID)"
					+ " VALUES"
					+ " (?, ?)";
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
			String delSQL2 = "DELETE FROM preorderparticipant WHERE preorderID = ?";
			ps = cr.getConnection().prepareStatement(delSQL2);
			ps.setString(1, thisPreorderID);
			ps.execute();
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String insertParticipantSQL = "INSERT INTO"
					+ " preorderparticipant"
					+ " (preorderID, participant)"
					+ " VALUES"
					+ " (?, ?)";
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
			String delSQL3 = "DELETE FROM preorderrecorder WHERE preorderID = ?";
			ps = cr.getConnection().prepareStatement(delSQL3);
			ps.setString(1, thisPreorderID);
			ps.execute();
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String insertRecorderSQL = "INSERT INTO"
					+ " preorderrecorder"
					+ " (preorderID, recorder)"
					+ " VALUES"
					+ " (?, ?)";
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
					"会议修改成功", "alert", 
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
	
	public void addMyComponentListenerForEdit() {
		
		enableRoomsTable.addMouseListener(new EnableTableClickListener());
		ConfirmButtonListener cbl = new ConfirmButtonListener();
		confirmButton.addActionListener(cbl);
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
		
		EditPanelButtonListener epbl = new EditPanelButtonListener();	
		extDeviceSelectButton.addActionListener(epbl);
		participantSelectTable.addMouseListener(new ParticipantTableClickListener());
		participantSelectButton.addActionListener(epbl);
		recorderSelectButton.addActionListener(epbl);
		
		submitButton.addActionListener(epbl);
		
		CancelEditButtonListener cebl = new CancelEditButtonListener();
		cancelPreorderButton.addActionListener(cebl);
		modifyButton.addActionListener(cebl);
	}
	
	private void cancelPreorder() {
		String sql = "DELETE FROM preorder WHERE preorderID = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, mpi.getPreorderID());
			ps.execute();
			JOptionPane.showMessageDialog(null, "删除成功", "成功", JOptionPane.OK_OPTION);
			mc.flushDatePanel();
			rmp.flushTable();
			pmp.flushTable();
			this.dispose();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "删除失败,稍后再试", "失败", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (ps != null) ps.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class EditPanelButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isConfirmed) {
				if (e.getSource() == extDeviceSelectButton) {
					odsf = new OtherDeviceSelectFrame(cr, mpf, "edit", mpi.getPreorderID(), startTime, endTime);
					odsf.launch();
				} else if (e.getSource() == recorderSelectButton) {
					if (e.getActionCommand().equals("set")) {
						int row = participantSelectTable.getSelectedRow();
						recorders.add((String)participantSelectTable.getValueAt(row, 0));
						participantSelectTable.setValueAt("记录人", row, 2);
						partitcipantSelectDTM.fireTableDataChanged();
					} else if (e.getActionCommand().equals("remove")) {
						int row = participantSelectTable.getSelectedRow();
						recorders.remove(participantSelectTable.getValueAt(row, 0));
						participantSelectTable.setValueAt("", row, 2);
						partitcipantSelectDTM.fireTableDataChanged();
					}
				} else if (e.getSource() == participantSelectButton) {
					recorders.clear();
					psf = new ParticipantsSelectFrame(cr, mpf, startTime, endTime, peopleNum, "edit", mpi.getPreorderID());
					psf.launch();
				} else if (e.getSource() == submitButton) {
					submitEdit();///
				}
			} else {
				JOptionPane.showMessageDialog(null, 
						"请在上方确认有符合条件的会议室", 
						"alert", JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
	
	private class CancelEditButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == modifyButton) {
				int res = JOptionPane.showConfirmDialog(null,
						"确认要修改本次会议？", "", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					setAllPanelEnable(true);
					modifyButton.setEnabled(false);
				} else {}
			} else if (e.getSource() == cancelPreorderButton) {
				int res = JOptionPane.showConfirmDialog(null,
						"确认要取消本次会议？", "", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					cancelPreorder();
					emailEditedMeeting("cancel");
				} 
			}
		}
	}
	
	private class ConfirmButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == confirmButton) {
				clickConfirmButton();
			}
		}
	}
	
	@Override
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
	@Override
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
				if((TimeComparetor.timeOverlap(sd, ed, recordSd, recordEd)
						|| (size < inputNumber)) 
						&& !rs.getString("preorderID").equals(mpi.getPreorderID())) {
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
	private void emailEditedMeeting(String operation) {
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
			if("edit".equals(operation)) {
				MyMail.sendMail( emails, "您的最近一项会议发生更改，请重新确认相关信息", emailNewContent("会议已经修改，如下"));
			} else if("cancel".equals(operation)) {
				MyMail.sendMail( emails, "您的最近一项会议已经取消", emailNewContent("会议取消"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected String emailNewContent(String info) {
		String str = "<h2>"+ info +"</h2>"
					+ "<b>当前所选时间: " + startTime + "---" + endTime + "</br>" 
					+ "所选与会人数: " + peopleNum + "</br>"
					+ "所选设备有: " + deviceList(selectedDevices) + "</br>" 
					+ "组织者: " + organizer + "</br>"
					+ "与会人: " + staffList(selectedStaffs) + "</br>" 
					+ "记录人: " + recorderList(recorders) + "</br>"
					+ "会议室: " + roomID + "</br>";
		return str;
	} 
}
