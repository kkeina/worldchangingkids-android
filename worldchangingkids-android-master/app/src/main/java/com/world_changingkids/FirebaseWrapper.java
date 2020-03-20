package com.world_changingkids;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A FirebaseWrapper to handle FirebaseAuth
 */
public final class FirebaseWrapper implements FirebaseWrapperInterface {

    /**
     * An instance of FirebaseAuth
     */
    private FirebaseAuth mFirebaseAuth;
    /**
     * An instance of FirebaseUser
     */
    private FirebaseUser mFirebaseUser;
    private CollectionReference mAccountsCollection;
    private CollectionReference mProfilesCollection;
    private FirebaseAuth.AuthStateListener mAuthListener;
    boolean authenticated = false;

    /**
     * default constructor
     */
    public FirebaseWrapper() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAccountsCollection = FirebaseFirestore.getInstance().collection("Users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    authenticated = true;
                    System.out.println("Success");
                }else{
                    mFirebaseUser = null;
                }
            }
        };


    }

    /**
     * getter for FirebaseUser
     * @return current FirebaseUser
     */
    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    /**
     * handle user sign in function
     * @param email
     * @param password
     * @return
     */
    public boolean signIn(String email, String password) {
        mFirebaseUser = null;
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        System.out.println("Credentials authenticated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthException){
                            System.out.println("A - Authentication failed " + ((FirebaseAuthException) e).getErrorCode());
                        }
                    }

                });

        mFirebaseAuth.addAuthStateListener(mAuthListener);//executes listener
        return mFirebaseUser != null;

    }

    /**
     * TODO
     * handle user log out
     */
    public void signOut() {

    }

    /**
     * handle user create account by email and password
     * @param email
     * @param password
     */
    public void createAccount(String email, String password) {
        mFirebaseUser = null;
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();

                        }

                    }
                });


    }

    public void createProfile() {

    }

    public void resetPassword() {

    }

    public void updateFirebase() {

    }

}
