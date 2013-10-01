package com.sr.blog.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Blog extends AbstractResource {
	private String name;
	private ResourceCollection<Post> posts;
		
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ResourceCollection<Post> getPosts() {
		return posts;
	}

	public void setPosts(ResourceCollection<Post> posts) {
		this.posts = posts;
	}
}
