package com.world_changingkids.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.FirebaseWrapper;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.ResetPasswordActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 *A fragment to display the app login page
 */
public class LoginFragment extends Fragment {
    /**
     * View elements below:
     */
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button createAccount;
    private Button resetPasswordBtn;
    /**
     * Navigation controller
     */
    private NavController mNavController;
    /**
     * Firebase auth instance
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseWrapper firebase = new FirebaseWrapper();
    private FirebaseFirestore db ;
    private String accountID = "";
    private String TAG = "Login Page";

    /**
     * default constructor
     */
    public LoginFragment() {
    }

    /**
     * Set layout to the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(false);

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * This event is triggered soon after onCreateView().
     *  Any view setup should occur here.  E.g., view lookups and attaching view listeners.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mFirebaseAuth =  FirebaseAuth.getInstance();
        setUpButtonActions(view);
        init();
    }

    /**
     * init the FirebaseUser
     */
    public void init(){
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * set button actions to the views
     * @param view
     */
    public void setUpButtonActions(View view) {
        emailField = view.findViewById(R.id.user_emailField);
        passwordField = view.findViewById(R.id.user_PasswordField);
        loginButton = view.findViewById(R.id.loginButton);
        createAccount = view.findViewById(R.id.createAccountButton);
        resetPasswordBtn = view.findViewById(R.id.user_ForgotPasswordButton);
        mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.navigate(R.id.action_loginFragment_to_createAccountFragment);
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * a method to handle user login amd authantication
     */
    public void login() {
        final String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (!validateInput(email, password)) {
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            Log.d("LoginFragment","signInWithEmailAndPassword task.isSuccessful()");
                            mNavController.navigate(R.id.action_loginFragment_to_ProfileSelection);
                        }else{
                            emailField.setError("Invalid Email or Password");
                        }
                    }
                });
    }

    /**
     * user input validation
     * @param email
     * @param password
     * @return
     */
    public boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            emailField.setError("Email Required");
            return false;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password Required");
            return false;
        }else if (password.length() < 6){
            passwordField.setError("Password is too short");
            return false;
        }

        return true;
    }







}
