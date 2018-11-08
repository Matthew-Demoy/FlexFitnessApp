package b.android.flex;

public class programManager extends Program {
    private int currentWorkout;
    private int currentExcercise;

    public int getCurrentWorkout() {
        return currentWorkout;
    }

    public int getCurrentExcercise() {
        return currentExcercise;
    }

    public programManager(){
        currentWorkout = 0;
        currentWorkout = 0;
        super.makeFiveByFive();
    }

    public void newProgram(){
        super.clearProgram();
        super.makeFiveByFive();
    }

    public workoutSchedule nextWorkout(){
        currentWorkout = (currentWorkout + 1) % (program.size() - 1);
        return super.program.get(currentWorkout).second;
    }


    public exercise nextExcercise(){
        currentExcercise = currentExcercise++;
        return super.program.get(currentWorkout).second.mExcersises.get(currentExcercise);
    }

    void increaseWeight(){

    }

}
