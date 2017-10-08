package mms.common;

import java.sql.Connection;

public class ConnectionRegistrar {
	
	private Connection connection;
	private Staff staff;
	private String path;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connetcion) {
		this.connection = connetcion;
	}
	
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}	
}

