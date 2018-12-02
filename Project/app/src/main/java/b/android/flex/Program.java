package b.android.flex;

import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class Program {

    protected Vector<Pair<String , workoutSchedule>> program;

    public Program(){
        this.program = new Vector<Pair<String,workoutSchedule>>();
    }

    public Program(Vector<String> titles, Vector<workoutSchedule> workouts) {
        if (titles.size() == workouts.size()) {
            for(int i = 0; i < titles.size(); i++)
            {
                addWorkoutToProgram(titles.get(i), workouts.get(i));
            }

            } else {
            //error
            }
    }

    public  Vector<Pair<String , workoutSchedule>> makeFiveByFive(Vector<Pair<String, Integer>> weights)
    {
        program = new Vector<Pair<String,workoutSchedule>>();
        String ChestDay = "Chest";
        String BackDay = "Back";
        String LegsDay = "Legs";

        workoutSchedule chestWorkout = new workoutSchedule();

        chestWorkout.addExcersise(new exercise("Flat Bench", 1, 5, 1, 2, 2, 10));
        chestWorkout.addExcersise(new exercise("Shoulder Press", 3, 5, 80, 3, 3, 60));
        chestWorkout.addExcersise(new exercise("Lateral Raise", 3, 10, 45, 2, 2, 60));
        chestWorkout.addExcersise(new exercise("Tricep Pushdown", 3, 12, 50, 1, 1, 60));
        chestWorkout.addExcersise(new exercise("Shrug", 4, 8, 160, 2, 3, 10));

        workoutSchedule backWorkout = new workoutSchedule();

        backWorkout.addExcersise(new exercise("Deadlift", 3, 5, 200, 2, 2, 90));
        backWorkout.addExcersise(new exercise("Barbell Row", 3, 8, 95, 3, 3, 60));
        backWorkout.addExcersise(new exercise("Lat Pulldown", 3, 10, 110, 2, 2, 60));
        backWorkout.addExcersise(new exercise("Dumbell Curl", 3, 12, 25, 1, 1, 60));
        backWorkout.addExcersise(new exercise("Reverse Fly", 3, 12, 40, 1, 1, 45));

        workoutSchedule legsWorkout = new workoutSchedule();
        legsWorkout.addExcersise(new exercise("Squat", 4, 5, 135, 2, 2, 90));
        legsWorkout.addExcersise(new exercise("Lunges", 3, 10, 70, 3, 3, 60));
        legsWorkout.addExcersise(new exercise("Leg Extensions", 3, 10, 70, 2, 2, 60));
        legsWorkout.addExcersise(new exercise("Hamstring Curl", 3, 10, 80, 1, 1, 60));
        legsWorkout.addExcersise(new exercise("Calf Raise", 3, 12, 90, 2, 3, 60));


        addWorkoutToProgram(ChestDay,chestWorkout);
        addWorkoutToProgram(BackDay,backWorkout);
        addWorkoutToProgram(LegsDay,legsWorkout);

        return program;

    }
    protected void addWorkoutToProgram(String title, workoutSchedule workout)
    {
        Pair<String, workoutSchedule> pair = new Pair(title, workout);
        program.add(pair);
    }

    protected  void clearProgram(){
        program = null;
    }

}


