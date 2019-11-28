package com.example.vybe.AddEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.vybe.util.Constants.GET_FROM_GALLERY;

public class ImageFieldFragment extends Fragment {

    private Button pickImageBtn;
    private ImageView imageView;
    private Context context;
    private OnImageSelectedListener onImageSelectedListener;
    private Bitmap imageBitmap;
    private Button removeImageBtn;

    interface OnImageSelectedListener {
        void onImageSelected(Bitmap selectedImageBitmap);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        onImageSelectedListener = (OnImageSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_field, container, false);

        pickImageBtn = view.findViewById(R.id.pick_image_btn);
        imageView = view.findViewById(R.id.image_view);
        removeImageBtn = view.findViewById(R.id.remove_image_btn);

        pickImageBtn.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GET_FROM_GALLERY);
        });

        // ---Remove Image Button---
        removeImageBtn.setOnClickListener((View v) -> {
            imageView.setImageBitmap(null);
            onImageSelectedListener.onImageSelected(null);
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImageUri);
                onImageSelectedListener.onImageSelected(imageBitmap);
                imageView.setImageBitmap(imageBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setImage(Bitmap image) {
        imageView.setImageBitmap(image);
    }
}
