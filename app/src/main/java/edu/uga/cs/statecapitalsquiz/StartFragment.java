package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    QuestionsData questionsData = null;

    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        Button reviewButton = v.findViewById( R.id.button );
        Button quizButton = v.findViewById( R.id.button2 );

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                QuizFragmentContainer quizFragmentContainer = QuizFragmentContainer.newInstance();
                fragmentTransaction.replace(R.id.fragmentContainerView, quizFragmentContainer).addToBackStack("main screen").commit();

//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.quizButtonClick();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                ReviewQuizzesFragment reviewQuizzesFragment = ReviewQuizzesFragment.newInstance();
                fragmentTransaction.replace(R.id.fragmentContainerView, reviewQuizzesFragment).addToBackStack("main screen").commit();
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.reviewButtonClick();
            }
        });

        questionsData = new QuestionsData(getActivity());
        setUpInitialData();
    }

    public void setUpInitialData() {
        if (questionsData != null) {
            questionsData.open();
            new QuestionDBInitializer().execute(); // AsyncTask to check db and see if questions table is empty
        }
    }

    private void readAndStoreValuesFromCSV() {
        try {
            InputStream in_s = getActivity().getAssets().open( "state_capitals.csv" );
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
    public void onPause() {
        super.onPause();
        if (questionsData != null) {
            questionsData.close();
        }
    }
}