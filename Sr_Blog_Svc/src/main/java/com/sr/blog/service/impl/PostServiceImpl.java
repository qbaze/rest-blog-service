package com.sr.blog.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;

import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.Blog;
import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;
import com.sr.blog.model.ResourceCollection;
import com.sr.blog.service.api.IDataAccessObject;
import com.sr.blog.service.api.IPostService;

public class PostServiceImpl implements IPostService {
	@Autowired
	private IDataAccessObject<String, Post> postDao;

	@Autowired
	private IDataAccessObject<String, Comment> commentDao;

	@Override
	public Post getById(String id) throws ResourceNotFoundException {
		Post post = postDao.get(id);
		
		if (post == null){
			throw new ResourceNotFoundException(Post.class.getSimpleName(), id);
		}
		
		List<Comment> list = commentDao.getByQuery(query(where("postId").is(id)));
		
		post.setComments(new ResourceCollection<Comment>(list.size()));
		
		return post;
	}

	@Override
	public Post createPost(String blogId, String title, String body) {
		Post post = new Post();
		DateTime dateTime = DateTime.now(DateTimeZone.UTC);

		post.setCreated(dateTime);
		post.setUpdated(dateTime);
		post.setTitle(title);
		post.setBody(body);
		
		Blog blog = new Blog();
		
		blog.setId(blogId);
		
		post.setBlog(blog);

		return postDao.save(post);
	}

	@Override
	public Post deletePost(String id) throws ResourceNotFoundException {
		Post post = postDao.delete(id);
		
		if (post == null){
			throw new ResourceNotFoundException(Post.class.getSimpleName(), id);
		}
		
		commentDao.deleteByQuery(query(where("postId").is(id)));
		
		return post;
	}

	@Override
	public Post updatePost(String id, Post post) throws ResourceNotFoundException {
		post.setId(id);
		post.setUpdated(DateTime.now(DateTimeZone.UTC));
		
		Post updated = postDao.update(post);

		if (updated == null) {
			throw new ResourceNotFoundException(Post.class.getSimpleName(), id);
		}
	
		return updated;
	}
	
	@Override
	public Post updatePostBody(String id, Post post) throws ResourceNotFoundException {
		post.setId(id);
		post.setUpdated(DateTime.now(DateTimeZone.UTC));
		
		Update update = new Update();
		
		update.set("body", post.getBody());
		
		Post updated = postDao.updatePartial(id, update);

		if (updated == null) {
			throw new ResourceNotFoundException(Post.class.getSimpleName(), id);
		}
	
		return updated;
	}

	@Override
	public Comment addCommentToPost(String postId, Comment comment) throws ResourceNotFoundException {
		postDao.get(postId);
		
		comment.setCreated(DateTime.now(DateTimeZone.UTC));
		
		Post post = new Post();
		
		post.setId(postId);
		
		comment.setPost(post);

		return commentDao.save(comment);
	}

	@Override
	public Comment deleteCommentFromPost(String postId, String commentId) throws ResourceNotFoundException {
		postDao.get(postId);
		
		Comment deleted = commentDao.delete(commentId);
		
		if (deleted == null){
			throw new ResourceNotFoundException(Comment.class.getSimpleName(), commentId);
		}
		
		return deleted;
	}

}
