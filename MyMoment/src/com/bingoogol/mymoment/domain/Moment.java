package com.bingoogol.mymoment.domain;

public class Moment {
	private int id;
	private String content;
	private String imgPath;
	private String publishTime;

	public Moment() {
	}

	public Moment(int id, String content, String imgPath, String publishTime) {
		this.id = id;
		this.content = content;
		this.imgPath = imgPath;
		this.publishTime = publishTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	
}
