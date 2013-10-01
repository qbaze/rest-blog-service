package com.sr.blog.service.rest;

import java.net.URI;

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

import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.service.api.IPostService;

@Path("/blogs/{blogId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogService {// implements IBlogService {
	private @Context
	UriInfo uriInfo;

	@Autowired
	private IPostService postService;

	private CacheControl buildCacheControl(int maxAge, boolean mustRevalidate, boolean noCache, boolean noStore, boolean noTransform, boolean isPrivate, boolean proxyRevalidate, int sMaxAge){
		CacheControl cc = new CacheControl();
		
		cc.setMaxAge(maxAge);
		cc.setMustRevalidate(mustRevalidate);
		cc.setNoCache(noCache);
		cc.setNoStore(noStore);
		cc.setNoTransform(noTransform);
		cc.setPrivate(isPrivate);
		cc.setProxyRevalidate(proxyRevalidate);
		cc.setSMaxAge(sMaxAge);
		
		return cc;
	}
	
	@GET
	@Path("/posts/{id}")
	public Response getPostById(@PathParam("id") String id,
			@Context Request request) throws ResourceNotFoundException {
		Post post = postService.getById(id);

		EntityTag eTag = EntityTagBuilder.calculateETag(post);

		ResponseBuilder builder = request.evaluatePreconditions(post
				.getUpdated().toDate(), eTag);

		post.link("self", uriInfo.getAbsolutePathBuilder().build());
		post.getComments().link("self", uriInfo.getAbsolutePathBuilder().path("comments").build());
		post.getBlog().link("self", uriInfo.getAbsolutePathBuilder().path("..").path("..").build() );
		
		if (builder == null) {
			builder = Response.ok(post);
		}

		CacheControl cc = buildCacheControl(0, true, false, false, true, true, false, 0);
		
		return builder.cacheControl(cc )
				.lastModified(post.getUpdated().toDate()).tag(eTag)
				.build();
	}

	@POST
	@Path("/posts")
	public Response createPost(@PathParam("blogId") String blogId, Post data) {
		if (data == null){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		Post post = postService.createPost(blogId, data.getTitle(), data.getBody());
		
		URI location = uriInfo.getAbsolutePathBuilder().path(post.getId())
				.build();
		
		EntityTag eTag = EntityTagBuilder.calculateETag(post);
		
		CacheControl cc = buildCacheControl(0, true, false, false, true, true, false, 0);
		
		return Response.created(location).cacheControl(cc).tag(eTag)
				.build();
	}
	
//	 /**
//	  * JAX_RS nie wspiera metody PATCH zalecanej do czesciowych aktualizacji zasobu 
//	  * PUT wg. specyfikacji wymaga aktualizacji pelnego obiektu 
//	  * zamiast tego POST do istniejacego obiektu
//	  * 
//	  * @param data
//	  * @return
//	  */
//	@POST
//	@Path("/posts/{id}")	
//	public Response partialUpdatePost(Post data) {
//		if (data == null){
//			throw new WebApplicationException(Response.Status.BAD_REQUEST);
//		}
//		
//		Post post = postService.createPost(data.getTitle(), data.getBody());
//		
//		URI location = uriInfo.getAbsolutePathBuilder().path(post.getId())
//				.build();
//		
//		EntityTag eTag = EntityTagBuilder.calculateETag(post);
//		
//		CacheControl cc = buildCacheControl(0, true, false, false, true, true, false, 0);
//		
//		return Response.created(location).cacheControl(cc).tag(eTag)
//				.build();
//	}
//	
//	@POST
//	@Path("/posts/{id}/body")
//	public Response updatePostBody(@PathParam("id") String postId, Post data) throws ResourceNotFoundException {
//		if (data == null){
//			throw new WebApplicationException(Response.Status.BAD_REQUEST);
//		}
//		
//		postService.updatePostBody(postId, data);
//
//		URI location = uriInfo.getBaseUriBuilder().path("..").build();
//
//		return Response.seeOther(location).build();
//	}

	@DELETE
	@Path("/posts/{id}")
	public Response deletePost(@PathParam("id") String id, @Context Request request) throws ResourceNotFoundException {
		Post post = postService.getById(id);

		EntityTag eTag = EntityTagBuilder.calculateETag(post);
		
		ResponseBuilder builder = request.evaluatePreconditions(post.getUpdated().toDate(), eTag);

		CacheControl cc = buildCacheControl(0, true, true, true, false, false, false, 0);
		
		if (builder == null){
			postService.deletePost(id);
			
			builder = Response.noContent().cacheControl(cc);
		}
		
		return builder.lastModified(post.getUpdated().toDate())
				.tag(eTag).build();
	}

	@PUT
	@Path("/posts/{id}")
	public Response updatePost(@PathParam("id") String id, Post data,
			@Context Request request) throws ResourceNotFoundException {
		Post post = postService.getById(id);
		
		EntityTag eTag = EntityTagBuilder.calculateETag(data);

		ResponseBuilder builder = request.evaluatePreconditions(post
				.getUpdated().toDate(), eTag);
		
		if (builder == null) {
			data = postService.updatePost(id, data);
			
			if (data != null) {
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
			Comment data) throws ResourceNotFoundException {
		Comment comment = postService.addCommentToPost(postId, data);

		URI location = uriInfo.getAbsolutePathBuilder().path(comment.getId()).build();
		EntityTag eTag = EntityTagBuilder.calculateETag(comment);
		CacheControl cc = buildCacheControl(0, true, false, false, true, true, false, 0);
		
		comment.link("self", location);

		return Response.created(location).tag(eTag).cacheControl(cc).build();
	}

	@DELETE
	@Path("/posts/{postId}/comments/{commentId}")
	public Response deletePostComment(@PathParam("postId") String postId,
			@PathParam("commentId") String commentId) throws ResourceNotFoundException {
		postService.deleteCommentFromPost(postId, commentId);

		return Response.noContent().build();
	}
}
