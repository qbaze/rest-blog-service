package com.sr.blog.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.mongodb.core.query.Query;

import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.service.api.IDataAccessObject;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {
	@Mock
	DataAccessObjectImpl<String, Comment> commentDao;

	@Mock
	IDataAccessObject<String, Post> postDao;

	@InjectMocks
	PostServiceImpl postService;

	@Test(expected = ResourceNotFoundException.class)
	public void testGetById_not_found() throws Exception {
		String id = "1";

		when(postDao.get(id)).thenReturn(null);

		postService.getById(id);
	}

	@Test
	public void testGetById() throws Exception {
		String id = "1";

		Post post = new Post();

		when(postDao.get(id)).thenReturn(post);
		
		Comment comment = new Comment();
		List<Comment> comments = Collections.nCopies(7, comment);
		
		when(commentDao.getByQuery(query(where("postId").is(id)))).thenReturn(comments);

		Post result = postService.getById(id);
		
		assertTrue(result.equals(post));
		assertEquals(7, result.getComments().getTotalItems());
	}

	@Test
	public void testCreatePost() {
		when(postDao.save((Post) any())).thenAnswer(new Answer<Post>() {
			@Override
			public Post answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Post) args[0];
			}
		});
		
		String title = "test";
		String body = "test body";
		
		Post post = postService.createPost(null, title, body);
		
		assertNotNull(post);
		assertEquals(post.getCreated(), post.getUpdated());
		assertEquals(title, post.getTitle());
		assertEquals(body, post.getBody());
	}

	@Test(expected = ResourceNotFoundException.class)
	public void testDeletePost_not_found() throws Exception {
		String id = "1";

		when(postDao.delete(id)).thenReturn(null);

		postService.deletePost(id);
	}

	@Test
	public void testDeletePost() throws Exception {
		String id = "1";

		when(postDao.delete(id)).thenReturn(new Post());

		postService.deletePost(id);

		verify(commentDao, times(1)).deleteByQuery((Query) any());
	}

//	@Test
//	public void testUpdatePost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddCommentToPost() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteCommentFromPost() {
//		fail("Not yet implemented");
//	}

}
