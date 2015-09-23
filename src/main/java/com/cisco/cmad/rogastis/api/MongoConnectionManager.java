package com.cisco.cmad.rogastis.api;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnectionManager {

	private static final MongoConnectionManager INSTANCE = new MongoConnectionManager();

	private final Datastore datastore;
	public static final String DB_NAME = "rogastis";

	private MongoConnectionManager() {
		try {
			MongoClientURI connectionString = new MongoClientURI(
					"mongodb://localhost:27017");
			MongoClient mongoclient = new MongoClient(connectionString);
			Morphia morphia = new Morphia();
			morphia.mapPackage("com.cisco.cmad.rogastis.api");
			datastore = morphia.createDatastore(mongoclient, "rogastis");
			datastore.ensureIndexes();
		} catch (Exception e) {
			throw new RuntimeException("Error initializing mongo db", e);
		}
	}

	public static MongoConnectionManager instance() {
		return INSTANCE;
	}

	public Datastore getDb() {
		return datastore;
	}

}
