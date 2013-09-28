package com.sr.blog.service.rest;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.service.api.IPostService;

@Path("/blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogService {// implements IBlogService {
	private @Context
	UriInfo uriInfo;

	@Autowired
	private IPostService postService;

	@GET
	@Path("/posts/{id}")
	public Response getPostById(@PathParam("id") String id,
			@Context Request request) {
		Post post = postService.getById(id);

		if (post == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		EntityTag eTag = new EntityTag(post.getId() + "_"
				+ post.getUpdated().getMillis());

		CacheControl cacheControl = new CacheControl();

		cacheControl.setMaxAge(86400);

		ResponseBuilder builder = request.evaluatePreconditions(post
				.getUpdated().toDate(), eTag);

		Resource<Post> resource = Resource.model(post).link("self", uriInfo.getAbsolutePathBuilder().path(post.getId())
				.build()).resource();
		
		if (builder == null) {
			builder = Response.ok(resource);
		}

		return builder.cacheControl(cacheControl)
				.lastModified(post.getUpdated().toDate()).tag(eTag)
				.build();
	}

	@POST
	@Path("/posts$")
	public Response createPost(Post data) {
		if (data == null){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		Post post = postService.createPost(data.getTitle(), data.getBody());
		URI location = uriInfo.getAbsolutePathBuilder().path(post.getId())
				.build();
		EntityTag eTag = new EntityTag(post.getId() + "_"
				+ post.getUpdated().getMillis());
		CacheControl cacheControl = new CacheControl();

		cacheControl.setMaxAge(-1);

		return Response.created(location).cacheControl(cacheControl).tag(eTag)
				.build();
	}
	
	@POST
	@Path("/posts/{id}/body")
	public Response updatePostBody(@PathParam("id") String postId, Map<String, String> body) {
		//Post post = postService.getById(postId);
		
		//post.setBody(body);
		
		URI location = uriInfo.getBaseUriBuilder().path("/posts/" + postId).build();

//		EntityTag eTag = new EntityTag(post.getId() + "_"
//				+ post.getUpdated().getMillis());
//		CacheControl cacheControl = new CacheControl();
//
//		cacheControl.setMaxAge(-1);

		return Response.seeOther(location).build();
	}

	@DELETE
	@Path("/posts/{id}")
	public Response deletePost(@PathParam("id") String id) {
		Post post = postService.deletePost(id);

		if (post == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		return Response.noContent().build();
	}

	@PUT
	@Path("/posts/{id}")
	public Response updatePost(@PathParam("id") String id, Post data,
			@Context Request request) {
		Post post = postService.getById(id);

		if (post == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		EntityTag eTag = new EntityTag(post.getId() + "_"
				+ post.getUpdated().getMillis());

		ResponseBuilder builder = request.evaluatePreconditions(post
				.getUpdated().toDate(), eTag);

		if (builder == null) {
			if (!post.getTitle().equals(data.getTitle())
					|| !post.getBody().equals(data.getBody())) {
				data = postService.updatePost(id, data);
				builder = Response.ok(data);
			} else {
				builder = Response.noContent();
			}
		}

		return builder.lastModified(post.getUpdated().toDate())
				.tag(eTag).build();
	}

	@POST
	@Path("/posts/{id}/comments")
	public Response addPostComment(@PathParam("id") String postId,
			Comment data) {
		Comment comment = postService.addCommentToPost(postId, data);

		URI location = uriInfo.getBaseUriBuilder().path("blog/posts/" + postId).build();

		return Response.seeOther(location).build();
	}

	@DELETE
	@Path("/posts/{postId}/comments/{commentId}")
	public Response deletePostComment(@PathParam("postId") String postId,
			@PathParam("commentId") String commentId) {
		Comment comment = postService.deleteCommentFromPost(postId, commentId);

		if (comment == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		URI location = uriInfo.getBaseUriBuilder().path("blog/posts/" + postId).build();
		
		return Response.seeOther(location).build();
	}
}
