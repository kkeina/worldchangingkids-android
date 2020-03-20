package com.world_changingkids.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.ActOfKindnessRecyclerAdapter;
import com.world_changingkids.ActsOfKindnessListAdapter;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.ActOfKindness;
import com.world_changingkids.model.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A Fragment to handle the list of ActsOfKindness
 */
public class ActsOfKindnessListFragment extends Fragment
		implements CompoundButton.OnCheckedChangeListener, ActOfKindnessDetailsFragment.ActOfKindnessPassBackListener {

	/**
	 * A Profile instance indicates which user's profile is playing
	 */
	private Profile mActiveProfile;
	/**
	 * View elements below:
	 */
	private ToggleButton mGivingToggleButton;
	private ToggleButton mListToggleButton;
	private ToggleButton mCommunityToggleButton;
	private ToggleButton mCookingToggleButton;
	private ToggleButton mArtToggleButton;
	private ToggleButton mFamilyToggleButton;

	private ViewPager mViewpager;
	private RecyclerView mViewList;
	/**
	 * A listadapter to render listview
	 */
	private ActsOfKindnessListAdapter mActsOfKindnessListAdapter;

	private ActOfKindnessRecyclerAdapter mActoActOfKindnessRecyclerAdapter;
	/**
	 * An arraylist to hold models
	 */
	private ArrayList mFilteredActOfKindnessModels;
	/**
	 * A map to hold ActOfKindnessCategories
	 */
	private Map<ActOfKindness.ActOfKindnessCategory, Boolean> mCategoryFilterStatus;

	/**
	 * Default constructor
	 */
	public ActsOfKindnessListFragment() { }

	/**
	 * Initial lifecycle method
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActiveProfile = ((MainActivity)getActivity()).getActiveAccount().retrieveProfile();
		mFilteredActOfKindnessModels = (ArrayList) mActiveProfile.getActsOfKindnessCollection().getActsOfKindnessList().clone();

		mCategoryFilterStatus = new HashMap<>();
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.COMMUNITY, true);
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.COOKING, true);
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.ART, true);
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.FAMILY, true);
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.GIVING, true);
		mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.LISTVIEW, true);

	}

	/**
	 * Init views and bind actions
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return view of fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(true);

		View v = inflater.inflate(R.layout.fragment_acts_of_kindness_list, container, false);

		mGivingToggleButton = v.findViewById(R.id.toggle_button_giving);
		mGivingToggleButton.setOnCheckedChangeListener(this);

        mListToggleButton = v.findViewById(R.id.toggle_button_list);
        mListToggleButton.setOnCheckedChangeListener(this);

		mCommunityToggleButton = v.findViewById(R.id.toggle_button_community);
		mCommunityToggleButton.setOnCheckedChangeListener(this);

		mCookingToggleButton = v.findViewById(R.id.toggle_button_cooking);
		mCookingToggleButton.setOnCheckedChangeListener(this);

		mArtToggleButton = v.findViewById(R.id.toggle_button_art);
		mArtToggleButton.setOnCheckedChangeListener(this);

		mFamilyToggleButton = v.findViewById(R.id.toggle_button_family);
		mFamilyToggleButton.setOnCheckedChangeListener(this);

		mViewpager = v.findViewById(R.id.aok_view_pager);
        mViewList = v.findViewById(R.id.r_view_List);

        mViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

		return v;
	}

	/**
	 * A method to handle checked button changed
	 * @param buttonView
	 * @param isChecked
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.toggle_button_giving:
				mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.GIVING, isChecked);
				break;
			case R.id.toggle_button_community:
				mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.COMMUNITY, isChecked);
				break;
			case R.id.toggle_button_cooking:
				mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.COOKING, isChecked);
				break;
			case R.id.toggle_button_art:
				mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.ART, isChecked);
				break;
			case R.id.toggle_button_family:
				mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.FAMILY, isChecked);
            case R.id.toggle_button_list:
                mCategoryFilterStatus.put(ActOfKindness.ActOfKindnessCategory.LISTVIEW, isChecked);
                if (isChecked){
                    mViewList.setVisibility(View.VISIBLE);
                    mViewpager.setVisibility(View.GONE);
                }else {
                    mViewList.setVisibility(View.GONE);
                    mViewpager.setVisibility(View.VISIBLE);
                }
				break;
		}

		filterItemsByCategory();
	}

	/**
	 * A method to handle data back
	 * @param actOfKindness
	 * @param positionIndex
	 */
	@Override
	public void passDataBack(ActOfKindness actOfKindness, int positionIndex) {
		updateFirestoreRecords(actOfKindness, positionIndex);
	}

	private void updateFirestoreRecords(ActOfKindness actOfKindness, int positionIndex) {
		mActiveProfile.getActsOfKindnessCollection().getActsOfKindnessList().set(positionIndex, actOfKindness);
		String accountDocId = ((MainActivity) getContext()).getAccountDocID();
		String profileDocId = ((MainActivity) getContext()).getProfileDocID();

		FirebaseFirestore.getInstance()
				.collection("Accounts").document(accountDocId)
				.collection("Profiles").document(profileDocId)
				.update("actsOfKindnessCollection", mActiveProfile.getActsOfKindnessCollection());
	}

	private void filterItemsByCategory() {
		Profile activeProfile = ((MainActivity)getActivity()).getActiveAccount().retrieveProfile();
		mFilteredActOfKindnessModels = (ArrayList) activeProfile.getActsOfKindnessCollection().getActsOfKindnessList().clone();
		Iterator it = mFilteredActOfKindnessModels.iterator();
		while (it.hasNext()) {
			ActOfKindness aok = (ActOfKindness) it.next();
			if (!isCategoryVisible(aok.getCategory())) {
				it.remove();
			}
		}

		updateUI();
	}

	public void notifyAdaptyerDataSetChanged(){
		mActsOfKindnessListAdapter.notifyDataSetChanged();
	}

	private void updateUI() {
		Collections.sort(mFilteredActOfKindnessModels);
        mActoActOfKindnessRecyclerAdapter = new ActOfKindnessRecyclerAdapter(mFilteredActOfKindnessModels,this,getActivity());
		mActsOfKindnessListAdapter = new ActsOfKindnessListAdapter(this, mFilteredActOfKindnessModels);
		mViewpager.setAdapter(null);
		mViewpager.setAdapter(mActsOfKindnessListAdapter);

      mViewList.setAdapter(null);
      mViewList.setAdapter(mActoActOfKindnessRecyclerAdapter);
	}

	private boolean isCategoryVisible(ActOfKindness.ActOfKindnessCategory category) {
		return mCategoryFilterStatus.get(category);
	}

}
