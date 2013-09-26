package com.sr.blog.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;

import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.service.api.IDataAccessObject;
import com.sr.blog.service.api.IPostService;

public class PostServiceImpl implements IPostService {
	@Autowired
	private IDataAccessObject<String, Post> postDao;

	@Autowired
	private IDataAccessObject<String, Comment> commentDao;

	@Override
	public Post getById(String id) {
		return postDao.get(id);
	}

	@Override
	public Post createPost(String title, String body) {
		Post post = new Post();
		DateTime dateTime = DateTime.now(DateTimeZone.UTC);

		post.setCreated(dateTime);
		post.setUpdated(dateTime);
		post.setTitle(title);
		post.setBody(body);

		return postDao.save(post);
	}

	@Override
	public Post deletePost(String id) {
		Post post = postDao.delete(id);
		
		commentDao.deleteByQuery(query(where("postId").is(id)));
		
		return post;
	}

	@Override
	public Post updatePost(String id, Post post) {
		return postDao.update(post);
	}

	@Override
	public Comment addCommentToPost(String postId, Comment comment) {
		comment.setCreated(DateTime.now(DateTimeZone.UTC));
		comment.setId(postId);

		return commentDao.save(comment);
	}

	@Override
	public Comment deleteCommentFromPost(String postId, String commentId) {
		return commentDao.delete(commentId);
	}

}
