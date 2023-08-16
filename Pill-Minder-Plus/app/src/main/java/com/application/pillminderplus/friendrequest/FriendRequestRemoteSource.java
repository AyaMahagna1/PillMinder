package com.application.pillminderplus.friendrequest;

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
//Geting all requests from firebase/accept it /refuse it
public class FriendRequestRemoteSource{
    private static FriendRequestRemoteSource remoteSource = null;
    List<RequestPojo> requests;
    DatabaseReference getData, requestRef;
    RequestPojo request;
    FriendRequestViewInterface view;
    private FriendRequestRemoteSource() {
    }
    public static FriendRequestRemoteSource getInstance(){
        if(remoteSource == null)
            remoteSource = new FriendRequestRemoteSource();
        return remoteSource;
    }

    public List<RequestPojo> getRequests(String receiverId){
        requests = new ArrayList<>();
        getData = FirebaseDatabase.getInstance().getReference().child("Users");
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");

        requestRef.orderByChild("receiverId").equalTo(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot temp : snapshot.getChildren()){
                        request = temp.getValue(RequestPojo.class);
                        if(Objects.requireNonNull(request).getStatus().equals("pending"))
                            requests.add(request);
                    }
                }
                view.setData(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return requests;
    }

    public void addFriend(String receiverId, String senderId){
        final String[] id = new String[1];
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        requestRef.orderByChild("receiverId").equalTo(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot temp : snapshot.getChildren()){
                        request = temp.getValue(RequestPojo.class);
                        if(Objects.requireNonNull(request).getSenderId().equals(senderId)){
                            id[0] = temp.getKey();
                            request.setStatus("accept");
                            requestRef.child(id[0]).setValue(request);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setView(FriendRequestViewInterface view) {
        this.view = view;
    }

}
