package com.sr.blog.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.net.HttpHeaders;
import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.Blog;
import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.model.ResourceCollection;
import com.sr.blog.model.User;
import com.sr.blog.service.api.IPostService;
import com.sr.blog.service.rest.BlogService;
import com.sr.blog.service.rest.EntityTagBuilder;
import com.sr.blog.service.rest.WrappingExceptionMapper;
import com.sr.blog.service.util.ApplicationContextListener;
import com.sr.blog.service.util.TestUtil;

public class BlogServiceTest extends JerseyTest {
	private static final String BLOG_POSTS = "/blogs/1/posts";
	private static final String HTTP_HOST = "http://localhost:9998";

	private IPostService getPostService() {
		return (IPostService) ApplicationContextListener.getCtx().getBean(
				"postService");
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(BlogService.class,
				WrappingExceptionMapper.class);
	}

	@Test
	public void testGetPostById_404() throws Exception {
		when(getPostService().getById("1")).thenThrow(
				new ResourceNotFoundException("Post", "1"));

		Response response = target(BLOG_POSTS + "/1").request(
				MediaType.APPLICATION_JSON).get();

		assertEquals(404, response.getStatus());

		printResponse(response);
	}

	@Test
	public void testGetPostById_200() throws Exception {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());
		post.setComments(new ResourceCollection<Comment>(0));

		Blog blog = new Blog();

		blog.setId("1231");
		post.setBlog(blog);

		User author = new User();

		author.setId("1234");

		post.setAuthor(author);

		when(getPostService().getById("1")).thenReturn(post);

		EntityTag etag = new EntityTag("incorrect");

		Response response = target(BLOG_POSTS + "/1")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.IF_NONE_MATCH, etag.toString()).get();

		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());

		printResponse(response);

	}

	private void printResponse(Response response) {
		try {
			IOUtils.writeLines(IOUtils.readLines(
					(java.io.ByteArrayInputStream) response.getEntity(),
					"UTF-8"), null, System.out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetPostById_304() throws Exception {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());
		post.setComments(new ResourceCollection<Comment>(0));

		Blog blog = new Blog();

		blog.setId("1231");
		post.setBlog(blog);

		User author = new User();

		author.setId("1234");

		post.setAuthor(author);

		when(getPostService().getById("1")).thenReturn(post);

		EntityTag etag = EntityTagBuilder.calculateETag(post);

		Response response = target(BLOG_POSTS + "/1")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.IF_NONE_MATCH, etag.toString()).get();

		assertEquals(304, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
	}

	@Test
	public void testCreatePost_201() {
		String body = "Test body";
		String title = "Test title";

		Post post = new Post();

		post.setId("1");
		post.setBody(body);
		post.setTitle(title);
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		Blog blog = new Blog();

		String blogId = "1";

		blog.setId(blogId);

		post.setBlog(blog);

		when(getPostService().createPost(blogId, title, body)).thenReturn(post);

		Response response = target(BLOG_POSTS).request(
				MediaType.APPLICATION_JSON).post(Entity.json(post));

		assertEquals(201, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
		assertEquals(HTTP_HOST + BLOG_POSTS + "/1", response.getLocation()
				.toString());
	}

	@Test
	public void testCreatePost_400() {

		Response response = target(BLOG_POSTS).request(
				MediaType.APPLICATION_JSON).post(null);

		assertEquals(400, response.getStatus());

		printResponse(response);
	}

	@Test
	public void testDeletePost_204() throws Exception {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().getById("1")).thenReturn(post);
		when(getPostService().deletePost("1")).thenReturn(post);

		EntityTag eTag = EntityTagBuilder.calculateETag(post);

		Response response = target(BLOG_POSTS + "/1")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.IF_MATCH, eTag.toString()).delete();

		assertEquals(204, response.getStatus());
	}

	@Test
	public void testDeletePost_412() throws Exception {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().getById("1")).thenReturn(post);
		when(getPostService().deletePost("1")).thenReturn(post);

		Response response = target(BLOG_POSTS + "/1")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.IF_MATCH, TestUtil.dquote("2_2")).delete();

		assertEquals(412, response.getStatus());
	}

	@Test
	public void testDeletePost_404() throws Exception {
		when(getPostService().getById("1")).thenThrow(
				new ResourceNotFoundException("Post", "1"));
		when(getPostService().deletePost("1")).thenThrow(
				new ResourceNotFoundException("Post", "1"));

		Response response = target(BLOG_POSTS + "/1").request(
				MediaType.APPLICATION_JSON).delete();

		assertEquals(404, response.getStatus());
	}

	// @Test
	// public void testUpdatePostBody() {
	// String body = "Test body";
	// String title = "Test title";
	//
	// Post post = new Post();
	//
	// post.setId("1");
	// post.setBody(body);
	// post.setTitle(title);
	// post.setCreated(DateTime.now());
	// post.setUpdated(DateTime.now());
	//
	// when(getPostService().createPost(title, body)).thenReturn(post);
	//
	// Response response = target(BLOG_POSTS + "/body").request(
	// MediaType.APPLICATION_JSON).post(Entity.json(post));
	//
	// assertEquals(201, response.getStatus());
	// assertEquals(false, response.hasEntity());
	// assertNotEquals(null, response.getEntityTag());
	// assertEquals("http://localhost:9998/blog/posts/1", response
	// .getLocation().toString());
	// }

	@Test
	public void testAddPostComment() throws Exception {
		String body = "Test body";

		Comment comment = new Comment();

		comment.setBody(body);
		Post post = new Post();
		
		post.setId("2121");
		
		comment.setPost(post);
		comment.setId("1");
		comment.setCreated(DateTime.now());

		when(getPostService().addCommentToPost(anyString(), (Comment) any()))
				.thenReturn(comment);

		Response response = target(BLOG_POSTS + "/1/comments").request(
				MediaType.APPLICATION_JSON).post(Entity.json(comment));

		assertEquals(201, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
		assertEquals(HTTP_HOST + BLOG_POSTS + "/1/comments/1", response
				.getLocation().toString());
	}

	@Test
	public void testDeletePostComment_404() throws ResourceNotFoundException {
		when(getPostService().deleteCommentFromPost("1", "1")).thenThrow(
				new ResourceNotFoundException("Post", "1"));

		Response response = target(BLOG_POSTS + "/1/comments/1").request(
				MediaType.APPLICATION_JSON).delete();

		assertEquals(404, response.getStatus());		
	}
	
	@Test
	public void testDeletePostComment() throws ResourceNotFoundException {
		Comment comment = new Comment();
		
		when(getPostService().deleteCommentFromPost("1", "1")).thenReturn(comment);

		Response response = target(BLOG_POSTS + "/1/comments/1").request(
				MediaType.APPLICATION_JSON).delete();

		assertEquals(204, response.getStatus());		
	}
}
