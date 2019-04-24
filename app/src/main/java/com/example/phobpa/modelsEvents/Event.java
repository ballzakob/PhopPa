package com.example.phobpa.modelsEvents;


public class Event {

    private String event_id, email, event_title, event_detail, event_date_start, event_date_end,
            event_time_start, event_time_end, event_number_people, event_gender, event_types, event_price, event_location_name,
            event_location_address, event_latitude, event_longitude, event_image, event_joint;

    public Event(String event_id, String email, String event_title, String event_detail,
                 String event_date_start, String event_date_end, String event_time_start,
                 String event_time_end, String event_number_people,
                 String event_gender, String event_types, String event_price,
                 String event_location_name, String event_location_address, String event_latitude,
                 String event_longitude, String event_image, String event_joint) {
        this.event_id = event_id;
        this.email = email;
        this.event_title = event_title;
        this.event_detail = event_detail;
        this.event_date_start = event_date_start;
        this.event_date_end = event_date_end;
        this.event_time_start = event_time_start;
        this.event_time_end = event_time_end;
        this.event_number_people = event_number_people;
        this.event_gender = event_gender;
        this.event_types = event_types;
        this.event_price = event_price;
        this.event_location_name = event_location_name;
        this.event_location_address = event_location_address;
        this.event_latitude = event_latitude;
        this.event_longitude = event_longitude;
        this.event_image = event_image;
        this.event_joint = event_joint;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEmail() {
        return email;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_detail() {
        return event_detail;
    }

    public String getEvent_date_start() {
        return event_date_start;
    }

    public String getEvent_date_end() {
        return event_date_end;
    }

    public String getEvent_time_start() {
        return event_time_start;
    }

    public String getEvent_time_end() {
        return event_time_end;
    }

    public String getEvent_number_people() {
        return event_number_people;
    }

    public String getEvent_gender() {
        return event_gender;
    }

    public String getEvent_types() {
        return event_types;
    }

    public String getEvent_price() {
        return event_price;
    }

    public String getEvent_location_name() {
        return event_location_name;
    }

    public String getEvent_location_address() {
        return event_location_address;
    }

    public String getEvent_latitude() {
        return event_latitude;
    }

    public String getEvent_longitude() {
        return event_longitude;
    }

    public String getEvent_image() {
        return event_image;
    }

    public String getEvent_joint() { return event_joint; }
}
