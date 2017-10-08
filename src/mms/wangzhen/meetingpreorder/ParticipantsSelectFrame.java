package mms.wangzhen.meetingpreorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;
import mms.common.TimeComparetor;
import mms.common.TinyStaff;

public class ParticipantsSelectFrame extends JFrame{
	private ConnectionRegistrar cr;
	private MeetingPreorderFrame mpf;
	
	private JLabel enableTitleLabel, selectedTitleLabel;
	private DefaultTableModel dtm1, dtm2;
	private JTable jt1, jt2;
	private JScrollPane jsc1, jsc2;
	private List<TinyStaff> enableContent;
	private List<TinyStaff> selectedContent;
	private JButton addButton, 
					deleteButton,
					confirmButton;
	private JPanel jpLeft,
					jpMid,
					jpRight;
	private int width = 600;
	private int height = 400;
	private Timestamp startTime, endTime;
	private int peopleNumbers;
	private String status;
	private String currentPreorderID;
	private class TableClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getComponent() == jt1) {
				setButtonEnable(true);
			} else if(e.getComponent() == jt2){
				setButtonEnable(false);
			}
		}
	}
	
	private class MyButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == addButton) {
				if(selectedContent.size() == peopleNumbers) {
					JOptionPane.showMessageDialog(null, 
							"超过设定人数！", 
							"alert", JOptionPane.ERROR_MESSAGE);
				} else {
					addStaff();
				}
			} else if(e.getSource() == deleteButton) {
				deleteStaff();
			} else if(e.getSource() == confirmButton) {
				mpf.setSelectedStaff(selectedContent);
				dispose();
			}
		}
	}
	public ParticipantsSelectFrame(ConnectionRegistrar cr, 
										MeetingPreorderFrame mpf,
										Timestamp startTime, 
										Timestamp endTime,
										int peopleNumbers, 
										String status, 
										String currentPreorderID) {
		this.cr = cr;
		this.mpf = mpf;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.currentPreorderID = currentPreorderID;
		this.peopleNumbers = peopleNumbers;
		this.setSize(new Dimension(width, height));
		
		enableContent = getEnableStaffs(this.startTime, this.endTime);
		selectedContent = new ArrayList<TinyStaff>();
		
		this.setSize(new Dimension(width, height));
		
		enableTitleLabel = new JLabel("当前可参见职员");
		enableTitleLabel.setPreferredSize(new Dimension(200, 25));
		
		String[] enableStaffTableHead = getTableHead();
		String[][] enableStaffContent = getTableContent(enableStaffTableHead, enableContent);
		dtm1 = new DefaultTableModel(enableStaffContent, enableStaffTableHead);
		
		jt1 = new JTable(dtm1){
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		
		jt1.addMouseListener(new TableClickListener());
		jsc1 = new JScrollPane(jt1);
		jsc1.setPreferredSize(new Dimension(200, 300));
		jpLeft = new JPanel();
		jpLeft.setPreferredSize(new Dimension(200, 350));
jpLeft.setBorder(BorderFactory.createLineBorder(Color.red));
		jpLeft.add(enableTitleLabel);
		jpLeft.add(jsc1);
		
		selectedTitleLabel = new JLabel("已选职员");
		selectedTitleLabel.setPreferredSize(new Dimension(200, 25));
		
		String[] selectedStaffTableHead = getTableHead();
		String[][] selectedStaffContent = getTableContent(selectedStaffTableHead, selectedContent);
		dtm2 = new DefaultTableModel(selectedStaffContent, selectedStaffTableHead);
		
		jt2 = new JTable(dtm2){
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		jt2.addMouseListener(new TableClickListener());
		jsc2 = new JScrollPane(jt2);
		jsc2.setPreferredSize(new Dimension(200, 300));
		jpRight = new JPanel();
		jpRight.setPreferredSize(new Dimension(200, 350));
jpRight.setBorder(BorderFactory.createLineBorder(Color.red));
		jpRight.add(selectedTitleLabel);
		jpRight.add(jsc2);
		
		MyButtonListener mbl = new MyButtonListener();
		addButton = new JButton(">>");
		addButton.setEnabled(false);
		addButton.addActionListener(mbl);
		deleteButton = new JButton("<<");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(mbl);
		confirmButton = new JButton("确认");
		confirmButton.addActionListener(mbl);
		
		jpMid = new JPanel();
		jpMid.setPreferredSize(new Dimension(150, 350));
jpMid.setBorder(BorderFactory.createLineBorder(Color.blue));
		jpMid.setLayout(new GridLayout(7, 1));
		jpMid.add(new JLabel());
		jpMid.add(addButton);
		jpMid.add(new JLabel());
		jpMid.add(deleteButton);
		jpMid.add(new JLabel());
		jpMid.add(new JLabel());
		jpMid.add(confirmButton);
		
		this.add(jpLeft, BorderLayout.WEST);
		this.add(jpMid, BorderLayout.CENTER);
		this.add(jpRight, BorderLayout.EAST);
	}
	
	public String[] getTableHead() {
		String[] head ={"职员ID","职员姓名"};
		return head;
	}
	
	public String[][] getTableContent(String[] head, List<TinyStaff> strs) {
		int row = strs.size();
		int col = head.length;
		String[][] content = new String[row][col];
		for(int i = 0; i < row; i++) {
			content[i][0] = strs.get(i).getStaffID();
			content[i][1] = strs.get(i).getStaffName();
		}
		return content;
	}
	
	public List<TinyStaff> getEnableStaffs(Timestamp st, Timestamp et) {
		List<TinyStaff> temp = new ArrayList<TinyStaff>();
		String sql0 = "SELECT staffID, staffName FROM staff";
		String sql1 = "select preorderID, startTime, endTime FROM preorder";
		String sql2 = "select participant FROM preorderparticipant WHERE preorderID = ?";
		Timestamp recordSd = null;
		Timestamp recordEd = null;
		PreparedStatement ps0 = null, ps1 = null, ps2 =null;
		ResultSet rs0 = null, rs1 = null, rs2 =null;
		try {
			ps0 = cr.getConnection().prepareStatement(sql0);
			rs0 = ps0.executeQuery();
			while(rs0.next()) {
				if(!rs0.getString("staffID").equals("superadmin")) {
					TinyStaff tsf = new TinyStaff(rs0.getString("staffID"), rs0.getString("staffName"));
					temp.add(tsf);
				}
			}
			ps1 = cr.getConnection().prepareStatement(sql1);
			rs1 = ps1.executeQuery();
			while(rs1.next()) {
				recordSd = rs1.getTimestamp("startTime");
				recordEd = rs1.getTimestamp("endTime");
				if (TimeComparetor.timeOverlap(st, et, recordSd, recordEd)) {
					if ("new".equals(status)) {
						String preorderID = rs1.getString("preorderID");
						ps2 = cr.getConnection().prepareStatement(sql2);
						ps2.setString(1, preorderID);
						rs2 = ps2.executeQuery();
						while(rs2.next()) {
							TinyStaff.deleteStaff(temp, rs2.getString("participant"));
						}
					} else {
						String preorderID = rs1.getString("preorderID");
						if (!preorderID.equals(currentPreorderID)) {
							ps2 = cr.getConnection().prepareStatement(sql2);
							ps2.setString(1, preorderID);
							rs2 = ps2.executeQuery();
							while(rs2.next()) {
								TinyStaff.deleteStaff(temp, rs2.getString("participant"));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps0 != null) ps0.close();
				if (ps1 != null) ps1.close();
				if (ps2 != null) ps2.close();
				if (rs0 != null) rs0.close();
				if (rs1 != null) rs1.close();
				if (rs2 != null) rs2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}
	
	public void addStaff() {
		int row = jt1.getSelectedRow();
		TinyStaff ts = new TinyStaff((String)jt1.getValueAt(row, 0),
								(String)jt1.getValueAt(row, 1));
		TinyStaff.deleteStaff(enableContent, ts.getStaffID());
		selectedContent.add(ts);
		if(enableContent.size() == 0)
			addButton.setEnabled(false);
		String[] enableStaffTableHead = getTableHead();
		String[][] enableStaffContent = getTableContent(enableStaffTableHead, enableContent);
		dtm1 = new DefaultTableModel(enableStaffContent, enableStaffTableHead);
		jt1.setModel(dtm1);
		String[] selectedStaffTableHead = getTableHead();
		String[][] selectedStaffContent = getTableContent(selectedStaffTableHead, selectedContent);
		dtm2 = new DefaultTableModel(selectedStaffContent, selectedStaffTableHead);
		jt2.setModel(dtm2);
	}
	
	public void deleteStaff() {
		int row = jt2.getSelectedRow();
		TinyStaff ts = new TinyStaff((String)jt2.getValueAt(row, 0),
				(String)jt2.getValueAt(row, 1));
		TinyStaff.deleteStaff(selectedContent, ts.getStaffID());
		enableContent.add(ts);
		if(selectedContent.size() == 0)
			deleteButton.setEnabled(false);
		String[] enableStaffTableHead = getTableHead();
		String[][] enableStaffContent = getTableContent(enableStaffTableHead, enableContent);
		dtm1.setDataVector(enableStaffContent, enableStaffTableHead);
		
		String[] selectedStaffTableHead = getTableHead();
		String[][] selectedStaffContent = getTableContent(selectedStaffTableHead, selectedContent);
		dtm2.setDataVector(selectedStaffContent, selectedStaffTableHead);
	}
	
	public void setButtonEnable(boolean flag) {
		addButton.setEnabled(flag);
		deleteButton.setEnabled(!flag);
	}
	public void launch() {
		this.setVisible(true);
	}
}
