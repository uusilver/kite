package com.tmind.kite.model;

import java.util.List;

public class SearchDetailInfoModel {

	private String id;
	private String telno;
	private String name;
	private String full_text;
	private String look_score;
	private String talk_score;
	private String act_score;
	private String peronal_score;
	private String total_score;
	private String come_from;   //来自哪，微信，世纪佳缘等等
	private String useful_mark_num; //此条信息对我有用标记数
	private String comments_no; //评论数
	private String comments_table_name;
	private String picStr;
	private List<CommentsModel> commentList;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLook_score() {
		return look_score;
	}
	public void setLook_score(String look_score) {
		this.look_score = look_score;
	}
	public String getTalk_score() {
		return talk_score;
	}
	public void setTalk_score(String talk_score) {
		this.talk_score = talk_score;
	}
	public String getAct_score() {
		return act_score;
	}
	public void setAct_score(String act_score) {
		this.act_score = act_score;
	}
	public String getPeronal_score() {
		return peronal_score;
	}
	public void setPeronal_score(String peronal_score) {
		this.peronal_score = peronal_score;
	}
	public String getCome_from() {
		return come_from;
	}
	public void setCome_from(String come_from) {
		this.come_from = come_from;
	}
	public String getUseful_mark_num() {
		return useful_mark_num;
	}
	public void setUseful_mark_num(String useful_mark_num) {
		this.useful_mark_num = useful_mark_num;
	}
	public String getComments_no() {
		return comments_no;
	}
	public void setComments_no(String comments_no) {
		this.comments_no = comments_no;
	}
	public String getComments_table_name() {
		return comments_table_name;
	}
	public void setComments_table_name(String comments_table_name) {
		this.comments_table_name = comments_table_name;
	}
	
	public List<CommentsModel> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<CommentsModel> commentList) {
		this.commentList = commentList;
	}
	
	public String getFull_text() {
		return full_text;
	}
	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}
	public String getTotal_score() {
		return total_score;
	}
	public void setTotal_score(String total_score) {
		this.total_score = total_score;
	}
	
	
	public String getPicStr() {
		return picStr;
	}
	public void setPicStr(String picStr) {
		this.picStr = picStr;
	}
	@Override
	public String toString() {
		return "SearchDetailInfoModel [id=" + id + ", telno=" + telno
				+ ", name=" + name + ", look_score=" + look_score
				+ ", talk_score=" + talk_score + ", act_score=" + act_score
				+ ", peronal_score=" + peronal_score + ", come_from="
				+ come_from + ", useful_mark_num=" + useful_mark_num
				+ ", comments_no=" + comments_no + ", comments_table_name="
				+ comments_table_name + ", getId()=" + getId()
				+ ", getTelno()=" + getTelno() + ", getName()=" + getName()
				+ ", getLook_score()=" + getLook_score() + ", getTalk_score()="
				+ getTalk_score() + ", getAct_score()=" + getAct_score()
				+ ", getPeronal_score()=" + getPeronal_score()
				+ ", getCome_from()=" + getCome_from()
				+ ", getUseful_mark_num()=" + getUseful_mark_num()
				+ ", getComments_no()=" + getComments_no()
				+ ", getComments_table_name()=" + getComments_table_name()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	
	
}
