package com.sr.blog.service.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.ErrorInfo;

@Provider
public class WrappingExceptionMapper implements ExceptionMapper<Exception> {
	public Response toResponse(Exception ex) {
		if (ex instanceof ResourceNotFoundException) {
			return Response.status(404).entity(ErrorInfo.toErrorInfo(ex))
					.build();
		}

		if (ex instanceof WebApplicationException) {
			WebApplicationException waex = (WebApplicationException) ex;

			return Response.status(waex.getResponse().getStatusInfo())
					.entity(ErrorInfo.toErrorInfo(waex)).build();
		}

		return Response.status(500).entity(ErrorInfo.toErrorInfo(ex)).build();
	}
}
