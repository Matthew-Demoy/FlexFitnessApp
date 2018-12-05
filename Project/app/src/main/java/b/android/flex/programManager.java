package b.android.flex;

import android.os.CountDownTimer;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class programManager extends Program {

    //keep the state of the program with these three integers
    private int currentWorkout;
    private int currentExcercise;
    private int currentSpot;
    //the program manager has a program that holds workout schedules which hold exercises
    public Vector<Pair<String , workoutSchedule>> program;
    //buttons do different things when if the timer is running so we always should know!
    public Boolean isTimerRunning;

    protected Vector<Pair<String, Integer>> weights = new Vector<Pair<String, Integer>>();

    public int getCurrentWorkout() {
        return currentWorkout;
    }

    public int getCurrentExcercise() {
        return currentExcercise;
    }

    //initialize the program with default values which starts the program from the begining
    public programManager(){
        currentWorkout = 0;
        currentExcercise = 0;
        currentSpot = 0;
        isTimerRunning = false;
        program = new Vector<Pair<String,workoutSchedule>>();
        program = super.makeFiveByFive(this.weights);
    }

    //used to restore the state of the program
    public programManager(int workout, int ex, int spot){
        currentWorkout = workout;
        currentExcercise = ex;
        currentSpot = spot;
        isTimerRunning = false;
        program = new Vector<Pair<String,workoutSchedule>>();
        program = super.makeFiveByFive(this.weights);
    }

    public void newProgram(){
        super.clearProgram();
        super.makeFiveByFive(this.weights);
    }

    public void nextWorkout(){
        switch(currentWorkout){
            case 0: currentWorkout = 1;
                    break;
            case 1: currentWorkout = 2;
                    break;
            case 2: currentWorkout = 0;
                    break;
        }
        //currentWorkout = (currentWorkout + 1) % (program.size() - 1);
    }

    public workoutSchedule firstWorkout(){
        return super.program.get(0).second;
    }

    public int returnCurrentWorkout(){
        return currentWorkout;
    }


    public exercise nextExcercise(){
        currentExcercise = currentExcercise++;
        return super.program.get(currentWorkout).second.mExcersises.get(currentExcercise);
    }

    //updates the weights object which is use to save the users weights in the database onPause and onDestroy
    public void increaseWeight(String exerciseName){
        for(int i = 0; i < this.weights.size(); i++){
            if(weights.get(i).first.equals(exerciseName)){
                Pair<String, Integer> newWeight = new Pair<>(exerciseName, weights.get(i).second + 5);
                weights.set(i,  newWeight);
            }
        }
    }

    public void decreaseWeight(String exerciseName){
        for(int i = 0; i < this.weights.size(); i++){
            if(weights.get(i).first.equals(exerciseName)){
                Pair<String, Integer> newWeight = new Pair<>(exerciseName, weights.get(i).second + 5);
                weights.set(i,  weights.get(i));
            }
        }
    }

    void makeWorkoutTimes(){


    }

    void createWorkoutTimes(){
        program.get(getCurrentWorkout()).second.getTimes();

    }

    public void increaseCurrentSpot(){
        currentSpot++;
    }

    public int getCurrentSpot(){
        return currentSpot;
    }

    public void resetCurrentSpot(){
        currentSpot = 0;
    }

    public void getWeight() {
        for (int j = 0; j < program.size(); j++) {
            for (int k = 0; k < program.get(j).second.mExcersises.size(); k++) {
                for (int i = 0; i < weights.size(); i++) {
                    if (program.get(j).second.mExcersises.get(k).mExcersiseName.equals(weights.get(i).first)) {
                        program.get(j).second.mExcersises.get(k).mWeightNumber = weights.get(i).second;
                    }
                }
            }

        }
    }


}
