package com.world_changingkids.model;

import android.content.Context;
import android.util.Log;

import com.world_changingkids.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class ActsOfKindnessCollection implements Serializable {

	private static ActsOfKindnessCollection instance;

	private ArrayList<ActOfKindness> mActsOfKindnessList;
	private Context mContext;
	private static final String TAG = "ACTSOFKINDNESS";

	public ActsOfKindnessCollection() { }

	private ActsOfKindnessCollection(Context context) {
		mContext = context;
		mActsOfKindnessList = new ArrayList<>();
		loadActsOfKindness();
	}

	public static ActsOfKindnessCollection getInstance(Context context){
		if(instance == null) {
			instance = new ActsOfKindnessCollection(context);
		}
 
		return instance;
	}


	private void loadActsOfKindness() {
		int number = 1;
		String title;
		String description;
		String question;
		String categoryString;
		ActOfKindness.ActOfKindnessCategory category;

		InputStream titlesStream = mContext.getResources().openRawResource(R.raw.aok_titles);
		BufferedReader titlesBuffer = new BufferedReader(new InputStreamReader(titlesStream));

		InputStream descriptionsStream = mContext.getResources().openRawResource(R.raw.aok_descriptions);
		BufferedReader descriptionsBuffer = new BufferedReader(new InputStreamReader(descriptionsStream));

		InputStream questionsStream = mContext.getResources().openRawResource(R.raw.aok_questions);
		BufferedReader questionsBuffer = new BufferedReader(new InputStreamReader(questionsStream));

		InputStream categoriesStream = mContext.getResources().openRawResource(R.raw.aok_categories);
		BufferedReader categoriesBuffer = new BufferedReader(new InputStreamReader(categoriesStream));

		try {
			while ((title = titlesBuffer.readLine()) != null &&
					(description = descriptionsBuffer.readLine()) != null &&
					(question = questionsBuffer.readLine()) != null &&
					(categoryString = categoriesBuffer.readLine()) != null) {

				int audio = mContext.getResources()
						.getIdentifier("aok_" + number, "raw", mContext.getPackageName());

				switch (categoryString) {
					case "community":
						category = ActOfKindness.ActOfKindnessCategory.COMMUNITY;
						break;
					case "cooking":
						category = ActOfKindness.ActOfKindnessCategory.COOKING;
						break;
					case "art":
						category = ActOfKindness.ActOfKindnessCategory.ART;
						break;
					case "family":
						category = ActOfKindness.ActOfKindnessCategory.FAMILY;
						break;
					case "giving":
						category = ActOfKindness.ActOfKindnessCategory.GIVING;
						break;
					default:
						category = ActOfKindness.ActOfKindnessCategory.NONE;
				}

				mActsOfKindnessList.add(new ActOfKindness(number, title, description, question, audio, category));
				number++;
			}
		} catch (IOException e) {
			Log.e(TAG, "Error AOK data from file.");
		}
	}

	public ArrayList<ActOfKindness> getActsOfKindnessList() {
		return mActsOfKindnessList;
	}

	public void setActsOfKindnessList(ArrayList<ActOfKindness> actsOfKindnessList){
		mActsOfKindnessList = actsOfKindnessList;
	}

}
