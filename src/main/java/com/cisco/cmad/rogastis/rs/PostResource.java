package com.cisco.cmad.rogastis.rs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONException;

import com.cisco.cmad.rogastis.api.Post;
import com.cisco.cmad.rogastis.api.Rogastis;
import com.cisco.cmad.rogastis.service.RogastisService;

@Path("/post")
public class PostResource {

	private Rogastis rogastis;

	public PostResource() {
		rogastis = new RogastisService();
	}

	@Path("/{questionId}")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Post add(Post post, @PathParam("questionId") ObjectId questionId)
			throws JSONException {
		Post newpost = rogastis.addPost(post, questionId);
		return newpost;
	}

	@Path("/{questionId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("questionId") ObjectId questionId) {
		List<Post> posts = rogastis.getPosts(questionId);
		GenericEntity<List<Post>> entity = new GenericEntity<List<Post>>(posts) {
		};
		return Response.ok().entity(entity)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("/{postId}/{option}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(@PathParam("postId, option") ObjectId postId,
			String option) {
		if (option.equals("likes"))
			rogastis.updateLikes(postId);
		if (option.equals("dislikes"))
			rogastis.updateDislikes(postId);
		return Response.ok().build();
	}
}
