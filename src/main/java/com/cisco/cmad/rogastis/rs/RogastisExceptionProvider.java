package com.cisco.cmad.rogastis.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.cisco.cmad.rogastis.api.DuplicateUserException;
import com.cisco.cmad.rogastis.api.InvalidAnswerException;
import com.cisco.cmad.rogastis.api.InvalidQuestionException;
import com.cisco.cmad.rogastis.api.InvalidUserException;
import com.cisco.cmad.rogastis.api.PostNotFoundException;
import com.cisco.cmad.rogastis.api.QuestionNotFoundException;
import com.cisco.cmad.rogastis.api.RogastisException;
import com.cisco.cmad.rogastis.api.UserNotFoundException;

@Provider
public class RogastisExceptionProvider implements
		ExceptionMapper<RogastisException> {

	public Response toResponse(RogastisException e) {
		if (e instanceof InvalidUserException)
			return Response.status(501).build();
		else if (e instanceof DuplicateUserException) {
			return Response.status(502).build();
		} else if (e instanceof UserNotFoundException) {
			return Response.status(503).build();
		} else if (e instanceof InvalidQuestionException) {
			return Response.status(504).build();
		} else if (e instanceof QuestionNotFoundException) {
			return Response.status(505).build();
		} else if (e instanceof InvalidAnswerException) {
			return Response.status(506).build();
		} else if (e instanceof PostNotFoundException) {
			return Response.status(507).build();
		} else {
			return Response.status(500).build();
		}
	}
}
