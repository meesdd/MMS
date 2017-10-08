package mms.lbb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;

public class RoomModifyFrame extends JFrame{
	private JLabel labelRoomID;
	private JLabel labelRoomSize;
	private JLabel labelSelectDevice;
	private JTextField textFieldRoomID;
	private JTextField textFieldRoomSize;
	private JTable tableAvaiableDevice;
	private JTable tableSelectedDevice;
	private JButton buttonAddDevice;
	private JButton buttonDeleteDevice;
	private JButton buttonSave;
	private JButton buttonCancel;
	private JScrollPane JSPTAD;	//用于放置table
	private JScrollPane JSPTSD;	//用于放置table
	private ConnectionRegistrar cr;
	private boolean inModifyModel; 	//是否是修改模式
	private String modifyroomID;	//修改模式下会议室ID
	private HashMap<String, Integer> hashMapdevices;
	
	public RoomModifyFrame(boolean imm,String roomid,int roomsize, ConnectionRegistrar cr){
		this.setSize(400,600);
		this.setLayout(null);
		this.cr=cr;
		this.modifyroomID=roomid;
		this.inModifyModel=imm;
		labelRoomID=new JLabel("会议室ID：");
		labelRoomSize=new JLabel("会议室大小(单位‘人’)：");
		labelSelectDevice=new JLabel("固有设备:");
		if(inModifyModel){
			this.setTitle("会议室信息修改");
			textFieldRoomID=new JTextField(roomid);
			textFieldRoomSize=new JTextField(String.valueOf(roomsize));
		}
		else{
			this.setTitle("会议室新增");
			textFieldRoomID=new JTextField();
			textFieldRoomSize=new JTextField();
		}
		
		tableAvaiableDevice=new JTable();
		tableSelectedDevice=new JTable();
		buttonAddDevice=new JButton("添加");
		buttonDeleteDevice=new JButton("删除");
		buttonSave=new JButton("保存");
		buttonCancel=new JButton("取消");
		JSPTAD=new JScrollPane(tableAvaiableDevice);
		JSPTSD=new JScrollPane(tableSelectedDevice);
		hashMapdevices=new HashMap<String,Integer>();
		
		//读取所有设备ID和Name
		getAlldevices();
		//限制textFieldRoomSize框只能输入数字，限制长度
		
		this.add(labelRoomID);
		this.add(labelRoomSize);
		this.add(labelSelectDevice);
		this.add(textFieldRoomID);
		this.add(textFieldRoomSize);
		this.add(JSPTAD);
		this.add(JSPTSD);
		this.add(buttonAddDevice);
		this.add(buttonDeleteDevice);
		this.add(buttonSave);
		this.add(buttonCancel);
		
		//表格数据初始化
		setTableAvailable();
		setTableSelected();
		//控件布局
		labelRoomID.setBounds(50,100,80,35);
		labelRoomSize.setBounds(50,160,135,35);
		labelSelectDevice.setBounds(50,210,80,35);
		textFieldRoomID.setBounds(210, 100, 80, 35);
		textFieldRoomSize.setBounds(210, 160, 80, 35);
		JSPTAD.setBounds(50,260,80,220);
		JSPTSD.setBounds(220,260,80,220);
		buttonAddDevice.setBounds(140,320,60,35);
		buttonDeleteDevice.setBounds(140,360,60,35);
		buttonSave.setBounds(60,510,60,35);
		buttonCancel.setBounds(220,510,60,35);
		
		//控件响应
		//将选中的可选设备添加到已选之中
		buttonAddDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int availabelIndexrow;		//当前可选设备所选的行
				String deviceName;
				availabelIndexrow=tableAvaiableDevice.getSelectedRow();
				if(availabelIndexrow==-1){
					
				}
				else{
					deviceName=tableAvaiableDevice.getValueAt(availabelIndexrow, 0).toString();
					DefaultTableModel tableModel = (DefaultTableModel) tableSelectedDevice.getModel();
					tableModel.addRow(new Object[]{deviceName});
					tableModel = (DefaultTableModel) tableAvaiableDevice.getModel();
					tableModel.removeRow(availabelIndexrow);// rowIndex是要删除的行序号
					
				}
			}
		});
		//将选中的已选删除到可选设备中
		buttonDeleteDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectIndexrow;		//当前可选设备所选的行
				String deviceName;
				selectIndexrow=tableSelectedDevice.getSelectedRow();
				if(selectIndexrow==-1){
					
				}
				else{
					deviceName=tableSelectedDevice.getValueAt(selectIndexrow, 0).toString();
					DefaultTableModel tableModel = (DefaultTableModel) tableAvaiableDevice.getModel();
					tableModel.addRow(new Object[]{deviceName});
					tableModel = (DefaultTableModel) tableSelectedDevice.getModel();
					tableModel.removeRow(selectIndexrow);
					
				}
			}
		});
		//单元格双击添加删除
		tableAvaiableDevice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2) { 
					int availabelIndexrow;		//当前可选设备所选的行
					String deviceName;
					availabelIndexrow=tableAvaiableDevice.getSelectedRow();
					if(availabelIndexrow==-1){
						
					}
					else{
						deviceName=tableAvaiableDevice.getValueAt(availabelIndexrow, 0).toString();
						DefaultTableModel tableModel = (DefaultTableModel) tableSelectedDevice.getModel();
						tableModel.addRow(new Object[]{deviceName});
						tableModel = (DefaultTableModel) tableAvaiableDevice.getModel();
						tableModel.removeRow(availabelIndexrow);// rowIndex是要删除的行序号
						
					}
            	}
				else
					return;
			}
		});
		tableSelectedDevice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2) { 
					int selectIndexrow;		//当前可选设备所选的行
					String deviceName;
					selectIndexrow=tableSelectedDevice.getSelectedRow();
					if(selectIndexrow==-1){
						
					}
					else{
						deviceName=tableSelectedDevice.getValueAt(selectIndexrow, 0).toString();
						DefaultTableModel tableModel = (DefaultTableModel) tableAvaiableDevice.getModel();
						tableModel.addRow(new Object[]{deviceName});
						tableModel = (DefaultTableModel) tableSelectedDevice.getModel();
						tableModel.removeRow(selectIndexrow);// rowIndex是要删除的行序号
						
					}
            	}
				else
					return;
			}
		});
		//保存按钮上传要保存的数据
		buttonSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//判断roomID和size
				
				if(textFieldRoomID.getText().length()==0||textFieldRoomSize.getText().length()==0){
					//提示错误
					JOptionPane.showMessageDialog(null, "会议室ID和会议室大小信息未完善！", "输入错误",JOptionPane.ERROR_MESSAGE);
				}
				else{
					String presentRoomID=textFieldRoomID.getText();
					int presentRoomSize=Integer.parseInt(textFieldRoomSize.getText());
					if(presentRoomSize<=0){
						JOptionPane.showMessageDialog(null, "会议室大小不合法！", "输入错误",JOptionPane.ERROR_MESSAGE);
					}
					else{
						PreparedStatement state = null;
						ResultSet result = null;
						String sql=null;
						if(inModifyModel){
							//先更新room表中当前roomID的id和size
							sql="UPDATE room SET roomID=?,roomSize=? WHERE roomID=?";
							try {
								state=cr.getConnection().prepareStatement(sql);
								state.setString(1, presentRoomID);
								state.setInt(2, presentRoomSize);
								state.setString(3, roomid);
								state.executeUpdate();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//删除belongtoroom表中当前roomID的所有行
							sql="DELETE FROM belongtoroom WHERE roomID=?";
							try {
								state=cr.getConnection().prepareStatement(sql);
								state.setString(1, presentRoomID);
								state.executeUpdate();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							//将已选的设备插入到belongtoroom表中
							int selecttotalrows=tableSelectedDevice.getRowCount();
							if(selecttotalrows>0){
								
								try {
									sql="INSERT INTO belongtoroom VALUES ";
									for(int i=0;i<selecttotalrows-1;i++){
										sql=sql+"(?,?),";
									}
									sql=sql+"(?,?)";
									state=cr.getConnection().prepareStatement(sql);
									for(int i=0;i<selecttotalrows;i++){
										state.setString(2*(i+1)-1, presentRoomID);
										state.setInt(2*(i+1), hashMapdevices.get(tableSelectedDevice.getValueAt(i, 0).toString()));
									}
									state.executeUpdate();
									 
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								
							}
							else{
								
							}
							JOptionPane.showMessageDialog(null, "更新成功！");
							dispose();
						}
						else{
							//判断roomID是否已存在
							sql="select * from room where roomID=?";
							try {
								state=cr.getConnection().prepareStatement(sql);
								state.setString(1, presentRoomID);
								result=state.executeQuery();
								if(result.next()){
									//提示已存在
									JOptionPane.showMessageDialog(null, presentRoomID+" 会议室信息已存在！", "错误",JOptionPane.ERROR_MESSAGE); 
								}
								else{
									//向room表插入新的会议室的信息
									sql="INSERT INTO room "
											+ "VALUES (?,?)";
									state=cr.getConnection().prepareStatement(sql);
									state.setString(1, presentRoomID);
									state.setInt(2, presentRoomSize);
									state.executeUpdate();
									//向belongtoroom表插入已选的设备
									int selecttotalrows=tableSelectedDevice.getRowCount();
									if(selecttotalrows>0){
										sql="INSERT INTO belongtoroom VALUES ";
										for(int i=0;i<selecttotalrows-1;i++){
											sql=sql+"(?,?),";
										}
										sql=sql+"(?,?)";
										state=cr.getConnection().prepareStatement(sql);
										for(int i=0;i<selecttotalrows;i++){
											state.setString(2*(i+1)-1, presentRoomID);
											state.setInt(2*(i+1), hashMapdevices.get(tableSelectedDevice.getValueAt(i, 0).toString()));
										}
										state.executeUpdate();
									}
									else{
										
									}
									JOptionPane.showMessageDialog(null, "保存成功！"); 
									dispose();
									
									
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					
				}
				
				
				
			}
		});
		//退出按钮
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				
			}
		});	
		//窗口关闭
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			
		});	
																																												
																																																			
		
	}
	
	//成员函数
	//初始化可选表
	private void setTableAvailable(){
		String[] tableHead={"可选设备"};
		Object tabledata[][] = getDataFromdb();
		tableAvaiableDevice.setModel(new DefaultTableModel(tabledata, tableHead){
			@Override
			public boolean isCellEditable(int row,int col){
				   return false;
			}
		});
		tableAvaiableDevice.repaint();
	}
	//初始化已选表
	private void setTableSelected(){

		String[] tableHead={"已选设备"};
		if(inModifyModel){
			Object tabledata[][] = getHoldDevice();
			tableSelectedDevice.setModel(new DefaultTableModel(tabledata, tableHead){
				@Override
				public boolean isCellEditable(int row,int col){
					   return false;
				}
			});
			tableAvaiableDevice.repaint();
		}
		else{
			tableSelectedDevice.setModel(new DefaultTableModel(null,tableHead){
				@Override
				public boolean isCellEditable(int row,int col){
					   return false;
				}
			});
		}
		tableSelectedDevice.repaint();
		
	}
	//从数据库获取可添加设备
	private Object[][] getDataFromdb(){
		Object[][] data=null;
		String sql="select deviceName from device where deviceID"
				+ " not IN "
				+ "(select device.deviceID from device,belongtoroom where device.deviceID=belongtoroom.deviceID)";
		PreparedStatement state=null;
		ResultSet result = null;
		try {
			state = cr.getConnection().prepareStatement(sql);
			result = state.executeQuery();
			result.last();
			int rows = result.getRow();		//获取总行数
			result.first();
			//result = state.executeQuery();
			data = new Object[rows][];
			int i=0;
			if(rows>0){
				data[i] = new Object[4];
				data[i][0] = result.getString("deviceName");
				i++;
			}
			
			while(result.next()){
				data[i] = new Object[4];
				data[i][0] = result.getString("deviceName");
				i++;
		} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	//从数据库获取会议室已有的设备
	private Object[][] getHoldDevice(){
		Object[][] data=null;
		String sql="select deviceName from device,belongtoroom "
				+ "where device.deviceID=belongtoroom.DeviceID and roomID=?";
		PreparedStatement state=null;
		ResultSet result = null;
		try {
			state = cr.getConnection().prepareStatement(sql);
			state.setString(1, modifyroomID);
			result = state.executeQuery();
			result.last();
			int rows = result.getRow();		//获取总行数
			result.first();
			//result = state.executeQuery();
			data = new Object[rows][];
			int i=0;
			if(rows>0){
				data[i] = new Object[4];
				data[i][0] = result.getString("deviceName");
				i++;
			}
			
			while(result.next()){
				data[i] = new Object[4];
				data[i][0] = result.getString("deviceName");
				i++;
		} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	//读取数据库device表中的内容存入到hashmap中
	private void getAlldevices(){
		String sql="select deviceID,deviceName from device";
		PreparedStatement state=null;
		ResultSet result = null;
		try {
			state=cr.getConnection().prepareStatement(sql);
			result=state.executeQuery();
			while (result.next()) {
				hashMapdevices.put(result.getString(2), result.getInt(1));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void lauch() {
		this.setVisible(true);
	}
	

}
