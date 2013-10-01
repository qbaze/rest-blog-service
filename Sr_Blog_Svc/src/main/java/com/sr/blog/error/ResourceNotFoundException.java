package com.sr.blog.error;

public class ResourceNotFoundException extends BlogException {
	private static final long serialVersionUID = -2664836280988934393L;
	
	public ResourceNotFoundException(String resourceTypeName, String resourceId) {
		super(String.format("Resource not found - type: %s id: %s", resourceTypeName, resourceId));
	}
}
