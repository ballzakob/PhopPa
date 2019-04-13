package com.example.phobpa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.phobpa.R;
import com.example.phobpa.activities.SettingsActivity;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.adapter.PagerAdapter;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyEventFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventMeAdapter adapter;
    private List<Event> eventList;

    private Button button_settings;

    private CircleImageView circleImageView_profile;
//    private ViewPager viewPager;
//    private TabLayout tablayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_event, container, false);

        button_settings = v.findViewById(R.id.button_settings);

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        });

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
        final ViewPager viewPager = v.findViewById(R.id.pager);
        TabLayout tablayout = v.findViewById(R.id.tablayout);


        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),tablayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()  {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }

}