package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import mms.common.ConnectionRegistrar;
import mms.common.Device;
import mms.common.MeetingPreorderInfo;
import mms.common.TableSorter;
import mms.common.TinyStaff;
import mms.lbb.MonthChart;
import mms.wangzhen.meetingpreorder.MeetingPreorderManagementFrame;

//显示最近该账户下预定的会议室
public class PreorderMeetingPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5210644095426531918L;
	// 连结数据库的对象
	private ConnectionRegistrar cr;
	private PreorderMeetingPanel pmp = this;
	private RecentMeetingPanel rmp;
	private MonthChart mc;
	private DefaultTableModel dtm;
	private JTable pmTable;
	private JScrollPane jsc;
	private JLabel pmLabel;
	private JComboBox<String> sortCombox;
	private JPanel jp;
	private String[] head;
	private String[][] content;
	private class TableClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				launchMeetingOrderFrame();
			}
		}
	}

	public PreorderMeetingPanel(ConnectionRegistrar cr, RecentMeetingPanel rmp, MonthChart mc) {
		this.cr = cr;
		this.rmp = rmp;
		this.mc = mc;
		this.setPreferredSize(new Dimension(540, 150));

		pmLabel = new JLabel("预约的会议");
		pmLabel.setPreferredSize(new Dimension(100, 25));
		sortCombox = new JComboBox<String>();
		sortCombox.addItem("  默认排序");
		sortCombox.addItem("按开始时间排序");
		sortCombox.addItem("按结束时间排序");
		sortCombox.addItem("按会议室排序");
		sortCombox.setPreferredSize(new Dimension(120, 25));
		sortCombox.addItemListener(new ComboxSortListener());
		jp = new JPanel();
		jp.setPreferredSize(new Dimension(500, 30));
		jp.add(pmLabel);
		jp.add(sortCombox);

		head = getHead();
		content = getContent(head);

		dtm = new DefaultTableModel(content, head);
		pmTable = new JTable(dtm) {
			private static final long serialVersionUID = 6244864775548941121L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		pmTable.addMouseListener(new TableClickListener());
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(SwingConstants.CENTER);
		pmTable.setDefaultRenderer(Object.class, r);
		JTableHeader pmTableHeader = pmTable.getTableHeader();
		pmTableHeader.setReorderingAllowed(false);
		pmTableHeader.setResizingAllowed(false);
		
		pmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		pmTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		pmTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		pmTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		jsc = new JScrollPane(pmTable);
		jsc.setPreferredSize(new Dimension(500, 100));
		this.add(jp, BorderLayout.NORTH);
		this.add(jsc, BorderLayout.CENTER);
	}

	public String[] getHead() {
		String[] temp = { "开始时间", "结束时间", "会议室", "预定号"};
		return temp;
	}

	public String[][] getContent(String[] head) {
		String[][] temp = null;
		List<PreorderMeetingRecord> records = new ArrayList<PreorderMeetingRecord>();
		String sql = "SELECT preorderID, startTime, endTime, roomID FROM preorder WHERE organizer = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, cr.getStaff().getStaffID());
			rs = ps.executeQuery();
			while (rs.next()) {
				PreorderMeetingRecord r = new PreorderMeetingRecord(
											rs.getString("preorderID"), rs.getTimestamp("startTime"),
											rs.getTimestamp("endTime"), rs.getString("roomID"));
				records.add(r);
			}
			if (records.size() != 0) {
				int row = records.size();
				int col = head.length;
				temp = new String[row][col];
				for (int i = 0; i < row; i++) {
					temp[i][0] = records.get(i).getStartTime().toString();
					temp[i][1] = records.get(i).getEndTime().toString();
					temp[i][2] = records.get(i).getRoomID();
					temp[i][3] = records.get(i).getPreorderID();
				}
			}
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
		return temp;
	}

	private class PreorderMeetingRecord {
		Timestamp startTime, endTime;
		String roomID, preorderID;

		public PreorderMeetingRecord(String preorderID, Timestamp startTime, Timestamp endTime, String roomID) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.roomID = roomID;
			this.preorderID = preorderID;
		}

		public Timestamp getStartTime() {
			return startTime;
		}

		public void setStartTime(Timestamp startTime) {
			this.startTime = startTime;
		}

		public Timestamp getEndTime() {
			return endTime;
		}

		public void setEndTime(Timestamp endTime) {
			this.endTime = endTime;
		}

		public String getRoomID() {
			return roomID;
		}

		public void setRoomID(String roomID) {
			this.roomID = roomID;
		}
		public String getPreorderID() {
			return preorderID;
		}
		public void setPreorderID(String preorderID) {
			this.preorderID = preorderID;
		}
	}

	public void flushTable() {
		head = getHead();
		content = getContent(head);
		dtm.setDataVector(content, head);
	}
	
	public void launchMeetingOrderFrame() {
		int row = pmTable.getSelectedRow();
		String preorderID = (String) pmTable.getValueAt(row, 3);
		MeetingPreorderInfo mpi = getMeetingPreorderInfo(preorderID);
		
		MeetingPreorderManagementFrame mpmf = 
		new MeetingPreorderManagementFrame("edit", cr, rmp, pmp, mc, mpi);
		mpmf.initalContentAgain();
		mpmf.addMyComponentListenerForEdit();
		mpmf.launch();
	}
	
	public MeetingPreorderInfo getMeetingPreorderInfo(String preorderID) {
		
		MeetingPreorderInfo mpi = new MeetingPreorderInfo();
		String sql = "SELECT * FROM preorder WHERE preorderID = " + preorderID;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = cr.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				mpi.setPreorderID(preorderID);
				mpi.setRoomID(rs.getString("roomID"));
				mpi.setStartTime(rs.getTimestamp("startTime"));
				mpi.setEndTime(rs.getTimestamp("endTime"));
				mpi.setOrganizer(rs.getString("organizer"));
			}
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String sql2 = "SELECT"
					+ " deviceID,"
					+ " deviceType,"
					+ " deviceName"
					+ " FROM"
					+ " device"
					+ " WHERE"
					+ " deviceID ="
					+ " (SELECT deviceID FROM preorderdevice WHERE preorderID = " + preorderID + ")";
			ps = cr.getConnection().prepareStatement(sql2);
			rs = ps.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while(rs.next()) {
				Device d = new Device(rs.getString("deviceID"), 
										rs.getString("deviceType"), 
										rs.getString("deviceName"));
				devices.add(d);
			}
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String sql3 = "select staff.staffID,"
					+ " staff.staffName, staff.email"
					+ " from"
					+ " staff, preorderparticipant"
					+ " WHERE"
					+ " staff.staffID = preorderparticipant.participant"
					+ " AND preorderID = " + preorderID;
			
			ps = cr.getConnection().prepareStatement(sql3);
			rs = ps.executeQuery();
			List<TinyStaff> staffs = new ArrayList<TinyStaff>();
			while(rs.next()) {
				TinyStaff ts = new TinyStaff(rs.getString("StaffID"), rs.getString("staffName"));
				staffs.add(ts);
			}
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String sql4 = "SELECT staff.staffID"
					+ " FROM staff, preorderrecorder"
					+ " WHERE staff.staffID = preorderrecorder.recorder"
					+ " AND preorderID = " + preorderID;
			
			ps = cr.getConnection().prepareStatement(sql4);
			rs = ps.executeQuery();
			List<String> recorders = new ArrayList<String>();
			while (rs.next()) {
				String r = rs.getString("staffID");
				recorders.add(r);
			}
			mpi.setDevices(devices);
			mpi.setParticipants(staffs);
			mpi.setRecorders(recorders);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mpi;
	}
	
	protected class ComboxSortListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				String sl  = (String) sortCombox.getSelectedItem();
				sortTable(sl);
			}
		}
	}
	
	public void sortTable(String op) {
		if ("按开始时间排序".equals(op)) {
			TableSorter.SortByTime(content, "nearest");
		} else if ("按结束时间排序".equals(op)) {
			TableSorter.SortByTime(content, "nearest");
		} else if ("按会议室排序".equals(op)) {
			TableSorter.SortByRoom(content);
		}
		dtm.setDataVector(content, head);
		pmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		pmTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		pmTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		pmTable.getColumnModel().getColumn(3).setPreferredWidth(100);
	}
}
