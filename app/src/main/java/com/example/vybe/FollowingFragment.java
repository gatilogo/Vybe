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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    private ArrayList<User> followingList;
    private ProfileAdapter profileAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;

    private RecyclerView followingRecyclerView;

    public FollowingFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        followingRecyclerView = view.findViewById(R.id.following_list);

        followingList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(R.layout.user_item, followingList);
        profileAdapter.setOnItemClickLister(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "HOALFEH:OIGFEW", Toast.LENGTH_LONG).show();
            }
        });

        followingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        followingRecyclerView.setAdapter(profileAdapter);

        if (user.getFollowing() != null){
            for (String uid: user.getFollowing()){
                db.collection("Users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot doc) {
                                User following = doc.toObject(User.class);
                                following.setUserID(uid);
                                followingList.add(following);
                                profileAdapter.notifyDataSetChanged();
                            }
                        });
            }
        }
    }
}
