package com.world_changingkids.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.world_changingkids.AudioController;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.ActOfKindness;
import com.world_changingkids.model.Profile;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A BaseFullScreenFragment subclass to display Act Of Kindness Details
 */
public class ActOfKindnessDetailsFragment extends BaseFullScreenFragment {

	/**
	 * An ActOfKindnessPassBackListener instance
	 */
	private ActOfKindnessPassBackListener mActOfKindnessPassBackListener;
	/**
	 * An ActOfKindness instance
	 */
	private ActOfKindness mActOfKindness;
	private int mActOfKindnessPositionIndex;
	private Profile mActiveProfile;
	/**
	 * View elements below:
	 */
	private TextView mAOKTitleTextView;
	//private TextView mAOKDescriptionTextView;
	private TextView mAOKQuestionTextView;
	private EditText mAOKNotesEditText;
	private ImageView mPlayPauseMediaImageView;
	private ToggleButton mDoneToggleButton;
	private ImageButton mCloseDialogButton;

	//han
//	Context context;
//	Point p = new Point();
//	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//	wm.getDefaultDisplay().getSize(p);
//	screenWidth = p.x;

//	DisplayMetrics metrics = new DisplayMetrics();
//	Display display = getActivity().getWindowManager().getDefaultDisplay();
//	display.getMetrics();

	public interface ActOfKindnessPassBackListener {
		void passDataBack(ActOfKindness actOfKindness, int positionIndex);
	}


	public static ActOfKindnessDetailsFragment newInstance(ActOfKindnessPassBackListener pbl, int actOfKindnessNumber) {
		ActOfKindnessDetailsFragment newFragment = new ActOfKindnessDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("aok_index", actOfKindnessNumber);
		newFragment.setArguments(bundle);
		newFragment.mActOfKindnessPassBackListener = pbl;
		return newFragment;
	}

	@Override
	public View specifyFragmentLayout(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_act_of_kindness_details, parent, false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mActOfKindnessPositionIndex = getArguments().getInt("aok_index");
		mActiveProfile = ((MainActivity)getActivity()).getActiveAccount().retrieveProfile();
		mActOfKindness = mActiveProfile.getActsOfKindnessCollection().getActsOfKindnessList().get(mActOfKindnessPositionIndex);
	}

	/**
	 * onCreateView method to init the views and bind actions
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_act_of_kindness_details, container, false);
		mAOKTitleTextView = v.findViewById(R.id.text_view_aok_title);
		String aokTitle = String.format(
				getContext().getResources().getString(R.string.aok_details_item_title),
				mActOfKindness.getNumber(),
				mActOfKindness.getTitle());
		mAOKTitleTextView.setText(aokTitle);

		//mAOKDescriptionTextView = v.findViewById(R.id.text_view_aok_description);
		//mAOKDescriptionTextView.setText(mActOfKindness.getDescription());

		mAOKQuestionTextView = v.findViewById(R.id.text_view_aok_question);
		mAOKQuestionTextView.setText(mActOfKindness.getQuestion());

		mAOKNotesEditText = v.findViewById(R.id.edit_text_aok_notes);
		mAOKNotesEditText.setText(mActOfKindness.getNotes());
		mAOKNotesEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					//if user enter the 'enter' key
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					if (TextUtils.isEmpty(mAOKNotesEditText.getText().toString().trim())) {
						Toast.makeText(getActivity(),"Please type the note", Toast.LENGTH_SHORT).show();


					} else {//is not empty
						mActOfKindness.setNotes(mAOKNotesEditText.getText().toString());

					}

					InputMethodManager inputManager = (InputMethodManager)
							getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.toggleSoftInput(0, 0);
					return true;
				}
				return false;
			}
		});


		mPlayPauseMediaImageView = v.findViewById(R.id.image_view_play_pause_media);
		mPlayPauseMediaImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean imageViewActiveState = mPlayPauseMediaImageView.isActivated();
				AudioController ac = AudioController.getInstance(getContext());
				String name="aok_"+mActOfKindness.getNumber();
				if (!imageViewActiveState) {
					ac.loadAudio(getAudioIdByName(name));
					ac.playAudio(false);
				} else {
					ac.stopAudio();
				}

				mPlayPauseMediaImageView.setActivated(!imageViewActiveState);
			}
		});

		mDoneToggleButton = v.findViewById(R.id.toggle_button_done);
		mDoneToggleButton.setChecked(mActOfKindness.isCompleted());
		mDoneToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mActOfKindness.setCompleted(isChecked);
				if(isChecked){
					Toast.makeText(getActivity(),"You did it! Congratulations!", Toast.LENGTH_SHORT).show();
				}

			}
		});
		mCloseDialogButton = v.findViewById(R.id.button_close_dialog);
		mCloseDialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				destroyDetailsFragment();
			}
		});

		return v;
	}

	/**
	 *
	 * @param name name of audio
	 * @return audio id
	 */
	private int getAudioIdByName(String name){
		int id=0;
		 id = this.getResources().getIdentifier(name, "raw", getActivity().getPackageName());
		 return id;
	}


	@Override
	public void onPause() {
		super.onPause();
		mActOfKindnessPassBackListener.passDataBack(mActOfKindness, mActOfKindnessPositionIndex);
	}

	private void destroyDetailsFragment() {
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.remove(this)
				.commit();
	}


}
