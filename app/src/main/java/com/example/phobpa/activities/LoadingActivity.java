package com.example.phobpa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.phobpa.R;

public class LoadingActivity extends AppCompatActivity {


    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView im = (ImageView)findViewById(R.id.imageView);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
//                    dialog = new ProgressDialog(LoadingActivity.this);
//                    dialog.setMessage("Loading");
//                    dialog.show();
                    Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();

    }
}
