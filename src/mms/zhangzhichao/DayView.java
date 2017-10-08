package mms.zhangzhichao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class DayView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5688589667183068404L;
	JPanel jPanel_content;
	JScrollPane jScrollPaneContent;
	
	//数据库连接
	/*static final String url = "jdbc:mysql://127.0.0.1/meeting_management_system?characterEncoding=utf-8";  
	static final String name = "com.mysql.jdbc.Driver";
	static final String user = "root";
	static final String password = "zhang120";*/
		
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
		
	/*private void buildConnection(){
		try {
			Class.forName(name);//指定连接类型 
			connection = DriverManager.getConnection(url, user, password);//获取连接 
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}*/
	private int getSqlRows(String tableName){
		ResultSet resultSet = null;
		try {
			String sql = "select count(*) as rowCount from " + tableName;
			
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int rows = resultSet.getInt("rowCount");
			resultSet.close();
			preparedStatement.close();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("数据库操作失败");
			return -1;
		}
	}
	private String[] getRoomID(){
		String []roomID = null;
		ResultSet resultSet = null;
		ArrayList<String> list = new ArrayList<>();
		try{
			String sql = "select roomID from room where 1";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next() == false){
				return roomID;
			}else{
				resultSet.first();
				list.add(resultSet.getString("roomID"));
				while(resultSet.next()){
					list.add(resultSet.getString("roomID"));
				}
			}
			roomID = list.toArray(new String[list.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
			return roomID;
		}
		return roomID;
	}

	private ArrayList<ArrayList<String>> getParticipantsIDAndName(long preorderID){
		ArrayList<ArrayList<String>> participantsIDAndName = new ArrayList<ArrayList<String>>();
		ResultSet resultSet = null;
		ArrayList<String> iDList = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement("select preorderparticipant.participant,staff.staffName "
					+ "from preorderparticipant,staff "
					+ "where preorderID=? "
					+ "and preorderparticipant.participant=staff.staffID");
			preparedStatement.setLong(1, preorderID);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				iDList.add(resultSet.getString("participant"));
				nameList.add(resultSet.getString("staffName"));
			}
			participantsIDAndName.add(iDList);
			participantsIDAndName.add(nameList);
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return participantsIDAndName;
	}	
	private ArrayList<ArrayList<String>> getRecordersIDAndName(long preorderID){
		ArrayList<ArrayList<String>> recordersIDAndName = new ArrayList<ArrayList<String>>();
		ResultSet resultSet = null;
		ArrayList<String> iDList = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement("select preorderrecorder.recorder,staff.staffName "
					+ "from preorderrecorder,staff "
					+ "where preorderID=? "
					+ "and preorderrecorder.recorder=staff.staffID");
			preparedStatement.setLong(1, preorderID);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				iDList.add(resultSet.getString("recorder"));
				nameList.add(resultSet.getString("staffName"));
			}
			recordersIDAndName.add(iDList);
			recordersIDAndName.add(nameList);
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordersIDAndName;
	}	
	private ArrayList<Long> getDeviceID(long preorderID){
		ResultSet resultSet = null;
		ArrayList<Long> iDList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement("select device.deviceID "
					+ "from preorderdevice,device "
					+ "where preorderID=? "
					+ "and preorderdevice.deviceID=device.deviceID");
			preparedStatement.setLong(1, preorderID);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				iDList.add(resultSet.getLong("deviceID"));
			}
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iDList;
	}
	private ArrayList<ArrayList<String>> getDeviceNameAndType(long preorderID){
		ResultSet resultSet = null;
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		ArrayList<String> typeList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement("select device.deviceName,device.deviceType "
					+ "from preorderdevice,device "
					+ "where preorderID=? "
					+ "and preorderdevice.deviceID=device.deviceID");
			preparedStatement.setLong(1, preorderID);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				nameList.add(resultSet.getString("deviceName"));
				typeList.add(resultSet.getString("deviceType"));
			}
			list.add(nameList);
			list.add(typeList);
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private long[] changeLongToLong(Long [] l){
		if(l == null){
			return null;
		}
		long[] result = new long[l.length];
		for(int i = 0; i < l.length; i++){
			result[i] = l[i];
		}
		return result;
	}
	MySplitPane [] mySplitPanes;
	MeetingInfo meetingInfo;
	public void createFrame(Timestamp currentTime){
		
		if(currentTime == null){//设置比较时间
			currentTime = new Timestamp(System.currentTimeMillis());
		}
		
		jPanel_content = new JPanel();
		ResultSet resultSet;
		meetingInfo = new MeetingInfo();
		int rows = getSqlRows("room");
		mySplitPanes = new MySplitPane[rows];
		String[] roomID = getRoomID();
		for(int i = 0; i < roomID.length; i++){
			try {
				resultSet = null;
				String sql = "select preorderID,organizer,startTime,endTime "
						+ "from preorder "
						+ "where roomID=? "
						+ "and datediff(startTime, ?)=0";//当天会议
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, roomID[i]);
				preparedStatement.setTimestamp(2, currentTime);
				resultSet = preparedStatement.executeQuery();
				mySplitPanes[i] = new MySplitPane(roomID[i]);
				while (resultSet.next()) {
					long preorderID = resultSet.getLong("preorderID");
					//设置meetingInfo的属性
					meetingInfo.setPreorderID(preorderID);
					meetingInfo.setRoomID(roomID[i]);
					meetingInfo.setOrganizer(resultSet.getString("organizer"));
					meetingInfo.setStartTime(resultSet.getTimestamp("startTime"));
					meetingInfo.setEndTime(resultSet.getTimestamp("endTime"));
					//与会人员
					Iterator<ArrayList<String>> participantsIterator = getParticipantsIDAndName(preorderID).iterator();
					ArrayList<String> participantsIDList = participantsIterator.next();
					ArrayList<String> participantsNameList = participantsIterator.next();
					String[] participantsID = participantsIDList.toArray(new String[participantsIDList.size()]);
					String[] participantsName = participantsNameList.toArray(new String[participantsNameList.size()]);
					meetingInfo.setParticipants(participantsID);
					meetingInfo.setParticipantsName(participantsName);
					//设备
					ArrayList<Long> list = getDeviceID(preorderID);
					long [] deviceID = changeLongToLong(list.toArray(new Long[list.size()]));
					meetingInfo.setDeviceID(deviceID);
					Iterator<ArrayList<String>> deviceIterator = getDeviceNameAndType(preorderID).iterator();
					ArrayList<String> deviceNameList = deviceIterator.next();
					ArrayList<String> deviceTypeList = deviceIterator.next();
					String [] deviceName = deviceNameList.toArray(new String[deviceNameList.size()]);
					String [] deviceType = deviceTypeList.toArray(new String[deviceTypeList.size()]);
					meetingInfo.setDeviceName(deviceName);
					meetingInfo.setDeviceType(deviceType);
					//记录员
					Iterator<ArrayList<String>> recodersIterator = getRecordersIDAndName(preorderID).iterator();
					ArrayList<String> recordersIDList = recodersIterator.next();
					ArrayList<String> recordersNameList = recodersIterator.next();
					String[] recordersID = recordersIDList.toArray(new String[recordersIDList.size()]);
					String[] recordersName = recordersNameList.toArray(new String[recordersNameList.size()]);
					meetingInfo.setRecordersID(recordersID);
					meetingInfo.setRecordersName(recordersName);
					
					String [] strings = new String[5];
					strings[0] = String.valueOf(meetingInfo.getPreorderID());
					strings[1] = meetingInfo.getStartTime().toString();
					strings[2] = meetingInfo.getEndTime().toString();
					strings[3] = meetingInfo.getOrganizer();
					strings[4] = String.valueOf(participantsName.length);
					mySplitPanes[i].addLine(strings);
					jPanel_content.add(mySplitPanes[i]);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		jPanel_content.setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanel_content.setPreferredSize(new Dimension(1000,3000));//为了滚动条正常使用
		jScrollPaneContent = new JScrollPane(jPanel_content);
		jScrollPaneContent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(jScrollPaneContent);
		
		setTitle("今日会议");
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 530);
		setLocation(SwingUtil.centreContainer(getSize()));// 让窗体居中显示
		
	}

	public void launch(){
		setVisible(true);
	}
	/*public DayView() {
		buildConnection();
		createFrame(null);
	}*/
	public DayView(Connection c) {
		connection = c;
		createFrame(null);
	}
	public DayView(Connection c, Timestamp currentTime) {
		connection = c;
		createFrame(currentTime);
	}
	/*public DayView(Timestamp currentTime) {
		buildConnection();
		createFrame(currentTime);
	}*/
//	public static void main(String[] args) {
//		Timestamp currentTime = Timestamp.valueOf("2017-03-31 00:00:00");
//		DayView dayView = new DayView(currentTime);
////		DayView dayView = new DayView();
//		dayView.launch();
//	}
//	
	
	class MySplitPane extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2622208895952858390L;
		private JScrollPane jScrollPane;
		private JSplitPane jSplitPane;
		private JLabel jLabel;
		private JTextArea jTextArea;
		private JTable jTable;
		private DefaultTableModel tableModel;
		public MySplitPane() {
			jScrollPane = null;
			jSplitPane = null;
			jLabel = null;
			jTextArea = null;
			jTable = null;
			tableModel = null;
		}
		public MySplitPane(String roomID){
			new String(roomID);
			jLabel = new JLabel(roomID + ":");
			jTextArea = new JTextArea();
			jTextArea.setRows(5);
			jTextArea.setColumns(10);
			jTextArea.setLineWrap(true);//自动换行
			jTextArea.setEditable(false);
			
			Object row[][]= new Object[0][5];
			String[] tableHeadName = {"预订号","开始时间", "结束时间", "组织者", "与会人数"};  //表头
			tableModel = new DefaultTableModel();
			tableModel.setDataVector(row,tableHeadName);//行列加入
			jTable = new JTable(tableModel);
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();//设置单元格内容居中
			r.setHorizontalAlignment(SwingConstants.CENTER);   
			jTable.setDefaultRenderer(Object.class, r);
			jTable.setPreferredScrollableViewportSize(new Dimension(600, 70));
			TableColumn startTime = jTable.getColumnModel().getColumn(1);//设置列宽
			startTime.setPreferredWidth(200);
			startTime.setMaxWidth(200);
			startTime.setMinWidth(200);
			TableColumn endTime = jTable.getColumnModel().getColumn(2);//设置列宽
			endTime.setPreferredWidth(200);
			endTime.setMaxWidth(200);
			endTime.setMinWidth(200);
			jTable.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					ResultSet resultSet = null;
					int index = jTable.getSelectedRow();
					String roomID = null;
					String organizerID = null;
					String organizerName = null;
					Timestamp startTime=null;
					Timestamp endTime = null;
					long preorderID = Integer.valueOf(String.valueOf(jTable.getValueAt(index, 0)));
					resultSet = null;
					try {
						String sql = "select roomID,organizer,staff.staffName,startTime,endTime "
								+ "from preorder,staff "
								+ "where preorderID=? and organizer=staff.staffID limit 1";
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setLong(1, preorderID);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							roomID = resultSet.getString("roomID");
							organizerID = resultSet.getString("organizer");
							organizerName = resultSet.getString("staffName");
							startTime = resultSet.getTimestamp("startTime");
							endTime = resultSet.getTimestamp("endTime");
						}
						resultSet.close();
						preparedStatement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					//与会人员
					Iterator<ArrayList<String>> participantsIterator = getParticipantsIDAndName(preorderID).iterator();
					ArrayList<String> participantsIDList = participantsIterator.next();
					ArrayList<String> participantsNameList = participantsIterator.next();
					String[] participantsID = participantsIDList.toArray(new String[participantsIDList.size()]);
					String[] participantsName = participantsNameList.toArray(new String[participantsNameList.size()]);
					//记录员
					Iterator<ArrayList<String>> recordersIterator = getRecordersIDAndName(preorderID).iterator();
					ArrayList<String> recordersIDList = recordersIterator.next();
					ArrayList<String> recordersNameList = recordersIterator.next();
					String[] recordersID = recordersIDList.toArray(new String[recordersIDList.size()]);
					String[] recordersName = recordersNameList.toArray(new String[recordersNameList.size()]);
					//设备
					Iterator<ArrayList<String>> deviceIterator = getDeviceNameAndType(preorderID).iterator();
					ArrayList<String> deviceNameList = deviceIterator.next();
					ArrayList<String> devicesTypeList = deviceIterator.next();
					String [] deviceName = deviceNameList.toArray(new String[deviceNameList.size()]);
					String [] devicesType = devicesTypeList.toArray(new String[devicesTypeList.size()]);
					MeetingInfoFrame meetingInfoFrame = new MeetingInfoFrame(preorderID, roomID, organizerID, organizerName, participantsID, participantsName, recordersID, recordersName, startTime, endTime, devicesType, deviceName);
					meetingInfoFrame.launch();
				}
			});
			
			jScrollPane = new JScrollPane(jTable);
			jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jLabel, jScrollPane);
			this.add(jSplitPane);
		}
		public void setText(String s){
			jTextArea.setText(s);
		}
		public void addLine(Object[] rowData){
			tableModel.addRow(rowData);
			jTable.revalidate();
		}
	}
	
}
