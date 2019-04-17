package com.example.phobpa.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phobpa.R;
import com.example.phobpa.adapter.UserFirebaseAdapter;
import com.example.phobpa.modelsUsers.Chat;
import com.example.phobpa.modelsUsers.UserFirebase;
import com.example.phobpa.storage.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserFirebaseAdapter userFirebaseAdapter;
    private List<UserFirebase> userFirebaseList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid())){
                        usersList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(firebaseUser.getUid())){
                        usersList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void readChats() {
        userFirebaseList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userFirebaseList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserFirebase userFirebase = snapshot.getValue(UserFirebase.class);

                    // display 1 users from chats
                    for(String id : usersList){
                        if(userFirebase.getId().equals(id)){
                            if(userFirebaseList.size() != 0){
                                for (UserFirebase userFirebase1 : userFirebaseList){
                                    if(!userFirebase.getId().equals(userFirebase1.getId())){
                                        userFirebaseList.add(userFirebase);
                                    }
                                }
                            } else{
                                userFirebaseList.add(userFirebase);
                            }
                        }
                    }
                }

                userFirebaseAdapter = new UserFirebaseAdapter(getContext(), userFirebaseList);
                recyclerView.setAdapter(userFirebaseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
