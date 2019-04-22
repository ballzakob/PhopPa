package com.example.phobpa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.LoginResponse;
import com.example.phobpa.modelsUsers.User;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener  {

    EditText editTextFirstName,editTextLastName,editTextBirthday,editTextTelephone;

    private CircleImageView circleImageView_profile;
    private String img;

    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;

    private String image_user = "";

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.textAccept).setOnClickListener(this);
        findViewById(R.id.textViewEditPic).setOnClickListener(this);
        findViewById(R.id.editTextBirthday).setOnClickListener(this);

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

    private void selectDate() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                EditProfileActivity.this,

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + day;
                        editTextBirthday.setText(date);
                        editTextBirthday.setTextColor(Color.BLACK);
                    }
                }, year, month, day);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
                if (!image_user.equals("")){
                    Call<LoginResponse> call2 = RetrofitClient.getInstance()
                            .getApi().updateProfileImg(
                                    user.getEmail(),
                                    image_user
                            );
                    call2.enqueue(new Callback<LoginResponse>() {
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
                }
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

//                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
//                getIntent().removeExtra("key");
//                startActivity(intent);
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

    private void CropImage(Uri uri) {
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }
            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_user = imageToString(bitmap);

                circleImageView_profile.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(EditProfileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
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
            case R.id.editTextBirthday:
                selectDate();
                break;
            case R.id.textViewEditPic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
                break;

        }

    }
}
