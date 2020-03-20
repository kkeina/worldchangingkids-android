package com.world_changingkids.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * the BaseFullScreenFragment
 */
public abstract class BaseFullScreenFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return specifyFragmentLayout(inflater, container, savedInstanceState);
	}

	public abstract View specifyFragmentLayout(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

}
