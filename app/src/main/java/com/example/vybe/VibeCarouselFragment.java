package com.example.vybe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class VibeCarouselFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;
    private int selectedEmoticon = R.drawable.ic_vibeless;

    public interface OnFragmentInteractionListener {
        void onOkPressed(int selectedVibeEmoticon);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vibe_carousel, null);

        CarouselPicker carouselPicker = view.findViewById(R.id.carousel_picker);
        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_vibeless));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_angry));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_disgusted));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_happy));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_sad));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_scared));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.ic_surprised));

        //Create an adapter
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(getActivity(), imageItems, 0);
        //Set the adapter
        carouselPicker.setAdapter(imageAdapter);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * On selection of an emoticon, set the Vibe for a Vibe Event
             *
             * @param position position of selected emoticon
             */
            @Override
            public void onPageSelected(int position) {
                selectedEmoticon = imageItems.get(position).getDrawable();
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedEmoticon != R.drawable.ic_vibeless) {
                            listener.onOkPressed(selectedEmoticon);
                        }
                        else {
                            // How do I force this to make me pick a vibe
                            Toast.makeText(getContext(), "Select a Vibe!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).create();
    }

}

