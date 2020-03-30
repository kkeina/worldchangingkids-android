package com.world_changingkids.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.world_changingkids.AvatarRecyclerAdapter;
import com.world_changingkids.MainActivity;
import com.world_changingkids.R;

import java.util.ArrayList;

public class AvatarSelectFragment extends Fragment {
    ArrayList<Integer> list;
    RecyclerView rvAvatarList;
    ImageView ivBack ;
    private NavHostFragment mNavController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.avatar_select_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAvatarList = view.findViewById(R.id.rvAvatarList);
        ivBack = view.findViewById(R.id.ivBack);
        mNavController = ((NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment));
        rvAvatarList.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        list = new ArrayList<Integer>();
        list.add(R.drawable.avatar1);
        list.add(R.drawable.avatar2);
        list.add(R.drawable.avatar3);
        list.add(R.drawable.avatar4);
        list.add(R.drawable.avatar5);
        list.add(R.drawable.avatar6);
        list.add(R.drawable.avatar7);
        list.add(R.drawable.avatar8);
        list.add(R.drawable.avatar9);
        list.add(R.drawable.avatar10);
        list.add(R.drawable.avatar11);
        list.add(R.drawable.avatar12);
        rvAvatarList.setAdapter(new AvatarRecyclerAdapter(list,getActivity(),this));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
    }

    public void setCornerImage(Integer id){
        ((MainActivity)getActivity()).updateCornerProfileButton(id);


    }
    public void backPress(){
        getActivity().onBackPressed();
    }
}
