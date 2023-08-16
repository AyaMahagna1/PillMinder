package com.application.pillminderplus.friendrequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.pillminderplus.R;
import com.application.pillminderplus.caregivers.RequestPojo;
import com.squareup.picasso.Picasso;
import java.util.List;
//Adapter for showing the request
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    Context context;
    List<RequestPojo> data;
    OnBtnClickListener listener;

    public FriendRequestAdapter(List<RequestPojo> data, Context context, OnBtnClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    public void setList(List<RequestPojo> updatedData) {
        this.data = updatedData;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_request_row, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.ViewHolder holder, int position) {
        holder.friendReqName.setText(data.get(position).getSenderUsername());
        Picasso.with(context).load(data.get(position).getProfile_image_uri()).into(holder.friendReqImg);

        holder.confirmBtn.setOnClickListener(v -> {
            listener.onConfirmClick(data.get(position).getReceiverId(), data.get(position).getSenderId());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendReqImg;
        TextView friendReqName;
        Button confirmBtn;
        Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendReqImg = itemView.findViewById(R.id.friendReqImgId);
            friendReqName = itemView.findViewById(R.id.friendReqNameId);
            confirmBtn = itemView.findViewById(R.id.confirmBtnId);
            deleteBtn = itemView.findViewById(R.id.deleteBtnId);
        }
    }
}
