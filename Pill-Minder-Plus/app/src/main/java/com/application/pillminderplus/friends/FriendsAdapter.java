package com.application.pillminderplus.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.caregivers.RequestPojo;
import com.squareup.picasso.Picasso;

import java.util.List;
//Adapter for showing friend object on screen
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{
    Context context;
    List<RequestPojo> data;
    private OnBtnClickListener onBtnClickListener;

    public FriendsAdapter(Context context, List<RequestPojo> data, OnBtnClickListener onBtnClickListener) {
        this.context = context;
        this.data = data;
        this.onBtnClickListener = onBtnClickListener;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_row, parent, false);
        return new ViewHolder(v);
    }

    public void setList(List<RequestPojo> updatedData){
        this.data = updatedData;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, int position) {
        int i = position;
        holder.friendName.setText(data.get(i).getSenderUsername());
        Picasso.with(context).load(data.get(position).getProfile_image_uri()).into(holder.friendImg);

        holder.friendRowId.setOnClickListener(view -> onBtnClickListener.onRowClick(data.get(position).getSenderId()));
    }

    @Override
    public int getItemCount() {
        if(data == null)
            return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImg;
        TextView friendName;
        ConstraintLayout friendRowId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendImg = itemView.findViewById(R.id.friendImgId);
            friendName = itemView.findViewById(R.id.friendNameId);
            friendRowId = itemView.findViewById(R.id.friendRowId);
        }
    }
}
