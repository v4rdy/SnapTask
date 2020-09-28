package com.example.snaptask;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.CalendarView;

        import com.example.snaptask.fragments.AddTasksFragment;
        import com.example.snaptask.menu.Add;

public class MainCalendar extends AppCompatActivity {
    private CalendarView Calendar;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
        Calendar = (CalendarView) findViewById(R.id.calendarView);



        Calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (dayOfMonth+"/"+month+"/"+year);
                System.out.println(date);
//                Intent intent = new Intent(MainCalendar.this, Add.class);
//                intent.putExtra("DATE_KEY", date);
                getInstance(date);
                finish();
            }

        });
    }

    public static AddTasksFragment getInstance(String date){
        Bundle bundle = new Bundle();
        bundle.putString("edttext", date);
        // set Fragmentclass Arguments
        AddTasksFragment fragobj = new AddTasksFragment ();
        fragobj.setArguments(bundle);
        return fragobj;
    }

}