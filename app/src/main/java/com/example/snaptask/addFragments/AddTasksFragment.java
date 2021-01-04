package com.example.snaptask.addFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.snaptask.MainCalendar;
import com.example.snaptask.R;
import com.example.snaptask.menu.Goals;
import com.example.snaptask.menu.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTasksFragment extends Fragment implements MainCalendar.OnDateSelected {
    private Button subtask_btn, apply_btn;
    private EditText subtask_name, task_name;
    private LinearLayout lin_lay, sb_lay;
    private RelativeLayout const_lay;
    private RadioGroup rgPriority, rgDay;
    private RadioButton todayBtn, tomorrowBtn, lowPrBtn, mediumPrBtn, highPrBtn;
    public RadioButton chooseDayBtn;
    private String TASKS_KEY = "Tasks";
    private static final String TAG = "AddTasksFragment";
    private String day;
    private String priority;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dataBase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_tasks_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subtask_btn = (Button) view.findViewById(R.id.add_subtask_btn);
        subtask_name = (EditText) view.findViewById(R.id.subtask_name);
        task_name = (EditText) view.findViewById(R.id.task_name);
        lin_lay = (LinearLayout) view.findViewById(R.id.linear_layout);
        sb_lay = (LinearLayout) view.findViewById(R.id.subtask_lay);
        apply_btn = (Button) view.findViewById(R.id.apply_btn);

        rgDay = (RadioGroup) view.findViewById(R.id.date_group);
        rgPriority = (RadioGroup) view.findViewById(R.id.priority_group);
        todayBtn = (RadioButton) view.findViewById(R.id.today_btn);
        tomorrowBtn = (RadioButton) view.findViewById(R.id.tomorrow_btn);
        chooseDayBtn = (RadioButton) view.findViewById(R.id.choose_day_btn);
        lowPrBtn = (RadioButton) view.findViewById(R.id.low_btn);
        mediumPrBtn = (RadioButton) view.findViewById(R.id.medium_btn);
        highPrBtn = (RadioButton) view.findViewById(R.id.high_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dataBase = FirebaseDatabase.getInstance().getReference();
        dataBase.keepSynced(true);




        subtask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeTask();
                newActivityAfterAddTask();
            }
        });



        rgPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(lowPrBtn.isChecked()){
                    priority = "c";

                lowPrBtn.setTextColor(getResources().getColor(R.color.white));
                mediumPrBtn.setTextColor(getResources().getColor(R.color.blue));
                highPrBtn.setTextColor(getResources().getColor(R.color.blue));
                }

                if(mediumPrBtn.isChecked()){
                    priority = "b";

                    lowPrBtn.setTextColor(getResources().getColor(R.color.blue));
                    mediumPrBtn.setTextColor(getResources().getColor(R.color.white));
                    highPrBtn.setTextColor(getResources().getColor(R.color.blue));
                }
                if(highPrBtn.isChecked()){
                    priority = "a";

                    lowPrBtn.setTextColor(getResources().getColor(R.color.blue));
                    mediumPrBtn.setTextColor(getResources().getColor(R.color.blue));
                    highPrBtn.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        rgDay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (todayBtn.isChecked()) {

                    todayBtn.setTextColor(getResources().getColor(R.color.white));
                    tomorrowBtn.setTextColor(getResources().getColor(R.color.blue));
                    chooseDayBtn.setTextColor(getResources().getColor(R.color.blue));

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    day = df.format(currentTime);

                }

                if (tomorrowBtn.isChecked()) {

                    todayBtn.setTextColor(getResources().getColor(R.color.blue));
                    tomorrowBtn.setTextColor(getResources().getColor(R.color.white));
                    chooseDayBtn.setTextColor(getResources().getColor(R.color.blue));

                    Date dt = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    c.add(Calendar.DATE, 1);
                    dt = c.getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    day = df.format(dt);
                    System.out.println(day);
                }

                if (chooseDayBtn.isChecked()) {

                    todayBtn.setTextColor(getResources().getColor(R.color.blue));
                    tomorrowBtn.setTextColor(getResources().getColor(R.color.blue));
                    chooseDayBtn.setTextColor(getResources().getColor(R.color.white));

                    chooseDayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainCalendar mainCalendar = new MainCalendar();
                            mainCalendar.show(getParentFragmentManager(), "Main Calendar");
                            mainCalendar.setTargetFragment(AddTasksFragment.this, 1);

                        }
                    });

                }
            }

        });



    }



    private void writeTask() {
        String Task;
        Task = task_name.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(day == null) Toast.makeText(getActivity(), "Please choose a date!", Toast.LENGTH_SHORT).show();
        else if(priority == null) Toast.makeText(getActivity(), "Please choose a priority!", Toast.LENGTH_SHORT).show();
        else if(Task.isEmpty()) Toast.makeText(getActivity(), "Please write a name of task!", Toast.LENGTH_SHORT).show();
        else if(sb_lay.getChildCount() != 0) {
            String[] subTasks = new String[sb_lay.getChildCount()];
            for(int i = 1; i<sb_lay.getChildCount(); i++){

                View subtaskView = sb_lay.getChildAt(i);
                EditText editTextName = (EditText) subtaskView.findViewById(R.id.subtask_name);
                subTasks[i] = editTextName.getText().toString();

                if(subTasks[i].isEmpty()) {
                    Toast.makeText(getActivity(), "Please write a name of Subtask!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            for (int i = 0; i < sb_lay.getChildCount(); i++) {

                View subtaskView = sb_lay.getChildAt(i);

                EditText editTextName = (EditText) subtaskView.findViewById(R.id.subtask_name);
                subTasks[i] = editTextName.getText().toString();
                dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(Task).child("subtasks").push().setValue(subTasks[i]);
            }
            dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(Task).child("priority").setValue(priority);
            dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(Task).child("status").setValue("false");
        }
        else{
            dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(Task).child("priority").setValue(priority);
            dataBase.child("users").child(firebaseUser.getUid()).child("tasks").child(day).child(Task).child("status").setValue("false");
        }

    }



    private void addView() {
        Animation onAdded = AnimationUtils.loadAnimation(getActivity(), R.anim.sample_anim);

        final View subtaskView = getLayoutInflater().inflate(R.layout.subtask_raw, null, false);
        subtaskView.startAnimation(onAdded);
        ImageView imageClose = (ImageView) subtaskView.findViewById(R.id.remove_subtask);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(subtaskView);
            }
        });

        sb_lay.addView(subtaskView);
        
    }

    private void removeView(View view) {

        sb_lay.removeView(view);

    }

    @Override
    public void sendDate(String date) {
        chooseDayBtn.setText(date);
        day = date;
    }


    private void newActivityAfterAddTask(){
        Toast.makeText(getActivity(), "New task was added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Tasks.class);
        startActivity(intent);
        getActivity().finish();
    }
    }



