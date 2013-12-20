package com.plip.persistence.models;

// Generated Dec 20, 2013 6:23:53 PM by Hibernate Tools 4.0.0

/**
 * TrayStatusId generated by hbm2java
 */
public class TrayStatusId implements java.io.Serializable {

	private long idTray;
	private int idStatus;

	public TrayStatusId() {
	}

	public TrayStatusId(long idTray, int idStatus) {
		this.idTray = idTray;
		this.idStatus = idStatus;
	}

	public long getIdTray() {
		return this.idTray;
	}

	public void setIdTray(long idTray) {
		this.idTray = idTray;
	}

	public int getIdStatus() {
		return this.idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TrayStatusId))
			return false;
		TrayStatusId castOther = (TrayStatusId) other;

		return (this.getIdTray() == castOther.getIdTray())
				&& (this.getIdStatus() == castOther.getIdStatus());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getIdTray();
		result = 37 * result + this.getIdStatus();
		return result;
	}

}
