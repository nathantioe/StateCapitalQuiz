package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragmentContainer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragmentContainer extends Fragment {


    ViewPager2 pager;
    QuizPagerAdapter qAdapter;

    boolean getsRotated = false;

    public static ArrayList<Question> the6Questions = new ArrayList<>();

    // answers will contain list of string of the answers/capital cities
    public static ArrayList<String> answers = new ArrayList<>();

    // classes to help read/store questions/quizzes
    private QuestionsData questionsData = null;
    private QuizzesData quizzesData = null;

    // save list of 50 questions so that we don't need to retrieve it again from db
    public static ArrayList<Question> all50Questions;
    public static long currentQuizID = -1;

    public QuizFragmentContainer() {
        // Required empty public constructor
    }

    public static QuizFragmentContainer newInstance() {
        QuizFragmentContainer fragment = new QuizFragmentContainer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            answers = savedInstanceState.getStringArrayList("answers");
            all50Questions = savedInstanceState.getParcelableArrayList("all50Questions");
            the6Questions = savedInstanceState.getParcelableArrayList("the6Questions");
            currentQuizID = savedInstanceState.getLong("currentQuizID");
            getsRotated = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {

        /** Added by Nathan */
        questionsData = new QuestionsData(getActivity());
        quizzesData = new QuizzesData(getActivity());
        questionsData.open();
        quizzesData.open();
        pager = view.findViewById( R.id.viewPager );
        if (savedInstanceState != null) {
            pager.setOffscreenPageLimit(8);
            qAdapter = new QuizPagerAdapter( getChildFragmentManager(), getLifecycle() );
            pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
            pager.setAdapter( qAdapter );
        } else {
            new QuestionDBReader().execute();
        }

        if (getsRotated == false){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("answers", answers);
        savedInstanceState.putParcelableArrayList("all50Questions", all50Questions);
        savedInstanceState.putParcelableArrayList("the6Questions", the6Questions);
        savedInstanceState.putLong("currentQuizID", currentQuizID);
    }

    /** ADDED FUNCTIONS BELOW by Nathan */

    @Override
    public void onResume() {
        super.onResume();
        if(questionsData != null && !questionsData.isDBOpen()) {
            questionsData.open();
        }
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(questionsData != null) {
            questionsData.close();
        }
        if(quizzesData != null) {
            quizzesData.close();
        }
    }

    /** AsyncTask for reading Questions from DB */
    public class QuestionDBReader extends AsyncTask<Void, ArrayList<Question>> {

        @Override
        protected ArrayList<Question> doInBackground( Void... params ) {
            // retrieve all the questions from database
            ArrayList<Question> questions = questionsData.retrieveAllQuestions();
            return questions;
        }

        @Override
        protected void onPostExecute( ArrayList<Question> allQuestions ) {
            // when all the questions have been retrieved from database, process them
            setUpQuestions(allQuestions);
            all50Questions = allQuestions;
            addQuizToDB();

            pager.setOffscreenPageLimit(8);
            qAdapter = new QuizPagerAdapter( getChildFragmentManager(), getLifecycle() );
            pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
            pager.setAdapter( qAdapter );
        }
    }

    /** helper method to set up the questions */
    private void setUpQuestions(List<Question> allQuestions) {
        List<Question> questions = allQuestions;
        Collections.shuffle(questions); // shuffle the 50 questions
        for (int i = 0; i < 6; i++) { // take the first 6 questions
            the6Questions.add(questions.get(i)); // add to list of the 6 questions
            answers.add(questions.get(i).getCapitalCity()); // add list of answers
        }
    }

    public static void setUpForNewQuiz() {
        the6Questions.clear();
        answers.clear();
        List<Question> questions = all50Questions;
        Collections.shuffle(questions);
        for (int i = 0; i < 6; i++) { // take the first 6 questions
            the6Questions.add(questions.get(i)); // add to list of the 6 questions
            answers.add(questions.get(i).getCapitalCity()); // add list of answers
        }

    }


    /**Ignore the code below, this should be moved to the QuizDoneFragment **/
    /** method to create quiz and add it to DB */
    public void addQuizToDB() {
        Quiz quiz = new Quiz("", // empty date
                the6Questions.get(0).getId(), // q1
                the6Questions.get(1).getId(), // q2
                the6Questions.get(2).getId(), // q3
                the6Questions.get(3).getId(), // q4
                the6Questions.get(4).getId(), // q5
                the6Questions.get(5).getId(), // q6
                0, // result
                0); // questions answered
        new QuizDBWriter().execute(quiz);
    }

    /** AsyncTask for creating Quiz row in Quiz table */
    public class QuizDBWriter extends AsyncTask<Quiz, Quiz> {

        // store quiz in db
        @Override
        protected Quiz doInBackground( Quiz... quizzes ) {
            return quizzesData.storeQuiz( quizzes[0] );
        }

        @Override
        protected void onPostExecute( Quiz quiz ) {
            currentQuizID = quiz.getId();
        }
    }
}