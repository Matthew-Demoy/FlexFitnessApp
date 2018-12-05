package b.android.flex;

import java.util.Map;

//copy of exercise object but uses longs instead of ints because thats how firebase stores numeric values
public class progressExercise {

    String mDate;
    String mName;
    long mReps;
    long mSets;
    long mWeight;
    boolean mToEasy;
    boolean mToHard;

    progressExercise(){
        mDate = "Tue Jan 1 19:50:58 EST 1990";
        mName = "Null Exercise";
    }

    progressExercise(String data, String name, long reps, long sets, long weight, boolean ToEasy, boolean ToHard)
    {
        mDate = data;
        mName = name;
        mReps = reps;
        mSets = sets;
        mWeight = weight;
        mToEasy = ToEasy;
        mToHard = ToHard;
    }

}
