package com.world_changingkids.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ActOfKindness implements Comparable {

	public enum ActOfKindnessCategory implements Serializable {
		NONE,
		COMMUNITY,
		COOKING,
		ART,
		FAMILY,
		GIVING,
		LISTVIEW
	}

	// Static AOK fields
	private int mNumber;
	private String mTitle;
	private String mDescription;
	private String mQuestion;
	private int mAudioResourceId;
	private ActOfKindnessCategory mCategory;

	// Dynamic AOK fields - profile-specific data
	private boolean mIsCompleted;
	private String mNotes;

	public ActOfKindness() {
		//public no-arg constructor needed
	}

	public ActOfKindness(int number, String title, String description, String question, int audioResourceId, ActOfKindnessCategory category) {
		mNumber = number;
		mTitle = title;
		mDescription = description;
		mQuestion = question;
		mAudioResourceId = audioResourceId;
		mCategory = category;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		ActOfKindness aok = (ActOfKindness) o;

		if (this.getNumber() < aok.getNumber()) {
			return -1;
		} else if (getNumber() > aok.getNumber()) {
			return 1;
		}

		return 0;
	}

	public int getNumber() {
		return mNumber;
	}

	public void setNumber(int number) {
		mNumber = number;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getQuestion() {
		return mQuestion;
	}

	public void setQuestion(String question) {
		mQuestion = question;
	}

	public int getAudioResourceId() {
		return mAudioResourceId;
	}

	public void setAudioResourceId(int audioResourceId) {
		mAudioResourceId = audioResourceId;
	}

	public boolean isCompleted() {
		return mIsCompleted;
	}

	public void setCompleted(boolean completed) {
		mIsCompleted = completed;
	}

	public String getNotes() {
		return mNotes;
	}

	public void setNotes(String notes) {
		mNotes = notes;
	}

	public ActOfKindnessCategory getCategory() {
		return mCategory;
	}

	public void setCategory(ActOfKindnessCategory category) {
		mCategory = category;
	}
}
