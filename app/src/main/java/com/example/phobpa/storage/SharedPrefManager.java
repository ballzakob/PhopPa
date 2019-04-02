package com.example.phobpa.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.phobpa.modelsUsers.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveUser(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",user.getEmail());
        editor.putString("firstname",user.getFirstname());
        editor.putString("lastname",user.getLastname());
        editor.putString("gender",user.getGender());
        editor.putString("birthday",user.getBirthday());
        editor.putString("telephone",user.getTelephone());
        editor.putString("image_user",user.getImage_user());
        editor.apply();

    }

    public boolean isLoggedIN(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString("email",null) != null;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("firstname",null),
                sharedPreferences.getString("lastname",null),
                sharedPreferences.getString("gender",null),
                sharedPreferences.getString("birthday",null),
                sharedPreferences.getString("telephone",null),
                sharedPreferences.getString("image_user",null)

        );
    }

    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
