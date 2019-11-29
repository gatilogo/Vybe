package com.example.vybe.AddEdit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.example.vybe.Models.Vibe;
import com.example.vybe.R;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * VibeCarouselDialogFragment extends a DialogFragment and serves as a custom
 * picker for a user to decide which Vibe they want to have added to a Vibe Event
 * Functionality is similar to that of a {@link android.app.DatePickerDialog}
 */
public class VibeCarouselDialogFragment extends DialogFragment {

    private Context context;
    private int selectedPosition = 0;
    private OnVibeSelectedListener onVibeSelectedListener;

    public interface OnVibeSelectedListener {
        void onVibeSelected(Vibe vibe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnVibeSelectedListener) {
            onVibeSelectedListener = (OnVibeSelectedListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnVibeSelectedListener");

        }
    }

    // create the custom carousel picker within the dialog fragment
    // populates a list of carousel picker items and then displays them for user's choosing
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vibe_carousel, null);

        CarouselPicker carouselPicker = view.findViewById(R.id.carousel_picker);

        // Populate the carousel with all the vibes
        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        ArrayList<Integer> emoticonList = Vibe.getEmoticons();
        for (Integer emoticon : emoticonList.subList(1, emoticonList.size())) {
            imageItems.add(new CarouselPicker.DrawableItem(emoticon));
        }

        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(getActivity(), imageItems, 0);
        carouselPicker.setAdapter(imageAdapter);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // on selection of an emoticon, set the selectedEmoticon to that emoticon
            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Select a Vibe")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedEmoticon = imageItems.get(selectedPosition).getDrawable();
                    Vibe selectedVibe = Vibe.ofEmoticon(selectedEmoticon);

                    if (selectedVibe != Vibe.NONE) {
                        onVibeSelectedListener.onVibeSelected(selectedVibe);
                    }

                }).create();
    }

}

