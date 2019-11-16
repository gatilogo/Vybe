package com.example.vybe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchProfilesActivity extends AppCompatActivity {

    private static final String TAG = "SearchProfilesActivity";

    private ArrayList<User> usersList;
    private ArrayList<User> searchList;
    private ProfileAdapter profileAdapter;

    private SearchView searchView;
    private ListView searchListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profiles);
        Log.d(TAG, "onCreate: In search profiles");

        searchView = findViewById(R.id.search_view);
        searchListView = findViewById(R.id.search_list_view);

        usersList = new ArrayList<>();
        usersList.add(new User("trishia", ""));
        usersList.add(new User("haha", ""));
        usersList.add(new User("hehe", ""));

        searchList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, R.layout.user_item, searchList);
        searchListView.setAdapter(profileAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                profileAdapter.clear();

                for (User user: usersList) {
                    String username = user.getUsername().toLowerCase();
                    if (username.contains(query.toLowerCase())) {
                        profileAdapter.add(user);
                    }
                }

                if (profileAdapter.isEmpty()) {
                    Toast.makeText(SearchProfilesActivity.this, "No match found", Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                User user = profileAdapter.getItem(position);
//                System.out.println(user.getUsername());
            }
        });
    }
}
