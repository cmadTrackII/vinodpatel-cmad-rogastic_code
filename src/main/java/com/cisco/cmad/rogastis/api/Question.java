package com.cisco.cmad.rogastis.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "questions", noClassnameStored = true)
@Indexes({ @Index("questionId"), @Index("question") })
public class Question {

	@Id
	protected ObjectId questionId;
	protected String question;
	protected ObjectId userId;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionId() {
		return questionId.toString();
	}

	public void setQuestionId(ObjectId questionId) {
		this.questionId = questionId;
	}

	public String getUserId() {
		return userId.toString();
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

}
