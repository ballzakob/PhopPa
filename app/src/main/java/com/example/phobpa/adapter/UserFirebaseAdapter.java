package com.example.phobpa.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phobpa.R;
import com.example.phobpa.activities.EventActivity;
import com.example.phobpa.activities.MessagesActivity;
import com.example.phobpa.api.RetrofitClient;
import com.example.phobpa.modelsUsers.UserFirebase;
import com.example.phobpa.modelsUsers.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFirebaseAdapter extends RecyclerView.Adapter<UserFirebaseAdapter.UserFirebaseViewHolder>  {

    private Context mCtx;
    private List<UserFirebase> userList;

    public UserFirebaseAdapter(Context mCtx, List<UserFirebase> users) {
        this.mCtx = mCtx;
        this.userList = users;
    }

    @NonNull
    @Override
    public UserFirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_user, viewGroup, false);
        return new UserFirebaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserFirebaseViewHolder userFirebaseViewHolder, int i) {
        final UserFirebase userFirebase = userList.get(i);

        userFirebaseViewHolder.textViewUsername.setText(userFirebase.getUsername());

        Call<UserResponse> call = RetrofitClient.getInstance().getApi().getUser(userFirebase.getUsername());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                System.out.println(response.body().getMessages());
                if (response.body().isStatus()) {
                    String username = response.body().getUser().getFirstname()+" "+response.body().getUser().getLastname();
                    userFirebaseViewHolder.textViewUsername.setText(username);

                    String picture = response.body().getUser().getImage_user();
                    if (picture.isEmpty()) {
                        userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.user);
                    } else {
                        String url = "http://pilot.cp.su.ac.th/usr/u07580457/phoppa/images/prof/" + picture;
                        System.out.println(url);
                        Picasso.get().load(url).into(userFirebaseViewHolder.circleImageViewUser);
                    }


                } else {
                    Toast.makeText(mCtx, response.body().getMessages(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(mCtx, "FAIL", Toast.LENGTH_LONG).show();
            }
        });

//        if(userFirebase.getImagerUrl().equals("defult")){
//            userFirebaseViewHolder.circleImageViewUser.setImageResource(R.drawable.user);
//        }else{
//            Picasso.get().load(userFirebase.getImagerUrl()).into(userFirebaseViewHolder.circleImageViewUser);
//        }
        userFirebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, MessagesActivity.class);
                i.putExtra("userid",userFirebase.getId());
                i.putExtra("email",userFirebase.getUsername());
                mCtx.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() { return userList.size();
    }

    class UserFirebaseViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageViewUser;
        TextView textViewUsername;

        public UserFirebaseViewHolder(View itemView) {
            super(itemView);

            circleImageViewUser = itemView.findViewById(R.id.circleImageViewUser);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
        }
    }
}
