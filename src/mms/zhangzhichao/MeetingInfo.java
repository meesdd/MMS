package mms.zhangzhichao;

import java.sql.Timestamp;
import java.util.Arrays;

public class MeetingInfo{
	private long preorderID;
	private String roomID;
	private Timestamp startTime;
	private Timestamp endTime;
	private String organizer;
	private String[] deviceName;
	private long [] deviceID;
	private String[] deviceType;
	private String [] participantsID;
	private String [] participantsName;
	private String [] recordersID;
	private String [] recordersName;
	private String recordPath;
	
//	public static void main(String[] args) {
//		
//	}
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append("[" + "preorderID: "+ preorderID + ",\nroomID: " + roomID + ",\norganizer: " + organizer + ",\nstartTime: " 
				+ startTime + ",\tendTime: " + endTime + ",\nrecordPath: " + recordPath + ",\n");
		if(participantsID == null){
			s.append("participants: [" + null + "],\n");
		}else{
			s.append("participants: " + Arrays.asList(participantsID) + ",\n");
		}
		if(recordersID == null){
			s.append("recordersID: [" + null + "],\n");
		}else {
			s.append("recordersID: " + Arrays.asList(recordersID) + ",\n");
		}
		if(recordersName == null){
			s.append("recordersName: [" + null + "],\n");
		}else {
			s.append("recordersName: " + Arrays.asList(recordersName) + ",\n");
		}
		s.append("deviceID: " + Arrays.asList(deviceID) + ",\n");
		if(deviceType == null){
			s.append("deviceType: [" + null + "]");
		}else{
			s.append("deviceType: " + Arrays.asList(deviceType) + "");
		}
		if(deviceName == null){
			s.append("deviceName: [" + null + "]");
		}else{
			s.append("deviceName: " + Arrays.asList(deviceName) + "");
		}
		s.append("]");
		return new String(s);
	}
	public MeetingInfo() {
		// TODO 自动生成的构造函数存根
		preorderID = 0;
		roomID = null;
		organizer = null;
		participantsID = null;
		participantsName = null;
		startTime = null;
		endTime = null;
		recordersID = null;
		recordersName = null;
		recordPath = null;
		deviceID = null;
		deviceName = null;
		deviceType = null;
	}
	public long [] getDeviceID() {
		return deviceID;
	}
	public String[] getDeviceName() {
		return deviceName;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public String getOrganizer() {
		return organizer;
	}
	public String[] getParticipantsID() {
		return participantsID;
	}
	public long getPreorderID() {
		return preorderID;
	}
	public String[] getRecordersName() {
		return recordersName;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public String getRoomID() {
		return roomID;
	}
	public String getRecordPath() {
		return recordPath;
	}
	public void setPreorderID(long l) {
		this.preorderID = l;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public void setStartTime(Timestamp time) {
		this.startTime = time;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public void setParticipants(String[] participantsID) {
		this.participantsID = participantsID;
	}
	public void setRecordersName(String[] recorders) {
		this.recordersName = recorders;
	}
	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}
	public void setDeviceID(long [] deviceID) {
		this.deviceID = deviceID;
	}
	public void setDeviceName(String[] deviceName) {
		this.deviceName = deviceName;
	}
	public String [] getParticipantsName() {
		return participantsName;
	}
	public void setParticipantsName(String [] participantsName) {
		this.participantsName = participantsName;
	}
	public String[] getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String[] deviceType) {
		this.deviceType = deviceType;
	}
	public void setParticipantsID(String[] participantsID) {
		this.participantsID = participantsID;
	}
	public String[] getRecordersID() {
		return recordersID;
	}
	public void setRecordersID(String[] recordersID) {
		this.recordersID = recordersID;
	}
}