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

import com.cisco.cmad.rogastis.api.Rogastis;
import com.cisco.cmad.rogastis.api.User;
import com.cisco.cmad.rogastis.service.RogastisService;

@Path("/user")
public class UserResource {

	private Rogastis rogastis;

	public UserResource() {
		rogastis = new RogastisService();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User add(User user) throws JSONException {
		User newuser = rogastis.registerUser(user);
		return newuser;
	}

	@Path("/users")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get() {
		List<User> users = rogastis.getAllUsers();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {
		};
		return Response.ok().entity(entity)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	@Path("/{userId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("userId") ObjectId userId) {
		User user = rogastis.getUserByUserId(userId);
		return Response.ok().entity(user)
				.header("Access-Control-Allow-Origin", "*").build();
	}

	@Path("/loginId/{loginId}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("loginId") String loginId) {
		User user = rogastis.getUserByLoginId(loginId);
		return Response.ok().entity(user)
				.header("Access-Control-Allow-Origin", "*").build();
	}

}
