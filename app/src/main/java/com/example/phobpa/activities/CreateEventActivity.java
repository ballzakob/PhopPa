package com.example.phobpa.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.DefaultResponse;
import com.example.phobpa.modelsUsers.StatusResponse;
import com.example.phobpa.storage.SharedPrefManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String types;

    private static final int REQUEST_CODE_LACEPICKER = 1;
    String name_event = "";
    String address_event = "";
    String latitude_event = "";
    String longitude_event = "";
    TextView textViewEventNameLocation;


    private TextInputLayout textInputTitle, textInputDetial;

    private TextView textViewEventSelectDateStart, textViewEventSelectTimeStart,
            textViewEventSelectDateEnd, textViewEventSelectTimeEnd, textViewNumber,textViewEditPic;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private EditText editTextPrice;

    RadioGroup radioGroup;
    RadioButton radioButton;

    private SeekBar seekBar;

    private Button buttonBack;

    ImageView imageViewEvent;
    private int SELECT_IMAGE = 1001;
    private int CROP_IMAGE = 2001;
    private String image_event = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        spinner = findViewById(R.id.spinner1);
        textViewEventNameLocation = findViewById(R.id.textViewEventNameLocation);
        textInputTitle = findViewById(R.id.text_input_title);
        textInputDetial = findViewById(R.id.text_input_detial);
        final Button buttonCreateEvent = findViewById(R.id.buttonCreateEvent);
        buttonBack = findViewById(R.id.buttonBack);
        textViewEventSelectDateStart = findViewById(R.id.textViewEventSelectDateStart);
        textViewEventSelectTimeStart = findViewById(R.id.textViewEventSelectTimeStart);
        textViewEventSelectDateEnd = findViewById(R.id.textViewEventSelectDateEnd);
        textViewEventSelectTimeEnd = findViewById(R.id.textViewEventSelectTimeEnd);
        radioGroup = findViewById(R.id.radioGroup);
        seekBar = findViewById(R.id.seekBar);
        textViewNumber = findViewById(R.id.textViewNumber);
        textViewEditPic = findViewById(R.id.textViewEditPic);
        imageViewEvent = findViewById(R.id.imageViewEvent);
        editTextPrice = findViewById(R.id.editTextPrice);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(CreateEventActivity.this);
                builder.setMessage("ต้องการยกเลิกการสร้างกิจกรรม?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "ยกเลิกการสร้างกิจกรรม", Toast.LENGTH_SHORT).show();
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
        });

        textViewEditPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Image from Gallery"), SELECT_IMAGE);
            }
        });


        // todo: ทำ spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        // todo: ทำ หน้าเลือกสถานที่
        textViewEventNameLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlancePickerAvtivity();
            }
        });


        // todo: เช็ค error ของ ชื่อกิจกรรม
        textInputTitle.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputTitle.setError("กรุณากรอกชื่อกิจกรรม");
                    textInputTitle.requestFocus();
                } else {
                    textInputTitle.setError(null);
                }
            }
        });

        textInputDetial.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputDetial.setError("กรุณากรอกรายละเอียดของกิจกรรม");
                    textInputDetial.requestFocus();
                } else {
                    textInputDetial.setError(null);
                }
            }
        });

        textViewEventSelectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                String str = year + "-" + (month + 1) + "-" + day;
                                textViewEventSelectDateStart.setTextColor(Color.WHITE);
                                textViewEventSelectDateStart.setText(str);
                            }
                        }, year, month, dayOfMonth);

                // TODO: 2019-03-06  ห้ามเอาวันที่อดีต
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        textViewEventSelectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // TODO: 2019-03-06  set textview เอาวันที่ที่เลือกมาแสดง
                                String str = year + "-" + (month + 1) + "-" + day;
                                textViewEventSelectDateEnd.setTextColor(Color.WHITE);
                                textViewEventSelectDateEnd.setText(str);
                            }
                        }, year, month, dayOfMonth);

                // TODO: 2019-03-06  ห้ามเอาวันที่อดีต
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        textViewEventSelectTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                String str = "";
                                if (hourOfDay < 10) {
                                    if (minutes < 10) {
                                        str = "0" + hourOfDay + ":0" + minutes + ":00";
                                    } else {
                                        str = "0" + hourOfDay + ":" + minutes + ":00";
                                    }
                                } else {
                                    if (minutes < 10) {
                                        str = hourOfDay + ":0" + minutes + ":00";
                                    } else {
                                        str = hourOfDay + ":" + minutes + ":00";
                                    }
                                }
                                textViewEventSelectTimeStart.setTextColor(Color.WHITE);
                                textViewEventSelectTimeStart.setText(str);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        textViewEventSelectTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
//                                if(hourOfDay>12){
//                                    textViewEventSelectTimeEnd.setText(String.valueOf(hourOfDay-12)+ ":"+(String.valueOf(minutes)+" pm"));
//                                }else if(hourOfDay==12) {
//                                    textViewEventSelectTimeEnd.setText("12"+ ":"+(String.valueOf(minutes)+" pm"));
//                                } else if(hourOfDay<12) {
//                                    if(hourOfDay!=0) {
//                                        textViewEventSelectTimeEnd.setText(String.valueOf(hourOfDay) + ":" + (String.valueOf(minutes) + " am"));
//                                    } else {
//                                        textViewEventSelectTimeEnd.setText("12" + ":" + (String.valueOf(minutes) + " am"));
//                                    }
//                                }
                                String str = "";
                                if (hourOfDay < 10) {
                                    if (minutes < 10) {
                                        str = "0" + hourOfDay + ":0" + minutes + ":00";
                                    } else {
                                        str = "0" + hourOfDay + ":" + minutes + ":00";
                                    }
                                } else {
                                    if (minutes < 10) {
                                        str = hourOfDay + ":0" + minutes + ":00";
                                    } else {
                                        str = hourOfDay + ":" + minutes + ":00";
                                    }
                                }
                                textViewEventSelectTimeEnd.setTextColor(Color.WHITE);
                                textViewEventSelectTimeEnd.setText(str);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    progress = 1;
                } else if (progress == 30) {
                    textViewNumber.setText("" + "Max");
                }
                textViewNumber.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        Call<StatusResponse> call = RetrofitClient.getInstance()
//                .getApi().getStatusEvent(
//                        SharedPrefManager.getInstance(CreateEventActivity.this).getUser().getEmail()
//                );
//
//
//        call.enqueue(new Callback<StatusResponse>() {
//            @Override
//            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
//
//                if (response.body().isStatus()) {
//
//                    if(response.body().getStatus_event().equals("wait")){
//
//                        Drawable img = CreateEventActivity.this.getResources().getDrawable( R.drawable.ic_wait_white );
//                        buttonCreateEvent.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null);
//                        buttonCreateEvent.setBackground(CreateEventActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
//                        buttonCreateEvent.setTextColor(Color.parseColor("#80FFFFFF"));
//                        buttonCreateEvent.setText(" กำลังตรวจสอบการยืนยันตัวตน");
//
//
//                    }else if(response.body().getStatus_event().equals("no")){
//
//                        buttonCreateEvent.setBackground(CreateEventActivity.this.getResources().getDrawable( R.drawable.background_button_dont_click ));
//                        buttonCreateEvent.setText("กรุณายืนยันตัวตน");
//                        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent i = new Intent(CreateEventActivity.this,ConfirmIdentityActivity.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//
//                    }else{
//                        // เมื่อกดปุ่ม buttonCreateEvent
//                        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                createEvent();
//                            }
//                        });
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<StatusResponse> call, Throwable t) {
//
//            }
//        });

// เมื่อกดปุ่ม buttonCreateEvent
        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });



    }


    //    method ทำ spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("อาหาร")) {
            types = "food";
        } else if (text.equals("แฟชั่น")) {
            types = "fashion";
        } else if (text.equals("เครื่องสำอาง")) {
            types = "cosmetic";
        } else if (text.equals("กีฬา")) {
            types = "sport";
        } else if (text.equals("บันเทิง")) {
            types = "entertainment";
        } else {
            types = "travel";
        }
        Toast.makeText(parent.getContext(), types, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // ทำ PlacePicker
    private void startPlancePickerAvtivity() {

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_LACEPICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_LACEPICKER) {
                displaySelectPlaceFromPlacePicker(data);
            } else if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    CropImage(data.getData());
                }
            } else if (requestCode == CROP_IMAGE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                image_event = imageToString(bitmap);
                imageViewEvent.setImageBitmap(bitmap);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(CreateEventActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void displaySelectPlaceFromPlacePicker(Intent data) {

        Place placeSelected = PlacePicker.getPlace(data, this);
        String namePlace = placeSelected.getName().toString();
        String addressPlace = placeSelected.getAddress().toString();
        String latitudePlace = String.valueOf(placeSelected.getLatLng().latitude);
        String longitudePlace = String.valueOf(placeSelected.getLatLng().longitude);

        textViewEventNameLocation.setText(addressPlace);
        textViewEventNameLocation.setTextColor(Color.WHITE);
        name_event = namePlace;
        address_event = addressPlace;
        latitude_event = latitudePlace;
        longitude_event = longitudePlace;

    }

    // เช็ค error
    private boolean validateTitle() {
        String TitleInput = textInputTitle.getEditText().getText().toString().trim();
        if (TitleInput.isEmpty()) {
            textInputTitle.setError("กรุณากรอกรายละเอียดของกิจกรรม");
            return false;
        } else {
            return true;
        }
    }

    // check error textInputDetial
    private boolean validateDetial() {
        String DetialInput = textInputDetial.getEditText().getText().toString().trim();
        if (DetialInput.isEmpty()) {
            textInputDetial.setError("กรุณากรอกรายละเอียดของกิจกรรม");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLocation() {
        String location = textViewEventNameLocation.getText().toString().trim();
        if (location.equals("เลือกสถานที่จัดกิจกรรม")) {
            textViewEventNameLocation.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTypes() {
        if (types.equals("no")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDateStart() {
        String DateStart = textViewEventSelectDateStart.getText().toString().trim();
        if (DateStart.equals("เลือกวันที่")) {
            textViewEventSelectDateStart.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDateEnd() {
        String DateEnd = textViewEventSelectDateEnd.getText().toString().trim();
        if (DateEnd.equals("เลือกวันที่")) {
            textViewEventSelectDateEnd.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTimeStart() {
        String TimeStart = textViewEventSelectTimeStart.getText().toString().trim();
        if (TimeStart.equals("เลือกเวลา")) {
            textViewEventSelectTimeStart.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTimeEnd() {
        String TimeEnd = textViewEventSelectTimeEnd.getText().toString().trim();
        if (TimeEnd.equals("เลือกเวลา")) {
            textViewEventSelectTimeEnd.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

//        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
//                Toast.LENGTH_SHORT).show();

    }

    public boolean checkPrice() {
        String price = editTextPrice.getText().toString().trim();
        if(price.equals("")){
            editTextPrice.setHintTextColor(Color.RED);
            return false;
        }
        else if( Character.toString(price.charAt(0)).equals(".")){
            editTextPrice.setTextColor(Color.RED);
            return false;
        }else{
            editTextPrice.setHintTextColor(Color.WHITE);
            editTextPrice.setTextColor(Color.WHITE);
            return true;
        }

    }

    public boolean validationError(){
        if (!checkLocation() || !checkTypes() || !validateTitle() || !validateDetial() ||
                !checkDateStart() || !checkDateEnd() || !checkTimeStart() || !checkTimeEnd() ||
                image_event.equals("") || !checkPrice()) {
            return false;
        }else{
            return true;
        }
    }

//        method สร้าง Event
    private void createEvent() {

        checkLocation();
        checkTypes();
        validateTitle();
        validateDetial();
        checkDateStart();
        checkDateEnd();
        checkTimeStart();
        checkTimeEnd();
        checkPrice();

        if (!validationError()) {
            Toast.makeText(this, "กรอกข้อมูลไม่ครบ", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(CreateEventActivity.this);
            builder.setMessage("ข้อมูลถูกต้อง?");
            builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String email = SharedPrefManager.getInstance(CreateEventActivity.this).getUser().getEmail();
                    String title = textInputTitle.getEditText().getText().toString().trim();
                    String detial = textInputDetial.getEditText().getText().toString().trim();
                    String dateStart = textViewEventSelectDateStart.getText().toString().trim();
                    String dateEnd = textViewEventSelectDateEnd.getText().toString().trim();
                    String timeStart = textViewEventSelectTimeStart.getText().toString().trim();
                    String timeEnd = textViewEventSelectTimeEnd.getText().toString().trim();
                    String nemberPeople = textViewNumber.getText().toString().trim();
                    String price = editTextPrice.getText().toString().trim();

                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    String gender = radioButton.getText().toString();

                    if (gender.equals("ทุกเพศ")) {
                        gender = "a";
                    } else if (gender.equals("หญิง")) {
                        gender = "f";
                    } else {
                        gender = "m";
                    }

                    String event_date_start = dateStart + " " + timeStart;
                    String event_date_end = dateEnd + " " + timeEnd;

                    System.out.println("email : " + email);
                    System.out.println("event_title : " + title);
                    System.out.println("event_detail : " + detial);
                    System.out.println("event_date_start : " + dateStart + " " + timeStart);
                    System.out.println("event_date_end : " + dateEnd + " " + timeEnd);
                    System.out.println("event_number_people : " + nemberPeople);
                    System.out.println("event_gender : " + gender);
                    System.out.println("event_types : " + types);
                    System.out.println("event_location_name : " + name_event);
                    System.out.println("event_latitude : " + latitude_event);
                    System.out.println("event_longitude : " + longitude_event);
                    System.out.println("event_image : "+image_event);
                    Call<DefaultResponse> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .createEvent(email, title, detial,event_date_start,event_date_end,nemberPeople,
                                    gender, types, price, name_event, address_event, latitude_event,
                                    longitude_event, image_event);
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            if (response.body().isStatus()) {
                                Toast.makeText(CreateEventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(CreateEventActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            Toast.makeText(CreateEventActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                        }
                    });
//                finish();
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


}
