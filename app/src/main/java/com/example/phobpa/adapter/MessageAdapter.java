package com.example.phobpa.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phobpa.R;
import com.example.phobpa.activities.MessagesActivity;
import com.example.phobpa.modelsUsers.Chat;
import com.example.phobpa.modelsUsers.UserFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.UserFirebaseViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mCtx;
    private List<Chat> chatList;
    private String imageurl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mCtx, List<Chat> chatList, String imageurl) {
        this.mCtx = mCtx;
        this.chatList = chatList;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.UserFirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if( i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.UserFirebaseViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.UserFirebaseViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.UserFirebaseViewHolder userFirebaseViewHolder, int i) {
        Chat chat = chatList.get(i);
        userFirebaseViewHolder.textViewShowTextMessage.setText(chat.getMessage());
        if(imageurl.equals("defult")){
            userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.user);
        }else{
            Picasso.get().load(imageurl).into(userFirebaseViewHolder.circleImageViewUser);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class UserFirebaseViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewUser;
        TextView textViewShowTextMessage;

        public UserFirebaseViewHolder(View itemView) {
            super(itemView);

            circleImageViewUser = itemView.findViewById(R.id.circleImageViewUser);
            textViewShowTextMessage = itemView.findViewById(R.id.textViewShowTextMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}