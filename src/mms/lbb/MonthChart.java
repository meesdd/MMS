package mms.lbb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import mms.common.ConnectionRegistrar;

public class MonthChart extends JPanel{
	ConnectionRegistrar cr;
	int year,nowYear;
	int month,nowMonth;
	JButton buttonLastMonth;	//上个月按钮
	JButton buttonNextMonth;	//下个月按钮
	MyCalendarButton mycbutton[];	//42个日期按钮
	JLabel labelTitles[];	//7个周几label
	JLabel labelpresentMonth;	//当前页面显示的月份
	JPanel jpanelTop;
	JPanel jpanelCalendar;
	
	
	public MonthChart(ConnectionRegistrar cr) {
		// TODO Auto-generated constructor stub
		this.cr=cr;
		this.setPreferredSize(new Dimension(450, 250));
		Calendar now = Calendar.getInstance(); 
		
		nowYear=now.get(Calendar.YEAR);
		year=nowYear;
		nowMonth=now.get(Calendar.MONTH)+1;
		month=nowMonth;
		buttonLastMonth=new JButton("上个月");
		buttonLastMonth.setEnabled(false);
		buttonNextMonth=new JButton("下个月");
		labelpresentMonth=new JLabel("月视图："+String.valueOf(year)+"年"+String .valueOf(month)+"月");
		jpanelTop=new JPanel(new FlowLayout());
		jpanelCalendar=new JPanel(new GridLayout(7, 7));
		labelTitles=new JLabel[7];
		String stringTital[]={"日","一","二","三","四","五","六"};
		for(int i=0;i<7;i++){
			labelTitles[i]=new JLabel(stringTital[i],SwingConstants.CENTER);
			jpanelCalendar.add(labelTitles[i]);
		}
		
		mycbutton=new MyCalendarButton[42];
		for(int i=0;i<42;i++){
			mycbutton[i]=new MyCalendarButton();
			mycbutton[i].setCR(cr);
			jpanelCalendar.add(mycbutton[i]);
		}
		jpanelTop.add(labelpresentMonth);
		jpanelTop.add(buttonLastMonth);
		jpanelTop.add(buttonNextMonth);
		//JScrollPane jsp=new JScrollPane();
		//jsp.add(jpanelCalendar);
		this.setLayout(new BorderLayout());

		this.add(jpanelTop,BorderLayout.NORTH);
		this.add(jpanelCalendar,BorderLayout.CENTER);
		
		//数据载入
		flushMycButtons(year, month);
		
		buttonLastMonth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lastMonth();
				labelpresentMonth.setText("月视图："+String.valueOf(year)+"年"+String .valueOf(month)+"月");
				flushMycButtons(year, month);
				if(year==nowYear&&month==nowMonth){
					buttonLastMonth.setEnabled(false);
				}
				else{
					
				}
			}
		});
		buttonNextMonth.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				nextMonth();
				labelpresentMonth.setText("月视图："+String.valueOf(year)+"年"+String .valueOf(month)+"月");
				flushMycButtons(year, month);
				if(!buttonLastMonth.isEnabled()){
					buttonLastMonth.setEnabled(true);
				}
				else{
					
				}
			}
		});
		
		
	}
	
	public void flushDatePanel() {
		flushMycButtons(nowYear, nowMonth);
	}
	
	private void flushMycButtons(int y,int m){
		String sql="SELECT DATE_FORMAT(startTime,?),count(*) "
				+ "FROM preorder "
				+ "WHERE DATE_FORMAT(startTime,?)=? "
				+ "GROUP by DATE_FORMAT(startTime,?) "
				+ "ORDER BY DATE_FORMAT(startTime,?)";
		PreparedStatement state=null;
		ResultSet result = null;
		try {
			state=cr.getConnection().prepareStatement(sql);
			state.setString(1, "%e");
			state.setString(2, "%Y%c");
			state.setString(3, String.valueOf(year)+String.valueOf(month));
			state.setString(4, "%Y %c %e");
			state.setString(5, "%e");
			result=state.executeQuery();
			HashMap<String, Integer> countMap=new HashMap<String,Integer>();
			while(result.next()){
				countMap.put(result.getString(1), result.getInt(2));
			}
			CalendarBean cBean=new CalendarBean(y,m);
			String theDate[]=cBean.getCalendar();
			for(int i=0;i<42;i++){
				mycbutton[i].setYear(y);
				mycbutton[i].setMonth(m);
				mycbutton[i].settext(theDate[i]);
				mycbutton[i].setCountOrder(0);
				if(countMap.containsKey(theDate[i])){
					mycbutton[i].setCountOrder(countMap.get(theDate[i]));
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void lastMonth(){
		if(month==1){
			month=12;
			year=year-1;
		}
		else{
			month=month-1;
		}
	}
	private void nextMonth(){
		if(month==12){
			month=1;
			year=year+1;
		}
		else{
			month=month+1;
		}
	}
	
}
