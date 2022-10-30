package edu.uga.cs.statecapitalsquiz;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizPagerAdapter extends FragmentStateAdapter {

    //public static int[] answers = {0, 0, 0, 0, 0, 0};
    public static ArrayList<String> userAnswers = new ArrayList<>();
    public static int score = 0;
    public static boolean quizComplete = false;

    public QuizPagerAdapter(FragmentManager fragmentManager,
                            Lifecycle lifecycle){
        super( fragmentManager, lifecycle );
        resetQuiz();
    }

    public static void resetQuiz() {
        userAnswers.clear();
        for (int i = 0; i < 6; i++) {
            userAnswers.add("");
        }
        quizComplete = false;
        score = 0;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 6){

            Log.d("Quizpageradapter", Integer.toString(score) + "scoree");
            quizComplete = true;
            return QuizDoneFragment.newInstance(score);

        }

        return QuizFragment.newInstance( position );
    }



    @Override
    public int getItemCount() {
        return 7;
    }
}
