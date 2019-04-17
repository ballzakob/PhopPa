package com.example.phobpa.modelsUsers;

public class UserFirebase {
    String id;
    String username;
    String imagerUrl;

    public UserFirebase(String id, String username, String imagerUrl) {
        this.id = id;
        this.username = username;
        this.imagerUrl = imagerUrl;
    }
    public UserFirebase() {

    }



    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImagerUrl() {
        return imagerUrl;
    }
}
