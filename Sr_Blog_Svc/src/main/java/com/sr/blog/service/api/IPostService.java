package com.sr.blog.service.api;

import com.sr.blog.model.Comment;
import com.sr.blog.model.Post;

public interface IPostService {
	Post getById(String id);
	Post createPost(String title, String body);
	Post deletePost(String id);
	Post updatePost(String id, Post post);
	Comment addCommentToPost(String postId, Comment comment);
	Comment deleteCommentFromPost(String postId, String commentId);
}

