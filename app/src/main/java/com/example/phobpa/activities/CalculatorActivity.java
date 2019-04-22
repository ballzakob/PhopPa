package com.example.phobpa.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.adapter.CalculatorAdapter;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculatorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CalculatorAdapter adapter;
    private List<Event> eventList;

    TextView textView_price;
    Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        textView_price = findViewById(R.id.textView_price);
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .calculatorEvent(SharedPrefManager.getInstance(this).getUser().getEmail());

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {


                eventList = response.body().getEvents();
                adapter = new CalculatorAdapter(CalculatorActivity.this, eventList);
                recyclerView.setAdapter(adapter);

                double sum = 0;
                for (int i = 0 ; i<eventList.size();i++){
                    double num = Double.valueOf(eventList.get(i).getEvent_price());
                    sum += num;
                }
                String sum_cal = String.valueOf(sum);
                if(sum_cal.substring(sum_cal.length()-2,sum_cal.length()).equals(".0")){
                    sum_cal = sum_cal.substring(0,sum_cal.length()-2);
                }
                textView_price.setText(sum_cal);


            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
//                        Toast.makeText(getContext(), eventList.get(position).getEvent_title(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CalculatorActivity.this, DetailEventActivity.class);
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
                        // do whatever
                    }
                })
        );
    }
}
