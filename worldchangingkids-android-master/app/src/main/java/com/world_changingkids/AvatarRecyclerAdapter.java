package com.world_changingkids;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.world_changingkids.fragments.AvatarSelectFragment;
import com.world_changingkids.model.Account;

import java.util.List;

public class AvatarRecyclerAdapter extends RecyclerView.Adapter<AvatarRecyclerAdapter.AvaterViewHolder>{
    private List<Integer> list;
    private Context context;
    private Account mActiveAccount;
    private AvatarSelectFragment avatarSelectFragment;

    public AvatarRecyclerAdapter(List<Integer> list, Context context, AvatarSelectFragment avatarSelectFragment) {
        this.list = list;
        this.context = context;
        this.avatarSelectFragment = avatarSelectFragment;
    }
    public void setList(List<Integer> data){
        list.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AvaterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AvaterViewHolder(LayoutInflater.from(context).inflate(R.layout.avatar_selection_design,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AvaterViewHolder avaterViewHolder, final int i) {
        avaterViewHolder.ivAvatar.setImageResource(list.get(i));
        avaterViewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mActiveAccount = ((MainActivity)context).getActiveAccount();
//                Log.d("TAG",mActiveAccount.retrieveProfile().getGrade());
                avatarSelectFragment.setCornerImage(list.get(i));
//
//                ((MainActivity)context)
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AvaterViewHolder extends RecyclerView.ViewHolder{
        ImageView ivAvatar;
        public AvaterViewHolder( View itemView) {
            super(itemView);
            ivAvatar=itemView.findViewById(R.id.ivAvatar);
        }
    }
}
