package b.android.flex;

//The workhorse when it comes to making the lists of exercises
//allot of code commented out because workouts are no longer incremented by reps however future versions will bee
import android.os.CountDownTimer;

import java.util.Vector;

public class workoutSchedule {

    //mExercises is literally a list of exercises
    public Vector<exercise> mExcersises;
    //exercises are listed in multiples of its set # with rests after each listing
    public Vector<exercise> mExercisesVerbose;
    //holds the amount of seconds each exercise requires
    public Vector<Integer> mTimes;
    //holds the countdowntimer objects in order so they can be easily stopped and restarted
    public Vector<CountDownTimer> mCountDownTimers;


    workoutSchedule()
    {
        this.mExcersises = new Vector<>();
        this.mExercisesVerbose = new Vector<>();
        this.mTimes = new Vector<>();
        this.mCountDownTimers = new Vector<>();
    }

    int getSize()
    {
        return  mExcersises.size();
    }

    void makeSchedule()
    {
        mTimes = new Vector<>();
        for(int i = 0; i < mExcersises.size(); i++)
        {
            for(int j = 0; j < mExcersises.get(i).mSetNumber; j++)
            {
                for(int k = 0; k < mExcersises.get(i).mRepNumber - 1; k++)
                {
                    //mTimes.add(mExcersises.get(i).mConcentric);
                    //mTimes.add(mExcersises.get(i).mEccentric);
                }
                mTimes.add(999);
                mTimes.add(mExcersises.get(i).mRestTime);
            }
        }
        //mTimes.remove(mTimes.size() - 1);

    }

    Vector<Integer> getTimes(){
        return mTimes;
    }
    void makeScheduleVerbose()
    {
        mExercisesVerbose = new Vector<>();

        //mExercisesVerbose.add(new exercise(mExcersises.get(0).mExcersiseName, 0, 0, mExcersises.get(0).mWeightNumber, mExcersises.get(0).mEccentric, mExcersises.get(0).mConcentric, mExcersises.get(0).mRestTime));

        for(int i = 0; i < mExcersises.size(); i++)
        {
            for(int j = 0; j < mExcersises.get(i).mSetNumber; j++)
            {
                for(int k = 0; k < mExcersises.get(i).mRepNumber; k++)
                {
                    //mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, j + 1, k, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));
                    //mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, j + 1, k, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));

                }
                mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, j+1, mExcersises.get(i).mRepNumber, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));
                mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, mExcersises.get(i).mSetNumber, 0, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));

            }
        }
        //mExercisesVerbose.remove(mExercisesVerbose.size() - 1);
    }

    public void addExcersise(exercise Exercise)
    {
        mExcersises.add(Exercise);

        /*
        int newExcercisePosition = mExcersises.size()
        for(int j = 0; j < mExcersises.get(newExcercisePosition).mSetNumber - 1; j++)
        {
            for(int k = 0; k < mExcersises.get(newExcercisePosition).mRepNumber - 1; k++)
            {
                mExercisesVerbose.add(new exercise(mExcersises.get(newExcercisePosition).mExcersiseName, j + 1, k, mExcersises.get(newExcercisePosition).mWeightNumber, mExcersises.get(newExcercisePosition).mEccentric, mExcersises.get(newExcercisePosition).mConcentric, mExcersises.get(newExcercisePosition).mRestTime));
                mExercisesVerbose.add(new exercise(mExcersises.get(newExcercisePosition).mExcersiseName, j + 1, k, mExcersises.get(newExcercisePosition).mWeightNumber, mExcersises.get(newExcercisePosition).mEccentric, mExcersises.get(newExcercisePosition).mConcentric, mExcersises.get(newExcercisePosition).mRestTime));

            }
            mExercisesVerbose.add(new exercise(mExcersises.get(i + 1).mExcersiseName, 0, 0, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));

        }
        */
    }



}
