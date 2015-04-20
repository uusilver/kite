package com.tmind.kite.model;

import java.util.List;

public class CommandModel {
	private String query_id;
	private List<CommandListModel> comdList;
	public String getQuery_id() {
		return query_id;
	}
	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}
	public List<CommandListModel> getComdList() {
		return comdList;
	}
	public void setComdList(List<CommandListModel> comdList) {
		this.comdList = comdList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comdList == null) ? 0 : comdList.hashCode());
		result = prime * result
				+ ((query_id == null) ? 0 : query_id.hashCode());
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
		CommandModel other = (CommandModel) obj;
		if (comdList == null) {
			if (other.comdList != null)
				return false;
		} else if (!comdList.equals(other.comdList))
			return false;
		if (query_id == null) {
			if (other.query_id != null)
				return false;
		} else if (!query_id.equals(other.query_id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CommandModel [query_id=" + query_id + ", comdList=" + comdList
				+ "]";
	}
	
	
}
