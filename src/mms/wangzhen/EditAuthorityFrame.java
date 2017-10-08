package mms.wangzhen;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import mms.common.ConnectionRegistrar;

public class EditAuthorityFrame extends JFrame {
	private ConnectionRegistrar cr;
	private SuperAdminFrame saf;
	
	private String staffID;
	private String authority;
	private JLabel idTitle,
					nameTitle,
					authorityTitle,
					id,
					name;
	private JTextField authorityTextField;
	private JButton confirmButton;
	private class MyButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == confirmButton) {
				String newAuthority = authorityTextField.getText();
				if(newAuthority.equals(authority)) {
					JOptionPane.showMessageDialog(null, "权限没有改变", 
							"", JOptionPane.ERROR_MESSAGE);
				} else if(!isRightAuthority(newAuthority)) {
					JOptionPane.showMessageDialog(null, "权限错误\n管理员权限为1\n普通用户权限为2", 
							"", JOptionPane.ERROR_MESSAGE);
				} else {
					setAuthority(newAuthority);
					saf.flushTable();
					dispose();
				}
			}
		}	
	}
	
	public EditAuthorityFrame(ConnectionRegistrar cr, SuperAdminFrame saf,String staffID, String staffName, int authority) {	
		this.cr = cr;
		this.saf = saf;
		this.staffID = staffID;
		this.authority = ""+authority;
		this.setSize(new Dimension(300, 150));
		this.setLayout(new GridLayout(3, 3));
		this.setLocationRelativeTo(null); 
		idTitle = new JLabel("职员ID");
		nameTitle = new JLabel("职员姓名");
		authorityTitle = new JLabel("职员权限");
		id = new JLabel(staffID);
		name = new JLabel(staffName);
		authorityTextField = new JTextField("" + authority);
		confirmButton = new JButton("确认修改");
		confirmButton.addActionListener(new MyButtonActionListener());
		this.add(idTitle);
		this.add(nameTitle);
		this.add(authorityTitle);
		this.add(id);
		this.add(name);
		this.add(authorityTextField);
		this.add(new JLabel());
		this.add(confirmButton);
		this.add(new JLabel());
	}
	
	public boolean isRightAuthority(String au) {
		if(au.equals("1")||au.equals("2"))
			return true;
		return false;
	}
	
	public void setAuthority(String au) {
		String sql = "update staff set authority = ? where staffID = ?";
		try {
			PreparedStatement ps = cr.getConnection().prepareStatement(sql);
			ps.setString(1, au);
			ps.setString(2, staffID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void launch() {
		this.setVisible(true);
	}
}
