package com.sr.blog.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

@XmlTransient
//@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractResource {
	@XmlElement
	private String id;
	@XmlElement
	private DateTime created;
	@XmlElement
	private DateTime updated;
	@XmlElement
	private List<Link> links;
	
	public AbstractResource link(String rel, URI uri){
		if (links == null){
			links = new ArrayList<AbstractResource.Link>(5);
		}
		
		Link link = new Link();
		
		link.setRel(rel);
		link.setUri(uri);
		
		links.add(link);
		
		return this;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}

	@XmlType
	public static class Link {
		private String rel;
		private URI uri;
		
		public Link() {}
		
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
}
