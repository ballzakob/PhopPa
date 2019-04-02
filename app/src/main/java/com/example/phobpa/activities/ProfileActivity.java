package com.example.phobpa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.phobpa.R;
import com.example.phobpa.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewFirstName, textViewLastName, textViewBirthday, textViewTelephone;

    private CircleImageView circleImageView_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.buttonLogout).setOnClickListener(this);
        findViewById(R.id.buttonDelete).setOnClickListener(this);

        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        textViewTelephone = findViewById(R.id.textViewTelephone);

        textViewFirstName.setText("Firstname : " + SharedPrefManager.getInstance(this).getUser().getFirstname());
        textViewLastName.setText("Lastname : " + SharedPrefManager.getInstance(this).getUser().getLastname());
        textViewBirthday.setText("Birthday : " + SharedPrefManager.getInstance(this).getUser().getBirthday());
        textViewTelephone.setText("Telephone : " + SharedPrefManager.getInstance(this).getUser().getTelephone());

        circleImageView_profile =findViewById(R.id.circleImageView_profile);
        String picture = SharedPrefManager.getInstance(this).getUser().getImage_user();
        if(picture.isEmpty()){
            circleImageView_profile.setImageResource(R.drawable.user);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            System.out.println(url);
            Picasso.get().load(url).into(circleImageView_profile);
        }
    }

    private void logout() {
        SharedPrefManager.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.buttonLogout:
                logout();
                break;
            case R.id.buttonDelete:
                break;
        }
    }
}
