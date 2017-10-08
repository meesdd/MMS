package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mms.common.ConnectionRegistrar;
import mms.lbb.MonthChart;

public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -474861354481692598L;

	private ConnectionRegistrar cr;
	
	private StaffPanel stfPanel;//职员面板
	private RecentMeetingPanel rmPanel;//最近参加会议
	private PreorderMeetingPanel pmPanel;//本人预定的会议
	private MonthChart mc;
	private JPanel jp;
	
	public MainFrame(ConnectionRegistrar cr) {
		
		this.cr = cr;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setSize(new Dimension(800, 650));
		this.setResizable(false);	
		this.setTitle("会议管理系统1.0");
		this.setLocationRelativeTo(null); 
		mc = new MonthChart(cr);
		mc.setBorder(BorderFactory.createLineBorder(Color.red));
		rmPanel = new RecentMeetingPanel(this.cr);
		rmPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		pmPanel = new PreorderMeetingPanel(this.cr, rmPanel, mc);	
		pmPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		stfPanel = new StaffPanel(this.cr, rmPanel, pmPanel);
		stfPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		
		
		jp = new JPanel();
		jp.setPreferredSize(new Dimension(550, 300));
		jp.setBorder(BorderFactory.createLineBorder(Color.red));
		
		jp.add(rmPanel);
		jp.add(pmPanel);
		jp.add(mc);
		
		this.add(stfPanel, BorderLayout.WEST);
		this.add(jp, BorderLayout.EAST);
	}
	public void launch() {
		this.setVisible(true);
	}
	
}
