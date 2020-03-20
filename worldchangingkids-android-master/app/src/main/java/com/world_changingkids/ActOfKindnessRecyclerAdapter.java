package com.world_changingkids;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.world_changingkids.fragments.ActOfKindnessDetailsFragment;
import com.world_changingkids.model.ActOfKindness;

import java.util.ArrayList;
import java.util.List;

public class ActOfKindnessRecyclerAdapter extends RecyclerView.Adapter<ActOfKindnessRecyclerAdapter.ViewHolder> {
    private List<ActOfKindness> mFilteredActOfKindnessModels;
    private List<CardView> mActsOfKindnessCardViews;
    private Context context;
    public ActOfKindnessRecyclerAdapter(List<ActOfKindness> mFilteredActOfKindnessModels, ActOfKindnessDetailsFragment.ActOfKindnessPassBackListener mPassBackListener, Context context) {
        this.mFilteredActOfKindnessModels = mFilteredActOfKindnessModels;
        mActsOfKindnessCardViews = new ArrayList<>();

        for (int i = 0; i < mFilteredActOfKindnessModels.size(); i++) {
            mActsOfKindnessCardViews.add(null);
        }
        this.mPassBackListener = mPassBackListener;
        this.context=context;
    }

    private ActOfKindnessDetailsFragment.ActOfKindnessPassBackListener mPassBackListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_adapter_design,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ActOfKindness actOfKindness = mFilteredActOfKindnessModels.get(i);
        String actOfKindnessTitle =
                String.format(context
                        .getResources()
                        .getString(R.string.aok_list_item_title), actOfKindness.getNumber(), actOfKindness.getTitle());
        viewHolder.textView.setText(actOfKindnessTitle);
        if(actOfKindness.isCompleted()){
            viewHolder.imageView.setImageResource(R.drawable.star_filled_64);
        }else{
            viewHolder.imageView.setImageResource(R.drawable.star_black_64);

        }
    }

    @Override
    public int getItemCount() {
        return mFilteredActOfKindnessModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.text_view_aok_image);
            this.textView = (TextView) itemView.findViewById(R.id.text_view_aok_title);
        }
    }
}
