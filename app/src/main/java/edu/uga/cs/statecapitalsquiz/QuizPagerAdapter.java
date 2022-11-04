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

    /**
     *
     * @param fragmentManager
     * @param lifecycle
     */
    public QuizPagerAdapter(FragmentManager fragmentManager,
                            Lifecycle lifecycle){
        super( fragmentManager, lifecycle );
    }

    /**
     * Creates the QuizDoneFragment at position 6 in the pager
     *
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 6){
            return QuizDoneFragment.newInstance();

        }

        return QuizFragment.newInstance( position );
    }


    /**
     *
     * @return no of items in the pager
     */
    @Override
    public int getItemCount() {
        return 7;
    }
}
