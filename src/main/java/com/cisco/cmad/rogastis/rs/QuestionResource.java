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

import com.cisco.cmad.rogastis.api.Question;
import com.cisco.cmad.rogastis.api.Rogastis;
import com.cisco.cmad.rogastis.service.RogastisService;

@Path("/question")
public class QuestionResource {

	private Rogastis rogastis;

	public QuestionResource() {
		rogastis = new RogastisService();
	}

	@Path("/{userId}")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Question add(Question question, @PathParam("userId") ObjectId userId)
			throws JSONException {
		Question newquestion = rogastis.postQuestion(question, userId);
		return newquestion;
	}

	@Path("/single/{questionId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("questionId") ObjectId questionId) {
		Question question = rogastis.getQuestion(questionId);
		Response.ok().header("Access-Control-Allow-Origin", "*");
		return Response.ok().entity(question).build();
	}

	@Path("/search/{searchstring}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("searchstring") String querystring) {
		List<Question> questions = null;
		questions = rogastis.findQuestions(querystring);
		GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(
				questions) {

		};
		Response.ok().header("Access-Control-Allow-Origin", "*");
		return Response.ok().entity(entity)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	@Path("/count/{count}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("count") Integer count) {
		List<Question> questions = null;
		if (count == 0) {
			questions = rogastis.getAllQuestions();
		} else {
			questions = rogastis.getNumOfQuestions(count);
		}
		GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(
				questions) {
		};
		return Response.ok().entity(entity)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	@Path("/count/{count}/user/{userId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("count, userId") Integer count,
			ObjectId userId) {
		List<Question> questions = null;
		if (count == 0) {
			questions = rogastis.getUserQuestions(userId);
		} else {
			questions = rogastis.getUserNumOfQuestions(userId, count);
		}
		GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(
				questions) {
		};
		return Response.ok().entity(entity)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	// @Path("/{questionId}/{userId}")
	// @POST
	// @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// public Response add(@PathParam("questionId, userId") ObjectId questionId,
	// ObjectId userId, Post post) {
	// Integer newpostid = rogastis.addComment(post, questionId, userId);
	// return Response.ok().entity(newpostid).build();
	// }

	// @Path("/{userId}")
	// @GET
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// public Response get(@PathParam("userId") ObjectId userId) {
	// List<Question> questions = rogastis.getUserQuestions(userId);
	// GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(
	// questions) {
	// };
	// return Response.ok().entity(entity).build();
	// }

	// @Path("/{count}")
	// @GET
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	// public Response get(@PathParam("count") Integer count) {
	// List<Question> questions = rogastis.getNumOfQuestions(count);
	// GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(
	// questions) {
	// };
	// return Response.ok().entity(entity)
	// .header("Access-Control-Allow-Origin", "*").build();
	// }

}
