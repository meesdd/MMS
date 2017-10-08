package mms.wangzhen;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mms.common.ConnectionRegistrar;
//显示日视图
public class MeetingDayViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 739066696733315739L;
	private JLabel l;
	public MeetingDayViewPanel(ConnectionRegistrar cr) {
		l = new JLabel("daydayday");
		this.add(l);
	}
}
