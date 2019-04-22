package com.example.phobpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.adapter.ShowEventAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowEventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowEventAdapter adapter;
    private List<Event> eventList;
    private TextView textViewEventNameType, textViewEventNameLocation ,textView;
    private Button buttonBack ,buttonCreate;
    String types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        textViewEventNameType = findViewById(R.id.textViewEventNameType);
        textViewEventNameLocation = findViewById(R.id.textViewEventNameLocation);
        textView = findViewById(R.id.textView);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowEventActivity.this,CreateEventActivity.class);
                finish();
                startActivity(intent);
            }
        });

        String type = getIntent().getExtras().getString("event_types");
        if (type.equals("อาหาร")) {
            types = "food";
        } else if (type.equals("แฟชั่น")) {
            types = "fashion";
        } else if (type.equals("เครื่องสำอาง")) {
            types = "cosmetic";
        } else if (type.equals("กีฬา")) {
            types = "sport";
        } else if (type.equals("บันเทิง")) {
            types = "entertainment";
        } else {
            types = "travel";
        }
        String name_location = getIntent().getExtras().getString("event_location_name");
        String address_location = getIntent().getExtras().getString("event_location_address");
        String latitude = getIntent().getExtras().getString("event_latitude");
        String longitude = getIntent().getExtras().getString("event_longitude");
        textViewEventNameType.setText(type);
        textViewEventNameLocation.setText(address_location);


        System.out.println(SharedPrefManager.getInstance(this).getUser().getEmail());
        System.out.println(latitude);
        System.out.println(longitude);
        System.out.println(type);

        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .showEventNotMe(SharedPrefManager.getInstance(this).getUser().getEmail(),latitude,longitude,types);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.body().isStatus()){
                    eventList =response.body().getEvents();
                    adapter = new ShowEventAdapter(ShowEventActivity.this,eventList);
                    recyclerView.setAdapter(adapter);
                }else{
                    textView.setText("ไม่พบกิจกกรมที่คล้ายกัน");
                }
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });

        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Toast.makeText(ShowEventActivity.this, eventList.get(position).getEvent_title(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ShowEventActivity.this, EventActivity.class);

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
                        Toast.makeText(ShowEventActivity.this, eventList.get(position).getEvent_title(), Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        String latitude = getIntent().getExtras().getString("event_latitude");
        String longitude = getIntent().getExtras().getString("event_longitude");
        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .showEventNotMe(SharedPrefManager.getInstance(this).getUser().getEmail(),latitude,longitude,types);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.body().isStatus()){
                    eventList =response.body().getEvents();
                    adapter = new ShowEventAdapter(ShowEventActivity.this,eventList);
                    recyclerView.setAdapter(adapter);
                }else{
                    textView.setText("ไม่พบกิจกกรมที่คล้ายกัน");
                }
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });
    }
}
