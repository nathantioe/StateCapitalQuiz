package edu.uga.cs.statecapitalsquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public final void quizButtonClick() {
        Fragment fragment = new QuizFragmentContainer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).addToBackStack("main screen" ).commit();
        Log.d( DEBUG_TAG, "onclick, quiz button" );

    }

    public final void reviewButtonClick() {
        Fragment fragment = new ReviewQuizzesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).addToBackStack("main screen" ).commit();
        Log.d( DEBUG_TAG, "onclick, review button" );

    }

    public final void activatePager() {
        ViewPager2 pager = findViewById( R.id.viewPager );
        //QuizPagerAdapter qAdapter = new QuizPagerAdapter( getFragmentManager(), getLifecycle() );
        //pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
        //pager.setAdapter( qAdapter );
    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button ) {
                //Fragment fragment = new QuizFragment();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace( R.id.fragmentContainerView, fragment).addToBackStack("main screen" ).commit();
                //
            }
        }
    }
}