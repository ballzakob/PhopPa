package com.example.phobpa.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private GoogleMap mMap;
    private Double Latitude_current = 0.0;
    private Double Longitude_current = 0.0;
    private List<Event> eventList;
    Button button_back_home;

    private static final int REQUEST_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button_back_home = findViewById(R.id.button_back_home);
        button_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .getEventNotMe(SharedPrefManager.getInstance(this).getUser().getEmail(),Latitude_current.toString(),Longitude_current.toString());

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList = response.body().getEvents();
                for (int i = 0; i < eventList.size(); i++) {

                    String name_event = eventList.get(i).getEvent_title();
                    String name = eventList.get(i).getEvent_location_name();
                    String address = eventList.get(i).getEvent_location_address();
                    double latitude = Double.parseDouble(eventList.get(i).getEvent_latitude());
                    double longitude = Double.parseDouble(eventList.get(i).getEvent_longitude());



                    LatLng picker = new LatLng(latitude, longitude);
                    MarkerOptions marker = new MarkerOptions().position(picker).title(name_event);
                    int height = 120;
                    int width = 120;

                    if ((eventList.get(i).getEvent_types()).equals("food")) {

                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.food);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    else if((eventList.get(i).getEvent_types()).equals("fashion")){
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.shirt);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    else if((eventList.get(i).getEvent_types()).equals("entertainment")){
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.poker);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    else if((eventList.get(i).getEvent_types()).equals("sport")){
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.dumbbell);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    else if((eventList.get(i).getEvent_types()).equals("cosmetic")){
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.cosmetic);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    else if((eventList.get(i).getEvent_types()).equals("travel")){
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.airplane);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    }
                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(picker));


                }

                int height = 120;
                int width = 120;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.placeholder);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                // ดึงตำแหน่งปัจจุบันมาแสดง
                LatLng Current = new LatLng(Latitude_current, Longitude_current);
                MarkerOptions marker = new MarkerOptions().position(Current).title("ตำแหน่งปัจจุบัน");
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Current));


                // Focus & Zoom
                LatLng coordinate = new LatLng(Latitude_current, Longitude_current);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 13));
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
            }
        });




//        // ดึงตำแหน่งปัจจุบันมาแสดง
//        LatLng Current = new LatLng(Latitude_current, Longitude_current);
//        mMap.addMarker(new MarkerOptions().position(Current).title("ตำแหน่งปัจจุบัน"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Current));
//
//
//        // Focus & Zoom
//        LatLng coordinate = new LatLng(Latitude_current, Longitude_current);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;


            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                Latitude_current = latti;
                Longitude_current = longi;

            } else {

                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
