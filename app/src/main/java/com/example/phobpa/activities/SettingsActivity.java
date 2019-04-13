package com.example.phobpa.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.example.phobpa.modelsUsers.StatusResponse;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewFirstName, textViewLastName, textViewBirthday, textViewTelephone;
    private Button buttonConfirmIdentity,buttonEditProfile;

    private CircleImageView circleImageView_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.buttonLogout).setOnClickListener(this);
        findViewById(R.id.buttonDelete).setOnClickListener(this);
        findViewById(R.id.buttonEditProfile).setOnClickListener(this);


        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        textViewTelephone = findViewById(R.id.textViewTelephone);

        textViewFirstName.setText(SharedPrefManager.getInstance(this).getUser().getFirstname());
        textViewLastName.setText(SharedPrefManager.getInstance(this).getUser().getLastname());
        textViewBirthday.setText(SharedPrefManager.getInstance(this).getUser().getBirthday());
        textViewTelephone.setText(SharedPrefManager.getInstance(this).getUser().getTelephone());

        buttonConfirmIdentity = findViewById(R.id.buttonConfirmIdentity);


        circleImageView_profile =findViewById(R.id.circleImageView_profile);
        String picture = SharedPrefManager.getInstance(this).getUser().getImage_user();
        if(picture.isEmpty()){
            circleImageView_profile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            System.out.println(url);
            Picasso.get().load(url).into(circleImageView_profile);
        }

        Call<StatusResponse> call = RetrofitClient.getInstance()
                .getApi().getStatusEvent(
                        SharedPrefManager.getInstance(SettingsActivity.this).getUser().getEmail()
                );


        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body().isStatus()) {

                    if(response.body().getStatus_event().equals("wait")){

                        Drawable img = SettingsActivity.this.getResources().getDrawable( R.drawable.ic_wait_white );
                        buttonConfirmIdentity.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                        buttonConfirmIdentity.setBackground(SettingsActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
                        buttonConfirmIdentity.setTextColor(Color.parseColor("#80FFFFFF"));
                        buttonConfirmIdentity.setText(" กำลังตรวจสอบการยืนยันตัวตน");


                    }else if(response.body().getStatus_event().equals("yes")){

                        Drawable img = SettingsActivity.this.getResources().getDrawable( R.drawable.ic_done_white );
                        buttonConfirmIdentity.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
                        buttonConfirmIdentity.setBackground(SettingsActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
                        buttonConfirmIdentity.setTextColor(Color.parseColor("#80FFFFFF"));
                        buttonConfirmIdentity.setText(" ยืนยันตัวตนเสร็จเรียบร้อย");

                    }else{
                        buttonConfirmIdentity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(SettingsActivity.this,ConfirmIdentityActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });


    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setMessage("คุณแน่ใจหรือไม่ว่าคุณต้องการออกจากระบบ");
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPrefManager.getInstance(SettingsActivity.this).clear();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
        SharedPrefManager.getInstance(this).clear();
    }

//    private void deleteUser() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Are you sure?");
//        builder.setMessage("This action is irreversible...");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                User user = SharedPrefManager.getInstance(getActivity()).getUser();
//                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteUser(user.getId());
//
//                call.enqueue(new Callback<DefaultResponse>() {
//                    @Override
//                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//
//                        if (!response.body().isErr()) {
//                            SharedPrefManager.getInstance(getActivity()).clear();
//                            SharedPrefManager.getInstance(getActivity()).clear();
//                            Intent intent = new Intent(getActivity(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//
//                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
//
//                    }
//                });
//
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        AlertDialog ad = builder.create();
//        ad.show();
//    }

    public void EditProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);

        intent.putExtra("firstname",SharedPrefManager.getInstance(this).getUser().getFirstname());
        intent.putExtra("lastname",SharedPrefManager.getInstance(this).getUser().getLastname());
        intent.putExtra("birthday",SharedPrefManager.getInstance(this).getUser().getBirthday());
        intent.putExtra("telephone",SharedPrefManager.getInstance(this).getUser().getTelephone());
        intent.putExtra("image_user",SharedPrefManager.getInstance(this).getUser().getImage_user());

        startActivity(intent);
        finish();
    }

    public void ConfirmIdentity(){
        Intent i = new Intent(SettingsActivity.this,ConfirmIdentityActivity.class);
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.buttonLogout:
                logout();
                break;
            case R.id.buttonDelete:
                break;
            case R.id.buttonEditProfile:
                EditProfile();
                break;
        }
    }
}
