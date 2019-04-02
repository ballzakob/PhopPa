package com.example.phobpa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phobpa.R;
import com.example.phobpa.modelsEvents.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowEventAdapter extends RecyclerView.Adapter<ShowEventAdapter.EventViewHolder> {


    private Context mCtx;
    private List<Event> eventList;

    public ShowEventAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_show_event_not_me, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {

        Event event = eventList.get(i);

        eventViewHolder.textViewEventTitle.setText(event.getEvent_title());
        eventViewHolder.textViewEventLocation.setText(event.getEvent_location_address());
        eventViewHolder.textViewNumberPeople.setText(event.getEvent_number_people());

        String picture = event.getEvent_image();
        System.out.println(picture);
        if(picture.isEmpty()){
            eventViewHolder.imageViewEvent.setImageResource(R.drawable.picture);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/event_img/"+picture;
            Picasso.get().load(url).into(eventViewHolder.imageViewEvent);
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventTitle, textViewEventLocation, textViewNumberPeople;
        ImageView imageViewEvent;

        public EventViewHolder(View itemView){
            super(itemView);
            textViewEventTitle = itemView.findViewById(R.id.textViewEventTitle);
            textViewEventLocation = itemView.findViewById(R.id.textViewEventLocation);
            textViewNumberPeople = itemView.findViewById(R.id.textViewNumberPeople);
            imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
        }
    }

}
