package com.application.pillminderplus.friends;

import com.application.pillminderplus.caregivers.RequestPojo;

import java.util.List;
//Get the list of accepted requests (User friends (people who he give care to))
public class FriendPresenter {
    FriendsViewInterface view;
    FriendRepository repo;

    public FriendPresenter(FriendsViewInterface view, FriendRepository repo) {
        this.view = view;
        this.repo = repo;
        repo.setView(view);
    }
    public List<RequestPojo> getFriends(String receiverId) {
        return repo.getFriends(receiverId);
    }
}
