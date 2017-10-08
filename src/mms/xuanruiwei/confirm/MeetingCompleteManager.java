package mms.xuanruiwei.confirm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import mms.common.ConnectionRegistrar;
import mms.common.Staff;

public class MeetingCompleteManager extends Frame {

	private JButton mConfirm, mDetail;
	private JPanel mainPanel;
	private JTable mJTable;
	private JTableHeader mJTableHeader;
	private java.sql.PreparedStatement mPreparedStatement;

	private FlowLayout mFlowLayout;

	private ConnectionRegistrar cr;
	private Staff mStaff;

	private ResultSet mResultSet;
	private java.sql.ResultSetMetaData metaData;

	private Object[][] mData;
	private String[] mDataName;

	private int mRowsNum, mColsNum;
	private String sql;

	private boolean[] flag;

	private static Font font;
	private static Color color;

	private DefaultTableModel mTableModel;
	private JScrollPane mJScrollPane;

	private MeetingCompleteManager mcm = this;
	
	
	public MeetingCompleteManager(ConnectionRegistrar mConnectionRegistrar) {

		color = Color.WHITE;
		font = new Font("宋体", Font.BOLD, 15);
		mFlowLayout = new FlowLayout(FlowLayout.LEFT);

		mainPanel = new JPanel();
		mConfirm = new JButton("确认");
		mDetail = new JButton("详情");
		mJScrollPane = new JScrollPane();

		this.cr = mConnectionRegistrar;
		this.mStaff = mConnectionRegistrar.getStaff();

		init();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});
	}

	public void init() {

		this.setSize(new Dimension(600, 400)); // 设置窗口大小
		this.setLocation(new Point(500, 200)); // 设置窗口位置

		mConfirm.setPreferredSize(new Dimension(155, 30));
		mDetail.setPreferredSize(new Dimension(155, 30));

		fill();
		
		mTableModel = new DefaultTableModel(mData, mDataName);
		mJTable = new JTable(mTableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		mJTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = mJTable.getSelectedRow();
				int column = mJTable.getSelectedColumn();
				String startTime = mJTable.getValueAt(row, 2).toString();
				String endTime = mJTable.getValueAt(row, 3).toString();
				int preorderID =Integer.parseInt(mJTable.getValueAt(row, 0).toString());
				String roomID =mJTable.getValueAt(row, 1).toString();
				if (column == mColsNum) {
					new PreorderDetail(mcm,cr, startTime, endTime, preorderID, roomID).launch();
				}
				if (column == mColsNum - 1 && !flag[row]) {
					new PreorderConfirm(mcm,cr, startTime, endTime, preorderID, roomID).launch();
				}
			}
		});

		mJTable.getColumnModel().getColumn(mColsNum).setCellRenderer(new MyRender());
		mJTable.getColumnModel().getColumn(mColsNum - 1).setCellRenderer(new MyRender());

		mJTableHeader = mJTable.getTableHeader();

		mJTableHeader.setReorderingAllowed(false);
		mJTableHeader.setResizingAllowed(false);
		mJTableHeader.setPreferredSize(new Dimension(mJTableHeader.getWidth(), 25));
		mJTable.setRowHeight(25);

		mJTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		mJTable.getColumnModel().getColumn(1).setPreferredWidth(40);
		mJTable.getColumnModel().getColumn(2).setPreferredWidth(115);
		mJTable.getColumnModel().getColumn(3).setPreferredWidth(115);
		mJTable.getColumnModel().getColumn(4).setPreferredWidth(35);
		mJTable.getColumnModel().getColumn(5).setPreferredWidth(45);
		mJTable.getColumnModel().getColumn(6).setPreferredWidth(45);

		mJScrollPane.setViewportView(mJTable);

		this.add(mJScrollPane);
	}

	public void fill() {
		
		sql = "SELECT preorder.* FROM preorder,preorderrecorder WHERE preorder.endTime <= NOW() AND preorder.preorderID = preorderrecorder.preorderID AND preorderrecorder.recorder = ? ";

		int i = 0, j = 0, k = 0;

		try {
			mPreparedStatement = cr.getConnection().prepareStatement(sql);
			mPreparedStatement.setString(1, mStaff.getStaffID());
			mResultSet = mPreparedStatement.executeQuery();

			metaData = mResultSet.getMetaData();
			mColsNum = metaData.getColumnCount();
			mResultSet.last();
			mRowsNum = mResultSet.getRow();
			mResultSet.beforeFirst();

			mData = new Object[mRowsNum][mColsNum + 1];
			mDataName = new String[mColsNum + 1];
			flag = new boolean[mRowsNum];

			for (i = 0; i < mRowsNum; i++)
				flag[i] = false;

			i = 0;
			mDataName[i++] = "预订ID";
			mDataName[i++] = "会议室ID";
			mDataName[i++] = "开始时间";
			mDataName[i++] = "结束时间";
			mDataName[i++] = "组织者";
			mDataName[i++] = "提交";
			mDataName[i++] = "详情";

			j = i = k = 0;
			while (mResultSet.next()) {
				k = 0;
				for (i = 1; i <= mColsNum; i++) {
					if (metaData.getColumnName(i).equals("isConfirmed")) {
						if (mResultSet.getInt(i) == 0) {
							mData[j][mColsNum - 1] = new JButton("提交");
						}else if (mResultSet.getInt(i) == 1) {
							flag[j] = true;
							mData[j][mColsNum - 1] = "已提交";
						}
					} else {
						mData[j][k] = mResultSet.getString(i);
						k++;
					}
				}
				j++;
			}
			for (i = 1; i <= mRowsNum; i++) {
				mData[i - 1][mColsNum] = new JButton("详情");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(mPreparedStatement != null) mPreparedStatement.close();
				if(mResultSet != null) mResultSet.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void launch() {
		
		this.setVisible(true);
		
	}
	
	private class MyRender implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (column == mColsNum - 1 && !flag[row]) {
				return (JButton) value;
			}
			if (column == mColsNum) {
				return (JButton) value;
			} else {
				return new JLabel((String) value);
			}
		}
	}

	/*public static void main(String[] args) {
		String url = "jdbc:mysql://127.0.0.1/meeting_management_system?characterEncoding=utf-8";
		String name = "com.mysql.jdbc.Driver";
		String user = "root";
		String password = "";
		ConnectionRegistrar cr = null;
		Connection connection = null;
		try {
			Class.forName(name);// 指定连接类型
			connection = DriverManager.getConnection(url, user, password);// 获取连接
			cr = new ConnectionRegistrar();
			cr.setConnection(connection);// wangzhen
		} catch (Exception e) {
			e.printStackTrace();
		}
		Staff mStaff = new Staff();
		mStaff.setStaffID("0919");
		cr.setStaff(mStaff);

		new MeetingCompleteManager(cr).launch();
	}*/
}
