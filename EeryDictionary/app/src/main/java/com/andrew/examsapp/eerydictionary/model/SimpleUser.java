package com.andrew.examsapp.eerydictionary.model;


public class SimpleUser {

	private int user_id;
	private String username;
	private String email;
	private String created_at;

	private int wordsCount;


	public SimpleUser() {
	}

	public SimpleUser(String username, String email, String created_at) {
		this.username = username;
		this.email = email;
		this.created_at = created_at;

		wordsCount = 0;
	}

	public SimpleUser(int user_id, String username, String email, String created_at) {
		this(username, email, created_at);
		this.user_id = user_id;
	}


	public int getUserId() {
		return user_id;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	public int getWordsCount() {
		return wordsCount;
	}

	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
	}
}
