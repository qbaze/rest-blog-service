package com.sr.blog.service.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;

@XmlType(propOrder={"links", "model"})
@XmlSeeAlso({Post.class, Comment.class})
public class Resource<T> {
	private List<Link> links;
	private T model;
	
	public static <T> ResourceBuilder<T> model(T model){
		return ResourceBuilder.newInstance(model);
	}
	
	public Resource() {
		this.links = new ArrayList<Resource.Link>();
	}
	
	@XmlElement
	@XmlPath(".")
	public T getModel(){
		return model;
	}
	
	@XmlAnyElement(lax=true)
	public List<Link> getLinks(){
		return links;
	}
	
	public void setModel(T model){
		this.model = model;
	}
	
	public void setLinks(List<Link> links){
		this.links = links;
	}
	
	@XmlType
	public static class Link {
		private String rel;
		private URI uri;
				
		@XmlElement
		public String getRel() {
			return rel;
		}
		
		@XmlElement
		public URI getUri() {
			return uri;
		}
		
		public void setRel(String rel) {
			this.rel = rel;
		}
		
		
		public void setUri(URI uri) {
			this.uri = uri;
		}		
	}
	
	public static class ResourceBuilder<R> {
		private Resource<R> resource;
		
		public ResourceBuilder(R model) {
			this.resource = new Resource<R>();
			
			this.resource.setModel(model);
		}

		protected static <R> ResourceBuilder<R> newInstance(R model){
			return new ResourceBuilder<R>(model);
		}
		
		public Resource<R> resource(){
			return resource;
		}
		
		public ResourceBuilder<R> link(String rel, URI uri){
			Link link = new Link();
			
			link.setRel(rel);
			link.setUri(uri);
			
			resource.getLinks().add(link);
			
			return this;
		}
	}
}
