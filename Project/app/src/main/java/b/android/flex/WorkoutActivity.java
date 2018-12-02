package b.android.flex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WorkoutActivity extends AppCompatActivity {

    Button go_button;
    Button pauseButton;
    Button increaseButton;
    Button decreaseButton;
    Button progressButton;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds;
    Vector<CountDownTimer> times = new Vector<>();
    DatabaseReference databaseExercises;
    Date currentDate;
    DatabaseReference databaseProgress;
    ValueEventListener mSendEventListner;
    protected Vector<Pair<String, Integer>> weights = new Vector<Pair<String, Integer>>();


    int currentSet = 0;
    CountDownTimer tempTimer;
    TextView weightView, setView, exerciseView, repView, workoutView;
    workoutSchedule currentWorkout = new workoutSchedule();
    programManager currentProgram;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateWeightsDB();
        outState.putInt("WORKOUT", currentProgram.getCurrentWorkout());
        outState.putInt("EXERCISE", currentProgram.getCurrentExcercise());
        outState.putInt("SPOT", currentProgram.getCurrentSpot());
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        SharedPreferences settings = getSharedPreferences("my_pref", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("WORKOUT", currentProgram.getCurrentWorkout());
        editor.putInt("EXERCISE", currentProgram.getCurrentExcercise());
        if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() < currentProgram.getCurrentSpot())
        {
            editor.putInt("SPOT", 0);
        }
        else
        {
            editor.putInt("SPOT", currentProgram.getCurrentSpot());
        }

        // Commit the edits!
        editor.commit();
        currentProgram.isTimerRunning = false;
        updateWeightsDB();

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentProgram = new programManager(savedInstanceState.getInt("WORKOUT"), savedInstanceState.getInt("EXERCISE"), savedInstanceState.getInt("SPOT"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        databaseExercises = FirebaseDatabase.getInstance().getReference();
        databaseProgress = FirebaseDatabase.getInstance().getReference();

        SharedPreferences settings = getSharedPreferences("my_pref", 0);

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.child("Weights").getChildren()) {
                    String name = (String) messageSnapshot.getKey();
                    Integer weight = (int) (long) messageSnapshot.getValue();
                    weights.add(new Pair<>(name, weight));

                }
                if(weights.size() == 15)
                {
                    currentProgram.weights = weights;
                    databaseProgress.removeEventListener(mSendEventListner);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        databaseProgress.addValueEventListener(valueEventListener);
        mSendEventListner = valueEventListener;

        if(settings != null)
        {
            currentProgram  = new programManager(settings.getInt("WORKOUT", 0),settings.getInt("EXERCISE", 0),settings.getInt("SPOT", 0));
        }
        else
        {
            currentProgram  = new programManager();
        }

        go_button = (Button)findViewById(R.id.go_button);
        pauseButton = (Button)findViewById(R.id.pauseButton);
        increaseButton = (Button)findViewById(R.id.increaseButton);
        decreaseButton = (Button)findViewById(R.id.decreaseButton);
        //progressButton = (Button)findViewById(R.id.progressButton);

        weightView = (TextView) findViewById(R.id.WeightTextview);
        setView = (TextView) findViewById(R.id.setTextview);
        exerciseView = (TextView) findViewById(R.id.ExcersiseTextview) ;
        repView = (TextView) findViewById(R.id.repsTextview);
        workoutView = findViewById(R.id.currentWorkoutView);

        handler = new Handler();
        createTimer(go_button, currentProgram.program.get(currentProgram.getCurrentWorkout()).second);



        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentProgram.weights.size() == 15)
                {
                    currentProgram.getWeight();

                    currentProgram.program.get(currentProgram.getCurrentWorkout()).second.makeSchedule();
                    currentProgram.program.get(currentProgram.getCurrentWorkout()).second.makeScheduleVerbose();
                }
                if(currentProgram.isTimerRunning == false){

                    if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() < currentProgram.getCurrentSpot())
                    {
                        currentProgram.resetCurrentSpot();
                    }

                    if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() != 0) {
                        currentProgram.isTimerRunning = true;
                        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();
                    }
                    else
                    {
                        createTimer(go_button, currentProgram.program.get(currentProgram.getCurrentWorkout()).second);
                        currentProgram.isTimerRunning = true;
                        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();
                    }

                }else{
                    if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() - 1 > currentProgram.getCurrentSpot()) {
                        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).cancel();
                        currentProgram.increaseCurrentSpot();
                        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();
                    }

                }

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProgram.isTimerRunning = false;
                if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() > currentProgram.getCurrentSpot()) {
                    currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).cancel();
                }

            }
        });
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentProgram.isTimerRunning == true && currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() >= currentProgram.getCurrentSpot())
                {
                    increaseWeight();
                    currentProgram.increaseWeight(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName);
                }


            }
        });
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentProgram.isTimerRunning == true && currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() >= currentProgram.getCurrentSpot())
                    {
                        decreaseWeight();
                        currentProgram.decreaseWeight(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName);

                    }

            }
        });

        /*
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WorkoutActivity.this, ProgressActivity.class);
                WorkoutActivity.this.startActivity(myIntent);
            }
        });
           */
        BottomNavigationView bottomNav= findViewById(R.id.bottom_navigation);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_progress:
                        Intent intent0 = new Intent(WorkoutActivity.this, ProgressActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.nav_schedule:
                        Intent intent1 = new Intent(WorkoutActivity.this, ScheduleActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_workout:
                        Intent intent2 = new Intent(WorkoutActivity.this, WorkoutActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;

            }

        });



    }


    void increaseWeight()
    {
        if(currentProgram.isTimerRunning == true && currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() >= currentProgram.getCurrentSpot()) {
            updateDatabase(false, true);
            for (int exercise = currentProgram.getCurrentExcercise(); exercise < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.size(); exercise++) {
                if (currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(exercise).mExcersiseName.equals(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName))
                    ;
                {
                    currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(exercise).setmWeightNumber(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.elementAt(exercise).getmWeightNumber() + 5);
                }
            }

            currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).cancel();
            while (currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber != 0) {
                //this might array out of bounds
                currentProgram.increaseCurrentSpot();
            }
            if (currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() >= currentProgram.getCurrentSpot()) {
                currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();
            }
        }
    }
    void decreaseWeight()
    {
        updateDatabase(true,false);
        for(int exercise = currentProgram.getCurrentExcercise(); exercise < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.size(); exercise++)
        {
            if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(exercise).mExcersiseName.equals(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName));
            {
                currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(exercise).setmWeightNumber(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.elementAt(exercise).getmWeightNumber() - 5);
            }
        }

        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).cancel();
        while(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber != 0)
        {
            //this might array out of bounds
            currentProgram.increaseCurrentSpot();
        }
        if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.size() >= currentProgram.getCurrentSpot())
        {
            currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();

        }
    }
    void createTimer(final Button button, workoutSchedule workout)
    {
        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.makeSchedule();
        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.makeScheduleVerbose();
        for(int x = 0 ; x < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mTimes.size(); x++)
        {
            tempTimer = new CountDownTimer(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mTimes.get(x)*1000, 50) {
                @Override
                public void onTick(long millisUntilFinished) {

                    int temp = currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber;
                    for(int i = currentProgram.getCurrentSpot(); i < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.size(); i++)
                    {
                        if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName.equals(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(i).mExcersiseName))
                        {
                            if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber <= currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(i).mSetNumber)
                            {
                                temp = currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(i).mSetNumber + 1;
                            }
                        }
                    }

                    if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber == 0)
                    {
                        button.setText((millisUntilFinished / 1000) + "seconds of rest" );
                    }
                    else
                    {
                        button.setText("Set " + Integer.toString(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber) + " of " + Integer.toString(temp));
                    }

                    workoutView.setText(currentProgram.program.get(currentProgram.getCurrentWorkout()).first + "day");
                    weightView.setText("at " + Integer.toString(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mWeightNumber) + " lbs");


                    setView.setText(Integer.toString(temp) + " sets of ");
                    exerciseView.setText(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName);
                    if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber != 0)
                    {
                        repView.setText( "for " + Integer.toString(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber) + " reps");

                    }


                }

                @Override
                public void onFinish() {
                    if(currentProgram.getCurrentSpot() < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mTimes.size() - 1)
                    {
                        for(int x = 0; x < currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExcersises.size(); x++)
                        {
                            if(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName.equals(currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExcersises.get(x).mExcersiseName))
                            {
                                int a = currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber;
                                int b = currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExcersises.get(x).mRepNumber - 1;
                                if(a == b)
                                {
                                    updateDatabase(false,false);
                                }
                            }

                        }

                        currentProgram.increaseCurrentSpot();
                        currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.get(currentProgram.getCurrentSpot()).start();
                        currentSet = currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber;


                    }
                    else
                    {
                        currentProgram.isTimerRunning = false;
                        currentProgram.nextWorkout();
                        button.setText("done");
                    }

                }

            };
            currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mCountDownTimers.add(tempTimer);
        }


    }


    void updateDatabase(boolean tooHard, boolean tooEasy)
    {
        JSONArray jsonArray;

        DatabaseReference databaseList = databaseExercises.child("Exercises").push();
        String key = databaseList.getKey();

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> obj = new HashMap<>();
        currentDate = new Date();


        obj.put("Date", currentDate.toString());
        databaseList.updateChildren(obj);
        obj.put("ExcerciseName", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName);
        databaseList.updateChildren(obj);
        obj.put("SetNumber", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber);
        databaseList.updateChildren(obj);
        obj.put("Weight", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mWeightNumber);
        databaseList.updateChildren(obj);
        obj.put("RepsCompleted", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber);
        databaseList.updateChildren(obj);

        if(tooEasy == false)
        {
            obj.put("tooEasy", false);
        }else
        {
            obj.put("tooEasy", true);
        }
        databaseList.updateChildren(obj);

        if(tooHard == false)
        {
            obj.put("tooHard", false);
        }else
        {
            obj.put("tooHard", true);
        }
        databaseList.updateChildren(obj);
    }

    void updateWeightsDB()
    {
        JSONArray jsonArray;

        DatabaseReference databaseList = databaseExercises.child("Weights");
        String key = databaseList.getKey();

        Map<String, Object> obj = new HashMap<>();

        for(int i = 0; i < currentProgram.weights.size(); i++)
        {
            obj.put(currentProgram.weights.get(i).first, currentProgram.weights.get(i).second);
            databaseList.updateChildren(obj);
        }
        /*
        obj.put("Date", currentDate.toString());
        databaseList.updateChildren(obj);
        obj.put("ExcerciseName", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mExcersiseName);
        databaseList.updateChildren(obj);
        obj.put("SetNumber", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mSetNumber);
        databaseList.updateChildren(obj);
        obj.put("Weight", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mWeightNumber);
        databaseList.updateChildren(obj);
        obj.put("RepsCompleted", currentProgram.program.get(currentProgram.getCurrentWorkout()).second.mExercisesVerbose.get(currentProgram.getCurrentSpot()).mRepNumber);
        databaseList.updateChildren(obj);
        */
        databaseList.updateChildren(obj);
    }


}
