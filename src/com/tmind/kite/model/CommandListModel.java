package com.tmind.kite.model;

public class CommandListModel {
	private String id;
	private String comments_content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComments_content() {
		return comments_content;
	}
	public void setComments_content(String comments_content) {
		this.comments_content = comments_content;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((comments_content == null) ? 0 : comments_content.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CommandListModel other = (CommandListModel) obj;
		if (comments_content == null) {
			if (other.comments_content != null)
				return false;
		} else if (!comments_content.equals(other.comments_content))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CommandListModel [id=" + id + ", comments_content="
				+ comments_content + "]";
	}
	
	
}
