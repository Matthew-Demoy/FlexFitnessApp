package b.android.flex;


import java.util.Vector;

public class workoutSchedule {


    private
    Vector<exercise> mExcersises = new Vector<>();;
    Vector<exercise> mExercisesVerbose = new Vector<>();;
    Vector<Integer> mTimes = new Vector<>();;
    workoutSchedule()
    {
        mExcersises.add(new exercise());
        mExcersises.add(new exercise());
        mExcersises.add(new exercise());
        mExcersises.add(new exercise());
    }


    public

    int getSize()
    {
        return  mExcersises.size();
    }

    void makeSchedule()
    {
        mTimes.add(60);

        for(int i = 0; i < mExcersises.size() - 1; i++)
        {
            for(int j = 0; j < mExcersises.get(i).mSetNumber - 1; j++)
            {
                for(int k = 0; k < mExcersises.get(i).mRepNumber - 1; k++)
                {
                    mTimes.add(mExcersises.get(i).mConcentric);
                    mTimes.add(mExcersises.get(i).mEccentric);
                }
                mTimes.add(mExcersises.get(i).mRestTime);
            }
        }
    }

    void makeScheduleVerbose()
    {
        mExercisesVerbose.add(new exercise(mExcersises.get(0).mExcersiseName, 0, 0, mExcersises.get(0).mWeightNumber, mExcersises.get(0).mEccentric, mExcersises.get(0).mConcentric, mExcersises.get(0).mRestTime));

        for(int i = 0; i < mExcersises.size() -1; i++)
        {
            for(int j = 0; j < mExcersises.get(i).mSetNumber - 1; j++)
            {
                for(int k = 0; k < mExcersises.get(i).mRepNumber - 1; k++)
                {
                    mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, j + 1, k, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));
                    mExercisesVerbose.add(new exercise(mExcersises.get(i).mExcersiseName, j + 1, k, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));

                }
                mExercisesVerbose.add(new exercise(mExcersises.get(i + 1).mExcersiseName, 0, 0, mExcersises.get(i).mWeightNumber, mExcersises.get(i).mEccentric, mExcersises.get(i).mConcentric, mExcersises.get(i).mRestTime));

            }
        }
    }




}
