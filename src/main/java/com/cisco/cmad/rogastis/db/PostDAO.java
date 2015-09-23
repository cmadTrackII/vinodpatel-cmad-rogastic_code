package com.cisco.cmad.rogastis.db;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.cisco.cmad.rogastis.api.Post;
import com.cisco.cmad.rogastis.api.PostNotFoundException;
import com.cisco.cmad.rogastis.api.Question;
import com.cisco.cmad.rogastis.api.QuestionNotFoundException;
import com.cisco.cmad.rogastis.api.User;

public class PostDAO {

	private static Datastore datastore;
	private static MongoConnectionManager instance;

	public PostDAO() {
		instance = MongoConnectionManager.getInstance();
		datastore = instance.getDatastore();
		datastore.ensureIndexes();
	}

	public Post create(Post post, ObjectId questionId) {
		Question question = null;
		question = (Question) datastore.get(Question.class, questionId);
		if (question == null)
			throw new QuestionNotFoundException();
		post.setQuestionId(questionId);
		post.setLikes(0);
		post.setDislikes(0);
		post.setPostedOn(System.currentTimeMillis() / 1000l);

		User user = (User) datastore.get(User.class, post.getUserId());

		post.setUserName(user.getFirstName() + ' ' + user.getLastName());

		datastore.save(post);

		return post;
	}

	public List<Post> readPosts(ObjectId questionId) {

		Query<Post> query = datastore.createQuery(Post.class);
		query.and(query.criteria("questionId").equal(questionId));

		if (datastore.getCount(query) > 0)
			return query.order("postedOn").asList();
		else
			throw new PostNotFoundException();
	}

	public boolean isPresent(ObjectId postId) {

		Query<Post> query = datastore.createQuery(Post.class);
		query.and(query.criteria(Mapper.ID_KEY).equal(postId));

		if (datastore.getCount(query) > 0)
			return true;
		else
			return false;
	}

	public void updateLikes(ObjectId postId) {

		Query<Post> updatequery = datastore.createQuery(Post.class)
				.field(Mapper.ID_KEY).equal(postId);
		UpdateOperations<Post> ops = datastore.createUpdateOperations(
				Post.class).inc("likes");
		datastore.update(updatequery, ops);
	}

	public void updateDisLikes(ObjectId postId) {
		Query<Post> updatequery = datastore.createQuery(Post.class)
				.field(Mapper.ID_KEY).equal(postId);
		UpdateOperations<Post> ops = datastore.createUpdateOperations(
				Post.class).inc("dislikes");
		datastore.update(updatequery, ops);
	}

}
