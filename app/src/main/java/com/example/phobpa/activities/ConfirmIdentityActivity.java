package com.example.phobpa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.example.phobpa.storage.SharedPrefManager;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmIdentityActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextIdCard;
    ImageView imageViewConfirmIdentity;
    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;
    private String image_confirm_identity = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_identity);

        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.buttonAddImage).setOnClickListener(this);
        findViewById(R.id.buttonConfirmIdentity).setOnClickListener(this);

        imageViewConfirmIdentity = findViewById(R.id.imageViewConfirmIdentity);
        editTextIdCard = findViewById(R.id.editTextIdCard);
    }

    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void CropImage(Uri uri) {
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 360);
            CropIntent.putExtra("outputY", 360);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }
            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_confirm_identity = imageToString(bitmap);
                imageViewConfirmIdentity.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(ConfirmIdentityActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    boolean cheackIdCard(String idCard){
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Integer.parseInt("" + Integer.parseInt("" + idCard.charAt(i)) * (13 - i));
        }
        sum = sum % 11;
        int ans = 11 - sum;
        return   String.valueOf(ans).equals(String.valueOf(idCard.charAt(12)));

    }

    public boolean checkEditTextIdCard() {
        String textIdCard = editTextIdCard.getText().toString().trim();
        if (textIdCard.length() != 13) {
            Toast.makeText(ConfirmIdentityActivity.this, "กรอกเลขบัตรประจำตัวประชาชนไม่ครบถ้วน", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!cheackIdCard(textIdCard)) {
            Toast.makeText(ConfirmIdentityActivity.this, "กรอกเลขบัตรประจำตัวประชาชนไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }
    }
    public boolean checkImageConfirm() {
        if (image_confirm_identity.equals("")) {
            Toast.makeText(ConfirmIdentityActivity.this, "ไม่ได้ใส่รูปภาพ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validationError() {
        if (!checkImageConfirm() || !checkEditTextIdCard()) {
            return false;
        } else {
            return true;
        }
    }

    public void buttonBack() {
        finish();
    }

    public void buttonAddImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
    }

    public void buttonConfirmIdentity() {
        if (!validationError()) {
            return;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmIdentityActivity.this);
            builder.setMessage("ยืนยันข้อมูลถูกต้อง");
            builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {

                String TextIdCard = editTextIdCard.getText().toString().trim();
                public void onClick(DialogInterface dialog, int id) {
                    Call<DefaultResponse> call = RetrofitClient.getInstance()
                            .getApi().confirmIdentity(
                                    SharedPrefManager.getInstance(ConfirmIdentityActivity.this).getUser().getEmail(),
                                    TextIdCard,
                                    image_confirm_identity
                            );


                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                            if (response.body().isStatus()) {
                                Toast.makeText(ConfirmIdentityActivity.this,
                                        response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });
                    finish();
                }
            });
            builder.setNegativeButton("ไม่ยืนยัน", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                }
            });
            builder.show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                buttonBack();
                break;
            case R.id.buttonAddImage:
                buttonAddImage();
                break;
            case R.id.buttonConfirmIdentity:
                buttonConfirmIdentity();
                break;
        }

    }
}
