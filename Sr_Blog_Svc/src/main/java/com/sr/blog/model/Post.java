package com.sr.blog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(propOrder={"links", "id", "blog", "author", "title", "body", "created", "updated", "comments"})
public final class Post extends AbstractResource {
	private Blog blog;
	private User author;
	private String title;
	private String body;
	
	private ResourceCollection<Comment> comments;
	
	public String getBody(){
		return body;
	}
	
	public String getTitle(){
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	public Blog getBlog(){
		return this.blog;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
