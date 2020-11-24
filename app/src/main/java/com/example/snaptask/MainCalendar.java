package com.example.snaptask;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.DialogFragment;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CalendarView;

        import com.example.snaptask.fragments.AddTasksFragment;
        import com.example.snaptask.menu.Add;

public class MainCalendar extends DialogFragment {
    private CalendarView Calendar;
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
        return inflater.inflate(R.layout.activity_main_calendar, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar = (CalendarView) view.findViewById(R.id.calendarView);

        Calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month +=1;
                date = (dayOfMonth+"-"+month+"-"+year);
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