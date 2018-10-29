package b.android.flex;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class GoTimer {
    private static final String TAG = "MyActivity";
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds ;
    int eccentricTime = 3;
    boolean state_complete = false;

    public int getConcentricTime() {
        return concentricTime;
    }

    public void setConcentricTime(int concentricTime) {
        this.concentricTime = concentricTime;
    }

    int concentricTime = 3;
    int reps;
    // State 1: Not Started, State 2 Start Countdown, State 3: In Set, State 4: Set Rest, State 5: Excersise Transition Rest
    private boolean[] states = new boolean[]{
            true,false,false,false,false
    };

    public int get_state()
    {
        for(int i = 0; i < states.length; i++)
        {
            if(states[i] == true)
            {
                return i;
            }
        }
        return -1;
    }

    public void start(Button GoButton, long Time, int time) {
        reps = 3;
        MillisecondTime = SystemClock.uptimeMillis() - Time;

        UpdateTime = TimeBuff + MillisecondTime;

        if((time - (UpdateTime / 1000) >= 60 ))
        {
            Seconds = (int) (time - (UpdateTime / 1000));

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;
            //MilliSeconds = (int) (UpdateTime % 1000);
            GoButton.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds));
        }
        else
        {
            Seconds = (int) (time - (UpdateTime / 1000));
            GoButton.setText("" + String.format("%02d", Seconds));

        }

        if(Seconds <= 0)
        {
            this.switch_state(0,1);
            this.state_complete = true;
        }

    }

    public void setConcentricTime(Button GoButton, long StartTime, int time) {
        MillisecondTime = SystemClock.uptimeMillis() - StartTime;

        UpdateTime = TimeBuff + MillisecondTime;

        Seconds = (int) (time - (UpdateTime / 1000));
        GoButton.setText("" + String.format("%02d", Seconds));

        if(Seconds <= 0)
        {
            //setEccentricTime(GoButton, SystemClock.uptimeMillis(), eccentricTime);
            switch_state(1,2);
            this.state_complete = true;
        }

    }

    public void setEccentricTime(Button GoButton, long StartTime, int time) {

        MillisecondTime = SystemClock.uptimeMillis() - StartTime;

        UpdateTime = TimeBuff + MillisecondTime;

        Seconds = (int) (time - (UpdateTime / 1000));
        GoButton.setText("" + String.format("%02d", Seconds));

        if(Seconds <= 0)
        {
            //setConcentricTime(GoButton, SystemClock.uptimeMillis(), concentricTime);
            reps--;
            switch_state(2,1);
            this.state_complete = true;
        }
        if(reps == 0)
        {
            Log.v("stuff", "reps = 0");
        }

    }

    public void switch_state(int oldState, int newState)
    {
        int state = this.get_state();
        this.states[oldState] = false;
        this.states[newState] = true;
    }

}
