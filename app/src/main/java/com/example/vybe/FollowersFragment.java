package com.example.vybe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vybe.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment {

    private ArrayList<User> followersList;
    private ProfileAdapter profileAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;

    private RecyclerView followerRecyclerView;

    public FollowersFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        followerRecyclerView = view.findViewById(R.id.followers_list);

        followersList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(R.layout.user_item, followersList);
        profileAdapter.setOnItemClickLister(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "HOALFEH:OIGFEW", Toast.LENGTH_LONG).show();
            }
        });

        followerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        followerRecyclerView.setAdapter(profileAdapter);

        if (user.getFollowers() != null){
            for (String uid: user.getFollowers()){
                db.collection("Users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot doc) {
                                User follower = doc.toObject(User.class);
                                follower.setUserID(uid);
                                followersList.add(follower);
                                profileAdapter.notifyDataSetChanged();
                            }
                        });
            }
        }
    }
}
