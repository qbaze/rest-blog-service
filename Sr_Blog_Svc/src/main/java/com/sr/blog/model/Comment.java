package com.sr.blog.model;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"links", "id", "post", "created", "updated", "author", "body"})
public final class Comment extends AbstractResource {
	private Post post;
	private String body;
	private User author;

	public void setPost(Post post) {
		this.post = post;
	}

	public void setBody(String content) {
		this.body = content;
	}

	public String getBody() {
		return body;
	}

	public Post getPost() {
		return post;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	@SuppressWarnings("unused")
	private void setUpdated(){
		
	}
}
