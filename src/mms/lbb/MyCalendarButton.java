package mms.lbb;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.SQLException;

import mms.common.ConnectionRegistrar;
import mms.zhangzhichao.DayView;

public class MyCalendarButton extends JButton{
	private int countOrder=0;	//当天的预定数
	private int year,month,day;	//该按钮对应的日期
	boolean withColor=false;	//按钮是否深色
	ConnectionRegistrar cr=null;
	public MyCalendarButton(){
//		setCR();
	}
	public MyCalendarButton(ConnectionRegistrar cr){
		this.cr = cr;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2) { 
					//双击按钮查看日视图
					String nowtime=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day)+" 00:00:00";
					Timestamp currentTime = Timestamp.valueOf(nowtime);
					Connection connection = cr.getConnection();
					DayView dayView = new DayView(connection,currentTime);
					dayView.launch();
            	}
				else
					return;
			}
		});
	}
	public void setCR(ConnectionRegistrar cr){
		this.cr=cr;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(countOrder>0){
					if(e.getClickCount() == 1) { 
						//双击按钮查看日视图
						String nowtime=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day)+" 00:00:00";
						Timestamp currentTime = Timestamp.valueOf(nowtime);
						Connection connection = cr.getConnection();
						DayView dayView = new DayView(connection,currentTime);
						dayView.launch();
					}
					else
						return;
				}
				else{
					return;
				}	
			}
		});
	}
	public int getYear(){
		return year;
	}
	public int getMonth(){
		return month;
	}
	public int getDay(){
		return day;
	}
	public int getCountOrder(){
		return countOrder;
		
	}
	public void setYear(int y){
		year=y;
	}
	public void setMonth(int m){
		month=m;
	}
	public void setDay(int d){
		day=d;
	}
	public void setCountOrder(int CO){
		countOrder=CO;
		if(countOrder!=0){
			withColor=true;
			this.setBackground(Color.red);
		}
		else{
			withColor=false;
			this.setBackground(Color.GREEN);
		}	
		this.setToolTipText("日期： "+String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day)+"  "+"预定数： "+String.valueOf(countOrder));
	}
	public void settext(String text){
		this.setText(text);
		if(text!=null){
			this.setVisible(true);
			day=Integer.parseInt(text);
		}
		else{
			this.setVisible(false);
		}
	}

}
