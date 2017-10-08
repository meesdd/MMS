package mms.common;

import java.sql.Timestamp;

public class TimeComparetor {
	public static boolean timeOverlap(Timestamp sd, Timestamp ed, Timestamp recordSd, Timestamp recordEd) {
		if(sd.compareTo(recordEd) > 0 || ed.compareTo(recordSd) < 0)
			return false;
		return true;
	}
}
