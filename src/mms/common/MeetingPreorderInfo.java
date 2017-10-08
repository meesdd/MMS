package mms.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MeetingPreorderInfo {
	private String preorderID, roomID, organizer;
	private Timestamp startTime, endTime;
	private List<Device> devices;
	private List<TinyStaff> participants;
	private List<String> recorders;
	public MeetingPreorderInfo(String preorderID, 
								String roomID, 
								Timestamp startTime,
								Timestamp endTime, 
								String organizer, 
								List<Device> devices,
								List<TinyStaff> participants, 
								List<String> recorders) {
		this.preorderID = preorderID;
		this.roomID = roomID;
		this.organizer = organizer;
		this.startTime = startTime;
		this.endTime = endTime;
		this.devices = devices;
		this.participants = participants;
		this.recorders = recorders;
	}
	public MeetingPreorderInfo() {
		devices = new ArrayList<Device>();
		participants = new ArrayList<TinyStaff>();
		recorders = new ArrayList<String>();
	}
	
	public String getPreorderID() {
		return preorderID;
	}
	public void setPreorderID(String preorderID) {
		this.preorderID = preorderID;
	}
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	public List<TinyStaff> getParticipants() {
		return participants;
	}
	public void setParticipants(List<TinyStaff> participants) {
		this.participants = participants;
	}
	public List<String> getRecorders() {
		return recorders;
	}
	public void setRecorders(List<String> recorders) {
		this.recorders = recorders;
	}
}
