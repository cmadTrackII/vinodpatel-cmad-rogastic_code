package com.cisco.cmad.rogastis.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.cisco.cmad.rogastis.api.Post;
import com.cisco.cmad.rogastis.api.Question;
import com.cisco.cmad.rogastis.api.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnectionManager {

	private static final MongoConnectionManager INSTANCE = new MongoConnectionManager();
	private static final String DB_NAME = "rogastis";

	private Morphia morphia = null;
	private Datastore datastore = null;
	private MongoClient mongoClient = null;

	private MongoConnectionManager() {
		MongoClientURI connectionString = new MongoClientURI(
				"mongodb://localhost:27017");
		morphia = new Morphia();
		morphia.map(User.class).map(Question.class).map(Post.class);
		mongoClient = new MongoClient(connectionString);
		datastore = morphia.createDatastore(mongoClient, DB_NAME);
	}

	public static MongoConnectionManager getInstance() {
		return INSTANCE;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

}