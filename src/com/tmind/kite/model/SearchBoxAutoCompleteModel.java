package com.tmind.kite.model;

public class SearchBoxAutoCompleteModel {

	private String id;
	private String status; //标志查询状态
	private int resultNum; //标识查询结果的数目
	private String matchWords;
	private String name_keywords;
	private String full_text_keywords;
	private String source_from;
	private String add_date;
	private String total_score;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	
	
	public String getMatchWords() {
		return matchWords;
	}
	public void setMatchWords(String matchWords) {
		this.matchWords = matchWords;
	}
	
	
	public String getName_keywords() {
		return name_keywords;
	}
	public void setName_keywords(String name_keywords) {
		this.name_keywords = name_keywords;
	}
	public String getFull_text_keywords() {
		return full_text_keywords;
	}
	public void setFull_text_keywords(String full_text_keywords) {
		this.full_text_keywords = full_text_keywords;
	}
	public String getSource_from() {
		return source_from;
	}
	public void setSource_from(String source_from) {
		this.source_from = source_from;
	}
	public String getAdd_date() {
		return add_date;
	}
	public void setAdd_date(String add_date) {
		this.add_date = add_date;
	}
	public String getTotal_score() {
		return total_score;
	}
	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
