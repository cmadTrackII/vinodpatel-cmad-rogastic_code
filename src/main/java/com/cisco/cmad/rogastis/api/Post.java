package com.cisco.cmad.rogastis.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "posts", noClassnameStored = true)
@Indexes({ @Index("postId"), @Index("postedOn"), @Index("post") })
public class Post {

	@Id
	protected ObjectId postId;
	protected String post;
	protected ObjectId userId;
	protected ObjectId questionId;
	protected long postedOn;
	protected Integer likes;
	protected Integer dislikes;
	protected String userName;

	public String getPostId() {
		return postId.toString();
	}

	public void setPostId(ObjectId postId) {
		this.postId = postId;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId.toString();
	}

	public void setQuestionId(ObjectId questionId) {
		this.questionId = questionId;
	}

	public long getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(long postedOn) {
		this.postedOn = postedOn;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
