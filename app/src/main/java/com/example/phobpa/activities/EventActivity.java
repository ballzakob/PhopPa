package com.example.phobpa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.adapter.UserJoinAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.fragment.HomeFragment;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.example.phobpa.modelsUsers.JoinResponse;
import com.example.phobpa.modelsUsers.StatusResponse;
import com.example.phobpa.modelsUsers.User;
import com.example.phobpa.modelsUsers.UserResponse;
import com.example.phobpa.storage.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    private TextView textViewEventTitle, textViewEventDetail, textViewNumberPeopleMax, textViewEventDateStart,
            textViewEventDateEnd, textViewEventGender, textViewNameOwnerEvent ,textViewEventLocationName
            , textViewEventAddress ,textViewNumberPeople , textViewShowText, textViewEventPrice;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ImageView imageViewEvent;
    private GoogleMap mMap;
    private MapView mapView;
    private CircleImageView circleImageViewOwnerEvent;
    private int countJoint=0;
    private int countMax=0;
    private Button buttonSignUp;

    private RecyclerView recyclerView;
    private UserJoinAdapter adapter;
    private List<User> userList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        findViewById(R.id.button_back_home).setOnClickListener(this);
//        findViewById(R.id.buttonSignUp).setOnClickListener(this);




        textViewEventTitle = findViewById(R.id.textViewEventTitle);
        textViewEventDetail = findViewById(R.id.textViewEventDetail);
        textViewNumberPeopleMax = findViewById(R.id.textViewNumberPeopleMax);
        textViewEventDateStart = findViewById(R.id.textViewEventDateStart);
        textViewEventDateEnd = findViewById(R.id.textViewEventDateEnd);
        textViewEventGender = findViewById(R.id.textViewEventGender);
        textViewNameOwnerEvent = findViewById(R.id.textViewNameOwnerEvent);
        circleImageViewOwnerEvent = findViewById(R.id.circleImageViewOwnerEvent);
        textViewEventAddress = findViewById(R.id.textViewEventAddress);
        textViewEventLocationName = findViewById(R.id.textViewEventLocationName);
        textViewNumberPeople = findViewById(R.id.textViewNumberPeople);
        textViewShowText = findViewById(R.id.textViewShowText);
        textViewEventPrice = findViewById(R.id.textViewEventPrice);


        imageViewEvent = findViewById(R.id.imageViewEvent);

        String event_id = getIntent().getExtras().getString("event_id");
        String event_title = getIntent().getExtras().getString("event_title");
        String event_detail = getIntent().getExtras().getString("event_detail");
        String event_number_people = getIntent().getExtras().getString("event_number_people");
        String event_date_start = getIntent().getExtras().getString("event_date_start");
        String event_date_end = getIntent().getExtras().getString("event_date_end");
        String event_location_name = getIntent().getExtras().getString("event_location_name");
        String event_location_address = getIntent().getExtras().getString("event_location_address");
        String gender = getIntent().getExtras().getString("event_gender");
        String price = getIntent().getExtras().getString("event_price");
        countMax = Integer.valueOf( event_number_people);

        String picture = getIntent().getExtras().getString("event_image");
        if (picture.isEmpty()) {
            imageViewEvent.setImageResource(R.drawable.picture);
        } else {
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/event_img/" + picture;
            System.out.println(url);
            Picasso.get().load(url).into(imageViewEvent);
        }

        textViewEventTitle.setText(event_title);
        textViewEventDetail.setText(event_detail);
        textViewNumberPeopleMax.setText(event_number_people);
        textViewEventDateStart.setText(event_date_start);
        textViewEventDateEnd.setText(event_date_end);

        textViewEventLocationName.setText(event_location_name);
        textViewEventAddress.setText(event_location_address);
        textViewEventPrice.setText(price);


        System.out.println(gender);
        if (gender.equals("m")) {
            gender = "ชาย";
        } else if (gender.equals("f")) {
            gender = "หญิง";
        } else {
            gender = "ชาย และ หญิง";
        }
        textViewEventGender.setText(gender);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        String email = getIntent().getExtras().getString("email");
        System.out.println("email : "+getIntent().getExtras().getString("email"));

        Call<UserResponse> call = RetrofitClient.getInstance().getApi().getUser(email);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                System.out.println(response.body().getMessages());
                if (response.body().isStatus()) {
                    // Toast.makeText(EventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                    String firstname = response.body().getUser().getFirstname();
                    String lastname = response.body().getUser().getLastname();
                    textViewNameOwnerEvent.setText(firstname + "\n" + lastname);

                    String picture = response.body().getUser().getImage_user();
                    if (picture.isEmpty()) {
                        circleImageViewOwnerEvent.setImageResource(R.drawable.user);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/" + picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(circleImageViewOwnerEvent);
                    }


                } else {
                    Toast.makeText(EventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EventActivity.this, "FAIL", Toast.LENGTH_LONG).show();
            }
        });

        Call<JoinResponse> call2 = RetrofitClient.getInstance().getApi().getJoinEventCount(event_id);
        System.out.println("event_id : " +event_id);
        call2.enqueue(new Callback<JoinResponse>() {
            @Override
            public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                System.out.println(response.body().getMessages());
                System.out.println(response.body().getUsers().size());
                if (response.body().isStatus()) {
                    if(response.body().getUsers().size()==0){
                        textViewShowText.setText("ยังไม่มีคนเข้าร่วมกิจกรรม");
                    }
                    else {
                        System.out.println(response.body().getUsers());
                        userList =response.body().getUsers();
                        adapter = new UserJoinAdapter(EventActivity.this,userList);
                        recyclerView.setAdapter(adapter);
                    }

                    countJoint= response.body().getUsers().size();
                    String count= String.valueOf(response.body().getUsers().size());
                    System.out.println(count);
                    textViewNumberPeople.setText(count);
                }else{
                }
            }

            @Override
            public void onFailure(Call<JoinResponse> call, Throwable t) {

            }
        });
        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        buttonSignUp = findViewById(R.id.buttonSignUp);

        Call<StatusResponse> call_status = RetrofitClient.getInstance()
                .getApi().getStatusEvent(
                        SharedPrefManager.getInstance(EventActivity.this).getUser().getEmail()
                );


        call_status.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {




                if (response.body().isStatus()) {

                    if(response.body().getStatus_event().equals("wait")){

                        Drawable img = EventActivity.this.getResources().getDrawable( R.drawable.ic_wait_white );
                        buttonSignUp.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                        buttonSignUp.setBackground(EventActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
                        buttonSignUp.setTextColor(Color.parseColor("#80FFFFFF"));
                        buttonSignUp.setText(" กำลังตรวจสอบการยืนยันตัวตน");


                    }else if(response.body().getStatus_event().equals("no")){

                        buttonSignUp.setText("กรุณายืนยันตัวตนก่อนเข้าร่วม");
                        buttonSignUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(EventActivity.this,ConfirmIdentityActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

                    }else{
                        // เมื่อกดปุ่ม buttonCreateEvent
                        buttonSignUp.setText("เข้าร่วมกิจกรรม");
                        buttonSignUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validateError();
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });


    }
    public boolean validateCountPeople(){
        if(countJoint < countMax){
            return true;
        }else{
            return false;
        }
    }

    public void sendData(){
        String event_id = getIntent().getExtras().getString("event_id");
        String email = SharedPrefManager.getInstance(this).getUser().getEmail();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().jointEvent(event_id,email);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body().isStatus()) {
                    Toast.makeText(EventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }
    public void validateError(){
        if(validateCountPeople()){
            sendData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(EventActivity.this, "กิจกรรมนี้มีคนเข้าร่วมครบแล้ว", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back_home:
                finish();
                break;
//            case R.id.buttonSignUp:
//                validateError();
//                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    // google mapView

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(12);
        mMap.setIndoorEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        String event_latitude =getIntent().getExtras().getString("event_latitude");
        String event_longitude =getIntent().getExtras().getString("event_longitude");

        LatLng ny = new LatLng(Double.parseDouble(event_latitude), Double.parseDouble(event_longitude));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}