package com.tmind.kite.model;

public class SearchBoxAutoCompleteModel {

	private int id;
	private String status; //标志查询状态
	private int resultNum; //标识查询结果的数目
	private String[] matchWords;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getResultNum() {
		return resultNum;
	}
	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}
	public String[] getMatchWords() {
		return matchWords;
	}
	public void setMatchWords(String[] matchWords) {
		this.matchWords = matchWords;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((matchWords == null) ? 0 : matchWords.hashCode());
		result = prime * result + resultNum;
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
		SearchBoxAutoCompleteModel other = (SearchBoxAutoCompleteModel) obj;
		if (id != other.id)
			return false;
		if (matchWords == null) {
			if (other.matchWords != null)
				return false;
		} else if (!matchWords.equals(other.matchWords))
			return false;
		if (resultNum != other.resultNum)
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
		return "SearchBoxAutoCompleteModel [id=" + id + ", status=" + status
				+ ", resultNum=" + resultNum + ", matchWords=" + matchWords
				+ "]";
	}

	
}
