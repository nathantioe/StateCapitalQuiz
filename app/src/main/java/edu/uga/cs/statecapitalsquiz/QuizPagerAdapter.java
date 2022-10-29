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

    public QuizPagerAdapter(FragmentManager fragmentManager,
                            Lifecycle lifecycle){
        super( fragmentManager, lifecycle );
        resetUserAnswers();
    }

    public static void resetUserAnswers() {
        userAnswers.clear();
        for (int i = 0; i < 6; i++) {
            userAnswers.add("");
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 6){

            Log.d("Quizpageradapter", Integer.toString(score) + "scoree");
            return QuizDoneFragment.newInstance(score);

        }

        return QuizFragment.newInstance( position );
    }



    @Override
    public int getItemCount() {
        return 7;
    }
}
