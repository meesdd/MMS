package mms.common;

import java.util.List;

public class TinyStaff {
	private String staffID;
	private String staffName;
	public TinyStaff(String staffID, String staffName) {
		this.staffID = staffID;
		this.staffName = staffName;
	}
	public String getStaffID() {
		return staffID;
	}
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public static boolean deleteStaff(List<TinyStaff> staffs, String staffID) {
		int size = staffs.size();
		for(int i = 0; i < size; i++) {
			if(staffs.get(i).getStaffID().equals(staffID)) {
				staffs.remove(i);
				return true;
			}
		}
		return false;
	}
}
