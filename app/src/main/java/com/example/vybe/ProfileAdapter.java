package com.example.vybe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vybe.Models.User;

import java.util.ArrayList;

/**
 * ProfileAdapter is an ArrayAdapter used in the SearchProfilesActivity class to create a
 * view for a list of Vybe users
 */
public class ProfileAdapter extends ArrayAdapter<User> {

    private static final String TAG = "ProfileAdapter";
    private Context context;
    private ArrayList<User> usersList;
    private int resource;

    public ProfileAdapter(@NonNull Context context, int resource, ArrayList<User> usersList) {
        super(context, resource, usersList);
        this.resource = resource;
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater
                    .from(this.context)
                    .inflate(this.resource, parent, false);
        }

        TextView usernameField = view.findViewById(R.id.username_text_view);

        User user = usersList.get(position);

        usernameField.setText(user.getUsername());

        return view;
    }
}
