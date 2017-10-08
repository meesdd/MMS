package mms.common;

import java.util.List;

public class Device {
	String deviceID, deviceType, deviceName;
	public Device(String deviceID, String deviceType, String deviceName) {
		this.deviceID = deviceID;
		this.deviceType = deviceType;
		this.deviceName = deviceName;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public static boolean deleteDevice(List<Device> devices, String deviceID) {
		int size = devices.size();
		for(int i = 0; i < size; i++) {
			if(devices.get(i).getDeviceID().equals(deviceID)) {
				devices.remove(i);
				return true;
			}
		}
		return false;
	}
}