package com.example.phobpa.modelsEvents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("events")
    @Expose
    private List<Event> events;

    public EventResponse(boolean status, List<Event> events) {
        this.status = status;
        this.events = events;
    }

    public boolean isStatus() {
        return status;
    }

    public List<Event> getEvents() {
        return events;
    }
}
