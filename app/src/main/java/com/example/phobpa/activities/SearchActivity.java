package com.example.phobpa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cielyang.android.clearableedittext.ClearableEditText;
import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ClearableEditText editText_search;
    private TextView textViewWord;

    private RecyclerView recyclerView;
    private EventMeAdapter adapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.button_search).setOnClickListener(this);
        findViewById(R.id.buttonBack).setOnClickListener(this);

        editText_search = findViewById(R.id.editText_search);
        textViewWord = findViewById(R.id.textViewWord);

        String textSearch = getIntent().getExtras().getString("textSearch");
        editText_search.setText(textSearch);
        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .searchEvent(SharedPrefManager.getInstance(this).getUser().getEmail(),textSearch);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList =response.body().getEvents();
                adapter = new EventMeAdapter( SearchActivity.this,eventList);
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

                        Intent intent = new Intent(SearchActivity.this, EventActivity.class);
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
                        Toast.makeText(SearchActivity.this, "รหัสกิจกรรม : "+eventList.get(position).getEvent_id(), Toast.LENGTH_SHORT).show();
                        // do whatever
                    }
                })
        );
    }

    private void searchText() {

        String textSearch = editText_search.getText().toString();
        Call<EventResponse> call = RetrofitClient.getInstance().getApi()
                .searchEvent(SharedPrefManager.getInstance(this).getUser().getEmail(),textSearch);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                eventList =response.body().getEvents();
                adapter = new EventMeAdapter( SearchActivity.this,eventList);
                recyclerView.setAdapter(adapter);

                if (eventList.size()>0){
                    textViewWord.setText("");
                }


            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_search:
                searchText();
                break;
            case R.id.buttonBack:
                finish();
                break;

        }
    }


}
