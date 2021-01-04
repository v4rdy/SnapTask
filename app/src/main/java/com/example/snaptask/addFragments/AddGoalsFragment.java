package com.example.snaptask.addFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.snaptask.R;
import com.example.snaptask.menu.Goals;
import com.example.snaptask.menu.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGoalsFragment extends Fragment {

    private EditText goalName;
    private Button addStep, applyBtn;
    private LinearLayout lin_lay, step_lay;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference database;


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
        applyBtn = (Button) view.findViewById(R.id.apply_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);


        this.getResources().getDisplayMetrics();

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeGoal();
                newActivityAfterAddGoal();
            }
        });


    }




    public void writeGoal() {
        String goal;
        goal = goalName.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (goal.isEmpty()) {
            Toast.makeText(getActivity(), "Please write a goal name!", Toast.LENGTH_SHORT).show();
        } else if (step_lay.getChildCount() != 0) {
            String[] goalStep = new String[step_lay.getChildCount()];
            for (int i = 0; i < step_lay.getChildCount(); i++) {

                View stepView = step_lay.getChildAt(i);
                EditText editTextName = (EditText) stepView.findViewById(R.id.step_name);
                goalStep[i] = editTextName.getText().toString();

                if (goalStep[i].isEmpty()) {
                    Toast.makeText(getActivity(), "Please write a name of goal step!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else database.child("users").child(firebaseUser.getUid()).child("goals").child(goal).child("steps").push().setValue(goalStep[i]);
            }
            database.child("users").child(firebaseUser.getUid()).child("goals").child(goal).child("status").setValue("false");
            database.child("users").child(firebaseUser.getUid()).child("goals").child(goal).child("delete_status").setValue("false");

        }
        else{
            database.child("users").child(firebaseUser.getUid()).child("goals").child(goal).child("status").setValue("false");
            database.child("users").child(firebaseUser.getUid()).child("goals").child(goal).child("delete_status").setValue("false");

        }

    }

    public void addView(){
        Animation onAdded = AnimationUtils.loadAnimation(getActivity(), R.anim.sample_anim);
        final View stepView = getLayoutInflater().inflate(R.layout.step_raw, null, false);
        stepView.startAnimation(onAdded);
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

    private void newActivityAfterAddGoal(){
        Toast.makeText(getActivity(), "New goal was added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Goals.class);
        startActivity(intent);
        getActivity().finish();
    }
}
