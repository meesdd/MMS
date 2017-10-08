package mms.wangzhen;

import javax.swing.JLabel;
import javax.swing.JPanel;
import mms.common.ConnectionRegistrar;
//显示日视图
public class MeetingMonthViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8703300075139254766L;
	private JLabel l;
	public MeetingMonthViewPanel(ConnectionRegistrar cr) {
		l = new JLabel("month");
		this.add(l);
	}
}
