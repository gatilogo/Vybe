package com.example.vybe.AddEdit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.Models.SocialSituation;
import com.example.vybe.R;

import java.util.List;

public class SocialSituationFieldFragment extends Fragment {

    private OnSocStnSelectedListener onSocStnSelectedListener;
    private Context context;
    private Spinner socStnDropdown;
    private ImageButton clearBtn;

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
        clearBtn = view.findViewById(R.id.clear_btn);

        ArrayAdapter<String> socStnAdapter = createSocStnAdapter();
        socStnDropdown.setAdapter(socStnAdapter);

        int hintPosition = socStnAdapter.getCount();
        socStnDropdown.setSelection(hintPosition);

        socStnDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != hintPosition) {
                    onSocStnSelectedListener.onSocStnSelected(SocialSituation.at(position).toString());

                } else {
                    onSocStnSelectedListener.onSocStnSelected(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        clearBtn.setOnClickListener((View v) -> {
            socStnDropdown.setSelection(hintPosition);
        });

        return view;
    }


    private ArrayAdapter<String> createSocStnAdapter() {
        List<String> options = SocialSituation.stringValues();
        options.add("Select a Social Situation");

        return new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, options) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
    }

    /**
     * Set the default Social Situation. If the string doesn't match any of the social situations,
     * the social situation is not set (it is null aka "Select a Social Situation")
     * @param socStnDesc
     */
    public void setDefaultSocStn(String socStnDesc) {
        SocialSituation ss = SocialSituation.of(socStnDesc);

        if (ss != null) {
            socStnDropdown.setSelection(ss.ordinal());
        }

    }
}
