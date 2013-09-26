package com.sr.blog.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.joda.time.DateTime;
import org.junit.Test;

import com.sr.blog.model.Post;
import com.sr.blog.service.api.IPostService;
import com.sr.blog.service.rest.BlogService;
import com.sr.blog.service.util.ApplicationContextListener;

public class BlogServiceTest extends JerseyTest {
	private IPostService getPostService(){
		return (IPostService)ApplicationContextListener.getCtx().getBean("postService");
	}
	
	@Override
    protected Application configure() {
		return new ResourceConfig(BlogService.class);
    }
	
	@Test
	public void testGetPostById_404() {
		when(getPostService().getById("1")).thenReturn(null);
		
		Response response = target("/blog/posts/1").request(MediaType.APPLICATION_JSON).get();
		
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
		
		Response response = target("/blog/posts/1").request(MediaType.APPLICATION_JSON).get();
		
		assertEquals(200, response.getStatus());
		IOUtils.writeLines(IOUtils.readLines((java.io.ByteArrayInputStream)response.getEntity(), "UTF-8"), null, System.out);
	}

	@Test
	public void testCreatePost() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePostBody() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePost() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePost() {
		fail("Not yet implemented");
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
