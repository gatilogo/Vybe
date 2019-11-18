package com.example.vybe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * SearchProfilesActivity enables the user to search for other participants/users of the Vybe app
 */
public class SearchProfilesActivity extends AppCompatActivity {

    private static final String TAG = "SearchProfilesActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        // Initialize list of users
        usersList = new ArrayList<>();

        // Get users from the database
        final CollectionReference collectionReference = db.collection("Users");
        Query query = collectionReference.orderBy("username", Query.Direction.DESCENDING);
        query.addSnapshotListener((@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) -> {
            usersList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String username = (String) doc.getData().get("username");
                String email = (String) doc.getData().get("email");
                usersList.add(new User(username, email));   // Populate users list
            }
        });

        // Initialize search list
        searchList = new ArrayList<>();
        // Create adapter
        profileAdapter = new ProfileAdapter(this, R.layout.user_item, searchList);
        // Set adapter
        searchListView.setAdapter(profileAdapter);

        // Listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // This function is called when a user presses enter in the search view
            public boolean onQueryTextSubmit(String query) {
                // Refresh adapter
                profileAdapter.clear();

                // Check if entered 'query' (username to search for) is in the users list
                for (User user: usersList) {
                    String username = user.getUsername().toLowerCase();
                    if (username.contains(query.toLowerCase())) {
                        profileAdapter.add(user);
                    }
                }

                // If adapter is empty, display a Toast
                if (profileAdapter.isEmpty()) {
                    Toast.makeText(SearchProfilesActivity.this, "No match found", Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            // This function is called when the text field in the search view changes. Currently unused
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
//            User user = profileAdapter.getItem(position);   // set-up for issue #45
//            System.out.println(user.getUsername());
        });
    }
}
