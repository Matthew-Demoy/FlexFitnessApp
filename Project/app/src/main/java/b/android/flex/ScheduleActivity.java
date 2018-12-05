package b.android.flex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleActivity  extends AppCompatActivity {
    Button nextWorkoutButton,lastWorkoutButton;
    int daytracker = 0;
    private int i;
    programManager currentProgram = new programManager();
    // workoutSchedule currentWorkout = new workoutSchedule();
    TextView Workout1View, Workout2View, Workout3View, Workout4View, Workout5View, WorkoutNameView,dayWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        nextWorkoutButton = (Button)findViewById(R.id.NextWorkout);
        lastWorkoutButton = (Button)findViewById(R.id.LastWorkout);
        Workout1View = (TextView) findViewById(R.id.Exercise1);
        Workout2View = (TextView) findViewById(R.id.Exercise2);
        Workout3View = (TextView) findViewById(R.id.Exercise3);
        Workout4View = (TextView) findViewById(R.id.Exercise4);
        Workout5View = (TextView) findViewById(R.id.Exercise5);
        dayWorkout = (TextView) findViewById(R.id.DayofWorkout);
        WorkoutNameView = (TextView) findViewById(R.id.WorkoutType);
        Intent mIntent = getIntent();
        i = mIntent.getIntExtra("workoutnumber",0);

        //iterates through the current workout and gets the exercise names
        if(currentProgram.program.get(i).second.mExcersises.size() == 5)
        {
            Workout1View.setText(currentProgram.program.get(i).second.mExcersises.get(0).mExcersiseName);
            Workout2View.setText(currentProgram.program.get(i).second.mExcersises.get(1).mExcersiseName);
            Workout3View.setText(currentProgram.program.get(i).second.mExcersises.get(2).mExcersiseName);
            Workout4View.setText(currentProgram.program.get(i).second.mExcersises.get(3).mExcersiseName);
            Workout5View.setText(currentProgram.program.get(i).second.mExcersises.get(4).mExcersiseName);
            WorkoutNameView.setText(currentProgram.program.get(i).first);
            dayWorkout.setText("Today's Workout");
        }


        //gets previouse workouts and keeps track of wether it was in the future or past
        lastWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i-1 != -1){
                    i= i-1;
                }
                else i = 2;
                int negativedays = 0;
                daytracker = daytracker - 1;
                Workout1View.setText(currentProgram.program.get(i).second.mExcersises.get(0).mExcersiseName);
                Workout2View.setText(currentProgram.program.get(i).second.mExcersises.get(1).mExcersiseName);
                Workout3View.setText(currentProgram.program.get(i).second.mExcersises.get(2).mExcersiseName);
                Workout4View.setText(currentProgram.program.get(i).second.mExcersises.get(3).mExcersiseName);
                Workout5View.setText(currentProgram.program.get(i).second.mExcersises.get(4).mExcersiseName);
                WorkoutNameView.setText(currentProgram.program.get(i).first);
                if(daytracker == -1){
                    dayWorkout.setText("Yesterday's Workout");
                }
                if(daytracker == 0){
                    dayWorkout.setText("Current Workout");
                }
                if(daytracker == 1){
                    dayWorkout.setText("Tomorrow's Workout");
                }
                if (daytracker < -1) {
                    for(int daycopy = daytracker;daycopy < 0;daycopy++){
                        negativedays++;
                    }
                    dayWorkout.setText(negativedays+" days ago Workout");
                }
                if(daytracker > 1){
                    dayWorkout.setText(daytracker + " days from now Workout");
                }
            }
        });
        nextWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i+1 != 3){
                    i= i+1;
                }
                else i = 0;
                daytracker = daytracker +1;
                int negativedays = 0;
                Workout1View.setText(currentProgram.program.get(i).second.mExcersises.get(0).mExcersiseName);
                Workout2View.setText(currentProgram.program.get(i).second.mExcersises.get(1).mExcersiseName);
                Workout3View.setText(currentProgram.program.get(i).second.mExcersises.get(2).mExcersiseName);
                Workout4View.setText(currentProgram.program.get(i).second.mExcersises.get(3).mExcersiseName);
                Workout5View.setText(currentProgram.program.get(i).second.mExcersises.get(4).mExcersiseName);
                WorkoutNameView.setText(currentProgram.program.get(i).first );
                if(daytracker == 1){
                    dayWorkout.setText("Tomorrow's Workout");
                }
                if(daytracker == -1){
                    dayWorkout.setText("Yesterday's Workout");
                }
                if(daytracker == 0){
                    dayWorkout.setText("Today's Workout");
                }
                if (daytracker > 1) {
                    dayWorkout.setText(daytracker+" days from now Workout");
                }
                if(daytracker < -1){
                    for(int daycopy = daytracker;daycopy < 0;daycopy++){
                        negativedays++;
                    }
                    dayWorkout.setText(negativedays+" days ago Workout");
                }
            }
        });

        BottomNavigationView bottomNav= findViewById(R.id.bottom_navigation_schedule);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        //This sets up the navigation view and tells which activity to go to based on which section of the nav bar is touched
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_progress:
                        Intent intent0 = new Intent(ScheduleActivity.this, ProgressActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.nav_schedule:
                        Intent intent1 = new Intent(ScheduleActivity.this, ScheduleActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_workout:
                        Intent intent2 = new Intent(ScheduleActivity.this, WorkoutActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;

            }

        });
    }
    public void goToTimer(View view){
        Intent intent = new Intent(this,WorkoutActivity.class);
        //int current = currentProgram.program.get(currentProgram.program.);
        startActivity(intent);
    }
}
