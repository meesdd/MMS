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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;
import mms.common.Device;
import mms.common.TimeComparetor;

public class OtherDeviceSelectFrame extends JFrame {
	private ConnectionRegistrar cr;
	private MeetingPreorderFrame mpf;
	private JLabel enableTitleLabel, selectedTitleLabel;
	private DefaultTableModel dtm1, dtm2;
	private JTable jt1, jt2;
	private JScrollPane jsc1, jsc2;
	private List<Device> enableContent;
	private List<Device> selectedContent;
	private JButton addButton, 
					deleteButton,
					confirmButton;
	private JPanel jpLeft,
					jpMid,
					jpRight;
	private int width = 600;
	private int height = 400;
	private String status;
	private String preorderID;
	private Timestamp startTime, endTime;
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
				addDevice();
			} else if(e.getSource() == deleteButton) {
				deleteDevice();
			} else if(e.getSource() == confirmButton) {
				mpf.setSelectedDevice(selectedContent);
				dispose();
			}
		}
	}
	
	public OtherDeviceSelectFrame(ConnectionRegistrar cr, 
									MeetingPreorderFrame mpf, 
									String status, 
									String preorderID, 
									Timestamp startTime,
									Timestamp endTime) {
		this.cr = cr;
		this.mpf = mpf;
		this.status = status;
		this.preorderID = preorderID;
		this.startTime = startTime;
		this.endTime = endTime;
		enableContent = getEnableDevices();
		selectedContent = new ArrayList<Device>();
		
		this.setSize(new Dimension(width, height));
		this.setLocationRelativeTo(null); 
		enableTitleLabel = new JLabel("当前可选设备");
		enableTitleLabel.setPreferredSize(new Dimension(200, 25));
		
		String[] enableDeviceTableHead = getTableHead();
		String[][] enableDeviceContent = getTableContent(enableDeviceTableHead, enableContent);
		dtm1 = new DefaultTableModel(enableDeviceContent, enableDeviceTableHead);
		
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
		
		selectedTitleLabel = new JLabel("已选设备");
		selectedTitleLabel.setPreferredSize(new Dimension(200, 25));
		
		String[] selectedDeviceTableHead = getTableHead();
		String[][] selectedDeviceContent = getTableContent(selectedDeviceTableHead, selectedContent);
		dtm2 = new DefaultTableModel(selectedDeviceContent, selectedDeviceTableHead);
		
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

	public void setButtonEnable(boolean flag) {
		addButton.setEnabled(flag);
		deleteButton.setEnabled(!flag);
	}
	
	public String[] getTableHead() {
		String[] head ={"设备ID","设备类型", "设备名称"};
		return head;
	}
	
	public String[][] getTableContent(String[] head, List<Device> strs) {
		int row = strs.size();
		int col = head.length;
		String[][] content = new String[row][col];
		for(int i = 0; i < row; i++) {
			content[i][0] = strs.get(i).getDeviceID();
			content[i][1] = strs.get(i).getDeviceType();
			content[i][2] = strs.get(i).getDeviceName();
		}
		return content;
	}
	
	
	public void addDevice() {
		int row = jt1.getSelectedRow();
		Device d = new Device((String)jt1.getValueAt(row, 0),
								(String)jt1.getValueAt(row, 1), 
								(String)jt1.getValueAt(row, 2));
		Device.deleteDevice(enableContent, d.getDeviceID());
		selectedContent.add(d);
		if(enableContent.size() == 0)
			addButton.setEnabled(false);
		String[] enableDeviceTableHead = getTableHead();
		String[][] enableDeviceContent = getTableContent(enableDeviceTableHead, enableContent);
		dtm1 = new DefaultTableModel(enableDeviceContent, enableDeviceTableHead);
		jt1.setModel(dtm1);
		String[] selectedDeviceTableHead = getTableHead();
		String[][] selectedDeviceContent = getTableContent(selectedDeviceTableHead, selectedContent);
		dtm2.setDataVector(selectedDeviceContent, selectedDeviceTableHead);
	}
	public void deleteDevice() {
		int row = jt2.getSelectedRow();
		Device d = new Device((String)jt2.getValueAt(row, 0),
								(String)jt2.getValueAt(row, 1), 
								(String)jt2.getValueAt(row, 2));
		Device.deleteDevice(selectedContent, d.getDeviceID());
		enableContent.add(d);
		if(selectedContent.size() == 0)
			deleteButton.setEnabled(false);
		String[] enableDeviceTableHead = getTableHead();
		String[][] enableDeviceContent = getTableContent(enableDeviceTableHead, enableContent);
		dtm1 = new DefaultTableModel(enableDeviceContent, enableDeviceTableHead);
		jt1.setModel(dtm1);
		String[] selectedDeviceTableHead = getTableHead();
		String[][] selectedDeviceContent = getTableContent(selectedDeviceTableHead, selectedContent);
		dtm2.setDataVector(selectedDeviceContent, selectedDeviceTableHead);
	}
	public List<Device> getEnableDevices() {
		List<Device> devices  = new ArrayList<Device>();
		String sql = "SELECT deviceID, deviceType, deviceName"
				+ " FROM device"
				+ " WHERE isBreakDown = 0"
				+ " AND deviceID"
				+ " NOT IN (SELECT deviceID FROM belongtoroom)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = cr.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Device d =
				new Device(rs.getString("deviceID"), rs.getString("deviceType"), rs.getString("deviceName"));
				devices.add(d);
			}
			try {
				if (ps != null) ps.close();
				if (rs != null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String sql1 = "SELECT preorder.preorderID, preorder.startTime, preorder.endTime, preorderdevice.deviceID"
					+ " FROM preorder, preorderdevice"
					+ " WHERE preorder.preorderID = preorderdevice.preorderID";
			ps = cr.getConnection().prepareStatement(sql1);
			rs = ps.executeQuery();
			while (rs.next()) {
				Timestamp recordSt = rs.getTimestamp("startTime");
				Timestamp recordEt = rs.getTimestamp("endTime");
				if (TimeComparetor.timeOverlap(startTime, endTime, recordSt, recordEt)) {
					if("new".equals(status)) {
						Device.deleteDevice(devices, rs.getString("deviceID"));
					} else {
						if (!this.preorderID.equals(rs.getString("preorderID"))) {
							Device.deleteDevice(devices, rs.getString("deviceID"));
						}
					}
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
		return devices;
	}
	public void launch() {
		this.setVisible(true);
	}
}
