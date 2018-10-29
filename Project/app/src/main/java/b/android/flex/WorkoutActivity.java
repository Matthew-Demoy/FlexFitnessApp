package b.android.flex;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

public class WorkoutActivity extends AppCompatActivity {

    Button go_button;
    Button pauseButton;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds;
    int scheduleTimes[]  = {10,3,3,10,3,3,10};
    Vector<CountDownTimer> times = new Vector<>();
    int currentSpot = 0;
    CountDownTimer tempTimer;
    TextView weightView, setView, exerciseView, repView;
    workoutSchedule currentWorkout = new workoutSchedule();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        go_button = (Button)findViewById(R.id.go_button);
        pauseButton = (Button)findViewById(R.id.pauseButton);
        handler = new Handler();
        createTimer(go_button, currentWorkout);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    times.get(currentSpot).start();

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                times.get(currentSpot).cancel();

            }
        });

    }

    void createTimer(final Button button, workoutSchedule workout)
    {
        for(int x = 0 ; x < workout.getSize(); x++)
        {
            tempTimer = new CountDownTimer(scheduleTimes[x]*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    button.setText("seconds remaining: " + millisUntilFinished / 1000 + " at spot " + currentSpot);
                }

                @Override
                public void onFinish() {
                    if(currentSpot < times.size() - 1)
                    {
                        currentSpot++;
                        times.get(currentSpot).start();
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
