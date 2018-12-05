package b.android.flex;

import java.util.Vector;

//There was a plan to have multiple programs in the app but since there is one this program is no long a unique obj
//just saving this if I want to build this feature in the future
public class fiveByFiveProgram extends Program {
    public fiveByFiveProgram()
    {
        String ChestDay = "Chest";
        String BackDay = "Back";
        String LegsDay = "Legs";

        workoutSchedule chestWorkout = new workoutSchedule();
        chestWorkout.addExcersise(new exercise("Flat Bench", 3, 5, 135, 2, 2, 90));
        chestWorkout.addExcersise(new exercise("Shoulder Press", 3, 5, 80, 3, 3, 60));
        chestWorkout.addExcersise(new exercise("Lateral Raise", 3, 10, 45, 2, 2, 60));
        chestWorkout.addExcersise(new exercise("Tricep Pushdown", 3, 12, 50, 1, 1, 60));
        chestWorkout.addExcersise(new exercise("Shrug", 4, 8, 160, 2, 3, 60));

        workoutSchedule backWorkout = new workoutSchedule();
        chestWorkout.addExcersise(new exercise("Deadlift", 3, 5, 200, 2, 2, 90));
        chestWorkout.addExcersise(new exercise("Barbell Row", 3, 8, 95, 3, 3, 60));
        chestWorkout.addExcersise(new exercise("Lat Pulldown", 3, 10, 110, 2, 2, 60));
        chestWorkout.addExcersise(new exercise("Dumbell Curl", 3, 12, 25, 1, 1, 60));


        workoutSchedule legsWorkout = new workoutSchedule();
        chestWorkout.addExcersise(new exercise("Squat", 4, 5, 135, 2, 2, 90));
        chestWorkout.addExcersise(new exercise("Lunges", 3, 10, 70, 3, 3, 60));
        chestWorkout.addExcersise(new exercise("Leg Extensions", 3, 10, 70, 2, 2, 60));
        chestWorkout.addExcersise(new exercise("Hamstring Curl", 3, 10, 80, 1, 1, 60));
        chestWorkout.addExcersise(new exercise("Calf Raise", 3, 12, 90, 2, 3, 60));

        super.addWorkoutToProgram(ChestDay,chestWorkout);
        super.addWorkoutToProgram(BackDay,backWorkout);
        super.addWorkoutToProgram(LegsDay,legsWorkout);


        /*
        Chest
        Flat bench 3x5
        Shoulder press 3x5
        Lateral raise 3x10
        Tricep Pushdown 3x12
        Shrug 3x8

            Back
        Deadlift 3x5
        Barbell Row 3x8
        Lat Pulldown 3x10
        Curl 3x12

            Legs
        Squat 4x5
        Lunge 3x10
        Leg Extensions 3x10
        Hamstring Curl 3x10
        Calf Raise 3x12
        */

    }


}
