package b.android.flex;


import java.util.Vector;

public class workoutSchedule {


    private
    Vector<exercise> mExcersises;

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



}
