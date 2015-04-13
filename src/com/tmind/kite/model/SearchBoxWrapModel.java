package com.tmind.kite.model;

import java.util.List;

public class SearchBoxWrapModel {
	
	private String status;
	private List<SearchBoxAutoCompleteModel> model;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<SearchBoxAutoCompleteModel> getModel() {
		return model;
	}
	public void setModel(List<SearchBoxAutoCompleteModel> model) {
		this.model = model;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
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
		SearchBoxWrapModel other = (SearchBoxWrapModel) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
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
		return "SearchBoxWrapModel [status=" + status + ", model=" + model
				+ "]";
	}
	
	
}
