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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;

public class QuizFragment extends Fragment {

    private static final String[] statesAndCapitals = {
            "Alabama", "Montgomery", "birmingham", "Auburn",
            "Alaska", "Juneau", "anchorage", "Fairbanks",
            "Arizona", "phonenic", "Tucson", "scottsdale",
            "arkanasas", "little rock", "hot springs", "bentonville",
            "californa", "sacramento", "los angeles", "san diego",
            "colorado", "denver", "boulder", "aspen"
    };

    private int[] answers;
    public static int[] rightAnswers = {1, 1, 1, 1, 1, 1};

    private int whichState;

    public QuizFragment() {

    }

    public static QuizFragment newInstance( int stateNum ) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt( "stateNum", stateNum);
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null ){
            whichState = getArguments().getInt( "stateNum" );
        }

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ){

        return inflater.inflate(R.layout.fragment_quiz, container, false );
    }

    public void updateScore(){
        int localScore = 0;
        for (int i=0;i<6;i++){
            if (QuizPagerAdapter.answers[i] == rightAnswers[i] ){
                localScore++;
            }
        }
        QuizPagerAdapter.score = localScore;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        //public void onActivityCreated(Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        if (answers == null){
             answers = new int[]{0, 0, 0, 0, 0, 0};
        }

        TextView question = view.findViewById( R.id.textView2 );
        RadioButton radioButton = view.findViewById( R.id.radioButton );
        RadioButton radioButton2 = view.findViewById( R.id.radioButton2 );
        RadioButton radioButton3 = view.findViewById( R.id.radioButton3 );

        question.setText( "What is the state caapital of: " + statesAndCapitals[ whichState * 4 ] );
        radioButton.setText( statesAndCapitals [ (whichState * 4) + 1] );
        radioButton2.setText( statesAndCapitals [ (whichState * 4) + 2] );
        radioButton3.setText( statesAndCapitals [ (whichState * 4) + 3] );

        updateScore();

        RadioGroup radioGroup = (RadioGroup) view.findViewById( R.id.radioGroup );
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateScore();

                if (i == 2131231209){
                    QuizPagerAdapter.answers[whichState] = 1;
                    if (answers != null) {
                        Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
                    }
                } else if (i == 2131231210){
                    QuizPagerAdapter.answers[whichState] = 2;
                    if (answers != null) {
                        Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
                    }
                } else if (i == 2131231211){
                    QuizPagerAdapter.answers[whichState] = 3;
                    if (answers != null) {
                        Log.d("Quizfragmnet", Arrays.toString(QuizPagerAdapter.answers));
                    }
                }
            }
        });




    }







}
