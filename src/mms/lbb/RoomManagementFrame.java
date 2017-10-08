package mms.lbb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;

public class RoomManagementFrame extends JFrame{
	private ConnectionRegistrar cr;
	private JButton buttonAddRoom;
	private JButton buttonModifyRoom;
	private JButton buttonDeleteRoom;
	private JButton buttonFlush;
	private JTable tableRooms;
	private JScrollPane JSPRoomstable;
	
	public RoomManagementFrame(ConnectionRegistrar cr) {
		// TODO Auto-generated constructor stub
		this.cr=cr;
		this.setTitle("会议室管理");
		this.setSize(600,450);
		this.setLayout(null);
		buttonAddRoom=new JButton("新增");
		buttonModifyRoom=new JButton("修改");
		buttonDeleteRoom=new JButton("删除");
		buttonFlush=new JButton("刷新");
		tableRooms=new JTable();
		JSPRoomstable=new JScrollPane(tableRooms);
		setRoomTable();
		
		
		this.add(buttonAddRoom);
		this.add(buttonModifyRoom);
		this.add(buttonDeleteRoom);
		this.add(buttonFlush);
		this.add(JSPRoomstable);
		
		//控件布局
		buttonAddRoom.setBounds(20,25,70,30);
		buttonModifyRoom.setBounds(110,25,70,30);
		buttonDeleteRoom.setBounds(200,25,70,30);
		buttonFlush.setBounds(290,25,70,30);
		JSPRoomstable.setBounds(20,70,540,335);
		
		//控件响应
		buttonAddRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				RoomModifyFrame rmf =new RoomModifyFrame(false,null,0,cr);
				rmf.lauch();
				
			}
		});
		buttonModifyRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int indexrow=tableRooms.getSelectedRow();
				if(indexrow<0){
					JOptionPane.showMessageDialog(null, "未选中任何会议室！");
				}
				else{
					RoomModifyFrame rmf =new RoomModifyFrame(true,
							tableRooms.getValueAt(indexrow, 0).toString(),
							Integer.parseInt(tableRooms.getValueAt(indexrow, 1).toString())
							,cr);
					rmf.lauch();
				}
			}
		});
		buttonDeleteRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int indexrow=tableRooms.getSelectedRow();
				if(indexrow<0){
					JOptionPane.showMessageDialog(null, "未选中任何会议室！");
				}
				else{
					String DeleteRoomID=tableRooms.getValueAt(indexrow, 0).toString();
					if(JOptionPane.showConfirmDialog(null, "确认删除会议室 "+DeleteRoomID, "提示",JOptionPane.YES_NO_OPTION)!=0){
						
					}
					else{
						DefaultTableModel tableModel = (DefaultTableModel) tableRooms.getModel();
						tableModel.removeRow(indexrow);
						String sql="delete from room where roomID=?";
						PreparedStatement state = null;
						ResultSet result = null;
						try {
							state=cr.getConnection().prepareStatement(sql);
							state.setString(1, DeleteRoomID);
							state.executeUpdate();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				}
			}
		});
		 buttonFlush.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setRoomTable();
			}
		});
		
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			
		});
	}
	public void launch() {
		this.setVisible(true);
	}
	
	
	//填充会议室表格
	private void setRoomTable(){
		String[] tableHead={"会议室ID","会议室大小（人）","可用设备"};
		Object tabledata[][] = getDataFromRoomDB();
		//DefaultTableModel dataModel  = new DefaultTableModel(tabledata, tableHead);
		tableRooms.setModel(new DefaultTableModel(tabledata, tableHead){
			@Override
			public boolean isCellEditable(int row,int col){
				   return false;
			}
		});
		tableRooms.repaint();
	}
	private Object[][] getDataFromRoomDB(){
		Object[][] data = null;
		String sql="SELECT room.roomID,room.roomSize,device.deviceName "
				+ "FROM room LEFT JOIN belongtoroom "
				+ "ON room.roomID=belongtoroom.roomID "
				+ "LEFT JOIN device "
				+ "on device.deviceID=belongtoroom.deviceID "
				+ "ORDER BY room.roomID";
		String sqlcount="select count(*) from room";
		int roomRows=0;
		PreparedStatement state = null;
		ResultSet result = null;
		try {
			state=cr.getConnection().prepareStatement(sqlcount);
			result=state.executeQuery();
			if(result.next()){
				roomRows=result.getInt(1);
				data=new Object[roomRows][];
				for(int i=0;i<roomRows;i++){
					data[i]=new Object[3];
				}
				state=cr.getConnection().prepareStatement(sql);
				result=state.executeQuery();
				int indexrow=0;
				String resultRoomID = null;
				int resultSize;
				String resultDevices="";
				if(result.next()){
					resultRoomID=result.getString(1);
					resultSize=result.getInt(2);
					resultDevices=resultDevices+result.getString(3)+" ";
					data[indexrow][0]=resultRoomID;
					data[indexrow][1]=resultSize;
					while(result.next()){
						if(resultRoomID.equals(result.getString(1))){
							resultDevices=resultDevices+result.getString(3)+" ";
						}
						else{
							data[indexrow][2]=resultDevices;
							indexrow++;
							resultRoomID=result.getString(1);
							resultSize=result.getInt(2);
							resultDevices=""+result.getString(3)+" ";
							data[indexrow][0]=resultRoomID;
							data[indexrow][1]=resultSize;							
						}
						data[indexrow][2]=resultDevices;
					}
				}
				else{
					
				}
				
			}
			else{
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
