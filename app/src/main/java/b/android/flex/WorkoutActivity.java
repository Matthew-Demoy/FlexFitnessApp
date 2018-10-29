package b.android.flex;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WorkoutActivity extends AppCompatActivity {

    GoTimer gotimer = new GoTimer();
    Button go_button;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);


        go_button = (Button)findViewById(R.id.go_button);
        handler = new Handler();
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //StartTime = SystemClock.uptimeMillis();
                //gotimer.state_complete = false;
                //handler.postDelayed(runnable, 0);

                new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        go_button.setText("" + String.format("%02d", (millisUntilFinished / 1000)));
                    }

                    public void onFinish() {
                        go_button.setText("done with start");
                        gotimer.switch_state(0,1);
                    }
                }.start();


            }
        });



        if(gotimer.get_state() == 1)
        {
            go_button.setText("done");
        }
        if(gotimer.get_state() == 2)
        {
            StartTime = SystemClock.uptimeMillis();
            gotimer.state_complete = false;
            handler.postDelayed(runnable, 0);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            int state = gotimer.get_state();
            if(gotimer.state_complete == false)
            {
                switch(state){
                    case 0:
                        gotimer.start(go_button, StartTime, 4);
                        handler.postDelayed(this, 0);
                        //gotimer.switch_state(0,1);
                        //handler.postDelayed(this, 0);
                        break;
                    case 1:
                        gotimer.setConcentricTime(go_button, StartTime, gotimer.getConcentricTime());
                        handler.postDelayed(this, 0);
                        break;
                    case 2:
                        gotimer.setEccentricTime(go_button, StartTime, gotimer.getConcentricTime());
                        handler.postDelayed(this, 0);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
            }

        }

    };
}
