package com.andrew.examsapp.eerydictionary.model;


public class SimpleWord {

	private int word_id;
	private int user_id;
	private String word;
	private String description;
	private String examples;
	private int likes_count;
	private int dislikes_count;
	private String createdAt;

	public SimpleWord() {
	}

	public SimpleWord(int word_id, int user_id, String word, String description, String examples, String createdAt) {
		this.word_id = word_id;
		this.user_id = user_id;
		this.word = word;
		this.description = description;
		this.examples = examples;
		this.createdAt = createdAt;
	}



	public int getWordId() {
		return word_id;
	}

	public void setWordId(int word_id) {
		this.word_id = word_id;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExamples() {
		return examples;
	}

	public void setExamples(String examples) {
		this.examples = examples;
	}

	public int getLikesCount() {
		return likes_count;
	}

	public void setLikesCount(int likes_count) {
		this.likes_count = likes_count;
	}

	public int getDislikesCount() {
		return dislikes_count;
	}

	public void setDislikesCount(int dislikes_count) {
		this.dislikes_count = dislikes_count;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
