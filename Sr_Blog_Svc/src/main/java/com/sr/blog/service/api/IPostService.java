package com.sr.blog.service.api;

import com.sr.blog.error.ResourceNotFoundException;
import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;

public interface IPostService {
	Post getById(String id) throws ResourceNotFoundException;
	Post createPost(String blogId, String title, String body);
	Post deletePost(String id) throws ResourceNotFoundException;
	Post updatePost(String id, Post post) throws ResourceNotFoundException;
	Comment addCommentToPost(String postId, Comment comment) throws ResourceNotFoundException ;
	Comment deleteCommentFromPost(String postId, String commentId) throws ResourceNotFoundException;
	public abstract Post updatePostBody(String id, Post post)
			throws ResourceNotFoundException;
}

