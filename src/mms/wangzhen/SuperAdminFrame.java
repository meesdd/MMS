package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;
import mms.common.Staff;
import mms.zhangzhichao.Register;

public class SuperAdminFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4016576369706466438L;
	private ConnectionRegistrar cr;
	private SuperAdminFrame saf = this;
	private Register rgr;
	private EditAuthorityFrame eaf;
	private JButton addButton,
					deleteButton,
					updateButton;
	private String[] head;
	private String[][] staffContent;
	private DefaultTableModel dtm;
	private JTable jt;
	private JScrollPane js;
	private JPanel jp;
	
	private int row;

	private class MyButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == addButton) {
				rgr = new Register(cr);
				rgr.launch();
			} else if(e.getSource() == deleteButton) {
				String staffID = (String)jt.getValueAt(row, 0);
				int i = JOptionPane.showConfirmDialog(null,"请问是否要删除用户：" + staffID,
															"删除提示",
															JOptionPane.YES_NO_OPTION);
				if(i == 0){
					deleteStaff(staffID);
					flushTable();
				}
			} else if(e.getSource() == updateButton) {
				String staffID = (String)jt.getValueAt(row, 0);
				String staffName = (String)jt.getValueAt(row, 1);
				int authority = Integer.parseInt((String)jt.getValueAt(row, 5));
				eaf = new EditAuthorityFrame(cr, saf, staffID, staffName, authority);
				eaf.launch();
			}
		}
	}
	public SuperAdminFrame(ConnectionRegistrar cr) {
		this.cr = cr;
		this.setSize(new Dimension(500, 500));
		this.setLocationRelativeTo(null); 
		this.setTitle("会议人员管理");
		head = getTableHead();
		staffContent = getTableContent(head, getStaffInfo());
		dtm = new DefaultTableModel(staffContent, head);
		jt = new JTable(dtm){
			private static final long serialVersionUID = 3506605703522955158L;
			@Override
			public boolean isCellEditable(int row, int column) { return false;}
		};
		
		jt.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {//仅当鼠标单击时响应
				setButtonEnabled(true);
				row = jt.getSelectedRow();
//
			}
		});
		
		js = new JScrollPane(jt);
		js.setPreferredSize(new Dimension(400, 400));
		
		jp = new JPanel(new GridLayout(1, 3));
		jp.setPreferredSize(new Dimension(400, 100));
		addButton = new JButton("添加");
		deleteButton = new JButton("删除");	
		updateButton = new JButton("修改权限");		
		
		MyButtonListener mbl = new MyButtonListener();
		addButton.addActionListener(mbl);
		deleteButton.addActionListener(mbl);
		updateButton.addActionListener(mbl);
		jp.add(addButton);
		jp.add(deleteButton);
		jp.add(updateButton);
		setButtonEnabled(false);
		
		this.add(js, BorderLayout.NORTH);
		this.add(jp, BorderLayout.CENTER);
	}
	
	public void setButtonEnabled(boolean b) {
		deleteButton.setEnabled(b);
		updateButton.setEnabled(b);
	}
	
	public List<Staff> getStaffInfo() {
		String sql = "select * from staff";
		PreparedStatement ps;
		ResultSet rs;
		List<Staff> staffs = new ArrayList<Staff>();
		try {
			ps = cr.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getString("staffID").equals(cr.getStaff().getStaffID())) {
					
				} else {
					Staff s = new Staff();
					s.setStaffID(rs.getString(1));
					s.setStaffName(rs.getString(2));
					s.setDepartment(rs.getString(3));
					s.setPhoneNumber(rs.getString(4));
					s.setEmail(rs.getString(5));
					s.setAuthority(rs.getInt(6));
					staffs.add(s);
				}
			}
			return staffs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteStaff(String staffID) {	
		try {
			String dltSql = "delete from staff where staffID = ?";
			PreparedStatement ps = cr.getConnection().prepareStatement(dltSql);
			ps.setString(1, staffID);
			if(1 == ps.executeUpdate()) {

			} else {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	
	public String[] getTableHead() {
		String[] s = {"职员ID", "姓名", "部门", "电话号码", "邮件地址", "权限"};
		return s;
	}
	
	public String[][] getTableContent(String[] tableHead,List<Staff> staffs) {
		int col = tableHead.length;
		int row = staffs.size();
		String[][] ss = new String[row][col];
		for(int i = 0; i < row; i++) {
			ss[i][0] = staffs.get(i).getStaffID();
			ss[i][1] = staffs.get(i).getStaffName();
			ss[i][2] = staffs.get(i).getDepartment();
			ss[i][3] = staffs.get(i).getPhoneNumber();
			ss[i][4] = staffs.get(i).getEmail();
			ss[i][5] = "" + staffs.get(i).getAuthority();
		}
		return ss;
	}
	
	public void flushTable() {
		staffContent = getTableContent(head, getStaffInfo());
		dtm.setDataVector(staffContent, head);
	}
	
	public void launch() {
		this.setVisible(true);
	}
}
