package com.world_changingkids.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.Profile;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A fragment to display and handle add profile function
 */
public class AddProfileFragment extends Fragment {


    /**
     * View elements below:
     */
    private EditText NAME;
    private EditText AGE;
    private Button savebtn;
    private Button backbtn;
    private ImageButton avatarButtonOne;
    private ImageButton avatarButtonTwo;
    private ImageButton avatarButtonThree;
    private ImageButton avatarButtonFour;
    private ImageButton avatarButtonFive;
    private NavController mNavController;
    private FirebaseAuth mAuth;
    private CollectionReference accounts;
    private FirebaseUser user;
    private FirebaseFirestore db ;
    private String accountID = "";
    private CollectionReference subCollectionRef ;
    /**
     *  use grade to indicate avatar's index
     */
    private int grade=1;

    /**
     * default constructor
     */
    public AddProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Init views and bind actions when the view has been creating
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(false);
        return inflater.inflate(R.layout.fragment_add_profile_avatar, container, false);
    }

    /**
     * Called when the view has been created
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpFragment(view);
    }

    /**
     * To set avatar with border if it has been selected
     * @param index
     */
    private void setAvatarSelected(int index){
        this.grade=index;
        switch(index) {
            case 1:
                avatarButtonOne.setImageResource(R.drawable.avatar_border);
                avatarButtonTwo.setImageResource(R.color.transparent);
                avatarButtonThree.setImageResource(R.color.transparent);
                avatarButtonFour.setImageResource(R.color.transparent);
                avatarButtonFive.setImageResource(R.color.transparent);
                break;
            case 2:
                avatarButtonOne.setImageResource(R.color.transparent);
                avatarButtonTwo.setImageResource(R.drawable.avatar_border);
                avatarButtonThree.setImageResource(R.color.transparent);
                avatarButtonFour.setImageResource(R.color.transparent);
                avatarButtonFive.setImageResource(R.color.transparent);
                break;
            case 3:
                avatarButtonOne.setImageResource(R.color.transparent);
                avatarButtonTwo.setImageResource(R.color.transparent);
                avatarButtonThree.setImageResource(R.drawable.avatar_border);
                avatarButtonFour.setImageResource(R.color.transparent);
                avatarButtonFive.setImageResource(R.color.transparent);
                break;
            case 4:
                avatarButtonOne.setImageResource(R.color.transparent);
                avatarButtonTwo.setImageResource(R.color.transparent);
                avatarButtonThree.setImageResource(R.color.transparent);
                avatarButtonFour.setImageResource(R.drawable.avatar_border);
                avatarButtonFive.setImageResource(R.color.transparent);
                break;
            case 5:
                avatarButtonOne.setImageResource(R.color.transparent);
                avatarButtonTwo.setImageResource(R.color.transparent);
                avatarButtonThree.setImageResource(R.color.transparent);
                avatarButtonFour.setImageResource(R.color.transparent);
                avatarButtonFive.setImageResource(R.drawable.avatar_border);
                break;
        }


    }

    /**
     * Bind actions to views
     * @param view
     */
    public void setUpFragment(View view) {
        avatarButtonOne = view.findViewById(R.id.image_btn_avatar_one);
        avatarButtonTwo = view.findViewById(R.id.image_btn_avatar_two);
        avatarButtonThree = view.findViewById(R.id.image_btn_avatar_three);
        avatarButtonFour = view.findViewById(R.id.image_btn_avatar_four);
        avatarButtonFive = view.findViewById(R.id.image_btn_avatar_five);
        NAME = view.findViewById(R.id.name);
        AGE = view.findViewById(R.id.age);
        savebtn = view.findViewById(R.id.savebtn);
        backbtn = view.findViewById(R.id.leftArrow_back);


        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        subCollectionRef = db.collection("Accounts");
        accountID = ((MainActivity) getActivity()).getAccountDocID();
        //getdocID(); // to get the doc id on start

        mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChild();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController.popBackStack();
            }
        });

        avatarButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatarSelected(1);
            }
        });

        avatarButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatarSelected(2);
            }
        });

        avatarButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatarSelected(3);
            }
        });

        avatarButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatarSelected(4);
            }
        });

        avatarButtonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatarSelected(5);
            }
        });
    }

    /**
     * add child
     */
    public void addChild() {

        final String name= NAME.getText().toString();
        final int age;


        if (AGE.getText().toString().trim().equals("")){
            age = 0;

        }else {
            age = Integer.parseInt(AGE.getText().toString());
        }


       FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String str = user.getEmail();

        accounts = FirebaseFirestore.getInstance().
                collection("Accounts");



        //accounts.document(accountID).collection("Profiles").add(new Profile())



        /*conditions for values*/
        if (NAME.getText().toString().trim().equals("") || NAME.length() < 2) {
            NAME.setError("Name is required!");

        } else if (AGE.getText().toString().trim().equals("") || AGE.length() <= 0) {
            AGE.setError("Age is required!");

        } else {
            Profile profile = new Profile(name, age, grade, getContext());

            if (!accountID.isEmpty() && ((MainActivity) getActivity()).getActiveAccount().retrieveNumOfProfiles() < 4) {
                subCollectionRef.document(accountID).collection("Profiles").add(profile)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
//                                    subCollectionRef.document(accountID).collection("Profiles").document(task.getResult().toString()).
//                                            collection("ActsOfKindnessCollection").add(profile);
                                    mNavController.navigate(R.id.action_AddProfile_to_ProfileSelection);

                                }
                            }
                        });
            }else{
                Toast.makeText(getContext(), "Account not added", Toast.LENGTH_SHORT).show();
                mNavController.navigate(R.id.action_AddProfile_to_ProfileSelection);

            }
        }
    }
}



