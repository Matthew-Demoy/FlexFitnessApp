package b.android.flex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static b.android.flex.ProgressActivity.LIFT.BenchPress;

//display diff between first and last bench


public class ProgressActivity extends AppCompatActivity {

    DatabaseReference databaseProgress;
    ValueEventListener mSendEventListner;

    TextView bestView;
    TextView worstView;
    Button mButton;
    Vector<progressExercise> completedExercises = new Vector<>();
    Vector<Vector<dateWeight>> userDateWeight;

    Button oneMonthDisplay;
    Button threeMonthDisplay;
    Button allTimeDisplay;

    Button benchDisplay;
    Button squatDisplay;
    Button deadliftDisplay;

    GraphView graph;
    int currentExerciseDisplayed;
    int currentTimespanDisplayed;

    BottomNavigationView bottomNav;

    public enum LIFT{
        BenchPress(0), Squat(1), Deadlift(2);

            private int id;

            LIFT(int id){
                this.id = id;
            }

            public int getID(){
                return id;
            }
    }

    private TextView tvX, tvY;

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        SharedPreferences settings = getSharedPreferences("my_pref", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("WORKOUT", currentExerciseDisplayed);
        editor.putInt("TIMESPAN", currentTimespanDisplayed);
        // Commit the edits!
        editor.commit();

        if (mSendEventListner != null) {
            databaseProgress.removeEventListener(mSendEventListner);

        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        databaseProgress = FirebaseDatabase.getInstance().getReference();

        userDateWeight = new Vector<Vector<dateWeight>>();
        userDateWeight.add(new Vector<dateWeight>());
        userDateWeight.add(new Vector<dateWeight>());
        userDateWeight.add(new Vector<dateWeight>());

        SharedPreferences settings = getSharedPreferences("my_pref", 0);

            currentExerciseDisplayed = BenchPress.getID();
            currentTimespanDisplayed  = 30;


        oneMonthDisplay = findViewById(R.id.monthButton);
        threeMonthDisplay = findViewById(R.id.threeMonthButton);
        allTimeDisplay = findViewById(R.id.allTimeButton);
        benchDisplay = findViewById(R.id.benchButton);
        squatDisplay = findViewById(R.id.squatButton);
        deadliftDisplay = findViewById(R.id.deadliftButton);

        graph = (GraphView) findViewById(R.id.graph);

        bottomNav= findViewById(R.id.bottom_navigation);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNav.getMenu().getItem(0).setChecked(true);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_progress:
                        Intent intent0 = new Intent(ProgressActivity.this, ProgressActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.nav_schedule:
                        Intent intent1 = new Intent(ProgressActivity.this, ScheduleActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_workout:
                        Intent intent2 = new Intent(ProgressActivity.this, WorkoutActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;

            }

        });


        oneMonthDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(30, currentExerciseDisplayed));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(30);

                graph.removeAllSeries();
                currentTimespanDisplayed = 30;
                graph.addSeries(series);
            }
        });

        threeMonthDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(90, currentExerciseDisplayed));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(90);

                graph.removeAllSeries();
                currentTimespanDisplayed = 90;
                graph.addSeries(series);
            }
        });

        allTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();

                Date currentDate = new Date();
                String helpDate = currentDate.toString();

                int xRange = calculateDaysBetween(helpDate, userDateWeight.get(currentExerciseDisplayed).get(0).mDate);

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(xRange, currentExerciseDisplayed));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(xRange);

                currentTimespanDisplayed = xRange;
                graph.removeAllSeries();
                graph.addSeries(series);
            }
        });

        benchDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(currentTimespanDisplayed, LIFT.BenchPress.getID()));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(currentTimespanDisplayed);

                graph.removeAllSeries();
                currentExerciseDisplayed = LIFT.BenchPress.getID();
                graph.addSeries(series);
            }
        });

        squatDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(currentTimespanDisplayed, LIFT.Squat.getID()));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(currentTimespanDisplayed);

                graph.removeAllSeries();
                currentExerciseDisplayed = LIFT.Squat.getID();
                graph.addSeries(series);
            }
        });

        deadliftDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrieveLiftData();
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData(currentTimespanDisplayed, LIFT.Deadlift.getID()));

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(currentTimespanDisplayed);

                graph.removeAllSeries();
                currentExerciseDisplayed = LIFT.Deadlift.getID();
                graph.addSeries(series);
            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.child("Exercises").getChildren()) {
                    if(messageSnapshot.child("RepsCompleted").getValue() != null)
                    {
                        String name = (String) messageSnapshot.child("ExcerciseName").getValue();
                        String date = (String) messageSnapshot.child("Date").getValue();
                        long reps = (long) messageSnapshot.child("RepsCompleted").getValue();
                        long sets = (long) messageSnapshot.child("SetNumber").getValue();
                        long weight = (long) messageSnapshot.child("Weight").getValue();
                        boolean easy = (boolean) messageSnapshot.child("tooEasy").getValue();
                        boolean hard = (boolean) messageSnapshot.child("tooHard").getValue();
                        completedExercises.add(new progressExercise(date, name, reps, sets, weight, easy, hard));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        databaseProgress.addValueEventListener(valueEventListener);
        mSendEventListner = valueEventListener;



    }

    /*
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            {
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            Fragment selectedFragment = new
                        }

                    };

            }
           */

    private DataPoint[] generateData(int days, int exercise) {
        DataPoint[] values = new DataPoint[days];
        long currentWeight = userDateWeight.get(exercise).get(0).mWeight;
        boolean weightAdded = false;
        Date currentDate = new Date();
        String helpDate = currentDate.toString();

        int j;
        int temp = 0;

        for (int i=0; i < days; i++){

                weightAdded = false;
                 for(j = 0; j < userDateWeight.get(exercise).size() && calculateDaysBetween(helpDate, userDateWeight.get(exercise).get(j).mDate) > days - i; j++)
                 {
                     temp = j;
                     currentWeight = userDateWeight.get(exercise).get(temp).mWeight;
                 }

                if(temp == days - i)
                {
                    weightAdded = true;
                    DataPoint v = new DataPoint(i, userDateWeight.get(exercise).get(temp).mWeight);
                    currentWeight = userDateWeight.get(exercise).get(temp).mWeight;
                    values[i] = v;
                }
                else
                {
                    values[i] = new DataPoint(i,currentWeight);
                }

                if(weightAdded == false)
                {
                    //values[i] = new DataPoint(i,currentWeight);
                }
            }

        return values;
    }

    private int calculateDaysBetween(String date1, String date2)
    {
        String[] split1 = date1.split(" ");
        String[] split2 = date2.split(" ");
        //Tue Sep 13 19:50:58 EST 2018
        String date1Fix;
        String date2Fix;

        date1Fix = split1[5] + '/' + monthToNumber(split1[1]) + '/' + split1[2];

        //if(Integer.parseInt(split2[2])>9)
        date2Fix = split2[5] + '/' + monthToNumber(split2[1]) + '/' + split2[2];
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDateNew = LocalDate.parse(date1Fix, format);
        LocalDate localDateOld = LocalDate.parse(date2Fix, format);

        int differenceInDays = localDateNew.getDayOfYear() - localDateOld.getDayOfYear();

        return differenceInDays;
    }

    private String monthToNumber(String month)
    {
        String newMonth;

        switch (month) {
            case "Jan":
                newMonth = "01";
                break;
            case "Feb":
                newMonth = "02";
                break;
            case "Mar":
                newMonth = "03";
                break;
            case "Apr":
                newMonth = "04";
                break;
            case "May":
                newMonth = "05";
                break;
            case "Jun":
                newMonth = "06";
                break;
            case "Jul":
                newMonth = "07";
                break;
            case "Aug":
                newMonth = "08";
                break;
            case "Sep":
                newMonth = "09";
                break;
            case "Oct":
                newMonth = "10";
                break;
            case "Nov":
                newMonth = "11";
                break;
            case "Dec":
                newMonth = "12";
                break;
                default:
                    newMonth="01";

        }
        return newMonth;
    }

    private void retrieveLiftData (){

        for(int i = 0; i < completedExercises.size(); i++)
        {
            if(completedExercises.get(i).mName.equals("Flat Bench"))
            {
                userDateWeight.get(BenchPress.getID()).add(new dateWeight(completedExercises.get(i).mDate, completedExercises.get(i).mWeight));
            }
            if(completedExercises.get(i).mName.equals("Squat"))
            {
                userDateWeight.get(LIFT.Squat.getID()).add(new dateWeight(completedExercises.get(i).mDate, completedExercises.get(i).mWeight));
            }
            if(completedExercises.get(i).mName.equals("Deadlift"))
            {
                userDateWeight.get(LIFT.Deadlift.getID()).add(new dateWeight(completedExercises.get(i).mDate, completedExercises.get(i).mWeight));
            }
        }


    }

    int getSelectedItem() {
        Menu menu = bottomNav.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }


}


