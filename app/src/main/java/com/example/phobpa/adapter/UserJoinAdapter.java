package com.example.phobpa.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phobpa.R;
import com.example.phobpa.modelsUsers.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserJoinAdapter extends RecyclerView.Adapter<UserJoinAdapter.UserJoinViewHolder> {

    private Context mCtx;
    private List<User> userList;

    public UserJoinAdapter(Context mCtx, List<User> users) {
        this.mCtx = mCtx;
        this.userList = users;
    }

    @NonNull
    @Override
    public UserJoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_user_join, viewGroup, false);
        return new UserJoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserJoinViewHolder userJoinViewHolder, int i) {
        User user = userList.get(i);

        userJoinViewHolder.textViewNameJoinUser.setText(user.getFirstname()+"\n"+user.getLastname());

        String picture = user.getImage_user();
        System.out.println(picture);
        if(picture.isEmpty()){
            userJoinViewHolder.circleImageViewJoinUser.setImageResource(R.drawable.no_image);
        }else{
            String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/"+picture;
            Picasso.get().load(url).into(userJoinViewHolder.circleImageViewJoinUser);
        }

    }

    @Override
    public int getItemCount() { return userList.size();
    }

    class UserJoinViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewJoinUser;
        TextView textViewNameJoinUser;

        public UserJoinViewHolder(View itemView) {
            super(itemView);

            circleImageViewJoinUser = itemView.findViewById(R.id.circleImageViewJoinUser);
            textViewNameJoinUser = itemView.findViewById(R.id.textViewNameJoinUser);
        }
    }
}
