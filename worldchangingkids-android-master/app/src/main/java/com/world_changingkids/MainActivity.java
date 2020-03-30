package com.world_changingkids;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_changingkids.fragments.ProfileFragment;
import com.world_changingkids.model.Account;
import com.world_changingkids.model.ActsOfKindnessCollection;
import com.world_changingkids.model.Profile;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import com.world_changingkids.model.BackgroundSoundService;
import android.content.Intent;

/**
 * The main activity for this application
 */
public class MainActivity extends AppCompatActivity {

    public static boolean check = false;
    public static int selectedImage = 0;
	/**
	 * Navigation controller to navigate pages
	 */
	private NavController mNavController;
	private NavHostFragment  mNavHost;
	/**
	 * FirebaseAuth to handle user authentication
	 */
	protected FirebaseAuth mFirebaseAuth;
	/**
	 * FirebaseUser instance indicates which user
	 */
	protected FirebaseUser mFirebaseUser;
	protected FirebaseAuth.AuthStateListener mAuthListener;
	/**
	 * mActiveAccount indicates current active account
	 */
	private Account mActiveAccount;
	/**
	 * current active account's id
	 */
	private String mActiveAccountID = "";
	private String mActiveProfileDocId = "";
	/**
	 * the collection of ActsOfKindness
	 */
	private ActsOfKindnessCollection mActsOfKindnessCollection;
	/**
	 * The layouts and view elements below:
	 */
	private ConstraintLayout mBottomContainer;
	private LinearLayout mShowButtonsGroup;
	private ImageButton mShowBottomBar;
	private LinearLayout mAllButtonsGroup;
	private ImageButton mHideBottomBar;
	private ImageButton mHomeButton;
	private ImageButton mProfileButton;
	private ImageButton mActsOfKindnessButton;
	private ImageButton mCornerProfileButton;
	private TextView mCornerProfileName;
	private ImageButton bgmButton;//han
	public static Intent svc;//han
	BackgroundSoundService bgmActivity;//han
	AudioController audioController;//han
	Context context;

	/**
	 * onCreate lifecycle method to initialize variables
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		svc=new Intent(this, BackgroundSoundService.class);//han
		startService(svc);//han

		mNavController = ((NavHostFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_host_fragment))
				.getNavController();

        mNavHost=((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment));
		mBottomContainer = findViewById(R.id.bottom_button_container);

		mFirebaseAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				mFirebaseUser = mFirebaseAuth.getCurrentUser();
			}
		};

		mActiveAccount = new Account();

		mActsOfKindnessCollection = new ActsOfKindnessCollection();

		getSupportActionBar().hide();
		setupMainNavButtons();


	}

	/**
	 * when user click
	 * @param v
	 */
	public void onClick(View v){

	}

	/**
	 * onCreateView life cycle method, passing to super class
	 * @param name
	 * @param context
	 * @param attrs
	 * @return
	 */
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	@Override
	public boolean onSupportNavigateUp() {
		return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_IMMERSIVE
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
			);
		}
	}

	public void updateCornerProfileButton(Integer id){
        mCornerProfileButton.setImageResource(id);
        onBackPressed();
        check = true;
        selectedImage = id;
        if (mNavHost.getChildFragmentManager().getFragments().get(0) instanceof ProfileFragment){
            ((ProfileFragment) mNavHost.getChildFragmentManager().getFragments().get(0)).updateAvatar(id);
        }
    }
	/**
	 *updateCornerProfile avatar based on profile's data
	 * @param source
	 */
	public void updateCornerProfileButton(Drawable source) {
		mCornerProfileName.setText(
				String.format(getResources().getString(R.string.main_activity_profile_name),
						mActiveAccount.retrieveProfile().getName()));

		switch(mActiveAccount.retrieveProfile().getGrade()){
			case 1:
				mCornerProfileButton.setImageResource(R.drawable.avatar1);
				break;
			case 2:
				mCornerProfileButton.setImageResource(R.drawable.avatar2);
				break;
			case 3:
				mCornerProfileButton.setImageResource(R.drawable.avatar3);
				break;
			case 4:
				mCornerProfileButton.setImageResource(R.drawable.avatar4);
				break;
			case 5:
				mCornerProfileButton.setImageResource(R.drawable.avatar5);
				break;
			default:
				mCornerProfileButton.setImageResource(R.drawable.avatar1);
				break;
		}
	}

	/*public void updateCornerProfileButton(Drawable source){
		mCornerProfileButton.setImageDrawable(source);
		mCornerProfileName.setText(String.format("Hi, %s", mActiveAccount.retrieveProfile().getName()));
	}*/

	/**
	 * set up navigation buttons
	 */
	private void setupMainNavButtons() {
		mAllButtonsGroup = findViewById(R.id.all_buttons_group);
		mShowButtonsGroup = findViewById(R.id.show_buttons_group);
		mHideBottomBar = findViewById(R.id.hide_button_bar);
		mShowBottomBar = findViewById(R.id.show_bottom_nav_bar_button);
		mHomeButton = findViewById(R.id.home_button);
		mProfileButton = findViewById(R.id.home_profile_button);
		mActsOfKindnessButton = findViewById(R.id.home_acts_of_kindness_button);
		mCornerProfileButton = findViewById(R.id.corner_profile_button);
		mCornerProfileName = findViewById(R.id.corner_profile_name);
		bgmButton = findViewById(R.id.bgm);

		View.OnClickListener navBarListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNavController.popBackStack();

				switch (v.getId()) {
					case R.id.home_button:
						mNavController.navigate(R.id.action_global_homeFragment);
						break;
					case R.id.home_profile_button:
					case R.id.corner_profile_button:
						mNavController.navigate(R.id.action_global_profileFragment);
						break;
					case R.id.home_acts_of_kindness_button:
						mNavController.navigate(R.id.action_global_actsOfKindnessListFragment);
						break;
				}
			}
		};

		mHideBottomBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAllButtonsGroup.setVisibility(View.GONE);
				mShowButtonsGroup.setVisibility(View.VISIBLE);
			}
		});

		mShowBottomBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mAllButtonsGroup.setVisibility(View.VISIBLE);
				mShowButtonsGroup.setVisibility(View.GONE);
			}
		});

		mHomeButton.setOnClickListener(navBarListener);
		mProfileButton.setOnClickListener(navBarListener);
		mActsOfKindnessButton.setOnClickListener(navBarListener);
		mCornerProfileButton.setOnClickListener(navBarListener);

		//han
		bgmButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				try{
					if(BackgroundSoundService.player.isLooping()) {
						stopService(svc);
					}
				}catch (IllegalStateException e){
					startService(svc);
				}
			}
		});
	}

	/**
	 *set bottom barAndProfileButtonVisibility
	 * @param isVisible
	 */
	public void setBottomBarAndProfileButtonVisibility(boolean isVisible) {
		if (isVisible) {
			mCornerProfileButton.setVisibility(View.VISIBLE);
			mCornerProfileName.setVisibility(View.VISIBLE);
			mBottomContainer.setVisibility(View.VISIBLE);
		} else {
			mCornerProfileButton.setVisibility(View.GONE);
			mCornerProfileName.setVisibility(View.GONE);
			mBottomContainer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mFirebaseAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mFirebaseAuth.removeAuthStateListener(mAuthListener);
		}
	}

	public Account getActiveAccount() {
		return mActiveAccount;
	}

	public void setActiveAccount(Account activeAccount) {
		this.mActiveAccount = activeAccount;
	}

	public void setProfile(Profile profile) {
		mActiveAccount.setProfile(profile);
	}

	public void setProfileDocumentReference(String mActiveProfileDocId) {
		this.mActiveProfileDocId = mActiveProfileDocId;
	}

	public void setAccountDocumentReference(String mActiveAccountID) {
		this.mActiveAccountID = mActiveAccountID;
	}

	public String getAccountDocID() {
		return mActiveAccountID;
	}

	public String getProfileDocID() {
		return mActiveProfileDocId;
	}

}
