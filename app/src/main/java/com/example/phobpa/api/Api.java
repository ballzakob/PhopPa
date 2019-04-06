package com.example.phobpa.api;

import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.example.phobpa.modelsUsers.JoinResponse;
import com.example.phobpa.modelsUsers.LoginResponse;
import com.example.phobpa.modelsUsers.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("createuser.php")
    Call<DefaultResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("gender") String gender,
            @Field("birthday") String birthday,
            @Field("telephone") String telephone,
            @Field("image_user") String image_user
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("getUser.php")
    Call<UserResponse> getUser(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("createevent.php")
    Call<DefaultResponse> createEvent(
            @Field("email") String email,
            @Field("event_title") String event_title,
            @Field("event_detail") String event_detail,
            @Field("event_date_start") String event_date_start,
            @Field("event_date_end") String event_date_end,
            @Field("event_number_people") String event_number_people,
            @Field("event_gender") String event_gender,
            @Field("event_types") String event_types,
            @Field("event_location_name") String event_location_name,
            @Field("event_location_address") String event_location_address,
            @Field("event_latitude") String event_latitude,
            @Field("event_longitude") String event_longitude,
            @Field("event_image") String event_image
    );

    @FormUrlEncoded
    @POST("geteventme.php")
    Call<EventResponse> getEventMe(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("geteventnotme2.php")
    Call<EventResponse> getEventNotMe(
            @Field("email") String email,
            @Field("event_latitude") String event_latitude,
            @Field("event_longitude") String event_longitude
    );

    @FormUrlEncoded
    @POST("showEventNotMe.php")
    Call<EventResponse> showEventNotMe(
            @Field("email") String email,
            @Field("event_latitude") String event_latitude,
            @Field("event_longitude") String event_longitude,
            @Field("event_types") String event_types
    );

    @FormUrlEncoded
    @POST("getJoinEventCount.php")
    Call<JoinResponse> getJoinEventCount(
            @Field("event_id") String event_id
    );

    @FormUrlEncoded
    @POST("jointEvent.php")
    Call<DefaultResponse> jointEvent(
            @Field("event_id") String event_id,
            @Field("email") String email
    );



}