package com.example.phobpa.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.storage.SharedPrefManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class SelectTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_LACEPICKER = 1;
    Spinner spinner;
    String types="";
    TextView textViewEventNameLocation;
    Button buttonNext , buttonBack,buttonSkip;

    String name_event = "";
    String address_event = "";
    String latitude_event = "";
    String longitude_event = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        spinner = findViewById(R.id.spinner1);
        textViewEventNameLocation = findViewById(R.id.textViewEventNameLocation);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        buttonSkip = findViewById(R.id.buttonSkip);

        // ทำ spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkLocation() || !checkType()){
                    Toast.makeText(SelectTypeActivity.this, "กรอกข้อมูลไม่ครบถ้วน", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Intent intent = new Intent(SelectTypeActivity.this, ShowEventActivity.class);

                    intent.putExtra("email", SharedPrefManager.getInstance(SelectTypeActivity.this).getUser().getEmail());
                    intent.putExtra("event_types", types);
                    intent.putExtra("event_location_name", name_event);
                    intent.putExtra("event_location_address", address_event);
                    intent.putExtra("event_latitude", latitude_event);
                    intent.putExtra("event_longitude", longitude_event);
//                    System.out.println(SharedPrefManager.getInstance(SelectTypeActivity.this).getUser().getEmail());
//                    System.out.println(types);
//                    System.out.println(address_event);
//                    System.out.println(latitude_event);
//                    System.out.println(longitude_event);
                    finish();
                    startActivity(intent);
                }

            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectTypeActivity.this,CreateEventActivity.class);
                startActivity(i);
                finish();
            }
        });


        textViewEventNameLocation.setClickable(true);
        // ทำ หน้าเลือกสถานที่
        textViewEventNameLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlancePickerAvtivity();
            }
        });

    }

    //    method ทำ spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("อาหาร")) {
            types = "อาหาร";
        } else if (text.equals("แฟชั่น")) {
            types = "แฟชั่น";
        } else if (text.equals("เครื่องสำอาง")) {
            types = "เครื่องสำอาง";
        } else if (text.equals("กีฬา")) {
            types = "กีฬา";
        } else if (text.equals("บันเทิง")) {
            types = "บันเทิง";
        } else if(text.equals("ท่องเที่ยว")){
            types = "ท่องเที่ยว";
        }
//        Toast.makeText(parent.getContext(), types, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // เช็ค error
    private boolean checkType(){
        if(types.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private boolean checkLocation(){
        if(address_event.equals("")){
            return false;
        }else{
            return true;
        }
    }

    // ทำ PlacePicker
    private void startPlancePickerAvtivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
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
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(SelectTypeActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
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

    // method ไปยังหน้าถัดไป
    private void next(){
        if(!!checkLocation() || !!checkType()){
            Toast.makeText(this, "กรอกข้อมูลไม่ครบถ้วน", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
