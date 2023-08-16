package com.application.pillminderplus.caregivers;
//Pojo is Plain Old Java Object and for short pojo and it's a class for requests/invitations between the user and caregiver
public class RequestPojo {
    private String receiverId;
    private String senderId;
    private String senderUsername;
    private String profile_image_uri;
    private String status;
    public RequestPojo(String receiverId, String senderId, String senderUsername, String profile_image_uri, String status) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.status = status;
        this.profile_image_uri = profile_image_uri;
    }

    public RequestPojo() {
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getProfile_image_uri() {
        return profile_image_uri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
