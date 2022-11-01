package edu.uga.cs.statecapitalsquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "MainActivity";
    QuestionsData questionsData = null;
    QuizzesData quizzesData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsData = new QuestionsData(getApplicationContext());
        quizzesData = new QuizzesData(getApplicationContext());
        setUpInitialData();
    }

    public final void quizButtonClick() {
        Fragment fragment = new QuizFragmentContainer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).addToBackStack("main screen" ).commit();
        Log.d( DEBUG_TAG, "onclick, quiz button" );
    }

    public final void reviewButtonClick() {
        Fragment fragment = new ReviewQuizzesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).addToBackStack("main screen" ).commit();
        Log.d( DEBUG_TAG, "onclick, review button" );
    }

    public void setUpInitialData() {
        if (questionsData != null) {
            questionsData.open();
            new QuestionDBInitializer().execute(); // AsyncTask to check db and see if questions table is empty
        }
    }

    private void readAndStoreValuesFromCSV() {
        try {
            InputStream in_s = getAssets().open( "state_capitals.csv" );
            CSVReader reader = new CSVReader( new InputStreamReader( in_s ) );
            reader.skip(1); // skip first line of csv since it contains the header
            String[] nextRow; // nextRow[] is an array of values from the line

            while( ( nextRow = reader.readNext() ) != null ) {
                Question question = new Question(nextRow[0], nextRow[1], nextRow[2], nextRow[3]);
                new QuestionDBWriter().execute(question); // AsyncTask to store in database
            }
        } catch (Exception e) {

        }
    }

    public class QuestionDBWriter extends AsyncTask<Question, Question> {

        // This method will run as a background process to write into db.
        @Override
        protected Question doInBackground( Question... questions ) {
            questionsData.storeQuestion(questions[0]);
            return questions[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.
        @Override
        protected void onPostExecute( Question question ) {

        }
    }

    public class QuestionDBInitializer extends AsyncTask<Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arguments) {
            if (questionsData != null && !questionsData.isEmpty()) {
                return false; // means that the question table already exists and has data
            }
            return true; // means the question table is empty and needs to be populated with data
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                readAndStoreValuesFromCSV(); // populate question table with data from CSV
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (questionsData != null) {
            questionsData.close();
        }
    }
}