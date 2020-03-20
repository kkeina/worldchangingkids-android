package com.world_changingkids.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.Account;
import com.world_changingkids.model.Profile;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A fragmemnt to display Profile Edit page and handle user input
 */
public class ProfileEditInformation extends Fragment {
    /**
     * View elements below:
     */
    private Button SAVE;
    private Button BACK_BUTTON;
    private EditText NAME;
    private EditText AGE;
    private EditText GRADE;
    /**
     * Navigation controller
     */
    private NavController mNavController;
    private static final String TAG = "Edit Profile";
    private CollectionReference accounts;
    private FirebaseAuth mAuth;
    private Account parentAccount;
    private Profile profile;
    /**
     * FOR UPDATIND DATA*/
    String documentName="";
    private CollectionReference users = FirebaseFirestore.getInstance().collection("Profiles");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context mContext;

    /**
     * Required empty public constructor
     */
    public ProfileEditInformation() {
    }

    /**
     * onCreate lifecycle method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * set layout to the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit_information, container, false);
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
     * Bind class variables to view
     * @param view
     */
    public void setUpFragment(View view) {
        SAVE = view.findViewById(R.id.ProfileSavebtn);
        BACK_BUTTON = view.findViewById(R.id.ProfileLeftArrow_back);
        NAME = view.findViewById(R.id.ProfileName);
        AGE = view.findViewById(R.id.ProfileAge);
        GRADE = view.findViewById(R.id.ProfileGrade);

        profile = ((MainActivity)getActivity()).getActiveAccount().retrieveProfile();
        NAME.setText(profile.getName());
        AGE.setText(Integer.toString(profile.getAge()));
        GRADE.setText(Integer.toString(profile.getGrade()));


        mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();

        documentName = ((MainActivity) getActivity()).getProfileDocID();

        SAVE.setOnClickListener(new View.OnClickListener() {

            String accID  = ((MainActivity) getActivity()).getAccountDocID();
            DocumentReference doc = db.getInstance().collection("Accounts").document(accID).collection("Profiles").document(documentName);

            @Override
            public void onClick(View view) {
                String name = NAME.getText().toString();
                String age = AGE.getText().toString();
                String grade = GRADE.getText().toString();
                //call updateChanges() -> query the database with new data, update Firebase and Obbjects
                profile.setName(name);
                profile.setAge(Integer.parseInt(age));
                profile.setGrade(Integer.parseInt(grade));

                doc.update(
                        "name", profile.getName(),
                        "age", profile.getAge(),
                        "grade", profile.getGrade()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document id is "+ ((MainActivity)getActivity()).getProfileDocID());
                        mNavController.navigate(R.id.action_profileEditInformation_to_selectProfile);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Log.d(TAG, "Failed: AccountID is"+ ((MainActivity)getActivity()).getAccountDocID());

                    }
                });

            }
        });

        BACK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController.popBackStack();
            }
        });


    }


}
