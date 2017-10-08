package mms.xuanruiwei;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import mms.common.ConnectionRegistrar;
import mms.common.Staff;

public class PasswordFrame extends JFrame{
	
	private JPasswordField mJPasswordField_1,
							mJPasswordField_2,
							mJPasswordField_3;
	
	private JButton mJButtonSave,mJButtonCancel;
	private JPanel mJPasswordPanel;
	private JPanel[] mRowsPanel;
	private java.sql.PreparedStatement mPreparedStatement;
	
	private FlowLayout mFlowLayout;
	
	private ConnectionRegistrar mConnectionRegistrar;
	private Staff staff;
	
	private String password_1,password_2,password_3;
	
	private static  Font font;
	private static Color color;
	
	public PasswordFrame(ConnectionRegistrar connectionRegistrar){
		
		color = Color.WHITE;
		font = new Font("宋体", Font.BOLD, 15);
		
		mRowsPanel = new JPanel[4];
		mJPasswordPanel = new JPanel(); 
		mJButtonSave = new JButton("修改密码");
		mJButtonCancel = new JButton("取消");
		this.mConnectionRegistrar = connectionRegistrar;
		this.staff = connectionRegistrar.getStaff();
		
		mFlowLayout = new FlowLayout(FlowLayout.LEFT);
		
		this.setSize(new Dimension(400, 200));	//设置窗口大小
		this.setResizable(false);				//设置不可更改窗口大小
		this.setLocation(new Point(500, 200));	//设置窗口位置
	
		mJButtonSave.setPreferredSize(new Dimension(155,30));
		mJButtonCancel.setPreferredSize(new Dimension(155, 30));
		
		mJPasswordPanel.setPreferredSize(new Dimension(350, 400));	//设置容器大小
		mJPasswordPanel.setLayout(new FlowLayout());				//设置布局格式
		mJPasswordPanel.setBackground(Color.WHITE);
		
		mRowsPanel[0] = new JPanel();
		mRowsPanel[0].setLayout(mFlowLayout);
		mRowsPanel[0].setBackground(color);
		JLabel mJLabel_1 = new JLabel("原始密码：");
		mJLabel_1.setFont(font);
		mRowsPanel[0].add(mJLabel_1);
		mJPasswordField_1 = new JPasswordField();
		mJPasswordField_1.setBackground(color);
		mJPasswordField_1.setFont(font);
		mJPasswordField_1.setColumns(25);
		mJPasswordField_1.setText("");
		mJPasswordField_1.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[0].add(mJPasswordField_1);	    
		mJPasswordPanel.add(mRowsPanel[0]);
		
		mRowsPanel[1] = new JPanel();
		mRowsPanel[1].setLayout(mFlowLayout);
		mRowsPanel[1].setBackground(color);
		JLabel mJLabel_2 = new JLabel("输入密码：");
		mJLabel_2.setFont(font);
		mRowsPanel[1].add(mJLabel_2);
		mJPasswordField_2 = new JPasswordField();
		mJPasswordField_2.setBackground(color);
		mJPasswordField_2.setFont(font);
		mJPasswordField_2.setColumns(25);
		mJPasswordField_2.setText("");
		mJPasswordField_2.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[1].add(mJPasswordField_2);	    
		mJPasswordPanel.add(mRowsPanel[1]);
			
		mRowsPanel[2] = new JPanel();
		mRowsPanel[2].setLayout(mFlowLayout);
		mRowsPanel[2].setBackground(color);
		JLabel mJLabel_3 = new JLabel("确认密码：");
		mJLabel_3.setFont(font);
		mRowsPanel[2].add(mJLabel_3);
		mJPasswordField_3 = new JPasswordField();
		mJPasswordField_3.setBackground(color);
		mJPasswordField_3.setFont(font);
		mJPasswordField_3.setColumns(25);
		mJPasswordField_3.setText("");
		mJPasswordField_3.setBorder(BorderFactory.createLineBorder(Color.gray));
		mRowsPanel[2].add(mJPasswordField_3);	    
		mJPasswordPanel.add(mRowsPanel[2]);
		
		mRowsPanel[3] = new JPanel();
		mRowsPanel[3].setLayout(mFlowLayout);
		mRowsPanel[3].setBackground(color);
		mRowsPanel[3].add(mJButtonSave);	
		mRowsPanel[3].add(mJButtonCancel);
		mJPasswordPanel.add(mRowsPanel[3]);
		
		this.setContentPane(mJPasswordPanel);
		this.setVisible(true);		
		
		this.addWindowListener(new WindowAdapter() {  
			@Override
			public void windowClosing(WindowEvent e) {  
				super.windowClosing(e);  
			}    
		});
		
		mJButtonSave.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				password_1 = new String(mJPasswordField_1.getPassword());
				password_2 = new String(mJPasswordField_2.getPassword());
				password_3 = new String(mJPasswordField_3.getPassword());
				if (!password_1.equals(staff.getPassword())) {
					JOptionPane.showMessageDialog(null, "原始密码错误", "错误", JOptionPane.ERROR_MESSAGE); 
					return ;
				}
				if (!password_2.equals(password_2)) {
					JOptionPane.showMessageDialog(null, "两次密码输入不一致", "错误", JOptionPane.ERROR_MESSAGE);
					return ;
				}
				if(password_2.equals("")){
					JOptionPane.showMessageDialog(null, "新密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);	
					return ;
				}
				else
				{
					try{
						String sql="UPDATE `meeting_management_system`.`staff` SET password = ?"
							+ " WHERE `staff`.`staffID` = '"+staff.getStaffID()+"'";
						mPreparedStatement = mConnectionRegistrar.getConnection().prepareStatement(sql);
						mPreparedStatement.setString(1,password_2);
						mPreparedStatement.execute();
						JOptionPane.showMessageDialog(null, "修改成功！", "成功", JOptionPane.WARNING_MESSAGE);	
						dispose();
					}catch(SQLException exception){
						
					}
				}
			}
	    });
		mJButtonCancel.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}    
	    });
	}
}
