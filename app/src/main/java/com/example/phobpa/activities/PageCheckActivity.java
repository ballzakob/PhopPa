package com.example.phobpa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.StatusResponse;
import com.example.phobpa.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageCheckActivity extends AppCompatActivity {

    Button button_check_status;
    Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_check);


        button_check_status = findViewById(R.id.button_check_status);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Call<StatusResponse> call_status = RetrofitClient.getInstance()
                .getApi().getStatusEvent(
                        SharedPrefManager.getInstance(PageCheckActivity.this).getUser().getEmail()
                );


        call_status.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {


                if (response.body().isStatus()) {

                    if (response.body().getStatus_event().equals("wait")) {

                        Drawable img = PageCheckActivity.this.getResources().getDrawable(R.drawable.ic_wait_white);
                        button_check_status.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        button_check_status.setBackground(PageCheckActivity.this.getResources().getDrawable(R.drawable.background_button_dont_click));
                        button_check_status.setTextColor(Color.parseColor("#80FFFFFF"));
                        button_check_status.setText(" กำลังตรวจสอบการยืนยันตัวตน");


                    } else if (response.body().getStatus_event().equals("no")) {

                        button_check_status.setText("กรุณายืนยันตัวตนก่อนเข้าร่วม");
                        button_check_status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(PageCheckActivity.this, ConfirmIdentityActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

                    } else {
                        // เมื่อกดปุ่ม buttonCreateEvent
                        Drawable img = PageCheckActivity.this.getResources().getDrawable( R.drawable.ic_done_white );
                        button_check_status.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                        button_check_status.setBackground(PageCheckActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
                        button_check_status.setTextColor(Color.parseColor("#80FFFFFF"));
                        button_check_status.setText(" ยืนยันตัวตนเสร็จเรียบร้อย");
                        Intent i = new Intent(PageCheckActivity.this, SelectTypeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });
    }
}
