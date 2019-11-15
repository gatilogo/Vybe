package com.example.vybe.AddEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vybe.R;
import com.example.vybe.VibeEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * This Activity displays the screen for a user to add a vibe event, or
 * edit an existing vibe event by adding or modifying the different vibe attributes
 */
public class AddEditVibeEventActivity extends AppCompatActivity implements SocialSituationFieldFragment.SocStnSelectedListener, VibeFieldFragment.VibeSelectedListener {

    private static final String TAG = "AddEditVibeEventActivity";
    private static final int GET_FROM_GALLERY = 1000;

    // --- XML Elements ---
    private EditText reasonField;
    private Button addBtn;
    private Button pickImageBtn;
    //private TextView outputBox;
    private TextView pageTitle;
    private ImageView imageView;
    private VibeFieldFragment vibeFieldFragment;
    // -------------------

    private VibeEvent vibeEvent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean editFlag = false;
    private boolean imageIsSelected = false;
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vibe);
        Log.d(TAG, "onCreate: In Add/Edit vibes");

        reasonField = findViewById(R.id.reason_edit_text);
        addBtn = findViewById(R.id.add_btn);
        // outputBox = findViewById(R.id.textView);
        pickImageBtn = findViewById(R.id.pickImageBtn);
        imageView = findViewById(R.id.imageView);
        pageTitle = findViewById(R.id.add_edit_vybe_title);
        pageTitle = findViewById(R.id.add_edit_vybe_title);
        vibeFieldFragment = (VibeFieldFragment) getSupportFragmentManager().findFragmentById(R.id.vibe_field_fragment);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            pageTitle.setText(getString(R.string.edit_vybe_name));

            vibeEvent = (VibeEvent) extras.getSerializable("vibeEvent");
            reasonField.setText(vibeEvent.getReason());
            editFlag = true;

            Bundle vibeFieldArgs = new Bundle();
            vibeFieldArgs.putSerializable("vibe", vibeEvent.getVibe().getName());
            vibeFieldFragment.setDefaultVibe(vibeFieldArgs);

        } else {
            vibeEvent = new VibeEvent();
            vibeEvent.setDateTime(LocalDateTime.now());
        }

        // --- Image Picker ---
        pickImageBtn.setOnClickListener((View view) -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GET_FROM_GALLERY);
        });

        // --- Show Output on button click ---
        addBtn.setOnClickListener(view -> {

            //TODO: integrate firestore stuff here i guess
            vibeEvent.setReason(reasonField.getText().toString());

            if (editFlag) {
                editVibeEvent(vibeEvent);
            } else {
                addVibeEvent(vibeEvent);
            }
            finish();
        });

    }

    @Override
    public void onVibeSelected(String selectedVibe) {
        vibeEvent.setVibe(selectedVibe);
    }

    @Override
    public void onSocStnSelected(String socStn) {
        vibeEvent.setSocialSituation(socStn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageIsSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            imageView.setImageBitmap(imageBitmap);

        }
    }

    public void uploadImage(Bitmap bitmap, String id) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

//        Log.d(TAG, id + ".jpg");
        String imgPath = "reasons/" + id + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = storageRef.child(imgPath);

        UploadTask uploadTask = mountainsRef.putBytes(byteArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Succ", Toast.LENGTH_LONG).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }

    // TODO: Move to Controller Class
    public void addVibeEvent(VibeEvent vibeEvent) {
        // TODO: Put as private attribute in Controller class if we use one

        String id = db.collection("VibeEvent").document().getId();
        vibeEvent.setId(id);
        if (imageIsSelected) {
            uploadImage(imageBitmap, id);
            vibeEvent.setImage("reasons/" + id + ".jpg");
        }
        HashMap<String, Object> data = createVibeEventData(vibeEvent);
        db.collection("VibeEvent").document(id).set(data);
    }

    public void editVibeEvent(VibeEvent vibeEvent) {
        if (imageIsSelected) {
            uploadImage(imageBitmap, vibeEvent.getId());
            vibeEvent.setImage("reasons/" + vibeEvent.getId() + ".jpg");
        }
        HashMap<String, Object> data = createVibeEventData(vibeEvent);
        db.collection("VibeEvent").document(vibeEvent.getId()).set(data);
    }

    public HashMap<String, Object> createVibeEventData(VibeEvent vibeEvent) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ID", vibeEvent.getId());
        data.put("vibe", vibeEvent.getVibe().getName());
        data.put("datetime", vibeEvent.getDateTimeFormat());
        data.put("reason", vibeEvent.getReason());
        data.put("socSit", vibeEvent.getSocialSituation());
        data.put("image", vibeEvent.getImage());
        return data;
    };

}
