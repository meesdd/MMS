package mms.zhangzhichao;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MeetingInfoFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5794604443205797617L;
	JScrollPane jScrollPaneContent;
	JPanel jPanelContent;
	JLabel jLabelPreorderID,
		jLabelRoom,
		jLabelOrganizer,
		jLabelDevices,
		jLabelTime,
		jLabelRecorders,
		jLabelParticipants;
	JTextField jTextFieldPreorderID,
			jTextFieldRoom,
			jTextFieldOrganizer,
			jTextFieldTime;
	JTextArea	jTextAreaDevices, 
			jTextAreaParticipants,
			jTextAreaRecorders;
	private void createFrame(long preorderID, String roomID, String organizerID, String organizerName, String[] participantsID,
				String[] participantsName, String[] recordersID, String[] recordersName, Timestamp startTime,
				Timestamp endTime, String [] devicesType, String[] devicesName){
		jPanelContent = new JPanel();
		//会议号
		JPanel jPanelPreorderID = new JPanel();
		jPanelContent.add(jPanelPreorderID);
		jLabelPreorderID = new JLabel("会议号：");
		jPanelPreorderID.add(jLabelPreorderID);
		jTextFieldPreorderID = new JTextField();
		jTextFieldPreorderID.setEditable(false);
		jTextFieldPreorderID.setColumns(25);
		jTextFieldPreorderID.setText(String.valueOf(preorderID));
		jPanelPreorderID.add(jTextFieldPreorderID);
		//会议室
		JPanel jPanelRoom = new JPanel();
		jPanelContent.add(jPanelRoom);
		jLabelRoom = new JLabel("会议室：");
		jPanelRoom.add(jLabelRoom);
		jTextFieldRoom = new JTextField();
		jTextFieldRoom.setText(roomID);
		jTextFieldRoom.setEditable(false);
		jTextFieldRoom.setColumns(25);
		jPanelRoom.add(jTextFieldRoom);
		//组织者
		JPanel jPanelOrganizer = new JPanel();
		jPanelContent.add(jPanelOrganizer);
		jLabelOrganizer = new JLabel("组织者：");
		jPanelOrganizer.add(jLabelOrganizer);
		jTextFieldOrganizer = new JTextField();
		jTextFieldOrganizer.setText(organizerID + "\t" + organizerName);
		jTextFieldOrganizer.setEditable(false);
		jTextFieldOrganizer.setColumns(25);
		jPanelOrganizer.add(jTextFieldOrganizer);
		//起始时间
		JPanel jPanelTime = new JPanel();
		jPanelContent.add(jPanelTime);
		jLabelTime = new JLabel("    时间：");
		jPanelTime.add(jLabelTime);
		jTextFieldTime = new JTextField();
		jTextFieldTime.setText(startTime.toString() + "  ~  " + endTime.toString());
		jTextFieldTime.setEditable(false);
		jTextFieldTime.setColumns(25);
		jPanelTime.add(jTextFieldTime);
		//与会人员：
		JPanel jPanelParticipants = new JPanel();
		jPanelContent.add(jPanelParticipants);
		jLabelParticipants = new JLabel("与会者：");
		jPanelParticipants.add(jLabelParticipants);
		jTextAreaParticipants = new JTextArea();
		jTextAreaParticipants.setColumns(25);
		jTextAreaParticipants.setRows(10);
		jTextAreaParticipants.setText(createStaffsText(participantsID, participantsName));
		jTextAreaParticipants.setEditable(false);
		JScrollPane jScrollPaneParticipants = new JScrollPane(jTextAreaParticipants);
		jPanelParticipants.add(jScrollPaneParticipants);
		//记录员
		JPanel jPanelRecorders = new JPanel();
		jPanelContent.add(jPanelRecorders);
		jLabelRecorders = new JLabel("记录员：");
		jPanelRecorders.add(jLabelRecorders);
		jTextAreaRecorders = new JTextArea();
		jTextAreaRecorders.setColumns(25);
		jTextAreaRecorders.setRows(7);
		jTextAreaRecorders.setEditable(false);
		jTextAreaRecorders.setText(createStaffsText(recordersID, recordersName));
		JScrollPane jScrollPaneRecorders = new JScrollPane(jTextAreaRecorders);
		jPanelRecorders.add(jScrollPaneRecorders);
		//设备
		JPanel jPanelDevices = new JPanel();
		jPanelContent.add(jPanelDevices);
		jLabelDevices = new JLabel("   设备：");
		jPanelDevices.add(jLabelDevices);
		jTextAreaDevices = new JTextArea();
		jTextAreaDevices.setText(createStaffsText(devicesType, devicesName));
		jTextAreaDevices.setEditable(false);
		jTextAreaDevices.setColumns(25);
		jTextAreaDevices.setRows(4);
		JScrollPane jScrollPaneDevices = new JScrollPane(jTextAreaDevices);
		jPanelContent.add(jScrollPaneDevices);
				
		jPanelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanelContent.setPreferredSize(new Dimension(200,500));//为了滚动条正常使用
		jScrollPaneContent = new JScrollPane(jPanelContent);
		setContentPane(jScrollPaneContent);
		
		setTitle("会议信息");
		setVisible(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 400, 560);
		setLocation(SwingUtil.centreContainer(getSize()));// 让窗体居中显示
		
	}
	private String createStaffsText(String[] staffID, String[] staffName){
		StringBuffer s = new StringBuffer();
			for(int i = 0; i < staffID.length; i++){
				s.append(staffID[i] + "\t" + staffName[i] + "\n");
			}
		return new String(s);
	}
	private void createFrame(){
		jPanelContent = new JPanel();
		//会议号
		JPanel jPanelPreorderID = new JPanel();
		jPanelContent.add(jPanelPreorderID);
		jLabelPreorderID = new JLabel("会议号：");
		jPanelPreorderID.add(jLabelPreorderID);
		jTextFieldPreorderID = new JTextField();
		jTextFieldPreorderID.setEditable(false);
		jTextFieldPreorderID.setColumns(25);
		jTextFieldPreorderID.setText("1");
		jPanelPreorderID.add(jTextFieldPreorderID);
		//会议室
		JPanel jPanelRoom = new JPanel();
		jPanelContent.add(jPanelRoom);
		jLabelRoom = new JLabel("会议室：");
		jPanelRoom.add(jLabelRoom);
		jTextFieldRoom = new JTextField();
		jTextFieldRoom.setText("211");
		jTextFieldRoom.setEditable(false);
		jTextFieldRoom.setColumns(25);
		jPanelRoom.add(jTextFieldRoom);
		//组织者
		JPanel jPanelOrganizer = new JPanel();
		jPanelContent.add(jPanelOrganizer);
		jLabelOrganizer = new JLabel("组织者：");
		jPanelOrganizer.add(jLabelOrganizer);
		jTextFieldOrganizer = new JTextField();
		jTextFieldOrganizer.setText("0919" + "\t" + "轩瑞伟");
		jTextFieldOrganizer.setEditable(false);
		jTextFieldOrganizer.setColumns(25);
		jPanelOrganizer.add(jTextFieldOrganizer);
		//起始时间
		JPanel jPanelTime = new JPanel();
		jPanelContent.add(jPanelTime);
		jLabelTime = new JLabel("    时间：");
		jPanelTime.add(jLabelTime);
		jTextFieldTime = new JTextField();
		jTextFieldTime.setText("2017-03-21 23:00:00" + "  ~  " + "2017-03-21 23:40:00");
		jTextFieldTime.setEditable(false);
		jTextFieldTime.setColumns(25);
		jPanelTime.add(jTextFieldTime);
		//与会人员：
		JPanel jPanelParticipants = new JPanel();
		jPanelContent.add(jPanelParticipants);
		jLabelParticipants = new JLabel("与会者：");
		jPanelParticipants.add(jLabelParticipants);
		jTextAreaParticipants = new JTextArea();
		jTextAreaParticipants.setColumns(25);
		jTextAreaParticipants.setRows(10);
		jTextAreaParticipants.setText("0920\t吴传祥\n0921\t闵鹏程\n0921\t闵鹏程\n");
		jTextAreaParticipants.setEditable(false);
		JScrollPane jScrollPaneParticipants = new JScrollPane(jTextAreaParticipants);
		jPanelParticipants.add(jScrollPaneParticipants);
		//记录员
		JPanel jPanelRecorders = new JPanel();
		jPanelContent.add(jPanelRecorders);
		jLabelRecorders = new JLabel("记录员：");
		jPanelRecorders.add(jLabelRecorders);
		jTextAreaRecorders = new JTextArea();
		jTextAreaRecorders.setColumns(25);
		jTextAreaRecorders.setRows(7);
		jTextAreaRecorders.setEditable(false);
		jTextAreaRecorders.setText("0920\t吴传祥\n0921\t闵鹏程\n0921\t闵鹏程\n");
		JScrollPane jScrollPaneRecorders = new JScrollPane(jTextAreaRecorders);
		jPanelRecorders.add(jScrollPaneRecorders);
		//设备
		JPanel jPanelDevices = new JPanel();
		jPanelContent.add(jPanelDevices);
		jLabelDevices = new JLabel("   设备：");
		jPanelDevices.add(jLabelDevices);
		jTextAreaDevices = new JTextArea();
		jTextAreaDevices.setText("1\tc003\n2\tt001");
		jTextAreaDevices.setEditable(false);
		jTextAreaDevices.setColumns(25);
		jTextAreaDevices.setRows(4);
		JScrollPane jScrollPaneDevices = new JScrollPane(jTextAreaDevices);
		jPanelContent.add(jScrollPaneDevices);
		
		jPanelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
		jPanelContent.setPreferredSize(new Dimension(200,500));//为了滚动条正常使用
		jScrollPaneContent = new JScrollPane(jPanelContent);
		setContentPane(jScrollPaneContent);
		
		setTitle("会议信息");
		setVisible(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 400, 560);
		setLocation(SwingUtil.centreContainer(getSize()));// 让窗体居中显示
		
	}
	public void launch(){
		setVisible(true);
	}
	public MeetingInfoFrame(long preorderID, String roomID, String organizerID, String organizerName, String[] participantsID,
			String[] participantsName, String[] recordersID, String[] recordersName, Timestamp startTime,
			Timestamp endTime, String [] devicesType, String[] deviceName) {
		createFrame(preorderID, roomID, organizerID, organizerName, participantsID, participantsName, recordersID,
				recordersName, startTime, endTime, devicesType, deviceName);
	}
	public MeetingInfoFrame(){
		createFrame();
	}
	public static void main(String[] args) {
		new MeetingInfoFrame().launch();
	}

}

