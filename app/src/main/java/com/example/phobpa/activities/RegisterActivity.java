package com.example.phobpa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private TextView textViewGender;

    RadioGroup radioGroup;
    RadioButton radioButton;


    private TextInputLayout textInputEmail, textInputPassword, textInputConfirmPassword, textInputFirstname,
            textInputLastname, textInputTelephone;

    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;

    private String image_user = "";

    CircleImageView CircleImageViewProfile;
    private TextView textViewBirthday;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CircleImageViewProfile = findViewById(R.id.CircleImageViewProfile);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputConfirmPassword = findViewById(R.id.text_input_confirm_password);
        textInputFirstname = findViewById(R.id.text_input_Firstname);
        textInputLastname = findViewById(R.id.text_input_Lastname);
        textInputTelephone = findViewById(R.id.text_input_Telephone);


        textViewGender = findViewById(R.id.textViewGender);

        radioGroup = findViewById(R.id.radioGroup);

        textViewBirthday = findViewById(R.id.textViewBirthdayRG);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.button_back_login).setOnClickListener(this);
        findViewById(R.id.textViewEdit).setOnClickListener(this);
        findViewById(R.id.textViewBirthdayRG).setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        textInputEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputEmail.setError("กรุณากรอกอีเมล");
                    textInputEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    textInputEmail.setError("รูปแบบอีเมลไม่ถูกต้อง");
                    textInputEmail.requestFocus();
                } else {
                    textInputEmail.setError(null);
                }

            }
        });

        textInputPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputPassword.setError("กรุณากรอกรหัสผ่าน");
                    textInputPassword.requestFocus();
                } else if (!PASSWORD_PATTERN.matcher(s.toString()).matches()) {
                    textInputPassword.setError("รหัสผ่านมีความยาวน้อยกว่า 6 ตัวอักษร");
                    textInputPassword.requestFocus();
                } else {
                    textInputPassword.setError(null);
                }

            }
        });

        textInputConfirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputConfirmPassword.setError("กรุณากรอกรหัสผ่านอีกครั้ง");
                    textInputConfirmPassword.requestFocus();
                } else if (!PASSWORD_PATTERN.matcher(s.toString()).matches()) {
                    textInputConfirmPassword.setError("รหัสผ่านต้องมีความยาวน้อยกว่า 6 ตัวอักษร");
                    textInputConfirmPassword.requestFocus();
                } else if (!(s.toString().equals(textInputPassword.getEditText().getText().toString().trim()))) {
                    textInputConfirmPassword.setError("รหัสผ่านไม่ถูกต้อง");
                    textInputConfirmPassword.requestFocus();
                } else if (s.toString().equals(textInputPassword.getEditText().getText().toString().trim())) {
                    textInputConfirmPassword.setError(null);
                }
            }
        });

        textInputFirstname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputFirstname.setHint("ชื่อจริง");
                if (s.toString().isEmpty()) {
                    textInputFirstname.setHint("ชื่อจริง (ไม่ต้องใส่คำนำหน้าชื่อ)");
                    textInputFirstname.setError("กรุณากรอกชื่อจริง");
                    textInputFirstname.requestFocus();
                } else if (s.toString().length() > 50) {
                    textInputFirstname.setError("ชื่อจริงมีความยาวเกินกำหนด");
                    textInputFirstname.requestFocus();
                } else {
                    textInputFirstname.setError(null);
                }
            }
        });

        textInputLastname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputLastname.setError("กรุณากรอกนามสกุล");
                    textInputLastname.requestFocus();
                } else if (s.toString().length() > 50) {
                    textInputLastname.setError("นามมสกุลมีความยาวเกินกำหนด");
                    textInputLastname.requestFocus();
                } else {
                    textInputLastname.setError(null);
                }
            }
        });

        textInputTelephone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputTelephone.setError("กรุณากรอกเบอร์โทรศัพท์มือถือ");
                    textInputTelephone.requestFocus();
                } else if (s.toString().length() < 10) {
                    textInputTelephone.setError("เบอร์โทรศัพท์มือถือไม่ครบถ้วน");
                    textInputTelephone.requestFocus();
                } else if (s.toString().length() > 10) {
                    textInputTelephone.setError("เบอร์โทรศัพท์มือถือไม่ครบถ้วน");
                    textInputTelephone.requestFocus();
                } else {
                    textInputTelephone.setError(null);
                }
            }
        });
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                RegisterActivity.this,

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + day;
                        textViewBirthday.setText(date);
                        textViewBirthday.setTextColor(Color.BLACK);
                    }
                }, year, month, day);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return false;
        } else {
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String ConfirmPasswordInput = textInputConfirmPassword.getEditText().getText().toString().trim();

        if (ConfirmPasswordInput.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(ConfirmPasswordInput).matches()) {
            return false;
        } else if (!(ConfirmPasswordInput.equals(textInputPassword.getEditText().getText().toString().trim()))) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFirstname() {
        String FirstnameInput = textInputFirstname.getEditText().getText().toString().trim();

        if (FirstnameInput.isEmpty()) {
            return false;
        } else if (FirstnameInput.length() > 50) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLastname() {
        String LastnameInput = textInputLastname.getEditText().getText().toString().trim();

        if (LastnameInput.isEmpty()) {
            return false;
        } else if (LastnameInput.length() > 50) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateTelephone() {
        String TelephoneInput = textInputTelephone.getEditText().getText().toString().trim();

        if (TelephoneInput.isEmpty()) {
            return false;
        } else if (TelephoneInput.length() < 10) {
            return false;
        } else if (TelephoneInput.length() > 10) {
            return false;
        } else {
            return true;
        }
    }


    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }

    private boolean validateBD() {
        String birthday = textViewBirthday.getText().toString().trim();

        if (birthday.equals("เลือกวันเกิด")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validationError() {
        if (!validateEmail() || !validatePassword() || !validateConfirmPassword() || !validateFirstname()
                || !validateLastname() || !validateTelephone() || !validateBD() || image_user.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void userSignUp() {
        System.out.println("รูป : " + image_user);
        if (!validationError()) {
            Toast.makeText(this, "กรอกข้อมูลไม่ครบ", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(this, "กรอกข้อมูลครบถ้วน", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("ข้อมูลถูกต้อง?");
            builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String email = textInputEmail.getEditText().getText().toString().trim();
                    String password = textInputPassword.getEditText().getText().toString().trim();
                    final String firstname = textInputFirstname.getEditText().getText().toString().trim();
                    String lastname = textInputLastname.getEditText().getText().toString().trim();
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String gender = radioButton.getText().toString();
                    if (gender.equals("ชาย")) {
                        gender = "m";
                    } else {
                        gender = "f";
                    }
                    String birthday = textViewBirthday.getText().toString().trim();
                    String telephone = textInputTelephone.getEditText().getText().toString().trim();

                    System.out.println("รูป : " + image_user);

                    create_user_php(email, password, firstname, lastname, gender, birthday, telephone, image_user);


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

    private void create_user_php(final String email, final String password, String firstname, String lastname, String gender,
                                 String birthday, String telephone, String image_user) {
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(email, password, firstname, lastname, gender, birthday, telephone, image_user);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.body().isStatus()) {
                    Toast.makeText(RegisterActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("username", email);
                                        hashMap.put("imagerUrl", "defult");

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "กรอกข้อมูลผิดพลาด", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "ไม่มีอินเทอร์เน็ต", Toast.LENGTH_LONG).show();
            }
        });
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
                CircleImageViewProfile.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(RegisterActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                userSignUp();
                break;
            case R.id.textViewBirthdayRG:
                selectDate();
                break;
            case R.id.button_back_login:
                finish();
                break;
            case R.id.textViewEdit:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
                break;
        }
    }
}