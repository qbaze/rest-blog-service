package com.sr.blog.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.net.HttpHeaders;
import com.sr.blog.model.Post;
import com.sr.blog.service.api.IPostService;
import com.sr.blog.service.rest.BlogService;
import com.sr.blog.service.util.ApplicationContextListener;
import com.sr.blog.service.util.TestUtil;

public class BlogServiceTest extends JerseyTest {
	private IPostService getPostService() {
		return (IPostService) ApplicationContextListener.getCtx().getBean(
				"postService");
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(BlogService.class);
	}

	@Test
	public void testGetPostById_404() {
		when(getPostService().getById("1")).thenReturn(null);

		Response response = target("/blog/posts/1").request(
				MediaType.APPLICATION_JSON).get();

		assertEquals(404, response.getStatus());
	}

	@Test
	public void testGetPostById_200() throws IOException {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().getById("1")).thenReturn(post);

		Response response = target("/blog/posts/1").request(
				MediaType.APPLICATION_JSON).get();

		assertEquals(200, response.getStatus());
		assertEquals(true, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
		// IOUtils.writeLines(IOUtils.readLines((java.io.ByteArrayInputStream)response.getEntity(),
		// "UTF-8"), null, System.out);

	}

	@Test
	public void testGetPostById_304() throws IOException {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().getById("1")).thenReturn(post);

		String etag = post.getId() + '_' + post.getUpdated().getMillis();

		Response response = target("/blog/posts/1")
				.request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.ETAG, etag)
				.header(HttpHeaders.IF_NONE_MATCH, TestUtil.dquote(etag)).get();

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

		when(getPostService().createPost(title, body)).thenReturn(post);

		Response response = target("/blog/posts").request(
				MediaType.APPLICATION_JSON).post(Entity.json(post));

		assertEquals(201, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
		assertEquals("http://localhost:9998/blog/posts/1", response
				.getLocation().toString());
	}

	@Test
	public void testCreatePost_400() {

		Response response = target("/blog/posts").request(
				MediaType.APPLICATION_JSON).post(null);

		assertEquals(400, response.getStatus());
	}

	@Test
	public void testDeletePost_204() {
		Post post = new Post();

		post.setId("1");
		post.setBody("Test body");
		post.setTitle("Test title");
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().deletePost("1")).thenReturn(post);

		Response response = target("/blog/posts/1").request(
				MediaType.APPLICATION_JSON).delete();

		assertEquals(204, response.getStatus());
	}

	@Test
	public void testDeletePost_404() {
		when(getPostService().deletePost("1")).thenReturn(null);

		Response response = target("/blog/posts/1").request(
				MediaType.APPLICATION_JSON).delete();

		assertEquals(404, response.getStatus());
	}

	@Test
	public void testUpdatePostBody() {
		String body = "Test body";
		String title = "Test title";

		Post post = new Post();

		post.setId("1");
		post.setBody(body);
		post.setTitle(title);
		post.setCreated(DateTime.now());
		post.setUpdated(DateTime.now());

		when(getPostService().createPost(title, body)).thenReturn(post);

		Response response = target("/blog/posts/1/body").request(
				MediaType.APPLICATION_JSON).post(Entity.json("{\"body\":\"dsdsdsds\""));

		assertEquals(201, response.getStatus());
		assertEquals(false, response.hasEntity());
		assertNotEquals(null, response.getEntityTag());
		assertEquals("http://localhost:9998/blog/posts/1", response
				.getLocation().toString());
	}

	@Test
	public void testAddPostComment() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePostComment() {
		fail("Not yet implemented");
	}
}
