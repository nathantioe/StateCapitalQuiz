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

    private boolean changedOrientation = false;

    private ArrayList<String> answerChoices;
    private int questionNumber;
    private boolean hasSelectedCorrectAnswer = false;
    private boolean alreadySelectedAnswer = false;
    private boolean firstTimeLoading = true;

    private TextView question;
    private TextView results;
    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private QuizzesData quizzesData;

    public QuizFragment() {

    }

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
            firstTimeLoading = savedInstanceState.getBoolean("firstTimeLoading");
            hasSelectedCorrectAnswer = savedInstanceState.getBoolean("hasSelectedCorrectAnswer");
            changedOrientation = savedInstanceState.getBoolean("changedOrientation");
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ){

        return inflater.inflate(R.layout.fragment_quiz, container, false );
    }

    public void updateScore(){
        if (QuizFragmentContainer.userAnswers.get(questionNumber)
                .equals(QuizFragmentContainer.the6Questions.get(questionNumber).getCapitalCity())) {
            hasSelectedCorrectAnswer = true;
            QuizFragmentContainer.score++;
        } else {
            if (hasSelectedCorrectAnswer) {
                QuizFragmentContainer.score--;
                hasSelectedCorrectAnswer = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.open();
        }
        if (firstTimeLoading) {
            displayCorrectOrIncorrect(questionNumber);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        new QuizDBUpdater().execute();
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.close();
        }
    }

    public class QuizDBUpdater extends AsyncTask<Void, Void> {
        @Override
        protected Void doInBackground(Void... arguments) {
            quizzesData.updateQuizByID(QuizFragmentContainer.currentQuizID, "", QuizFragmentContainer.score, questionNumber);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("questionNumber", questionNumber);
        outState.putStringArrayList("answerChoices", answerChoices);
        outState.putBoolean("hasSelectedCorrectAnswer", hasSelectedCorrectAnswer);
        outState.putBoolean("firstTimeLoading", firstTimeLoading);
        outState.putBoolean("changedOrientation", true);
    }

    public void displayCorrectOrIncorrect(int questionNumber) {
        String text = "";
        if (questionNumber == 0) {
            return;
        }
        if (QuizFragmentContainer.userAnswers.get(questionNumber - 1)
                .equals(QuizFragmentContainer.the6Questions.get(questionNumber - 1).getCapitalCity())) {
            text = "Correct!";
        } else {
            text = "Incorrect!";
        }
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.show();
        firstTimeLoading = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        ViewPager2 pager = view.findViewById( R.id.viewPager );
        if (pager != null){
            pager.setUserInputEnabled(false);
        }
        quizzesData = new QuizzesData(getActivity());
        quizzesData.open();

        results = (TextView)getActivity().findViewById(R.id.results);
        question = view.findViewById( R.id.textView2 );
        radioButton = view.findViewById( R.id.radioButton );
        radioButton2 = view.findViewById( R.id.radioButton2 );
        radioButton3 = view.findViewById( R.id.radioButton3 );

        question.setText("What is the state capital of: " + QuizFragmentContainer.the6Questions.get(questionNumber).getStateName());

        if (savedInstanceState == null) {
            answerChoices = new ArrayList<>();
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getCapitalCity());
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getSecondCity());
            answerChoices.add(QuizFragmentContainer.the6Questions.get(questionNumber).getThirdCity());
            Collections.shuffle(answerChoices);
        }

        radioButton.setText(answerChoices.get(0));
        radioButton2.setText(answerChoices.get(1));
        radioButton3.setText(answerChoices.get(2));

        RadioGroup radioGroup = (RadioGroup) view.findViewById( R.id.radioGroup );
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!changedOrientation || QuizFragmentContainer.userAnswers.get(questionNumber).equals("")) {
                    if (i == R.id.radioButton) {
                        QuizFragmentContainer.userAnswers.set(questionNumber, answerChoices.get(0));
                        //selectedAnswer = answerChoices.get(0);
                    } else if (i == R.id.radioButton2) {
                        QuizFragmentContainer.userAnswers.set(questionNumber, answerChoices.get(1));
                        //selectedAnswer = answerChoices.get(1);
                    } else if (i == R.id.radioButton3) {
                        QuizFragmentContainer.userAnswers.set(questionNumber, answerChoices.get(2));
                        //selectedAnswer = answerChoices.get(2);
                    }
                    updateScore();
                }
                changedOrientation = false;
            }
        });
    }
}
