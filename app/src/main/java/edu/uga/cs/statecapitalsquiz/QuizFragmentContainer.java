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

    //private static ArrayList<Question> questions = new ArrayList<>(); // ** note, use the6Questions ArrayList instead?
    //public static int[] rightAnswers = {0,0,0,0,0,0}; // ** note, use answers ArrayList instead? See added variables
    //public int hej = 1;

    boolean getsRotated = false;

//    public static final String[] statesAndCapitals = {
//            "Alabama", "Montgomery", "birmingham", "Auburn",
//            "Alaska", "Juneau", "anchorage", "Fairbanks",
//            "Arizona", "phonenic", "Tucson", "scottsdale",
//            "arkanasas", "little rock", "hot springs", "bentonville",
//            "californa", "sacramento", "los angeles", "san diego",
//            "colorado", "denver", "boulder", "aspen"
//    };

    /** ADDED VARIABLES from Nathan */
    // the6Questions list will be populated with 6 questions that are randomly ordered
    // The question numbers are random, but you still may need to reorder the answer choices
    public static ArrayList<Question> the6Questions = new ArrayList<>();

    // answers will contain list of string of the answers/capital cities
    public static ArrayList<String> answers = new ArrayList<>();

    // classes to help read/store questions/quizzes
    private QuestionsData questionsData = null;
    private QuizzesData quizzesData = null;

    // save list of 50 questions so that we don't need to retrieve it again from db
    public static List<Question> all50Questions;
    public static long currentQuizID = -1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragmentContainer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragmentContainer.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragmentContainer newInstance(String param1, String param2) {
        QuizFragmentContainer fragment = new QuizFragmentContainer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (savedInstanceState != null){
            answers = savedInstanceState.getStringArrayList("answers");
            //rightAnswers = savedInstanceState.getIntArray("rightAnswers");
            //questions = savedInstanceState.getParcelableArrayList("questions");
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
        new QuestionDBReader().execute();

        /** */

        //here the database should give a list question on the same structure as below,
        // this code will then scrable the answers and keep track of the score.

        if (getsRotated == false){

            /** No longer needed?? */
//            questions.add(new Question("Alabama", "Montgomery", "birmingham", "Auburn"));
//            questions.add(new Question("Alaska", "Juneau", "anchorage", "Fairbanks"));
//            questions.add(new Question("Arizona", "phonenic", "Tucson", "scottsdale"));
//            questions.add(new Question("arkanasas", "little rock", "hot springs", "bentonville"));
//            questions.add(new Question("californa", "sacramento", "los angeles", "san diego"));
//            questions.add(new Question("colorado", "denver", "boulder", "aspen"));


            /**
             * Was a little confused here.
             * Is this shuffling the question order, or is it shuffling the answer choices the user sees?
             * Had to comment it out for the code to work*/
//            for (int i=0;i<6;i++){
//                int random_int = (int)Math.floor(Math.random()*(6));
//                Log.d("random int", Integer.toString(random_int));
//                if (random_int == 0){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getCapitalCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getSecondCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getThirdCity();
//                    rightAnswers[i] = 1;
//                } else if (random_int == 1){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getCapitalCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getThirdCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getSecondCity();
//                    rightAnswers[i] = 1;
//                } else if (random_int == 2){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getSecondCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getCapitalCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getThirdCity();
//                    rightAnswers[i] = 2;
//                } else if (random_int == 3){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getThirdCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getCapitalCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getSecondCity();
//                    rightAnswers[i] = 2;
//                } else if (random_int == 4){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getSecondCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getThirdCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getCapitalCity();
//                    rightAnswers[i] = 3;
//                } else if (random_int == 5){
//                    statesAndCapitals[i*4] = questions.get(i).getStateName();
//                    statesAndCapitals[i*4 + 1] = questions.get(i).getThirdCity();
//                    statesAndCapitals[i*4 + 2] = questions.get(i).getSecondCity();
//                    statesAndCapitals[i*4 + 3] = questions.get(i).getCapitalCity();
//                    rightAnswers[i] = 3;
//                }
//
//            }
        }


        //Log.d("states and capitals", Arrays.toString(statesAndCapitals));
        //Log.d("right answers", Arrays.toString(rightAnswers));


        pager = view.findViewById( R.id.viewPager );
//        pager.setOffscreenPageLimit(8);
//        qAdapter = new QuizPagerAdapter( getChildFragmentManager(), getLifecycle() );
//        pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
//        pager.setAdapter( qAdapter );
        //pager.setUserInputEnabled(false);




        //pager.setUserInputEnabled(false);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putStringArrayList("answers", answers);
        //savedInstanceState.putIntArray("rightAnswers", rightAnswers);
        //savedInstanceState.putParcelableArrayList("questions", (ArrayList<? extends Parcelable>) questions);

        // etc.
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
    public class QuestionDBReader extends AsyncTask<Void, List<Question>> {

        @Override
        protected List<Question> doInBackground( Void... params ) {
            // retrieve all the questions from database
            List<Question> questions = questionsData.retrieveAllQuestions();
            return questions;
        }

        @Override
        protected void onPostExecute( List<Question> allQuestions ) {
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