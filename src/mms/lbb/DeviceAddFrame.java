package mms.lbb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import mms.common.ConnectionRegistrar;

public class DeviceAddFrame extends JFrame{
	
	private ConnectionRegistrar cr;
	private JLabel typeTitle;	//设备类型
	private JTextField deviceNameTextField;		//设备名称
	private JLabel deviceNameTitle;		//设备名 label
	private JButton newdevicetypeButton,//添加新类型设备
					confirmButton,		//确认按钮
					cancelButton;		//取消按钮
	private JComboBox<String> deviceCombbox;	//所有设备种类 下拉框
	

	public DeviceAddFrame(ConnectionRegistrar cr){
		this.cr = cr;
		this.setTitle("添加新设备");
		//this.setSize(new Dimension(300, 300));
		this.setSize(300, 450);
		this.setLayout(null);
		
		//所有的控件
		typeTitle = new JLabel("设备类型");
		deviceCombbox = new JComboBox<String>();
		deviceNameTitle = new JLabel("设备标识");
		deviceNameTextField = new JTextField();
		newdevicetypeButton = new JButton("新类型");
		confirmButton = new JButton("添加");
		cancelButton = new JButton("退出");
		setCombbox();
		this.add(typeTitle);
		this.add(deviceCombbox);
		this.add(newdevicetypeButton);
		this.add(deviceNameTitle);
		this.add(deviceNameTextField);
		this.add(confirmButton);
		this.add(cancelButton);
		
		//控件的布局
		typeTitle.setBounds(20, 90, 70, 40);
		deviceNameTitle.setBounds(20, 170, 70, 40);
		confirmButton.setBounds(30, 300, 80, 50);
		
		deviceCombbox.setBounds(100, 90, 80, 40);
		deviceNameTextField.setBounds(100, 170, 120, 40);
		cancelButton.setBounds(130, 300, 80, 50);
		
		newdevicetypeButton.setBounds(200, 90, 80, 40);
		
		//添加新设备按钮事件
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(deviceNameTextField.getText().length()==0){
					JOptionPane.showMessageDialog(null, "请填写设备标识", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else{
					insertAdevice();
					JOptionPane.showMessageDialog(null, "设备添加成功");  
					deviceNameTextField.setText(null);
					deviceCombbox.setSelectedIndex(0);
				}
			}
		});
		
		//添加新类型按钮
		newdevicetypeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addNewType();
								
			}
		});
		
		//退出按钮
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closewin();		
			}
		});
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	public void lauch() {
		this.setVisible(true);
	}
	
	//关闭窗口
	private void closewin(){
		dispose();
	}
	
	//添加一种新类型
	private void addNewType(){
		PreparedStatement state = null;
		ResultSet result = null;
		String newType = JOptionPane.showInputDialog(null,
				"请输入要添加的新类型设备名称：\n\n","添加新类型设备",JOptionPane.PLAIN_MESSAGE);
		if(newType==null||newType.length()==0){
			
		}
		else{
			String sql = "select * from devicetypelist where deviceType = ?";	
			try {
				state = cr.getConnection().prepareStatement(sql);
				state.setString(1, newType);
				result = state.executeQuery();
				if(result.next()){
					JOptionPane.showMessageDialog(null, "这种设备已经存在，无需添加！", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else{
					sql="insert into devicetypelist (deviceType) values(?)";
					state = cr.getConnection().prepareStatement(sql);
					state.setString(1, newType);
					state.executeUpdate();
					JOptionPane.showMessageDialog(null, "新种类添加成功");
					
					//重置下拉框
					setCombbox();
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(state != null) state.close();
					if(result != null) result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//添加一件新设备
	private void insertAdevice(){
		PreparedStatement state = null;
		String devicename = deviceNameTextField.getText();
		String devicetype = (String)deviceCombbox.getSelectedItem();
		String sql = "INSERT INTO device (deviceType, deviceName) VALUES (?,?)";
		try {
			state = cr.getConnection().prepareStatement(sql);
			state.setString(1, devicetype);
			state.setString(2, devicename);
			state.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(state != null) state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//下拉框初始化
	private void setCombbox() {
		String sql = "select deviceType from devicetypelist";
		PreparedStatement state = null;
		ResultSet result = null;
		try {
			deviceCombbox.removeAllItems();
			state = cr.getConnection().prepareStatement(sql);
			result = state.executeQuery();
			while(result.next()){
				deviceCombbox.addItem(result.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(state != null) state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

}
