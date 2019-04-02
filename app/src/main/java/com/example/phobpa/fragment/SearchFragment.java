package com.example.phobpa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phobpa.R;


public class SearchFragment extends Fragment {
    ImageView imageViewFood;
    ImageView imageViewCosmetics;
    ImageView imageViewFashion;
    ImageView imageViewSport;
    ImageView imageViewEntertainment;
    ImageView imageViewTravel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        imageViewFood = v.findViewById(R.id.imageViewFood);
        imageViewCosmetics = v.findViewById(R.id.imageViewCosmetics);
        imageViewFashion = v.findViewById(R.id.imageViewFashion);
        imageViewSport = v.findViewById(R.id.imageViewSport);
        imageViewEntertainment = v.findViewById(R.id.imageViewEntertainment);
        imageViewTravel = v.findViewById(R.id.imageViewTravel);
        return v;
    }
}