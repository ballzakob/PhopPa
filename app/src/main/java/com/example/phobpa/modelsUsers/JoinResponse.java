package com.example.phobpa.modelsUsers;

import com.example.phobpa.modelsEvents.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JoinResponse {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("messages")
    @Expose
    private String messages;

    @SerializedName("users")
    @Expose
    private List<User> users;

    public JoinResponse(boolean status, String messages, List<User> users) {
        this.status = status;
        this.messages = messages;
        this.users = users;
    }

    public boolean isStatus() { return status; }

    public String getMessages() { return messages; }

    public List<User> getUsers() { return users; }
}
