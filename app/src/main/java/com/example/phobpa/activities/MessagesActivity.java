package com.example.phobpa.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.adapter.MessageAdapter;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.Chat;
import com.example.phobpa.modelsUsers.UserFirebase;
import com.example.phobpa.modelsUsers.UserResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView circleImageViewUser;
    private TextView textViewUsername;

    private EditText editTextSend;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    List<Chat> chatList;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        findViewById(R.id.button_back_home).setOnClickListener(this);
        findViewById(R.id.imageButtonSend).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        circleImageViewUser = findViewById(R.id.circleImageViewUser);
        textViewUsername = findViewById(R.id.textViewUsername);
        editTextSend = findViewById(R.id.editTextSend);

        String email = getIntent().getStringExtra("email");
        Call<UserResponse> call = RetrofitClient.getInstance().getApi().getUser(email);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                System.out.println(response.body().getMessages());
                if (response.body().isStatus()) {

                    String username = response.body().getUser().getFirstname()+" "+response.body().getUser().getLastname();
                    textViewUsername.setText(username);

                    String picture = response.body().getUser().getImage_user();
                    if (picture.isEmpty()) {
                        circleImageViewUser.setImageResource(R.drawable.user);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/" + picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(circleImageViewUser);
                    }


                } else {
                    Toast.makeText(MessagesActivity.this, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MessagesActivity.this, "FAIL", Toast.LENGTH_LONG).show();
            }
        });

        final String userid = getIntent().getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
//                textViewUsername.setText(userFirebase.getUsername());

//                if(userFirebase.getImagerUrl().equals("defult")){
//                    circleImageViewUser.setImageResource(R.drawable.user);
//                }
//                else{
//                    Picasso.get().load(userFirebase.getImagerUrl()).into(circleImageViewUser);
//                }
                readMessages(firebaseUser.getUid(), userid, userFirebase.getImagerUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendMessage(){

        String sender = firebaseUser.getUid();
        String receiver = getIntent().getStringExtra("userid");
        String message = editTextSend.getText().toString();

        if(!message.equals("")){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);

            reference.child("Chats").push().setValue(hashMap);
        }else{
            Toast.makeText(MessagesActivity.this, "ยังไม่ได้พิมพ์ข้อความ", Toast.LENGTH_SHORT).show();
        }
        editTextSend.setText("");
    }

    public void readMessages(final String myid , final String userid, final String imageurl){
        chatList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) &&  chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        chatList.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessagesActivity.this , chatList, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back_home:
                finish();
                break;
            case R.id.imageButtonSend:
                sendMessage();
                break;
        }
    }
}
