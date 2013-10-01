package com.sr.blog.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public final class ResourceCollection<T> extends AbstractResource {
	private int totalItems;
	
	private List<T> items;

	@SuppressWarnings("unused")
	private ResourceCollection() {
	}
	
	public ResourceCollection(int totalCount) {
		this.totalItems = totalCount;
	}

	@XmlElement
	public int getTotalItems() {
		return totalItems;
	}

	public List<T> getItems() {
		if (items == null){
			return items;
		}
		
		return Collections.unmodifiableList(items);
	}

	public void setItems(List<T> items) {
		if (items  == null){
			throw new IllegalArgumentException();
		}

		this.items = items;
	}
}
