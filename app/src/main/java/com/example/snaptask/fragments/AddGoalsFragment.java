package com.example.snaptask.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.snaptask.R;
import com.example.snaptask.menu.Add;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddGoalsFragment extends Fragment implements View.OnClickListener {

    private EditText goalName;
    private Button addStep;
    private LinearLayout lin_lay, step_lay;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_goals_fragment,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addStep = (Button) view.findViewById(R.id.add_step);
        goalName = (EditText) view.findViewById(R.id.goal_name);
        lin_lay = (LinearLayout) view.findViewById(R.id.linear_layout);
        step_lay = (LinearLayout) view.findViewById(R.id.step_lay);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.getResources().getDisplayMetrics();

        addStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        addView();
    }

    public void addView(){
        final View stepView = getLayoutInflater().inflate(R.layout.step_raw, null, false);

        EditText editText = (EditText)stepView.findViewById(R.id.step_name);
        ImageView imageClose = (ImageView)stepView.findViewById(R.id.remove_step);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(stepView);
            }
        });

        step_lay.addView(stepView);

    }

    private void removeView(View view){
        step_lay.removeView(view);
    }
}
