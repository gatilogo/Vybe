package com.example.vybe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ProfileAdapter is an ArrayAdapter used in the SearchProfilesActivity class to create a
 * view for a list of Vybe users
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.UserHolder> {

    private static final String TAG = "ProfileAdapter";
    private Context context;
    private ArrayList<User> usersList;
    private int resource;

    public class UserHolder extends RecyclerView.ViewHolder{
        private TextView usernameField;

        public UserHolder(@NonNull View view) {
            super(view);
            usernameField = view.findViewById(R.id.username_text_view);
        }
    }

    public ProfileAdapter(@NonNull Context context, int resource, ArrayList<User> usersList) {
        this.resource = resource;
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resource, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = usersList.get(position);
        holder.usernameField.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void clear(){
        usersList.clear();
        this.notifyDataSetChanged();
    }

    public void add(User user){
        usersList.add(user);
        this.notifyItemInserted(usersList.size() - 1);
    }

    public boolean isEmpty() {
        return usersList.isEmpty();
    }
}
