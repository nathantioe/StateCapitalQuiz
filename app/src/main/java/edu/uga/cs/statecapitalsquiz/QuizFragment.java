package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class QuizFragment extends Fragment {

//    private static final String[] statesAndCapitalss = {
//            "Alabama", "Montgomery", "birmingham", "Auburn",
//            "Alaska", "Juneau", "anchorage", "Fairbanks",
//            "Arizona", "phonenic", "Tucson", "scottsdale",
//            "arkanasas", "little rock", "hot springs", "bentonville",
//            "californa", "sacramento", "los angeles", "san diego",
//            "colorado", "denver", "boulder", "aspen"
//    };

    private ArrayList<String> answerChoices;
    private int questionNumber;
    private boolean hasSelectedCorrectAnswer = false;
    private String selectedAnswer = "";
    private boolean firstTimeLoading = true;

    private TextView question;
    private TextView results;
    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
//    private int[] answers;
//
//    private int whichState;

    public QuizFragment() {

    }

//    public static QuizFragment newInstance( int stateNum ) {
//        QuizFragment fragment = new QuizFragment();
//        Bundle args = new Bundle();
//        args.putInt( "stateNum", stateNum);
//        fragment.setArguments( args );
//        return fragment;
//    }

    public static QuizFragment newInstance( int questionNumber ) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt( "questionNumber", questionNumber);
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null ){
            questionNumber = getArguments().getInt( "questionNumber" );
        }
        if (savedInstanceState != null) {
            questionNumber = savedInstanceState.getInt("questionNumber");
            answerChoices = savedInstanceState.getStringArrayList("answerChoices");
            selectedAnswer = savedInstanceState.getString("selectedAnswer");
            firstTimeLoading = savedInstanceState.getBoolean("firstTimeLoading");
            QuizPagerAdapter.userAnswers.set(questionNumber, selectedAnswer);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ){

        return inflater.inflate(R.layout.fragment_quiz, container, false );
    }

    public void updateScore(){
        // TODO: Fix scoring system, not properly updating in the Done screen
        if (QuizPagerAdapter.userAnswers.get(questionNumber).equals(QuizFragmentContainer.answers.get(questionNumber))) {
            hasSelectedCorrectAnswer = true;
            QuizPagerAdapter.score++;
        } else {
            if (hasSelectedCorrectAnswer) {
                QuizPagerAdapter.score--;
            }
        }

       // int localScore = 0;
        //Log.d("answers", Arrays.toString(QuizPagerAdapter.answers));
        //Log.d("rightanswers", Arrays.toString(rightAnswers));


//        for (int i=0;i<6;i++){
//            if (QuizPagerAdapter.answers[i] == QuizFragmentContainer.rightAnswers[i] ){
//                localScore++;
//            }
//        }
//        for (int i = 0; i < 6; i++) {
//            if (QuizPagerAdapter.userAnswers.get(questionNumber).equals(QuizFragmentContainer.answers.get(questionNumber))) {
//                localScore++;
//            }
//        }

        //Log.d("updatescore", Integer.toString(localScore));
       // QuizPagerAdapter.score = localScore;
    }

    @Override
    public void onResume() {
        super.onResume();
        //results = (TextView)getActivity().findViewById(R.id.results)
        if (firstTimeLoading) {
            displayCorrectOrIncorrect();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("questionNumber", questionNumber);
        outState.putStringArrayList("answerChoices", answerChoices);
        outState.putString("selectedAnswer", selectedAnswer);
        outState.putBoolean("firstTimeLoading", firstTimeLoading);
    }

    public void displayCorrectOrIncorrect() {
        if (questionNumber >= 1) {
            String text = "";
            if (QuizPagerAdapter.userAnswers.get(questionNumber - 1).equals(QuizFragmentContainer.answers.get(questionNumber - 1))) {
                text = "Correct!";
            } else {
                text = "Incorrect!";
            }
            Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();
            firstTimeLoading = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        //public void onActivityCreated(Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

//        if (answers == null){
//             answers = new int[]{0, 0, 0, 0, 0, 0};
//        }

        ViewPager2 pager = view.findViewById( R.id.viewPager );
        if (pager != null){
            pager.setUserInputEnabled(false);
        }

        results = (TextView)getActivity().findViewById(R.id.results);
        question = view.findViewById( R.id.textView2 );
        radioButton = view.findViewById( R.id.radioButton );
        radioButton2 = view.findViewById( R.id.radioButton2 );
        radioButton3 = view.findViewById( R.id.radioButton3 );

        //question.setText( "What is the state capital of: " + QuizFragmentContainer.statesAndCapitals[whichState * 4] );
        question.setText("What is the state capital of: " + QuizFragmentContainer.the6Questions.get(questionNumber).getStateName());

        if (savedInstanceState == null) {
            answerChoices = new ArrayList<>();
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getCapitalCity());
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getSecondCity());
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getThirdCity());
            Collections.shuffle(answerChoices);
//            radioButton.setText(answerChoices.get(0));
//            radioButton2.setText(answerChoices.get(1));
//            radioButton3.setText(answerChoices.get(2));
        }
        radioButton.setText(answerChoices.get(0));
        radioButton2.setText(answerChoices.get(1));
        radioButton3.setText(answerChoices.get(2));

//        radioButton.setText( QuizFragmentContainer.statesAndCapitals[whichState * 4 + 1] );
//        radioButton2.setText( QuizFragmentContainer.statesAndCapitals[whichState * 4 + 2] );
//        radioButton3.setText( QuizFragmentContainer.statesAndCapitals[whichState * 4 + 3] );

       // updateScore();
        //Log.d("whichstate", Integer.toString(whichState));


        RadioGroup radioGroup = (RadioGroup) view.findViewById( R.id.radioGroup );
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //ViewPager2 pager = view.findViewById( R.id.viewPager );
                //if (pager != null){
                //    pager.setUserInputEnabled(true);
                //}

                if (i == R.id.radioButton){
                    QuizPagerAdapter.userAnswers.set(questionNumber, answerChoices.get(0));
                    selectedAnswer = answerChoices.get(0);
//                    QuizPagerAdapter.answers[questionNumber] = 1;
//                    if (answers != null) {
//                        //Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
//                    }
                } else if (i == R.id.radioButton2){
                    QuizPagerAdapter.userAnswers.set(questionNumber, answerChoices.get(1));
                    selectedAnswer = answerChoices.get(1);
//                    QuizPagerAdapter.answers[questionNumber] = 2;
//                    if (answers != null) {
//                        //Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
//                    }
                } else if (i == R.id.radioButton3){
                    QuizPagerAdapter.userAnswers.set(questionNumber, answerChoices.get(2));
                    selectedAnswer = answerChoices.get(2);
//                    QuizPagerAdapter.answers[questionNumber] = 3;
//                    if (answers != null) {
//                        //Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
//                    }
                }

                updateScore();
                //Log.d("whichstate", Integer.toString(whichState));
//                if (questionNumber > 0){
//                    results = (TextView)getActivity().findViewById(R.id.results);
//
//                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                    LocalDateTime now = LocalDateTime.now();
//                    System.out.println(dtf.format(now));
//                    String time = dtf.format(now);
//
//                    if (results != null){
//                        results.setText("Score: " + QuizPagerAdapter.score + "/6 " + "Time: " + time);
//                    }
//
//                    //Log.d("quizpageadapterscore", Integer.toString(QuizPagerAdapter.score));
//                    //Log.d("checkedchangeSettext", (String) results.getText());
//                }
            }
        });




    }







}
