package com.sr.blog.model;

import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

@XmlType(propOrder={"id", "postId", "created", "body"})
public class Comment {
	private String id;

	private String postId;

	private DateTime created;
	private String body;

	public void setId(String id) {
		this.id = id;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public void setCreated(DateTime creationDate) {
		this.created = creationDate;
	}

	public void setBody(String content) {
		this.body = content;
	}

	public String getBody() {
		return body;
	}

	public DateTime getCreated() {
		return created;
	}

	public String getId() {
		return id;
	}

	public String getPostId() {
		return postId;
	}
}
