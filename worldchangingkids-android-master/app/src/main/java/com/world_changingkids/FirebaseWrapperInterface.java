package com.world_changingkids;

import com.google.firebase.auth.FirebaseUser;

/**
 * FirebaseWrapperInterface to implement Firebase auth basic functions
 */
public interface FirebaseWrapperInterface {

    FirebaseUser getFirebaseUser();


    boolean signIn(String email, String password);

    void signOut();

    void createAccount(String email, String password);

    void createProfile();

    void resetPassword();

    void updateFirebase();

}
