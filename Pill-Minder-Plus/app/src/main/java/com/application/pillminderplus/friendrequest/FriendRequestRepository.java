package com.application.pillminderplus.friendrequest;


import com.application.pillminderplus.caregivers.RequestPojo;

import java.util.List;

public class FriendRequestRepository {
    private static FriendRequestRepository repository = null;
    FriendRequestRemoteSource remoteSource;

    private FriendRequestRepository(FriendRequestRemoteSource remoteSource) {
        this.remoteSource = remoteSource;

    }

    public static FriendRequestRepository getInstance(FriendRequestRemoteSource remoteSource) {
        if (repository == null)
            repository = new FriendRequestRepository(remoteSource);
        return repository;
    }

    public List<RequestPojo> getRequests(String receiverId) {
        return remoteSource.getRequests(receiverId);
    }

    public void addFriend(String receiverId, String senderId) {
        remoteSource.addFriend(receiverId, senderId);
    }

    public void setView(FriendRequestViewInterface view) {
        remoteSource.setView(view);
    }


}
