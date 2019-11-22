package com.example.vybe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.Models.User;

import java.util.ArrayList;

/**
 * ProfileAdapter is an ArrayAdapter used in the SearchProfilesActivity class to create a
 * view for a list of Vybe users
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.UserHolder> {

    private static final String TAG = "ProfileAdapter";
    private ArrayList<User> usersList;
    private int resource;

    private OnItemClickListener itemClickListener;
    private OnRequestClickListener requestClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnRequestClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private TextView usernameField;
        private ImageButton rejectBtn;
        private ImageButton acceptBtn;

        public UserHolder(@NonNull View view) {
            super(view);
            usernameField = view.findViewById(R.id.username_text_view);
            acceptBtn = view.findViewById(R.id.accept_btn);
            rejectBtn = view.findViewById(R.id.reject_btn);

            if (view.getContext() instanceof SearchProfilesActivity) {
                acceptBtn.setVisibility(View.GONE);
                rejectBtn.setVisibility(View.GONE);

                itemView.setOnClickListener((View) -> {
                    itemClickListener.onItemClick(getAdapterPosition());
                });
            }

            // TODO: make this more generic and stub it out if possible?
            acceptBtn.setOnClickListener((View) -> {
                requestClickListener.onAcceptClick(getAdapterPosition());
            });

            rejectBtn.setOnClickListener((View) -> {
                requestClickListener.onRejectClick(getAdapterPosition());
            });
        }

    }

    public ProfileAdapter(int resource, ArrayList<User> usersList) {
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

    public void setOnItemClickLister(OnItemClickListener itemClickLister) {
        this.itemClickListener = itemClickLister;
    }

    public void setRequestClickListener(OnRequestClickListener requestClickListener) {
        this.requestClickListener = requestClickListener;
    }

}
