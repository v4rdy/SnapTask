package com.example.snaptask.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.snaptask.R;

public class AddTasksFragment extends Fragment {
    private Button subtask_btn;
    private EditText subtask_name;
    private LinearLayout lin_lay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_tasks_fragment, container, false);

        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subtask_btn = (Button) view.findViewById(R.id.add_subtask_btn);
        subtask_name = (EditText) view.findViewById(R.id.subtask_name);
        lin_lay = (LinearLayout) view.findViewById(R.id.linear_layout);
        subtask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(getActivity());
                editText.setText("Pizda");
                editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                lin_lay.addView(editText);
            }
        });

    }
    }


