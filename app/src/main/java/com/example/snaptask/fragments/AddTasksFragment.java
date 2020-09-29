package com.example.snaptask.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.snaptask.MainActivity;
import com.example.snaptask.MainCalendar;
import com.example.snaptask.R;
import com.example.snaptask.Registration;
import com.example.snaptask.menu.Add;
import com.example.snaptask.menu.Goals;
import com.example.snaptask.menu.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddTasksFragment extends Fragment implements View.OnClickListener {
    private Button subtask_btn, apply_btn;
    private EditText subtask_name, task_name;
    private LinearLayout lin_lay, sb_lay;
    private RelativeLayout const_lay;
    private RadioGroup rgPriority, rgDay;
    private RadioButton todayBtn, tomorrowBtn, chooseDayBtn, lowPrBtn, mediumPrBtn, highPrBtn;
    private String TASKS_KEY = "Tasks";
    public static final String DATE_KEY = "date";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dataBase;



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
        dataBase = FirebaseDatabase.getInstance().getReference(TASKS_KEY);
        this.getResources().getDisplayMetrics();


        subtask_btn.setOnClickListener(this);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeTask();
            }
        });
        chooseDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainCalendar.class);
                startActivity(intent);
            }
        });
      //  String strtext = getArguments().getString("edttext");
        //System.out.println(strtext);
    }

    @Override
    public void onClick(View v) {
        addView();
    }



    private void writeTask() {
        String Task;
        Task = task_name.getText().toString();
        dataBase.push().setValue(Task);

        String[] subTasks = new String[sb_lay.getChildCount()];
        for (int i = 0; i < sb_lay.getChildCount(); i++) {

            View subtaskView = sb_lay.getChildAt(i);

            EditText editTextName = (EditText) subtaskView.findViewById(R.id.subtask_name);
            subTasks[i] = editTextName.getText().toString();

            dataBase.child(Task).child("Subtasks").push().setValue(subTasks[i]);
            }
            dataBase.child(Task).push().setValue(getDay());
    }

    private String getDay(){
        String day=null;

        if(todayBtn.isChecked()){
            Date currentTime = Calendar.getInstance().getTime();
            day = currentTime.toString();
        }
        else if(tomorrowBtn.isChecked()){
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            day = dt.toString();
        }else if(chooseDayBtn.isChecked()) {
//            day =  getArguments().getString("DATE_KEY");
//            System.out.println(" Date is choosed ----------------------------------------------------------------------------" + day);
//            chooseDayBtn.setText(day);
        }
        return day;
    }

    private void addView(){
        final View subtaskView = getLayoutInflater().inflate(R.layout.subtask_raw, null, false);

        EditText editText = (EditText)subtaskView.findViewById(R.id.subtask_name);
        ImageView imageClose = (ImageView)subtaskView.findViewById(R.id.remove_subtask);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(subtaskView);
            }
        });

        sb_lay.addView(subtaskView);
        String day;
        day =  getArguments().getString("DATE_KEY");
        System.out.println(" Date is choosed ----------------------------------------------------------------------------" + day);

    }

    private void removeView(View view){
        sb_lay.removeView(view);
    }


        //SignOut!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });

}


