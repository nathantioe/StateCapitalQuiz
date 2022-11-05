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
 * Fragment that stores the Quiz data, including quiz questions, the current
 * quiz ID, the score, etc...
 */
public class QuizFragmentContainer extends Fragment {


    private ViewPager2 pager;
    private QuizPagerAdapter qAdapter;
    private QuestionsData questionsData = null;
    private QuizzesData quizzesData = null;
    public static ArrayList<Question> the6Questions = new ArrayList<>(6);
    public static ArrayList<String> userAnswers = new ArrayList<>(6);
    public static long currentQuizID = -1;
    public static int score = 0;

    public QuizFragmentContainer() {
        // Required empty public constructor
    }

    /**
     *
     * @return
     */
    public static QuizFragmentContainer newInstance() {
        QuizFragmentContainer fragment = new QuizFragmentContainer();
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            userAnswers = savedInstanceState.getStringArrayList("userAnswers");
            the6Questions = savedInstanceState.getParcelableArrayList("the6Questions");
            currentQuizID = savedInstanceState.getLong("currentQuizID");
            score = savedInstanceState.getInt("score");
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
        return inflater.inflate(R.layout.fragment_quiz_container, container, false);
    }

    /**
     * Opens the database and assigns adapter, resets the quiz and answers if a new quiz should
     * be created
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
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
            setUpQuiz();
            new QuizQuestionGenerator().execute();
        }
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("the6Questions", the6Questions);
        savedInstanceState.putStringArrayList("userAnswers", userAnswers);
        savedInstanceState.putInt("score", score);
        savedInstanceState.putLong("currentQuizID", currentQuizID);
    }

    /**
     * Opens the database if its closed
     */
    @Override
    public void onResume() {
        super.onResume();
        if(questionsData != null && !questionsData.isDBOpen()) { //Is there a reason to having 2 of theese?
            questionsData.open();
        }
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** AsyncTask for reading Questions from DB */
    public class QuizQuestionGenerator extends AsyncTask<Void, ArrayList<Question>> {

        /**
         * Returns six random questions in arraylist
         *
         * @param params
         * @return
         */
        @Override
        protected ArrayList<Question> doInBackground( Void... params ) {
            return questionsData.generate6QuizQuestions();
        }

        /**
         * Adds quiz to database and sets adapter
         *
         * @param generatedQuestions
         */
        @Override
        protected void onPostExecute( ArrayList<Question> generatedQuestions ) {
            addQuizToDB(generatedQuestions);
            the6Questions = generatedQuestions;

            pager.setOffscreenPageLimit(8);
            qAdapter = new QuizPagerAdapter( getChildFragmentManager(), getLifecycle() );
            pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
            pager.setAdapter( qAdapter );
        }
    }

    /**
     * Clears variables for new quiz
     */
    public static void setUpQuiz() {
        the6Questions.clear();
        userAnswers.clear();
        for (int i = 0; i < 6; i++) {
            userAnswers.add("");
        }
        score = 0;

    }

    /**
     * Writes new quiz to database
     *
     * @param questionList
     */
    public void addQuizToDB(ArrayList<Question> questionList) {
        Quiz quiz = new Quiz("", // empty date
                questionList.get(0).getId(), // q1
                questionList.get(1).getId(), // q2
                questionList.get(2).getId(), // q3
                questionList.get(3).getId(), // q4
                questionList.get(4).getId(), // q5
                questionList.get(5).getId(), // q6
                0, // result
                0); // questions answered
        new QuizDBWriter().execute(quiz);
    }

    /**
     * AsyncTask for creating Quiz row in Quiz table
     */
    public class QuizDBWriter extends AsyncTask<Quiz, Quiz> {



        /**
         * store quiz in database
         *
         * @param quizzes
         * @return
         */
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