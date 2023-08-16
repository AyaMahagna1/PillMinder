package com.application.pillminderplus.friendrequest;

import com.application.pillminderplus.caregivers.RequestPojo;

import java.util.List;
public class FriendRequestPresenter {
    FriendRequestViewInterface view;
    FriendRequestRepository repository;

    public FriendRequestPresenter(FriendRequestViewInterface view, FriendRequestRepository repository) {
        this.view = view;
        this.repository = repository;
        repository.setView(view);
    }

    public List<RequestPojo> getRequests(String receiverId) {
        return repository.getRequests(receiverId);
    }

    public void addFriend(String receiverId, String senderId){
        repository.addFriend(receiverId, senderId);
    }
}
