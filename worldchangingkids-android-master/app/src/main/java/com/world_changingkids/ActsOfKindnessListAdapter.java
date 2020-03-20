package com.world_changingkids;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.world_changingkids.fragments.ActOfKindnessDetailsFragment;
import com.world_changingkids.model.ActOfKindness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A PagerAdapter subclass to render the row of listview
 */
public class ActsOfKindnessListAdapter extends PagerAdapter {

	private List<ActOfKindness> mFilteredActOfKindnessModels;
	private List<CardView> mActsOfKindnessCardViews;
	private ActOfKindnessDetailsFragment.ActOfKindnessPassBackListener mPassBackListener;

	public ActsOfKindnessListAdapter(ActOfKindnessDetailsFragment.ActOfKindnessPassBackListener pbl, ArrayList<ActOfKindness> filteredModels) {
		mFilteredActOfKindnessModels = filteredModels;
		    mActsOfKindnessCardViews = new ArrayList<>();

		for (int i = 0; i < mFilteredActOfKindnessModels.size(); i++) {
			mActsOfKindnessCardViews.add(null);
		}

		mPassBackListener = pbl;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull final ViewGroup container, final int positionIndex) {
		View v = LayoutInflater.from(container.getContext()).
				inflate(R.layout.fragment_acts_of_kindness_list_item, container, false);

		v.findViewById(R.id.button_view_act)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						createDetailsView(container, positionIndex);
					}
				});

		container.addView(v);
		bind(mFilteredActOfKindnessModels.get(positionIndex), v);
		CardView cardView = v.findViewById(R.id.act_of_kindness_card_view);
		mActsOfKindnessCardViews.set(positionIndex, cardView);

		return v;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
		mActsOfKindnessCardViews.set(position, null);
	}

	@Override
	public int getCount() {
		return mFilteredActOfKindnessModels.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	private void bind(ActOfKindness model, View view) {
		String actOfKindnessTitle =
				String.format(view.getContext()
						.getResources()
						.getString(R.string.aok_list_item_title), model.getNumber(), model.getTitle());
		((TextView) view.findViewById(R.id.text_view_aok_title)).setText(actOfKindnessTitle);
		((TextView) view.findViewById(R.id.text_view_aok_description)).setText(model.getDescription());
		String drawStr="aok_"+model.getNumber();
		int resId = getDrawableId(drawStr);
//		if(resId!=0) ((ImageView) view.findViewById(R.id.act_detail_image)).setImageResource(resId);
		if(model.isCompleted()){
			((ImageView) view.findViewById(R.id.text_view_aok_image)).setImageResource(R.drawable.star_filled_64);
		}else{
			((ImageView) view.findViewById(R.id.text_view_aok_image)).setImageResource(R.drawable.star_black_64);

		}
	}

	private int getDrawableId(String drawableName) {
		try {
			Field field = R.drawable.class.getField(drawableName);
			return field.getInt(null);
		} catch (Exception e) {
            Log.d("AOKListAdapter"," inside getDrawableId() cannot drawable recourse with this name:"+drawableName);
		}
		return 0;
	}

	private void createDetailsView(final ViewGroup container, int positionIndex) {
		ActOfKindnessDetailsFragment aokDetailsFragment = ActOfKindnessDetailsFragment.newInstance(mPassBackListener, positionIndex);

		FragmentManager fm = ((MainActivity) container.getContext()).getSupportFragmentManager();
		fm.beginTransaction()
				.add(R.id.container_act_of_kindness_details, aokDetailsFragment)
				.commit();
	}

}
