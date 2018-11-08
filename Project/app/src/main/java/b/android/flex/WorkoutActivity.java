package b.android.flex;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

public class WorkoutActivity extends AppCompatActivity {

    Button go_button;
    Button pauseButton;
    Button increaseButton;
    Button decreaseButton;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds;
    Vector<CountDownTimer> times = new Vector<>();
    int currentSpot = 0;
    int currentSet = 0;
    CountDownTimer tempTimer;
    TextView weightView, setView, exerciseView, repView;
    workoutSchedule currentWorkout = new workoutSchedule();
    programManager currentProgram = new programManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        go_button = (Button)findViewById(R.id.go_button);
        pauseButton = (Button)findViewById(R.id.pauseButton);
<<<<<<< HEAD
=======
        increaseButton = (Button)findViewById(R.id.increaseButton);
        decreaseButton = (Button)findViewById(R.id.decreaseButton);

>>>>>>> c4d7fd7be8f09f81da0dcdb38fdc31fef4e0d532
        weightView = (TextView) findViewById(R.id.WeightTextview);
        setView = (TextView) findViewById(R.id.setTextview);
        exerciseView = (TextView) findViewById(R.id.ExcersiseTextview) ;
        repView = (TextView) findViewById(R.id.repsTextview);


        handler = new Handler();
        createTimer(go_button, currentWorkout);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    times.get(currentProgram.getCurrentExcercise()).start();

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                times.get(currentSpot).cancel();

            }
        });
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseWeight();

            }
        });
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseWeight();

            }
        });
    }
    void increaseWeight()
    {
    currentWorkout.mExercisesVerbose.get(currentSet).setmWeightNumber(currentWorkout.mExercisesVerbose.elementAt(currentSet).getmWeightNumber() + 5);
    }
    void decreaseWeight()
    {
        currentWorkout.mExercisesVerbose.get(currentSet).setmWeightNumber(currentWorkout.mExercisesVerbose.elementAt(currentSet).getmWeightNumber() - 5);
    }
    void createTimer(final Button button, workoutSchedule workout)
    {
        workout.makeSchedule();
        workout.makeScheduleVerbose();
        for(int x = 0 ; x < workout.mTimes.size()- 1; x++)
        {
            tempTimer = new CountDownTimer(workout.mTimes.get(x)*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    button.setText("seconds remaining: " + millisUntilFinished / 1000 + " at spot " + currentSpot);
                    weightView.setText(Integer.toString(currentWorkout.mExercisesVerbose.get(currentSet).mWeightNumber));
                    setView.setText(Integer.toString(currentWorkout.mExercisesVerbose.get(currentSpot).mSetNumber));
                    exerciseView.setText(currentWorkout.mExercisesVerbose.get(currentSpot).mExcersiseName);
                    repView.setText(Integer.toString(currentWorkout.mExercisesVerbose.get(currentSpot).mRepNumber));

                }

                @Override
                public void onFinish() {
                    if(currentSpot < times.size() - 1)
                    {
                        currentSpot++;
                        times.get(currentSpot).start();
                        currentSet = currentWorkout.mExercisesVerbose.get(currentSpot).mSetNumber;
                    }
                    else
                    {
                        button.setText("done");
                    }

                }

            };
            times.add(tempTimer);
        }
    }

}
