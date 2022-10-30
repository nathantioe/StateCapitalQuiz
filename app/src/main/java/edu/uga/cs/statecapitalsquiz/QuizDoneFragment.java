package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizDoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizDoneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int score;
    private QuizzesData quizzesData;
    private TextView results;
    private String time;

    public QuizDoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param score Parameter 1
     * @return A new instance of fragment quizDoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizDoneFragment newInstance(int score) {
        QuizDoneFragment fragment = new QuizDoneFragment();
        Bundle args = new Bundle();
        args.putInt("score", score);
        Log.d("quizdonefragmentcons", Integer.toString(score));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            score = getArguments().getInt("score");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_done, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {

        results = view.findViewById( R.id.results );
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(dtf.format(now));
//        String time = dtf.format(now);

        //results.setText("Score: " + QuizPagerAdapter.score + "/6 " + "Time: " + time);

        //TODO: Update the quiz data in database
        quizzesData = new QuizzesData(getActivity());
        quizzesData.open();

        //new QuizDBUpdater().execute(time);

        Button homeButton = view.findViewById(R.id.button3);
//        Button newQuiz = view.findViewById( R.id.button4 );
//        Button reviewQuiz = view.findViewById( R.id.button3 );

//        newQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                QuizFragmentContainer.setUpForNewQuiz();
//                //QuizPagerAdapter.score = 0;
//                //QuizPagerAdapter.answers = new int[]{0, 0, 0, 0, 0, 0};
//                QuizPagerAdapter.resetQuiz();
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.quizButtonClick();
//            }
//        });

//        reviewQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //QuizPagerAdapter.score = 0;
//                //QuizPagerAdapter.answers = new int[]{0, 0, 0, 0, 0, 0};
//                QuizPagerAdapter.resetQuiz();
////                MainActivity mainActivity = (MainActivity) getActivity();
////                mainActivity.reviewButtonClick();
//                getActivity().onBackPressed();
//
//            }
//        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizPagerAdapter.resetQuiz();
                getActivity().onBackPressed();
            }
        });
    }

    public class QuizDBUpdater extends AsyncTask<String, Void> {
        @Override
        protected Void doInBackground(String... time) {
            quizzesData.updateQuizByID(QuizFragmentContainer.currentQuizID, time[0], QuizPagerAdapter.score, 6);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }

        // store quiz in db
    }

    @Override
    public void onResume() {
        super.onResume();
        if(quizzesData != null && !quizzesData.isDBOpen()) {
            quizzesData.open();
        }
        if (QuizPagerAdapter.quizComplete) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            time = dtf.format(now);
            results.setText("Score: " + QuizPagerAdapter.score + "/6 " + "Time: " + time);

            new QuizDBUpdater().execute(time);
            // reset the quiz so that the time will not change if
            // the user decides to review quiz and press back button
        } else {
            results.setText("Score: " + QuizPagerAdapter.score + "/6 " + "Time: " + time);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(quizzesData != null) {
            quizzesData.close();
        }
    }

}