package com.sr.blog.model;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class User extends AbstractResource {
	private String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
