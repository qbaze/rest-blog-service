package com.sr.blog.service.rest;

import javax.ws.rs.core.EntityTag;

import com.sr.blog.model.AbstractResource;

public class EntityTagBuilder {
	// TODO: implement a strong/deep etag from an object (e.g. md5 hash)
	public static EntityTag calculateETag(AbstractResource resource){
		if (resource.getUpdated() == null){
			return new EntityTag(resource.getId(), true);
		}
		
		return new EntityTag(resource.getId() + '_' + resource.getUpdated().getMillis());
	}
}
