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

public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.CalculatorViewHolder> {
    private Context mCtx;
    private List<Event> eventList;

    public CalculatorAdapter(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public CalculatorAdapter.CalculatorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_cal, viewGroup, false);
        return new CalculatorAdapter.CalculatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculatorAdapter.CalculatorViewHolder calculatorViewHolder, int i) {

        Event event = eventList.get(i);
        calculatorViewHolder.textView_event_title.setText(event.getEvent_title());
        calculatorViewHolder.textView_price.setText(event.getEvent_price());

        String picture = event.getEvent_image();
        System.out.println(picture);
        if(picture.isEmpty()){
            calculatorViewHolder.imageView_event.setImageResource(R.drawable.no_image);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/event_img/"+picture;
            Picasso.get().load(url).into(calculatorViewHolder.imageView_event);
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class CalculatorViewHolder extends RecyclerView.ViewHolder{

        TextView textView_event_title, textView_price;
        ImageView imageView_event;

        public CalculatorViewHolder(View itemView){
            super(itemView);

            textView_event_title = itemView.findViewById(R.id.textView_event_title);
            textView_price = itemView.findViewById(R.id.textView_price);
            imageView_event = itemView.findViewById(R.id.imageView_event);


        }

    }

}
