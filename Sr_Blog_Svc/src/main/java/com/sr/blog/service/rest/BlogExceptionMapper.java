package com.sr.blog.service.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sr.blog.error.BlogException;

public class BlogExceptionMapper implements
		ExceptionMapper<BlogException> {
	public Response toResponse(BlogException ex) {
		return Response.status(404).entity(ex.getMessage()).type("text/plain")
				.build();
	}
}
