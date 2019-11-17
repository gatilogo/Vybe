package com.example.vybe.AddEdit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.R;

public class SocialSituationFieldFragment extends Fragment {

    private Spinner socStnDropdown;
    private OnSocStnSelectedListener onSocStnSelectedListener;
    private Context context;

    interface OnSocStnSelectedListener {
        void onSocStnSelected(String socStn);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        onSocStnSelectedListener = (OnSocStnSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_situation_field, container, false);

        socStnDropdown = view.findViewById(R.id.soc_stn_dropdown);

        String[] socStn = new String[]{"Select a Social Situation", "Alone", "In a group", "Alone in a group"};
        ArrayAdapter<String> socialSituationAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, socStn);
        socStnDropdown.setAdapter(socialSituationAdapter);
        socStnDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onSocStnSelectedListener.onSocStnSelected(socStn[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        return view;
    }
}
