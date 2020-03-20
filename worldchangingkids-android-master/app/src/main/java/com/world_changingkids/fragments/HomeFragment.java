package com.world_changingkids.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.world_changingkids.HowToUseAppActivity;
import com.world_changingkids.MainActivity;
import com.world_changingkids.NotesToAdultActivity;
import com.world_changingkids.R;

/**
 *A fragment to display the app home page
 */
public class HomeFragment extends Fragment {
	/**
	 * the notes To Adults Button
	 */
	private ImageButton notesToAdultsButton;
	/**
	 * the how To Use App Button
	 */
	private ImageButton howToUseAppButton;

	/**
	 * required default constructor
	 */
	public HomeFragment() { }

	/**
	 * bind the layout to fragment when creating the view
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(true);

		return inflater.inflate(R.layout.fragment_home, container, false);


	}

	/**
	 *
	 * This event is triggered soon after onCreateView().
	 * Any view setup should occur here.  E.g., view lookups and attaching view listeners.
 	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		howToUseAppButton=view.findViewById(R.id.button_how_to_use_this_app);
		howToUseAppButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), HowToUseAppActivity.class);
				startActivity(intent);
			}
		});
		notesToAdultsButton=view.findViewById(R.id.button_note_to_adults);
		notesToAdultsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), NotesToAdultActivity.class);
				startActivity(intent);
			}
		});
	}

}
