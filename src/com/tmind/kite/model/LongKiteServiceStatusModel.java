package com.tmind.kite.model;

public class LongKiteServiceStatusModel {

	private String status;
	private String serviceStatus;
	private String touchFreq;
	private String aim;
	private String purpose;
	private String estime_time;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	
	public String getAim() {
		return aim;
	}
	public void setAim(String aim) {
		this.aim = aim;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getEstime_time() {
		return estime_time;
	}
	public void setEstime_time(String estime_time) {
		this.estime_time = estime_time;
	}
	
	
	public String getTouchFreq() {
		return touchFreq;
	}
	public void setTouchFreq(String touchFreq) {
		this.touchFreq = touchFreq;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aim == null) ? 0 : aim.hashCode());
		result = prime * result
				+ ((estime_time == null) ? 0 : estime_time.hashCode());
		result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
		result = prime * result
				+ ((serviceStatus == null) ? 0 : serviceStatus.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongKiteServiceStatusModel other = (LongKiteServiceStatusModel) obj;
		if (aim == null) {
			if (other.aim != null)
				return false;
		} else if (!aim.equals(other.aim))
			return false;
		if (estime_time == null) {
			if (other.estime_time != null)
				return false;
		} else if (!estime_time.equals(other.estime_time))
			return false;
		if (purpose == null) {
			if (other.purpose != null)
				return false;
		} else if (!purpose.equals(other.purpose))
			return false;
		if (serviceStatus == null) {
			if (other.serviceStatus != null)
				return false;
		} else if (!serviceStatus.equals(other.serviceStatus))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LongKiteServiceStatusModel [status=" + status
				+ ", serviceStatus=" + serviceStatus + ", aim=" + aim
				+ ", purpose=" + purpose + ", estime_time=" + estime_time + "]";
	}
	
	
}
