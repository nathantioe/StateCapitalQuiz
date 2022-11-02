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

    public QuizPagerAdapter(FragmentManager fragmentManager,
                            Lifecycle lifecycle){
        super( fragmentManager, lifecycle );
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 6){
            return QuizDoneFragment.newInstance();

        }

        return QuizFragment.newInstance( position );
    }



    @Override
    public int getItemCount() {
        return 7;
    }
}
