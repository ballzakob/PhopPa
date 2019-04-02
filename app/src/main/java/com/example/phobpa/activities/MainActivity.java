package com.example.phobpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.phobpa.R;
import com.example.phobpa.fragment.HomeFragment;
import com.example.phobpa.fragment.MyEventFragment;
import com.example.phobpa.fragment.NotificationFragment;
import com.example.phobpa.fragment.SearchFragment;
import com.example.phobpa.storage.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    HomeFragment home;
    NotificationFragment noti;
    SearchFragment search;
    MyEventFragment my_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

//        todo: ทำให่ BottomNavigationView แสดงทั้ง icon และ ตัวหนังสือ พร้อมกัน ไม่ขยับไปขยับมา

        // TODO: 3/3/2019 เปลี่ยน ขนาด font ที่ ไฟล์ values/dimen.xml 
        bottomNav.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);


        bottomNav.setOnNavigationItemSelectedListener(navListener);
        home = new HomeFragment();
        noti = new NotificationFragment();
        search = new SearchFragment();
        my_event = new MyEventFragment();

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    home).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIN()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.nav_home:
                            selectedFragment = home;
                            break;
                        case R.id.nav_notification:
                            selectedFragment = noti;
                            break;
                        case R.id.nav_search:
                            selectedFragment = search;
                            break;
                        case R.id.nav_my_event:
                            selectedFragment = my_event;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}