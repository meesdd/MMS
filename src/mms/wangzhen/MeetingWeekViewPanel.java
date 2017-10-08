package mms.wangzhen;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mms.common.ConnectionRegistrar;
//显示日视图
public class MeetingWeekViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7019072660736530844L;
	private JLabel l;
	public MeetingWeekViewPanel(ConnectionRegistrar cr) {
		l = new JLabel("weekweek");
		this.add(l);
	}
}
