package com.example.phobpa.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.LoginResponse;
import com.example.phobpa.modelsUsers.User;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    EditText editTextFirstName,editTextLastName,editTextBirthday,editTextTelephone;

    private CircleImageView circleImageView_profile;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.textAccept).setOnClickListener(this);

        String firstname = getIntent().getExtras().getString("firstname");
        String lastname = getIntent().getExtras().getString("lastname");
        String birthday = getIntent().getExtras().getString("birthday");
        String telephone = getIntent().getExtras().getString("telephone");
        String image_user = getIntent().getExtras().getString("image_user");
        img = image_user;

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextTelephone = findViewById(R.id.editTextTelephone);

        editTextFirstName.setText(firstname);
        editTextLastName.setText(lastname);
        editTextBirthday.setText(birthday);
        editTextTelephone.setText(telephone);

        circleImageView_profile =findViewById(R.id.circleImageView_profile);
        String picture = image_user;
        if(picture.isEmpty()){
            circleImageView_profile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            System.out.println(url);
            Picasso.get().load(url).into(circleImageView_profile);
        }
        
    }


    public void cancle(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(EditProfileActivity.this);
        builder.setMessage("ต้องการยกเลิกการแก้ไขข้อมูลส่วนตัว ?");
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),
                        "ยกเลิกการแก้ไขข้อมูลส่วนตัว", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }


    public void Accept(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(EditProfileActivity.this);
        builder.setMessage("ยืนยันการแก้ไขข้อมูลส่วนตัว");
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),
                        "แก้ไขข้อมูลส่วนตัวเสร็จเรียบร้อย", Toast.LENGTH_SHORT).show();
                String firstname = editTextFirstName.getText().toString().trim();
                String lastname = editTextLastName.getText().toString().trim();
                String birthday = editTextBirthday.getText().toString( ).trim();
                String telephone = editTextTelephone.getText().toString().trim();

                User user = SharedPrefManager.getInstance(EditProfileActivity.this).getUser();

                Call<LoginResponse> call = RetrofitClient.getInstance()
                        .getApi().updateProfile(
                                user.getEmail(),
                                firstname,
                                lastname,
                                birthday,
                                telephone
                        );
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.body().isStatus()) {
                            SharedPrefManager.getInstance(EditProfileActivity.this).saveUser(response.body().getUser());
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                getIntent().removeExtra("key");
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                cancle();
                break;
            case R.id.textAccept:
                Accept();
                break;

        }

    }
}
