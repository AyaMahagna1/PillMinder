package com.application.pillminderplus.friends;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.caregivers.RequestPojo;
import com.application.pillminderplus.home.HomeActivity;

import java.util.List;
//The screen for showing all user friends
public class FriendsActivity extends AppCompatActivity implements FriendsViewInterface, OnBtnClickListener {
    FriendPresenter presenter;
    FriendsAdapter adapter;
    List<RequestPojo> friendsData;
    RecyclerView recyclerView;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        recyclerView = findViewById(R.id.friendRecyclerId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent inIntent = getIntent();
        userId = inIntent.getStringExtra("userId");
        presenter = new FriendPresenter(this, FriendRepository.getInstance());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        friendsData = presenter.getFriends(userId);
        adapter = new FriendsAdapter(this, friendsData, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setData(List<RequestPojo> friends) {
        friendsData = friends;
        adapter.setList(friendsData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRowClick(String uid) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}