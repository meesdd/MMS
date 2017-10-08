package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
//显示最近的要进行的会议
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import mms.common.ConnectionRegistrar;
import mms.common.TableSorter;
public class RecentMeetingPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 49057423777671963L;
	//连结数据库的对象
	private ConnectionRegistrar cr;
	private DefaultTableModel dtm;
	private JTable rmTable;
	private JScrollPane jsc;
	private JLabel rmLabel;
	private JComboBox<String> sortCombox;
	private JPanel jp;
	private String[] head;
	private String[][] content;
	
	public RecentMeetingPanel(ConnectionRegistrar cr) {
		this.cr = cr;
		this.setPreferredSize(new Dimension(540, 150));

		
		
		rmLabel = new JLabel("将要参加的会议");
		rmLabel.setPreferredSize(new Dimension(100, 25));
		sortCombox = new JComboBox<String>();
		sortCombox.addItem("  默认排序");
		sortCombox.addItem("按开始时间排序");
		sortCombox.addItem("按结束时间排序");
		sortCombox.addItem("按会议室排序");
		sortCombox.addItem("按组织者排序");
		sortCombox.setPreferredSize(new Dimension(120, 25));
		sortCombox.addItemListener(new ComboxSortListener());
		jp = new JPanel();
		jp.setPreferredSize(new Dimension(500, 30));
		jp.add(rmLabel);
		jp.add(sortCombox);
		
		head = getHead();
		content = getContent(head);
		dtm = new DefaultTableModel(content, head);
		rmTable = new JTable(dtm){
			private static final long serialVersionUID = 3534140693928284011L;
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();     
	    r.setHorizontalAlignment(SwingConstants.CENTER); 
		rmTable.setDefaultRenderer(Object.class, r);
		JTableHeader rmTableHeader = rmTable.getTableHeader();
		rmTableHeader.setReorderingAllowed(false);
		rmTableHeader.setResizingAllowed(false);
		rmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		rmTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		rmTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		rmTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		jsc = new JScrollPane(rmTable);
		jsc.setPreferredSize(new Dimension(500, 100));
		this.add(jp, BorderLayout.NORTH);
		this.add(jsc, BorderLayout.CENTER);
	}
	
	public String[] getHead() {
		String[] temp = {"开始时间", "结束时间", "会议室", "组织者"};
		return temp;
	}
	
	public String[][] getContent(String[] head) {
		String[][] temp = null;
		List<RecentMeetingRecord> records = new ArrayList<RecentMeetingRecord>();
		String sql = "SELECT"
				+ " preorder.startTime,"
				+ " preorder.endTime,"
				+ " preorder.roomID,"
				+ " staff.staffName"
				+ " FROM"
				+ " preorder, preorderparticipant, staff"
				+ " WHERE"
				+ " preorder.preorderID = preorderparticipant.preorderID"
				+ " AND"
				+ " preorderparticipant.participant = ?"
				+ " AND staff.staffID = preorder.organizer";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, cr.getStaff().getStaffID());
			rs = ps.executeQuery();
			while(rs.next()) {
				
				RecentMeetingRecord rmr = new RecentMeetingRecord(rs.getTimestamp("startTime"), 
																	rs.getTimestamp("endTime"), 
																	rs.getString("roomID"), 
																	rs.getString("staffName"));
				records.add(rmr);
			}
			if(records.size() > 0) {
				
				int row = records.size();
				int col = head.length;
				temp = new String[row][col];
				for(int i = 0; i < row; i++) {
					temp[i][0] = records.get(i).getStartTime().toString();
					temp[i][1] = records.get(i).getEndTime().toString();
					temp[i][2] = records.get(i).getRoomID();
					temp[i][3] = records.get(i).getStaffName();
				}
			}
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
		return temp;
	}
	
	public void flushTable() {
		head = getHead();
		content = getContent(head);
		dtm.setDataVector(content, head);
	}
	
	private class RecentMeetingRecord {
		Timestamp startTime, endTime;
		String staffName, roomID;
		public RecentMeetingRecord(Timestamp startTime, Timestamp endTime, String roomID, String staffName) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.staffName = staffName;
			this.roomID = roomID;
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
		public String getStaffName() {
			return staffName;
		}
		public void setStaffName(String staffName) {
			this.staffName = staffName;
		}
		public String getRoomID() {
			return roomID;
		}
		public void setRoomID(String roomID) {
			this.roomID = roomID;
		}
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
			/*
			head = getHead();*/
			TableSorter.SortByTime(content, "nearest");
		} else if ("按结束时间排序".equals(op)) {
			/*
			head = getHead();*/
			TableSorter.SortByTime(content, "nearest");
		} else if ("按会议室排序".equals(op)) {
			/*
			head = getHead();*/
			TableSorter.SortByRoom(content);
		} else if ("按组织者排序".equals(op)) {
			/*
			head = getHead();*/
			TableSorter.SortByOrganizer(content);
			
		}
		dtm.setDataVector(content, head);
		rmTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		rmTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		rmTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		rmTable.getColumnModel().getColumn(3).setPreferredWidth(100);
	}
}
