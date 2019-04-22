package com.example.phobpa.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.activities.CalculatorActivity;
import com.example.phobpa.activities.EventActivity;
import com.example.phobpa.activities.MapsActivity;
import com.example.phobpa.activities.MessagesActivity;
import com.example.phobpa.activities.PageCheckActivity;
import com.example.phobpa.activities.SettingsActivity;
import com.example.phobpa.activities.SelectTypeActivity;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private EventMeAdapter adapter;
    private List<Event> eventList;

    private CircleImageView circleImageView_profile;

    private LocationManager locationManager;
    private Double Latitude_current = 0.0;
    private Double Longitude_current = 0.0;

    private static final int REQUEST_LOCATION = 1;

    // TODO: 2019-03-07   method main ของหน้า
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        v.findViewById(R.id.buttonCreateEvent).setOnClickListener(this);
        v.findViewById(R.id.buttonMap).setOnClickListener(this);
        v.findViewById(R.id.button_calculator).setOnClickListener(this);


        recyclerView = v.findViewById(R.id.recyclerView_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


//        button_messages = v.findViewById(R.id.button_messages);
//        button_messages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), MessagesActivity.class);
//                startActivity(i);
//            }
//        });

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        circleImageView_profile =v.findViewById(R.id.circleImageView_profile);
        String picture = SharedPrefManager.getInstance(getContext()).getUser().getImage_user();
        System.out.println(picture);
        if(picture.isEmpty()){
            circleImageView_profile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            Picasso.get().load(url).into(circleImageView_profile);
        }
//        circleImageView_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i =  new Intent(getContext(), SettingsActivity.class);
//                startActivity(i);
//            }
//        });


        // TODO: 2019-03-07  สร้าง layout มาโชว์
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println(Latitude_current);
                        System.out.println(Longitude_current);

//                        Toast.makeText(getContext(), eventList.get(position).getEvent_title(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), EventActivity.class);
                        intent.putExtra("event_id",eventList.get(position).getEvent_id());
                        intent.putExtra("email",eventList.get(position).getEmail());
                        intent.putExtra("event_title",eventList.get(position).getEvent_title());
                        intent.putExtra("event_detail",eventList.get(position).getEvent_detail());
                        intent.putExtra("event_date_start",eventList.get(position).getEvent_date_start());
                        intent.putExtra("event_date_end",eventList.get(position).getEvent_date_end());
                        intent.putExtra("event_time_start",eventList.get(position).getEvent_time_start());
                        intent.putExtra("event_time_end",eventList.get(position).getEvent_time_end());
                        intent.putExtra("event_number_people",eventList.get(position).getEvent_number_people());
                        intent.putExtra("event_gender",eventList.get(position).getEvent_gender());
                        intent.putExtra("event_types",eventList.get(position).getEvent_types());
                        intent.putExtra("event_price",eventList.get(position).getEvent_price());
                        intent.putExtra("event_location_name",eventList.get(position).getEvent_location_name());
                        intent.putExtra("event_location_address",eventList.get(position).getEvent_location_address());
                        intent.putExtra("event_latitude",eventList.get(position).getEvent_latitude());
                        intent.putExtra("event_longitude",eventList.get(position).getEvent_longitude());
                        intent.putExtra("event_image",eventList.get(position).getEvent_image());
                        intent.putExtra("event_price",eventList.get(position).getEvent_price());
                        startActivity(intent);
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String price=  eventList.get(position).getEvent_price();
//                        Toast.makeText(getContext(), eventList.get(position).getEvent_title()+"\nby :"+eventList.get(position).getEmail(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "รหัสกิจกรรม : "+eventList.get(position).getEvent_id(), Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .getEventNotMe(SharedPrefManager.getInstance(getContext()).getUser().getEmail(),Latitude_current.toString(),Longitude_current.toString());

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList =response.body().getEvents();
                adapter = new EventMeAdapter( getActivity(),eventList);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        String picture = SharedPrefManager.getInstance(getContext()).getUser().getImage_user();
        System.out.println(picture);
        if(picture.isEmpty()){
            circleImageView_profile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            Picasso.get().load(url).into(circleImageView_profile);
        }
        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .getEventNotMe(SharedPrefManager.getInstance(getContext()).getUser().getEmail(),Latitude_current.toString(),Longitude_current.toString());

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList =response.body().getEvents();
                adapter = new EventMeAdapter( getActivity(),eventList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });

    }

    public void button_calculator(){
        Intent intent = new Intent(getContext(), CalculatorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCreateEvent:
                Intent intent = new Intent(getContext(), PageCheckActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMap:
                Intent intent2 = new Intent(getContext(), MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.button_calculator:
                button_calculator();
                break;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

                Toast.makeText(getContext(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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