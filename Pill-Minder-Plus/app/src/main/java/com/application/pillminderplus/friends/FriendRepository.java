package com.application.pillminderplus.friends;

import androidx.annotation.NonNull;

import com.application.pillminderplus.caregivers.RequestPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//Geting friends from firebase
public class FriendRepository {
    private static FriendRepository friendRepository = null;
    List<RequestPojo> friends;
    DatabaseReference freindReference;
    RequestPojo friend;
    FriendsViewInterface view;
    private FriendRepository(){
    }
    public static FriendRepository getInstance(){
        if(friendRepository == null)
            friendRepository = new FriendRepository();
        return friendRepository;
    }
    public List<RequestPojo> getFriends(String receiverId){
        friends = new ArrayList<>();
        freindReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        freindReference.orderByChild("receiverId").equalTo(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot temp : snapshot.getChildren()){
                        friend = temp.getValue(RequestPojo.class);
                        if(Objects.requireNonNull(friend).getStatus().equals("accept")){
                            friends.add(friend);
                        }

                    }
                }
                view.setData(friends);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return friends;
    }

    public void setView(FriendsViewInterface view) {
        this.view = view;
    }
}
