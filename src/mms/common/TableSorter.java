package mms.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TableSorter {
	private static SimpleDateFormat format;
	private static Date date;

	public static void SortByTime(String[][] oldData, String cmd) {
		int row = oldData.length;
		Timestamp[] times = new Timestamp[row];
		for (int i = 0; i < row; i++) {
			times[i] = Timestamp.valueOf(oldData[i][0]);
		}
		if ("nearest".equals(cmd)) {
			for (int i = row - 1; i > 0; i--) {
				for (int j = 0; j < i; j++) {
					if (times[j].compareTo(times[j + 1]) == 1) {
						swapTime(times, j, j + 1);
						swapData(oldData, j, j + 1);
					}
				}
			}
		} else {
			for (int i = row - 1; i > 0; i--) {
				for (int j = 0; j < i; j++) {
					if (times[j].compareTo(times[j + 1]) == -1) {
						swapTime(times, j, j + 1);
						swapData(oldData, j, j + 1);
					}
				}
			}
		}
	}
	
	private static void swapOrganizer(String[] organizers, int i, int j) {
		String s = organizers[i];
		organizers[i] = organizers[j];
		organizers[j] = s;
	}
	
	private static void swapTime(Timestamp[] ts, int i, int j) {
		Timestamp t = ts[i];
		ts[i] = ts[j];
		ts[j] = t;
	}
	
	private static void swapRoom(String[] rooms, int i, int j) {
		String s = rooms[i];
		rooms[i] = rooms[j];
		rooms[j] = s;
	}
	
	private static void swapData(String[][] data, int i, int j) {
		int row = data[0].length;
		String[] ss = new String[row];
		for(int x = 0; x < row; x++) {
			ss[x] = data[i][x];
			data[i][x] = data[j][x];
			data[j][x] = ss[x];
		}
	}
	
	public static void SortByRoom(String[][] oldData) {
		int row = oldData.length;
		String[] rooms = new String[row];
		for (int i = 0; i < oldData.length; i++) {
			rooms[i] = oldData[i][2];
		}
		for (int i = row - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (rooms[j].compareTo(rooms[j + 1]) == 1) {
					swapRoom(rooms, j, j + 1);
					swapData(oldData, j, j + 1);
				}
			}
		}
	}
	
	public static void SortByOrganizer(String[][] oldData) {
		int row = oldData.length;
		String[] organizers = new String[row];
		for (int i = 0; i < oldData.length; i++) {
			organizers[i] = oldData[i][3];
		}
		for (int i = row - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (organizers[j].compareTo(organizers[j + 1]) == 1) {
					swapRoom(organizers, j, j + 1);
					swapData(oldData, j, j + 1);
				}
			}
		}
	}
}
