package com.example.vybe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ArrayList<User> usersList;
    private int resource;

    private OnItemClickListener itemClickListener;

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView usernameField;
        private ImageButton rejectBtn;
        private ImageButton acceptBtn;

        public UserHolder(@NonNull View view) {
            super(view);
            usernameField = view.findViewById(R.id.username_text_view);
            acceptBtn = view.findViewById(R.id.accept_btn);
            rejectBtn = view.findViewById(R.id.reject_btn);
            itemView.setOnClickListener(this);
            // TODO: make this more generic and stub it out if possible?
            acceptBtn.setOnClickListener((View) -> {
                itemClickListener.onAcceptClick(getAdapterPosition());
            });

            rejectBtn.setOnClickListener((View) -> {
                itemClickListener.onDeleteClick(getAdapterPosition());
            });
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition());
        }

    }

    public ProfileAdapter(OnItemClickListener itemClickListener, int resource, ArrayList<User> usersList) {
        this.itemClickListener = itemClickListener;
        this.resource = resource;
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

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onAcceptClick(int position);
        void onDeleteClick(int position); // TODO: rename to rejectClick?
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

    public void deleteItem(int position) {
        usersList.remove(position);
        notifyItemRemoved(position);
    }

}
