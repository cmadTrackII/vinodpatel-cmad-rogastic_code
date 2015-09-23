package com.cisco.cmad.rogastis.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.cisco.cmad.rogastis.api.DuplicateUserException;
import com.cisco.cmad.rogastis.api.InvalidAnswerException;
import com.cisco.cmad.rogastis.api.InvalidQuestionException;
import com.cisco.cmad.rogastis.api.InvalidUserException;
import com.cisco.cmad.rogastis.api.Post;
import com.cisco.cmad.rogastis.api.PostNotFoundException;
import com.cisco.cmad.rogastis.api.Question;
import com.cisco.cmad.rogastis.api.QuestionNotFoundException;
import com.cisco.cmad.rogastis.api.Rogastis;
import com.cisco.cmad.rogastis.api.User;
import com.cisco.cmad.rogastis.api.UserNotFoundException;
import com.cisco.cmad.rogastis.db.PostDAO;
import com.cisco.cmad.rogastis.db.QuestionDAO;
import com.cisco.cmad.rogastis.db.UserDAO;

public class RogastisService implements Rogastis {

	private UserDAO udao;
	private QuestionDAO qdao;
	private PostDAO pdao;

	public RogastisService() {
		udao = new UserDAO();
		qdao = new QuestionDAO();
		pdao = new PostDAO();
	}

	public List<User> getAllUsers() {
		if (udao.readAll() != null)
			return udao.readAll();
		throw new UserNotFoundException();
	}

	public User registerUser(User user) {
		if (user == null || user.getFirstName() == null
				|| user.getLastName() == null || user.getLoginId() == null
				|| user.getPassword() == null)
			throw new InvalidUserException();
		if (udao.isLoginIdPresent(user.getLoginId()))
			throw new DuplicateUserException();
		User newuser = udao.create(user);
		return newuser;
	}

	public User getUserByUserId(ObjectId userId) {
		if (udao.isUserIdPresent(userId))
			return udao.read(userId);
		throw new UserNotFoundException();
	}

	public User getUserByLoginId(String loginId) {
		if (udao.isLoginIdPresent(loginId))
			return udao.readByLoginId(loginId);
		throw new UserNotFoundException();
	}

	public Question postQuestion(Question question, ObjectId userId) {
		if (question == null || question.getQuestion() == null)
			throw new InvalidQuestionException();
		Question newquestion = qdao.create(question, userId);
		return newquestion;
	}

	public Post addPost(Post post, ObjectId questionId) {
		if (post == null || post.getPost() == null)
			throw new InvalidAnswerException();
		Post newpost = pdao.create(post, questionId);
		return newpost;
	}

	public List<Post> getPosts(ObjectId questionId) {
		if (qdao.isPresent(questionId))
			return pdao.readPosts(questionId);
		throw new QuestionNotFoundException();
	}

	public List<Question> findQuestions(String searchstring) {
		if (qdao.Find(searchstring) != null)
			return qdao.Find(searchstring);
		throw new QuestionNotFoundException();
	}

	public void updateLikes(ObjectId postId) {
		if (pdao.isPresent(postId))
			pdao.updateLikes(postId);
		else
			throw new PostNotFoundException();
	}

	public void updateDislikes(ObjectId postId) {
		if (pdao.isPresent(postId))
			pdao.updateDisLikes(postId);
		else
			throw new PostNotFoundException();
	}

	public Question getQuestion(ObjectId questionId) {
		if (qdao.isPresent(questionId))
			return qdao.read(questionId);
		throw new QuestionNotFoundException();
	}

	public List<Question> getAllQuestions() {
		if (qdao.readAll() != null)
			return qdao.readAll();
		throw new QuestionNotFoundException();
	}

	public List<Question> getUserQuestions(ObjectId userId) {
		if (qdao.readUserAll(userId) != null)
			return qdao.readUserAll(userId);
		throw new QuestionNotFoundException();
	}

	public List<Question> getUserNumOfQuestions(ObjectId userId, Integer count) {
		if (qdao.readUserQuestionsCount(userId, count) != null)
			return qdao.readUserQuestionsCount(userId, count);
		throw new QuestionNotFoundException();
	}

	public List<Question> getNumOfQuestions(Integer count) {
		if (qdao.readNumOfQuestions(count) != null)
			return qdao.readNumOfQuestions(count);
		throw new QuestionNotFoundException();
	}
}
