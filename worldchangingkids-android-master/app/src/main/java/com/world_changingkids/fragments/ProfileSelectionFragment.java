package com.world_changingkids.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.Account;
import com.world_changingkids.model.Profile;

import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.model.Account;
import com.google.firebase.auth.FirebaseUser;
import com.world_changingkids.model.Profile;

/**
 *A fragment to display profiles and handle profile selection function
 */
public class ProfileSelectionFragment extends Fragment {
	/**
	 * View elements below:
	 */
	private ImageButton ADDPROFILE;
	private ImageButton PROFILE1;
	private ImageButton PROFILE2;
	private ImageButton PROFILE3;
	private ImageButton PROFILE4;
	private ImageView SPINNER;
	private AnimationDrawable SPINNER_ANIMATION;
	private TextView PROFILE1_NAME;
	private TextView PROFILE2_NAME;
	private TextView PROFILE3_NAME;
	private TextView PROFILE4_NAME;
	private TextView INSTRUCTIONS;
	private TextView ADDPROFILE_TEXT;

	//private ImageButton MANAGEPROFILE;
	/**
	 * Navigation controller
	 */
	private NavController mNavController;
	private static final String TAG = "Select Profile";
	private CollectionReference accounts;
	private FirebaseAuth mAuth;
	private Account parentAccount;
	private Profile profile;
	private FirebaseUser user;
	private FirebaseFirestore db ;
	private String accountID = "";
	private String [] profileID;
	private int childPosition = 0;
	View view;

	/**
	 * Default constructor
	 */
	public ProfileSelectionFragment() { }


	/**
	 * // This event fires 2nd, before views are created for the fragment
	 * 	// The onCreate method is called when the Fragment instance is being created, or re-created.
	 * 	// Use onCreate for any standard setup that does not require the activity to be fully created
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
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
		((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(false);

		return inflater.inflate(R.layout.fragment_profile_selection, container, false);
	}

	/**
	 * Setup any handles to view objects here
	 * @param view
	 * @param savedInstanceState
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		this.view = view;
		setUpFragment(view);
		setupView();

	}

	/**
	 * initialize instances
	 */
	public void initialize(){
		Log.d("ProfileSelectionFrag","initialize");

		mAuth = FirebaseAuth.getInstance();
		user = mAuth.getInstance().getCurrentUser();
		db = FirebaseFirestore.getInstance();
		profileID = new String[4];
		childPosition = 0;
		populateAccount();
	}

	/**
	 * Bind class variables to views
	 * @param view
	 */
	public void setUpFragment(View view) {
		ADDPROFILE = view.findViewById(R.id.addProfileButton);
		PROFILE1 = view.findViewById(R.id.profile1Button);
		PROFILE2 = view.findViewById(R.id.profile2Button);
		PROFILE3 = view.findViewById(R.id.profile3Button);
		PROFILE4 = view.findViewById(R.id.profile4Button);
		PROFILE1_NAME = view.findViewById(R.id.profile1Name);
		PROFILE2_NAME = view.findViewById(R.id.profile2Name);
		PROFILE3_NAME = view.findViewById(R.id.profile3Name);
		PROFILE4_NAME = view.findViewById(R.id.profile4Name);
		INSTRUCTIONS = view.findViewById(R.id.instruction_Text);
		ADDPROFILE_TEXT  = view.findViewById(R.id.addButtonText);
		SPINNER = view.findViewById(R.id.plant);
		SPINNER.setBackgroundResource(R.drawable.animation_profile_selection);
		SPINNER_ANIMATION = (AnimationDrawable) SPINNER.getBackground();
		SPINNER_ANIMATION.start();

		mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.nav_host_fragment))
				.getNavController();

		ADDPROFILE.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addProfile();
			}
		});
		PROFILE1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity)getActivity()).getActiveAccount().setProfilePosition(0);
				((MainActivity)getActivity()).setProfileDocumentReference(profileID[0]);
				((MainActivity)getActivity()).updateCornerProfileButton(PROFILE1.getBackground());
				chooseProfile();
			}
		});
		PROFILE2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity)getActivity()).getActiveAccount().setProfilePosition(1);
				((MainActivity)getActivity()).setProfileDocumentReference(profileID[1]);
				((MainActivity)getActivity()).updateCornerProfileButton(PROFILE2.getBackground());
				chooseProfile();
			}
		});
		PROFILE3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity)getActivity()).getActiveAccount().setProfilePosition(2);
				((MainActivity)getActivity()).setProfileDocumentReference(profileID[2]);
				((MainActivity)getActivity()).updateCornerProfileButton(PROFILE3.getBackground());
				chooseProfile();
			}
		});
		PROFILE4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity)getActivity()).getActiveAccount().setProfilePosition(3);
				((MainActivity)getActivity()).setProfileDocumentReference(profileID[3]);
				((MainActivity)getActivity()).updateCornerProfileButton(PROFILE4.getBackground());
				chooseProfile();
			}
		});
	}

	/**
	 * add profile
	 */
	private void addProfile() {
		mNavController.navigate(R.id.action_ProfileSelection_to_AddProfile);

	}

	/**
	 * choose profile
	 */
	private void chooseProfile() {
		mNavController.navigate(R.id.action_ProfileSelection_to_HomePage);
	}

	private void setupView(){
		Log.d("ProfileSelectionFrag","setupView()");

		/*if this method gets called before finishing query, make sure it doesn't crash*/
		if (parentAccount == null){
			Log.d("ProfileSelectionFrag","setupView() parentAccount == null");

			return;
		}

		if (SPINNER_ANIMATION.isRunning()){
			SPINNER_ANIMATION.stop();
			SPINNER.setVisibility(View.GONE);
		}
		switch(parentAccount.retrieveNumOfProfiles()){
			case 1:
				PROFILE1_NAME.setText(parentAccount.getProfileName(0));
				setAvatar(PROFILE1,parentAccount.getmProfiles().get(0).getGrade());
				PROFILE1.setVisibility(View.VISIBLE);
				PROFILE1_NAME.setVisibility(View.VISIBLE);
				ADDPROFILE.setVisibility(View.VISIBLE);
				ADDPROFILE_TEXT.setVisibility(View.VISIBLE);
				INSTRUCTIONS.setVisibility(View.INVISIBLE);

				break;

			case 2:
				PROFILE1_NAME.setText(parentAccount.getProfileName(0));
				setAvatar(PROFILE1,parentAccount.getmProfiles().get(0).getGrade());
				PROFILE1.setVisibility(View.VISIBLE);
				PROFILE1_NAME.setVisibility(View.VISIBLE);
				PROFILE2_NAME.setText(parentAccount.getProfileName(1));
				setAvatar(PROFILE2,parentAccount.getmProfiles().get(1).getGrade());
				PROFILE2.setVisibility(View.VISIBLE);
				PROFILE2_NAME.setVisibility(View.VISIBLE);
				ADDPROFILE.setVisibility(View.VISIBLE);
				ADDPROFILE_TEXT.setVisibility(View.VISIBLE);
				INSTRUCTIONS.setVisibility(View.INVISIBLE);
				break;

			case 3:
				PROFILE1_NAME.setText(parentAccount.getProfileName(0));
				setAvatar(PROFILE1,parentAccount.getmProfiles().get(0).getGrade());
				PROFILE1.setVisibility(View.VISIBLE);
				PROFILE1_NAME.setVisibility(View.VISIBLE);
				PROFILE2_NAME.setText(parentAccount.getProfileName(1));
				setAvatar(PROFILE2,parentAccount.getmProfiles().get(1).getGrade());
				PROFILE2.setVisibility(View.VISIBLE);
				PROFILE2_NAME.setVisibility(View.VISIBLE);
				PROFILE3_NAME.setText(parentAccount.getProfileName(2));
				setAvatar(PROFILE3,parentAccount.getmProfiles().get(2).getGrade());
				PROFILE3.setVisibility(View.VISIBLE);
				PROFILE3_NAME.setVisibility(View.VISIBLE);
				ADDPROFILE.setVisibility(View.VISIBLE);
				ADDPROFILE_TEXT.setVisibility(View.VISIBLE);
				INSTRUCTIONS.setVisibility(View.INVISIBLE);


				break;
			case 4:
				PROFILE1_NAME.setText(parentAccount.getProfileName(0));
				setAvatar(PROFILE1,parentAccount.getmProfiles().get(0).getGrade());
				PROFILE1.setVisibility(View.VISIBLE);
				PROFILE1_NAME.setVisibility(View.VISIBLE);

				PROFILE2_NAME.setText(parentAccount.getProfileName(1));
				setAvatar(PROFILE2,parentAccount.getmProfiles().get(1).getGrade());

				PROFILE2.setVisibility(View.VISIBLE);
				PROFILE2_NAME.setVisibility(View.VISIBLE);

				PROFILE3_NAME.setText(parentAccount.getProfileName(2));
				setAvatar(PROFILE3,parentAccount.getmProfiles().get(2).getGrade());
				PROFILE3.setVisibility(View.VISIBLE);
				PROFILE3_NAME.setVisibility(View.VISIBLE);

				PROFILE4_NAME.setText(parentAccount.getProfileName(3));
				setAvatar(PROFILE4,parentAccount.getmProfiles().get(3).getGrade());

				PROFILE4.setVisibility(View.VISIBLE);
				PROFILE4_NAME.setVisibility(View.VISIBLE);
				ADDPROFILE.setVisibility(View.INVISIBLE);
				ADDPROFILE_TEXT.setVisibility(View.INVISIBLE);
				INSTRUCTIONS.setVisibility(View.INVISIBLE);
				break;

			default:
				INSTRUCTIONS.setVisibility(View.VISIBLE);
				ADDPROFILE.setVisibility(View.VISIBLE);
				ADDPROFILE_TEXT.setVisibility(View.VISIBLE);
				break;
		}

	}

	private void setAvatar(ImageButton avatar,int index) {
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

	public void populateAccount() {

		db.getInstance().collection("Accounts").get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

						for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
							//Log.d(TAG, document.getId() + " => " + document.getString("email"));
							if (user.getEmail().equals(document.getString("email").toLowerCase())) {
								accountID = document.getId();
								((MainActivity) getActivity()).setAccountDocumentReference(accountID);
								parentAccount = document.toObject(Account.class);
								parentAccount.clearNumOfProfiles();
								((MainActivity) getActivity()).setActiveAccount(parentAccount);
								break;
							}
						}

						if (parentAccount != null) {
							Log.d("ProfileSelectionFrag","parentAccount != null");
							db.getInstance().collection("Accounts").document(accountID).collection("Profiles").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
								@Override
								public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
									for (QueryDocumentSnapshot profileDocument : queryDocumentSnapshots) {

										if (profileDocument.exists()) {
											Log.d("ProfileSelectionFrag","profileDocument.exists()");

											profileID[childPosition] = profileDocument.getId();
											profile = profileDocument.toObject(Profile.class);
											Log.d("ProfileSelectionFrag","profile="+profile.getName());

											((MainActivity) getActivity()).setProfile(profile);
											//Log.d(TAG, profileDocument.getString("name") + " => " + ((MainActivity) getActivity()).getActiveAccount().getProfileName(childPosition));
											childPosition++;
										}

									}
									setupView();
								}

							});
						}
					}

				});
	}

}

