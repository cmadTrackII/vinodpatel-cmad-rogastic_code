package com.cisco.cmad.rogastis.db;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.cisco.cmad.rogastis.api.User;
import com.cisco.cmad.rogastis.api.UserNotFoundException;

public class UserDAO {

	private static Datastore datastore;
	private static MongoConnectionManager instance;

	public UserDAO() {
		instance = MongoConnectionManager.getInstance();
		datastore = instance.getDatastore();
		datastore.ensureIndexes();
	}

	public User create(User user) {
		user.setLastLogin(System.currentTimeMillis() / 1000l);
		user.setCreatedOn(System.currentTimeMillis() / 1000l);
		datastore.save(user);
		return user;
	}

	public List<User> readAll() {
		List<User> users = null;
		Query<User> query = datastore.createQuery(User.class);
		users = query.asList();
		if (users == null)
			throw new UserNotFoundException();
		return users;
	}

	public User read(ObjectId userId) {
		User user = null;
		user = (User) datastore.get(User.class, userId);
		if (user == null)
			throw new UserNotFoundException();
		return user;
	}

	public User readByLoginId(String loginId) {

		Query<User> query = datastore.createQuery(User.class);
		query.and(query.criteria("loginId").equal(loginId));

		if (datastore.getCount(query) > 0)
			return query.get();
		else
			throw new UserNotFoundException();
	}

	public boolean isLoginIdPresent(String loginId) {
		Query<User> query = datastore.createQuery(User.class);
		query.and(query.criteria("loginId").equal(loginId));

		if (datastore.getCount(query) > 0)
			return true;
		else
			return false;
	}

	public boolean isUserIdPresent(ObjectId userId) {
		Query<User> query = datastore.createQuery(User.class);
		query.and(query.criteria("userId").equal(userId));

		if (datastore.getCount(query) > 0)
			return true;
		else
			return false;
	}

}
