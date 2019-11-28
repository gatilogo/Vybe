package com.example.vybe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vybe.Models.VibeEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * MyVibesAdapter is a CustomList used for the ListAdapter in the main activity that serves as the
 * list for storing vibe events
 */
public class MyVibesAdapter extends RecyclerView.Adapter<MyVibesAdapter.VibeEventHolder> {

    private static final String TAG = "MyVibesAdapter";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String mUsername = mAuth.getCurrentUser().getDisplayName();
    private Context context;
    private ArrayList<VibeEvent> vibeEventList;
    private int resource;

    public class VibeEventHolder extends RecyclerView.ViewHolder {
        private TextView dateField;
        private TextView vibeTitleField;
        private ImageView vibeImage;

        public VibeEventHolder(View view) {
            super(view);
            dateField = view.findViewById(R.id.view_date_text_view);
            vibeTitleField = view.findViewById(R.id.vibe_title_text_view);
            vibeImage = view.findViewById(R.id.image_view);
        }
    }

    public MyVibesAdapter(@NonNull Context context, int resource, ArrayList<VibeEvent> vibeEventList) {
        this.resource = resource;
        this.context = context;
        this.vibeEventList = vibeEventList;
    }

    @NonNull
    @Override
    public VibeEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resource, parent, false);
        return new VibeEventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VibeEventHolder holder, int position) {
        VibeEvent vibeEvent = vibeEventList.get(position);
        String datetimeText = vibeEvent.getDateTimeString();
        holder.dateField.setText(datetimeText);
        holder.vibeImage.setImageResource(vibeEvent.getVibe().getEmoticon());

        String itemText = vibeEvent.getVibe().getName();
        if (vibeEvent.getOwner().equals(mUsername)) {
            itemText = "@" + vibeEvent.getOwner();
        }
        holder.vibeTitleField.setText(itemText);

        holder.itemView.setOnClickListener((View v) -> {
            Intent viewVibe = new Intent(context, ViewVibeActivity.class);
            viewVibe.putExtra("vibeEvent", vibeEvent);
            context.startActivity(viewVibe);
        });
    }

    @Override
    public int getItemCount() {
        return vibeEventList.size();
    }

    public void deleteItem(int position, String vibeEventDBPath) {
        VibeEvent vibeEvent = vibeEventList.get(position);
        // TODO: Stub this out as it is the exact same code in AddEditVibeEventActivity but it should go in the Controller
        if (vibeEvent.getImage() != null){
            StorageReference imageRef = storageRef.child(vibeEvent.getImage());
            imageRef.delete();
        }
        db.collection(vibeEventDBPath).document(vibeEvent.getId()).delete();
        vibeEventList.remove(position);
        notifyItemRemoved(position);
    }

    public VibeEvent getItem(int position) {
        return vibeEventList.get(position);
    }

}
