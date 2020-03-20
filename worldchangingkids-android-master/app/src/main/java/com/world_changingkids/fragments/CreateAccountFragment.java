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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;
import com.world_changingkids.model.Account;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A fragment to handle user creates account
 */
public class CreateAccountFragment extends Fragment {
	/**
	 * View elements below:
	 */
	private EditText EMAIL;
	private EditText PASSWORD;
	private EditText FIRSTNAME;
	private EditText LASTNAME;
	private Button createAccount;
	private Button backbtn;
	/**
	 * Navigation controller
	 */
	private NavController mNavController;
	/**
	 * A string indicates TAG for logger
	 */
	private static final String TAG = "Create Account";
	private CollectionReference accounts;
	private FirebaseAuth mAuth;

	/**
	 * default constructor
	 */
	public CreateAccountFragment() {
		// Required empty public constructor
	}

	/**
	 * Init views and bind actions when fragment creating view
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		((MainActivity) getActivity()).setBottomBarAndProfileButtonVisibility(false);
		return inflater.inflate(R.layout.fragment_create_account, container, false);
	}

	/**
	 * Called when the view has been created
	 * @param view
	 * @param savedInstanceState
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Setup any handles to view objects here
		// EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
		setUpFragment(view);
	}

	/**
	 * bind actions to the views
	 * @param view
	 */
	public void setUpFragment(View view) {
		EMAIL = view.findViewById(R.id.user_emailField);
		PASSWORD = view.findViewById(R.id.user_PasswordField);
		FIRSTNAME = view.findViewById(R.id.user_fnameField);
		LASTNAME = view.findViewById(R.id.user_lnameField);
		mAuth = FirebaseAuth.getInstance();
		//TODO: checkbox agree to terms
		createAccount = view.findViewById(R.id.createAccountButton);
		backbtn = view.findViewById(R.id.leftArrow_back);
		mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.nav_host_fragment))
				.getNavController();

		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createAccount();
			}
		});

		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNavController.popBackStack();
			}
		});
	}

	/**
	 * a method to handle create account
	 */
	public void createAccount(){

		if (!validateInput()) {
			return;
		}

		final String email = EMAIL.getText().toString().trim();
		final String password = PASSWORD.getText().toString().trim();
		final String fname = FIRSTNAME.getText().toString().trim();
		final String lname = LASTNAME.getText().toString().trim();

		accounts = FirebaseFirestore.getInstance().
				collection("Accounts");



		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {

					accounts.add(new Account(fname, lname, email)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
						@Override
						public void onComplete(@NonNull Task<DocumentReference> task) {
							mNavController.popBackStack();//goes to login page
						}
					});

				} else{
					EMAIL.setError("Invalid Email or Password");
				}
			}
		});

	}
	/**
	 * check the email if is valid
	 */
	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	/**
	 * user inputs validation
	 * @return
	 */
	public boolean validateInput() {
		if (FIRSTNAME.getText().toString().trim().equals("") || FIRSTNAME.length() < 2) {
			FIRSTNAME.setError("First name is required!");
			return false;
		}

		if (LASTNAME.getText().toString().trim().equals("") || LASTNAME.length() < 2) {
			LASTNAME.setError("Last name is required!");
			return false;
		}

		if (EMAIL.getText().toString().trim().equals("")) {
			EMAIL.setError("Email name is required!");
			return false;
		}

		if (PASSWORD.getText().toString().trim().equals("") || PASSWORD.length() < 6) {
			PASSWORD.setError("Password is required and should be over 6!");
			return false;
		}

		if (!isEmailValid(EMAIL.getText().toString().trim())) {
			EMAIL.setError("Please check you email!");
			return false;
		}

		return true;
	}

}
