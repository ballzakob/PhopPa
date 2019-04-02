package com.example.phobpa.modelsUsers;

public class User {
    private  String email, firstname, lastname,
            gender, birthday, telephone,image_user;

    public User(String email, String firstname, String lastname, String gender, String birthday, String telephone, String image_user) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthday = birthday;
        this.telephone = telephone;
        this.image_user = image_user;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getImage_user() {
        return image_user;
    }
}
