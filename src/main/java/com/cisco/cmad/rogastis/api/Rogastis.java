package com.cisco.cmad.rogastis.api;

import java.util.List;

import org.bson.types.ObjectId;

public interface Rogastis {

	public List<User> getAllUsers();

	public User registerUser(User user);

	public User getUserByUserId(ObjectId userId);

	public User getUserByLoginId(String loginId);

	public Question postQuestion(Question question, ObjectId userId);

	public Question getQuestion(ObjectId questionId);

	public List<Question> getAllQuestions();

	public List<Question> getNumOfQuestions(Integer count);

	public List<Question> getUserQuestions(ObjectId userId);

	public List<Question> getUserNumOfQuestions(ObjectId userId, Integer count);

	public List<Question> findQuestions(String searchstring);

	public Post addPost(Post post, ObjectId questionId);

	public List<Post> getPosts(ObjectId questionId);

	public void updateLikes(ObjectId postId);

	public void updateDislikes(ObjectId postId);
}
