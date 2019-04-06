package com.example.phobpa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.RecyclerItemClickListener;
import com.example.phobpa.activities.EventActivity;
import com.example.phobpa.activities.MessagesActivity;
import com.example.phobpa.activities.ProfileActivity;
import com.example.phobpa.adapter.EventMeAdapter;
import com.example.phobpa.adapter.PagerAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsEvents.Event;
import com.example.phobpa.modelsEvents.EventResponse;
import com.example.phobpa.storage.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyEventFragment extends Fragment implements Sub1Fragment.OnFragmentInteractionListener,Sub2Fragment.OnFragmentInteractionListener{

    private RecyclerView recyclerView;
    private EventMeAdapter adapter;
    private List<Event> eventList;

    private Button button_messages, button_profile;
//    private ViewPager viewPager;
//    private TabLayout tablayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_event, container, false);

        button_messages = v.findViewById(R.id.button_messages);

        button_profile = v.findViewById(R.id.button_profile);

        button_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MessagesActivity.class);
                startActivity(i);
            }
        });

        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
            }
        });

        ViewPager viewPager = v.findViewById(R.id.pager);
        TabLayout tablayout = v.findViewById(R.id.tablayout);


        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),tablayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()  {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

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