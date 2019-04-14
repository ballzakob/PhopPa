package com.example.phobpa.modelsUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("messages")
    @Expose
    private String messages;

    @SerializedName("status_event")
    @Expose
    private String status_event;

    public StatusResponse(boolean status, String messages, String status_event) {
        this.status = status;
        this.messages = messages;
        this.status_event = status_event;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }

    public String getStatus_event() {
        return status_event;
    }
}
