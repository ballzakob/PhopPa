package com.example.phobpa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewEventType;
    private TextView textViewWord;

    private RecyclerView recyclerView;
    private EventMeAdapter adapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);

        findViewById(R.id.button_back).setOnClickListener(this);

        String type = getIntent().getExtras().getString("event_types");

        textViewEventType = findViewById(R.id.textViewEventType);
        textViewWord = findViewById(R.id.textViewWord);

        setType(type);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchTypeActivity.this, 2));

        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .searchEventType(SharedPrefManager.getInstance(this).getUser().getEmail(),type);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList =response.body().getEvents();
                adapter = new EventMeAdapter( SearchTypeActivity.this,eventList);
                recyclerView.setAdapter(adapter);

                if (eventList.size()>0){
                    textViewWord.setText("");
                }


            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

//                        Toast.makeText(getContext(), eventList.get(position).getEvent_title(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SearchTypeActivity.this, EventActivity.class);
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
                        Toast.makeText(SearchTypeActivity.this, "รหัสกิจกรรม : "+eventList.get(position).getEvent_id(), Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );

    }

    public void setType(String type){
        if(type.equals("food")){

            textViewEventType.setText("อาหาร");

        }else if(type.equals("cosmetic")){

            textViewEventType.setText("เครื่องสำอาง");

        }else if(type.equals("fashion")){

            textViewEventType.setText("แฟชั่น");

        }else if(type.equals("sport")){

            textViewEventType.setText("กีฬา");

        }else if(type.equals("entertainment")){

            textViewEventType.setText("บันเทิง");

        }else if(type.equals("travel")){
            textViewEventType.setText("ท่องเที่ยว");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
        }
    }
}
