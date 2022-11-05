package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Fragment that appears when the user finishes the quiz.
 * Displays to the user his/her results.
 */
public class QuizDoneFragment extends Fragment {

    private QuizzesData quizzesData;
    private TextView results;
    private String time;
    private boolean alreadySetTime = false;

    public QuizDoneFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return new instance of the fragment
     */
    public static QuizDoneFragment newInstance() {
        QuizDoneFragment fragment = new QuizDoneFragment();
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            time = savedInstanceState.getString("time");
            alreadySetTime = savedInstanceState.getBoolean("alreadySetTime");
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_done, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {

        results = view.findViewById( R.id.results );
        quizzesData = new QuizzesData(getActivity());
        quizzesData.open();

        Button homeButton = view.findViewById(R.id.button3);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizFragmentContainer.setUpQuiz();
                getActivity().onBackPressed();
            }
        });
    }


    public class QuizDBUpdater extends AsyncTask<String, Void> {

        /**
         * Updates database with an asynchronous task
         *
         * @param time
         * @return
         */
        @Override
        protected Void doInBackground(String... time) {
            quizzesData.updateQuizByID(QuizFragmentContainer.currentQuizID, time[0], QuizFragmentContainer.score, 6);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    /**
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("time", time);
        outState.putBoolean("alreadySetTime", alreadySetTime);
    }


    /**
     * Checks if the db is open and that there is a time assigned to the quiz
     */
    @Override
    public void onResume() {
        super.onResume();
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.open();
        }

        if (!alreadySetTime) {
            Calendar calendar = Calendar.getInstance();
            time = calendar.getTime().toString();
            new QuizDBUpdater().execute(time);
            alreadySetTime = true;
        }
        results.setText("Score: " + QuizFragmentContainer.score + "/6 " + "\nTime: " + time);
    }

    /**
     * Closes db
     */
    @Override
    public void onPause() {
        super.onPause();
        if(quizzesData != null) {
            quizzesData.close();
        }
    }

}