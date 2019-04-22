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

public class EventMeAdapter extends RecyclerView.Adapter<EventMeAdapter.EventViewHolder> {


    private Context mCtx;
    private List<Event> eventList;

    public EventMeAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {

        Event event = eventList.get(i);

        eventViewHolder.textViewEventTitle.setText(cutString(event.getEvent_title()));
        eventViewHolder.textViewEventDetail.setText(cutString(event.getEvent_detail()));
        eventViewHolder.textViewEventDateStart.setText(splitDate(event.getEvent_date_start()));
        eventViewHolder.textViewNumberPeople.setText(event.getEvent_number_people());

        String picture = event.getEvent_image();
        System.out.println(picture);
        if(picture.isEmpty()){
            eventViewHolder.imageViewEvent.setImageResource(R.drawable.no_image);
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

        TextView textViewEventTitle, textViewEventDetail, textViewEventDateStart , textViewNumberPeople;
        ImageView imageViewEvent;

        public EventViewHolder(View itemView){
            super(itemView);

            textViewEventTitle = itemView.findViewById(R.id.textViewEventTitle);
            textViewEventDetail = itemView.findViewById(R.id.textViewEventDetail);
            textViewEventDateStart = itemView.findViewById(R.id.textViewEventDateStart);
            textViewNumberPeople = itemView.findViewById(R.id.textViewNumberPeople);
            imageViewEvent = itemView.findViewById(R.id.imageViewEvent);


        }

    }

    public String cutString(String detail){
        if(detail.length() >16){
            return detail.substring(0,14)+"...";
        }else{
            return detail;
        }
    }

    public String splitDate(String date) {
        String[] arrDate = date.split("-");
        String day = arrDate[2];
        String mount = "";
        int year = Integer.valueOf(arrDate[0])+543;
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
        return day+mount;
    }


}
