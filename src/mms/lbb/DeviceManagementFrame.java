package mms.lbb;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import mms.common.ConnectionRegistrar;

public class DeviceManagementFrame extends Frame {
	private ConnectionRegistrar cr;
	
	//所有的控件
	private JButton buttonAddDevice;
	private JButton buttonModifyDevice;
	private JButton buttonDeleteDevice;
	private JButton buttonflush;
	private JTable tableDevices;
	private JScrollPane JSP;
	
	
	public DeviceManagementFrame(ConnectionRegistrar cr){
		this.cr = cr;
		this.setSize(600, 400);
		this.setLayout(null);
		this.setTitle("设备管理");
		
		//所有控件
		buttonAddDevice=new JButton("添加");
		buttonModifyDevice=new JButton("修改");
		buttonDeleteDevice=new JButton("删除");
		buttonflush=new JButton("刷新");
		tableDevices=new JTable();
		JSP=new JScrollPane(tableDevices);
		
		setTable();
		tableDevices.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.add(buttonAddDevice);
		this.add(buttonModifyDevice);
		this.add(buttonDeleteDevice);
		this.add(buttonflush);
		this.add(JSP);
		
		//控件布局
		buttonAddDevice.setBounds(40, 40, 80, 30);
		buttonDeleteDevice.setBounds(140,40,80,30);
		buttonModifyDevice.setBounds(240,40,80,30);
		buttonflush.setBounds(340,40,80,30);
		JSP.setBounds(20,100,560,300);
		
		//按钮响应事件
		buttonAddDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DeviceAddFrame DAF = new DeviceAddFrame(cr);
				DAF.lauch();				
			}
		});
		buttonDeleteDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index;	//记录表格被选中的行
				index=tableDevices.getSelectedRow();
				if(index==-1){
					JOptionPane.showMessageDialog(null, "请选中需要删除的设备", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else{
					String deleteID=tableDevices.getValueAt(index, 0).toString();
					String sql="select roomID from belongtoroom where deviceID=?";
					PreparedStatement state=null;
					ResultSet result = null;
					String tips="";
					try {
						state=cr.getConnection().prepareStatement(sql);
						state.setString(1, deleteID);
						result=state.executeQuery();
						if(result.next()){
							tips="该设备正在被会议室"+result.getString(1)+"占用,";
						}						
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					if(JOptionPane.showConfirmDialog(null, tips+"确认删除设备 "+deleteID, "提示",JOptionPane.YES_NO_OPTION)!=0){
						
					}
					else{
						sql="delete from device where deviceID=?";
						state = null;
						
						try {
							state = cr.getConnection().prepareStatement(sql);
							state.setString(1, deleteID);
							state.executeUpdate();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						//表格删除行
						DefaultTableModel tableModel = (DefaultTableModel) tableDevices.getModel();
						tableModel.removeRow(index);		
						JOptionPane.showMessageDialog(null, "成功删除设备 "+deleteID, "提示",JOptionPane.WARNING_MESSAGE);
					}
					
				}
				
			}
		});
		buttonModifyDevice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int indexrow;	//记录当前选中的行
				int indexcol;	//记录当前选中的列
				indexrow=tableDevices.getSelectedRow();
				indexcol=tableDevices.getSelectedColumn();
				if(indexcol==-1||indexrow==-1){
					JOptionPane.showMessageDialog(null, "请选中需要修改的单元格", "提示",JOptionPane.WARNING_MESSAGE);
				}
				else{
					if(indexcol==0){
						JOptionPane.showMessageDialog(null, "设备ID无法修改！", "警告",JOptionPane.WARNING_MESSAGE);
					}
					else{
						switch(indexcol){
						case 1:
							modifyType(indexrow, indexcol);
							break;
						case 2:
							modifyName(indexrow, indexcol);
							break;
						case 3:
							modifyBreakdown(indexrow, indexcol);
							break;
							default:
								break;
						}
					}
				}
				
			}
		});
		buttonflush.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setTable();
				
			}
		});
		//单元格双击事件
		tableDevices.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2) { 
                    int indexrow =((JTable)e.getSource()).rowAtPoint(e.getPoint()); //获得行位置 
            		int indexcol=((JTable)e.getSource()).columnAtPoint(e.getPoint()); //获得列位置 
            		if(indexcol==0){
						JOptionPane.showMessageDialog(null, "设备ID无法修改！", "警告",JOptionPane.WARNING_MESSAGE);
					}
					else{
							switch(indexcol){
							case 1:
								modifyType(indexrow, indexcol);
								break;
							case 2:
								modifyName(indexrow, indexcol);
								break;
							case 3:
								modifyBreakdown(indexrow, indexcol);
								break;
								default:
									break;
							}
					}
            		
            	}
				else
					return;
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
	//修改设备类型字段
	void modifyType(int row,int col){
		String dtypes[] = null;
		String sql = "select deviceType from devicetypelist";
		PreparedStatement state = null;
		ResultSet result = null;
		try {
			state = cr.getConnection().prepareStatement(sql);
			result = state.executeQuery();
			result.last();
			int totaltype=result.getRow();
			result.first();
			dtypes=new String[totaltype];
			int i=0;
			if(totaltype>0){
				dtypes[i]=result.getString(1);
				i++;
			}
			while(result.next()){
				dtypes[i]=result.getString(1);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String s = (String) JOptionPane.showInputDialog(null,
				"请选择修改后的设备类型:\n",
				"修改设备类型", JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("icon.png"),
				dtypes, null);
		if(s==null){
			return;
		}
		else{
			String indexID=tableDevices.getValueAt(row, 0).toString();
			sql = "update device set deviceType=? where deviceID=?";
			try {
				state = cr.getConnection().prepareStatement(sql);
				state.setString(1, s);
				state.setString(2, indexID);
				state.executeUpdate();
				setTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	//修改设备名称字段
	void modifyName(int row,int col){
		String s=JOptionPane.showInputDialog(null,"请输入修改后的设备名：\n","修改设备名称",JOptionPane.PLAIN_MESSAGE);
		if(s==null||s.length()==0){
			
		}
		else{
			PreparedStatement state = null;
			String indexID=tableDevices.getValueAt(row, col).toString();
			String sql = "update device set deviceName=? where deviceID=?";
			try {
				state = cr.getConnection().prepareStatement(sql);
				state.setString(1, s);
				state.setString(2, indexID);
				state.executeUpdate();
				setTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	//修改是否损坏字段
	void modifyBreakdown(int row,int col){
		String options[]={"是","否"};
		boolean indexbreakdown;
		String s = (String) JOptionPane.showInputDialog(null,
				"设备是否损坏:\n",
				"修改设备损耗",
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon("icon.png"),
				options, null);
		if(s==null){
			return;
		}
		
		
		else{
			if(s.equals("是")){
				indexbreakdown=true;
			}
			else{
				indexbreakdown=false;
			}
			String indexID=tableDevices.getValueAt(row, 0).toString();
			String sql = "update device set isBreakDown=? where deviceID=?";
			try {
				PreparedStatement state = null;
				state = cr.getConnection().prepareStatement(sql);
				state.setBoolean(1, indexbreakdown);
				state.setString(2, indexID);
				state.executeUpdate();
				setTable();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//载入表格数据
	private void setTable(){
		String[] tableHead={"设备ID","设备种类","设备标识","是否损坏"};
		Object tabledata[][] = getDataFromdb();
		//DefaultTableModel dataModel  = new DefaultTableModel(tabledata, tableHead);
		tableDevices.setModel(new DefaultTableModel(tabledata, tableHead){
			@Override
			public boolean isCellEditable(int row,int col){
				   return false;
			}
		});
		tableDevices.repaint();
		
	}
	//得到数据库device表数据
	private Object[][] getDataFromdb(){
		Object[][] data = null;
		String sql = "select * from device order by deviceType";
		PreparedStatement state = null;
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
				data[i][0] = result.getString("deviceID");
				data[i][1] = result.getString("deviceType");
				data[i][2] = result.getString("deviceName");
				if(result.getBoolean("isBreakDown")){
					data[i][3]="是";
				}
				else{
					data[i][3]="否";
				}
				i++;
			}
			
			while(result.next()){
				data[i] = new Object[4];
				data[i][0] = result.getString("deviceID");
				data[i][1] = result.getString("deviceType");
				data[i][2] = result.getString("deviceName");
				if(result.getBoolean("isBreakDown")){
					data[i][3]="是";
				}
				else{
					data[i][3]="否";
				}
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}	
	public void launch() {
		this.setVisible(true);
	}

}
