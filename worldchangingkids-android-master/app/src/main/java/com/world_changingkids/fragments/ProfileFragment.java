package com.world_changingkids.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.Account;
import com.world_changingkids.model.ActOfKindness;
import com.world_changingkids.model.Profile;

/**
 * A Fragment to display Profile and update data of profile
 */
public class ProfileFragment extends Fragment {
	/**
	 * View elements below
	 */
	ProgressBar proBar;
	private Profile profile;
	private TextView level;
	private ImageView avatar;
	private ImageView proBtnCustom;
	/**
	 * Navigation controller
	 */
	private NavController mNavController;

	/**
	 * Required empty public constructor
	 */
	public ProfileFragment() {

	}

	/**
	 * OnCreate lifecycle method
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * onCreateView lifecycle method to set layout to view
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	/**
	 * called when view has been created
	 * @param view
	 * @param savedInstanceState
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Setup any handles to view objects here
		setUpFragment(view);
	}

	/**
	 * Bind class variables to the views
	 * @param view
	 */
	public void setUpFragment(View view) {
		proBar = view.findViewById(R.id.proProgressBar);
		level =view.findViewById(R.id.proTxtName);
		avatar = view.findViewById(R.id.proImgAvatar);
		proBtnCustom = view.findViewById(R.id.proBtnCustom);
		mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.nav_host_fragment))
				.getNavController();
		profile = ((MainActivity)getActivity()).getActiveAccount().retrieveProfile();
		int sum =0;
		for(ActOfKindness a: profile.getActsOfKindnessCollection().getActsOfKindnessList()){
			if(a.isCompleted()){
				sum++;
			}
		}
		//set progressbar's max and current finished progress
		proBar.setMax(profile.getActsOfKindnessCollection().getActsOfKindnessList().size());
		proBar.setProgress(sum);

		String Lvl = "Level "+sum;
		//set level number
		level.setText(Lvl);
		String accID  = ((MainActivity) getActivity()).getAccountDocID();

		//set avatar
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//
//			}
//		},100);
		if (MainActivity.check){
			updateAvatar(MainActivity.selectedImage);
		}
		else {
			setAvatar(profile.getGrade());
		}
		proBtnCustom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mNavController.navigate(R.id.action_select_avatar);
			}
		});
	}

	public void updateAvatar(Integer id){
		avatar.setImageResource(id);
	}
	/**
	 * Set avatar for profile
	 * @param index
	 */
	private void setAvatar(int index) {
		switch(index){
			case 1:
				avatar.setImageResource(R.drawable.avatar1);
				break;
			case 2:
				avatar.setImageResource(R.drawable.avatar2);
				break;
			case 3:
				avatar.setImageResource(R.drawable.avatar3);
				break;
			case 4:
				avatar.setImageResource(R.drawable.avatar4);
				break;
			case 5:
				avatar.setImageResource(R.drawable.avatar5);
				break;
			default:
				avatar.setImageResource(R.drawable.avatar1);
				break;
		}

	}
}
