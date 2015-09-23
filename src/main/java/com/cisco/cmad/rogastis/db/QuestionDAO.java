package com.cisco.cmad.rogastis.db;

import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.cisco.cmad.rogastis.api.Question;
import com.cisco.cmad.rogastis.api.QuestionNotFoundException;
import com.cisco.cmad.rogastis.api.UserNotFoundException;

public class QuestionDAO {

	private static Datastore datastore;
	private static MongoConnectionManager instance;

	public QuestionDAO() {
		instance = MongoConnectionManager.getInstance();
		datastore = instance.getDatastore();
		datastore.ensureIndexes();
	}

	public Question create(Question question, ObjectId userId) {

		question.setUserId(userId);
		datastore.save(question);

		return question;

	}

	public Question read(ObjectId questionId) {
		Question question = null;
		question = (Question) datastore.get(Question.class, questionId);
		if (question == null)
			throw new QuestionNotFoundException();
		return question;
	}

	public List<Question> readAll() {
		List<Question> questions = null;
		Query<Question> query = datastore.createQuery(Question.class);
		questions = query.asList();
		if (questions == null)
			throw new QuestionNotFoundException();
		return questions;
	}

	public List<Question> Find(String searchstring) {
		datastore.ensureIndexes();
		List<Question> questions = null;
		// questions = query.search(searchstring).asList();
		Query<Question> query = datastore.createQuery(Question.class);
		query.and(query.criteria("question").containsIgnoreCase(searchstring));
		// questions = query.filter("question",
		// Pattern.compile(searchstring, Pattern.CASE_INSENSITIVE))
		// .asList();
		if (datastore.getCount(query) > 0)
			questions = query.asList();
		else
			throw new QuestionNotFoundException();
		return questions;
	}

	public boolean isPresent(ObjectId questionId) {
		Query<Question> query = datastore.createQuery(Question.class);
		query.and(query.criteria("questionId").equal(questionId));

		if (datastore.getCount(query) > 0)
			return true;
		else
			return false;
	}

	public List<Question> readUserAll(ObjectId userId) {
		List<Question> questions = null;
		Query<Question> query = datastore.createQuery(Question.class);
		query.and(query.criteria("userId").equal(userId));

		if (datastore.getCount(query) > 0)
			questions = query.asList();
		else if (datastore.getCount(query) == 0)
			throw new QuestionNotFoundException();
		else
			throw new UserNotFoundException();
		return questions;
	}

	public List<Question> readNumOfQuestions(Integer count) {
		List<Question> questions = null;
		Query<Question> query = datastore.createQuery(Question.class);
		questions = query.limit(count).asList();
		if (questions == null)
			throw new QuestionNotFoundException();
		return questions;
	}

	public List<Question> readUserQuestionsCount(ObjectId userId, Integer count) {
		List<Question> questions = null;
		Query<Question> query = datastore.createQuery(Question.class);
		query.and(query.criteria("userId").equal(userId));

		if (datastore.getCount(query) > 0)
			questions = query.limit(count).asList();
		else if (datastore.getCount(query) == 0)
			throw new QuestionNotFoundException();
		else
			throw new UserNotFoundException();
		return questions;
	}
}
