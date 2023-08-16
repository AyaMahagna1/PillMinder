package com.application.pillminderplus.friendrequest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.caregivers.RequestPojo;

import java.util.List;
// The screen for showing all friend/Caregiver requests where the user can accept or refuse
public class FriendRequestActivity extends AppCompatActivity implements FriendRequestViewInterface, OnBtnClickListener {
    FriendRequestPresenter presenter;
    FriendRequestAdapter adapter;
    List<RequestPojo> requestsData;
    RecyclerView recyclerView;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        recyclerView = findViewById(R.id.friendReqRecyclerId);
        Intent inIntent = getIntent();
        userId = inIntent.getStringExtra("userId");
        presenter = new FriendRequestPresenter(this, FriendRequestRepository.getInstance(FriendRequestRemoteSource.getInstance()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        requestsData = presenter.getRequests(userId);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void setData(List<RequestPojo> requests) {
        requestsData = requests;
        adapter = new FriendRequestAdapter(requestsData, this, this);
        recyclerView.setAdapter(adapter);
        adapter.setList(requestsData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmClick(String receiverId, String senderId) {
        presenter.addFriend(receiverId, senderId);
    }
}