package com.tmind.kite.model;

public class LongKiteServiceStatusModel {

	private String status;
	private String serviceStatus;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
				+ ", serviceStatus=" + serviceStatus + "]";
	}
	
	
}
