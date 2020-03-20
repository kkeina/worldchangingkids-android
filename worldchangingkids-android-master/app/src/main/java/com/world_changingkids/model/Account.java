package com.world_changingkids.model;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private static final int MAX_NUM_PROFILES = 4;
	private String firstName, lastName, email;
	private int profileCounter = 0;
	private ROLE mRole;
	//private Profile[] mProfiles;
	private List<Profile> mProfiles;
	private int currentProfile;

	private enum ROLE {
		REGULAR,
		ADMIN
	}

	public Account() {
		mRole = ROLE.REGULAR;
		mProfiles = new ArrayList<>(MAX_NUM_PROFILES);
	}

	public Account(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		mRole = ROLE.REGULAR;
		mProfiles = new ArrayList<>(MAX_NUM_PROFILES);
	}


	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public ROLE getRole() {
		return mRole;
	}

	public String getProfileName(int childPosition){
		return mProfiles.get(childPosition).getName();
	}

	public Profile retrieveProfile(){
		return mProfiles.get(currentProfile);
	}

	public void setProfilePosition(int currentProfile){
		this.currentProfile = currentProfile;
	}
	public int retrieveNumOfProfiles(){
		return profileCounter;
	}

	public void clearNumOfProfiles(){
		profileCounter = 0;
	}

	public void setRole(ROLE role) {
		mRole = role;
	}

	public List<Profile> getmProfiles() {
		return mProfiles;
	}

	public void setProfile(Profile child){
		if(profileCounter==4){ return; }
		mProfiles.add(child);
		profileCounter++;
	}

}
