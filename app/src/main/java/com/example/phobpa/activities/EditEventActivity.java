package com.example.phobpa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.modelsUsers.UserResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private TextView textViewNameOwnerEvent, textViewEventDateStart, textViewEventTimeStart,
            textViewEventDateEnd, textViewEventTimeEnd, textViewEventLocationName,
            textViewEventAddress,textViewEventGender;
    private CircleImageView circleImageViewOwnerEvent;

    private EditText editTextEventTitle, editTextNumberPeopleMax, editTextEventPrice,
            editTextEventDetail;

    private GoogleMap mMap;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private String event_latitude = "";
    private String event_longitude = "";

    private ImageView imageViewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        findViewById(R.id.buttonBack).setOnClickListener(this);

        textViewNameOwnerEvent = findViewById(R.id.textViewNameOwnerEvent);
        textViewEventDateStart = findViewById(R.id.textViewEventDateStart);
        textViewEventDateEnd = findViewById(R.id.textViewEventDateEnd);
        textViewEventTimeStart = findViewById(R.id.textViewEventTimeStart);
        textViewEventTimeEnd = findViewById(R.id.textViewEventTimeEnd);
        textViewEventLocationName = findViewById(R.id.textViewEventLocationName);
        textViewEventAddress = findViewById(R.id.textViewEventAddress);
        textViewEventGender = findViewById(R.id.textViewEventGender);

        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextNumberPeopleMax = findViewById(R.id.editTextNumberPeopleMax);
        editTextEventPrice = findViewById(R.id.editTextEventPrice);
        editTextEventDetail = findViewById(R.id.editTextEventDetail);

        circleImageViewOwnerEvent = findViewById(R.id.circleImageViewOwnerEvent);

        imageViewEvent = findViewById(R.id.imageViewEvent);



        String event_id = getIntent().getExtras().getString("event_id");

        Call<EventResponse> call = RetrofitClient.getInstance().getApi().getEvent(event_id);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                if (response.body().isStatus()) {

                    editTextEventTitle.setText(response.body().getEvents().get(0).getEvent_title());
                    editTextEventDetail.setText(response.body().getEvents().get(0).getEvent_detail());
                    editTextNumberPeopleMax.setText(response.body().getEvents().get(0).getEvent_number_people());
                    textViewEventDateStart.setText(splitDate(response.body().getEvents().get(0).getEvent_date_start()));
                    textViewEventDateEnd.setText(splitDate(response.body().getEvents().get(0).getEvent_date_end()));
                    textViewEventTimeStart.setText(splitTime(response.body().getEvents().get(0).getEvent_time_start()));
                    textViewEventTimeEnd.setText(splitTime(response.body().getEvents().get(0).getEvent_time_end()));
                    textViewEventLocationName.setText(response.body().getEvents().get(0).getEvent_location_name());
                    textViewEventAddress.setText(response.body().getEvents().get(0).getEvent_location_address());
                    editTextEventPrice.setText(response.body().getEvents().get(0).getEvent_price());

                    event_latitude = response.body().getEvents().get(0).getEvent_latitude();
                    event_longitude = response.body().getEvents().get(0).getEvent_longitude();


                    String picture = response.body().getEvents().get(0).getEvent_image();
                    if (picture.isEmpty()) {
                        imageViewEvent.setImageResource(R.drawable.picture);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/event_img/" + picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(imageViewEvent);
                    }

                    String gender = response.body().getEvents().get(0).getEvent_gender();
                    if (gender.equals("m")) {
                        gender = "ชาย";
                    } else if (gender.equals("f")) {
                        gender = "หญิง";
                    } else {
                        gender = "ชาย และ หญิง";
                    }
                    textViewEventGender.setText(gender);

                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
//                Toast.makeText(EventMeActivity.this, "fall", Toast.LENGTH_LONG).show();
            }
        });

        String email = getIntent().getExtras().getString("email");

        Call<UserResponse> call2 = RetrofitClient.getInstance().getApi().getUser(email);
        call2.enqueue(new Callback<UserResponse>() {
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
                    Toast.makeText(EditEventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditEventActivity.this, "FAIL", Toast.LENGTH_LONG).show();
            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

    }

    public String splitDate(String date) {
        String[] arrDate = date.split("-");
        String day = arrDate[2];
        String mount = "";
        int year = Integer.valueOf(arrDate[0]) + 543;
        if (arrDate[1].equals("01")) {
            mount = " ม.ค. ";
        } else if (arrDate[1].equals("02")) {
            mount = " ก.พ. ";
        } else if (arrDate[1].equals("03")) {
            mount = " มี.ค. ";
        } else if (arrDate[1].equals("04")) {
            mount = " เม.ย. ";
        } else if (arrDate[1].equals("05")) {
            mount = " พ.ค. ";
        } else if (arrDate[1].equals("06")) {
            mount = " มิ.ย. ";
        } else if (arrDate[1].equals("07")) {
            mount = " ก.ค. ";
        } else if (arrDate[1].equals("08")) {
            mount = " ส.ค. ";
        } else if (arrDate[1].equals("09")) {
            mount = " ก.ย. ";
        } else if (arrDate[1].equals("10")) {
            mount = " ต.ค. ";
        } else if (arrDate[1].equals("11")) {
            mount = " พ.ย. ";
        } else {
            mount = " ธ.ค. ";
        }
        return day + mount + String.valueOf(year);
    }

    public String splitTime(String time) {
        String[] arrTime = time.split(":");
        String hour = arrTime[0];
        String minute = arrTime[1];
        return hour + ":" + minute + " น.";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                finish();
                break;
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

        LatLng ny = new LatLng(Double.parseDouble(event_latitude), Double.parseDouble(event_longitude));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        mMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ny, 17));
    }
}
