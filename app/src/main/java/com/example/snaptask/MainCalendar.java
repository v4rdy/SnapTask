package com.example.snaptask;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.fragment.app.DialogFragment;

        import android.app.Dialog;
        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.icu.util.Calendar;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CalendarView;

        import java.util.Date;

public class MainCalendar extends DialogFragment {
    private CalendarView calendarView;
    private String date;
    private static final String TAG = "MainCalendar";

    public interface OnDateSelected{
         void sendDate(String date);
    }
    public OnDateSelected mOnInputSelected;


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.calendar_background);
        return inflater.inflate(R.layout.activity_main_calendar, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setMinDate((new Date().getTime()));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month +=1;
                if(dayOfMonth>9) date = (dayOfMonth+"-"+month+"-"+year);
                else date = ("0"+dayOfMonth+"-"+month+"-"+year);
                System.out.println(date);
                mOnInputSelected.sendDate(date);
                getDialog().dismiss();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnDateSelected) getTargetFragment();
            mOnInputSelected = (OnDateSelected) getActivity();

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: "+ e.getMessage());
        }
    }
}