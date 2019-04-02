package com.example.phobpa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.phobpa.R;
import com.example.phobpa.storage.SharedPrefManager;

public class MessagesActivity extends AppCompatActivity {
    private Button button_back_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        button_back_home = findViewById(R.id.button_back_home);
        button_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
