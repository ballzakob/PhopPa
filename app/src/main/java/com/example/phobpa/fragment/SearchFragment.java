package com.example.phobpa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cielyang.android.clearableedittext.ClearableEditText;
import com.example.phobpa.R;
import com.example.phobpa.activities.RegisterActivity;
import com.example.phobpa.activities.SearchActivity;
import com.example.phobpa.activities.SearchTypeActivity;


public class SearchFragment extends Fragment implements View.OnClickListener {


    ClearableEditText editText_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        v.findViewById(R.id.imageViewFood).setOnClickListener(this);
        v.findViewById(R.id.imageViewCosmetics).setOnClickListener(this);
        v.findViewById(R.id.imageViewFashion).setOnClickListener(this);
        v.findViewById(R.id.imageViewSport).setOnClickListener(this);
        v.findViewById(R.id.imageViewEntertainment).setOnClickListener(this);
        v.findViewById(R.id.imageViewTravel).setOnClickListener(this);
        v.findViewById(R.id.button_search).setOnClickListener(this);

        editText_search = v.findViewById(R.id.editText_search);
        editText_search.getText().toString();


        return v;
    }

    public void sendType(String type){

        Intent intent = new Intent(getContext(), SearchTypeActivity.class);
        intent.putExtra("event_types",type);
        startActivity(intent);

    }

    private void search() {
        String search = editText_search.getText().toString();
        if(search.equals("")){
            Toast.makeText(getContext(), "ยังไมไ่ด้พิมพ์ข้อความ", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("textSearch",search);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewFood:
                sendType("food");
                break;
            case R.id.imageViewCosmetics:
                sendType("cosmetic");
                break;
            case R.id.imageViewFashion:
                sendType("fashion");
                break;
            case R.id.imageViewSport:
                sendType("sport");
                break;
            case R.id.imageViewEntertainment:
                sendType("entertainment");
                break;
            case R.id.imageViewTravel:
                sendType("travel");
                break;
            case R.id.button_search:
                search();
                break;
        }
    }

}