package com.sr.blog.model;

import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;


@XmlType(propOrder={"id", "title", "body", "created", "updated", "comments"})
public final class Post {
	private String id;
	private DateTime created; 
	private DateTime updated; 
	
	private String title;
	private String body;
	
	private ResourceCollection<Comment> comments;
	
	public String getId(){
		return id;
	}
	
	public DateTime getCreated(){
		return created;
	}
	
	public DateTime getUpdated(){
		return updated;
	}
	
	public String getBody(){
		return body;
	}
	
	public String getTitle(){
		return title;
	}

	public void setUpdated(DateTime modification) {
		this.updated = modification;
	}

	public void setCreated(DateTime creation) {
		this.created=creation;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setBody(String body){
		this.body = body;
	}

	public ResourceCollection<Comment> getComments() {
		return comments;
	}

	public void setComments(ResourceCollection<Comment> comments) {
		this.comments = comments;
	}
}
