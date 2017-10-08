package mms.wangzhen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import mms.common.ConnectionRegistrar;
import mms.wangzhen.meetingpreorder.MeetingPreorderFrame;
import mms.xuanruiwei.confirm.MeetingCompleteManager;


public class MeetingManagementFrame extends JFrame {
	private ConnectionRegistrar cr;
	private RecentMeetingPanel rmp;
	private PreorderMeetingPanel pmp;
	private MeetingPreorderFrame mpf;
	private MeetingCompleteManager mcm;
	//private PreorderedMeetingManager pmm; 
	private JButton mofButton, mcmButton, pmmButton;
	
	private class MybuttonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == mofButton) {
				mpf = new MeetingPreorderFrame("add" , cr, rmp, pmp);
				mpf.initalContent();
				mpf.addMyComponentListener();
				mpf.launch();
			} else if(e.getSource() == mcmButton) {
				mcm = new MeetingCompleteManager(cr);
				mcm.launch();
			}
		}
	}
	
	public MeetingManagementFrame(ConnectionRegistrar cr, RecentMeetingPanel rmp, PreorderMeetingPanel pmp) {
		this.cr = cr;
		this.rmp = rmp;
		this.pmp = pmp;
		this.setSize(new Dimension(300, 150));
		this.setLocationRelativeTo(null);
		MybuttonListener mbl = new MybuttonListener();
		mofButton = new JButton("会议预约");
		mofButton.addActionListener(mbl);
		mcmButton = new JButton("会议确认");
		mcmButton.addActionListener(mbl);
		
		this.add(mofButton, BorderLayout.WEST);
		this.add(mcmButton, BorderLayout.EAST);
	}
	
	public void launch() {
		this.setVisible(true);
	}
}
