rest-blog-service
=================
RUN

Run the test server:

mvn jetty:run

The test server starts by default on:

localhost:8080

SUPPORTED SERVICE OPERATIONS:

Content-Type: application/json

Conditional requests supported

- Create a post:

	Post
	/blog/{blogId}/posts

	{
		"title":"Post title",
  	"body":"Post body"
	}

- Get a post:

	Get
	/blog/{blogId}/posts/{postId}

- Update a post:

	Put
	/blog/{blogId}/posts/{postId}

	{
  	"title":"Post title",
  	"body":"Post body"
	}

- Delete a post:

	Delete
	/blog/{blogId}/posts/{postId}

- Add a comment:

	Post
	/blog/{blogId}/posts/{postId}/comments

	{
  	"body":"comment body"
	}

- Delete a comment:

	Delete
	/blog/{blogId}/posts/{postId}/comments/{commentId}
