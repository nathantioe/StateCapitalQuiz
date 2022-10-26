package edu.uga.cs.statecapitalsquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //QuizzesData quizzesData = null;

    // Note: Feel free to ignore/delete comments. I just wrote it to test creating the databases

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //quizzesData = new QuizzesData(getApplicationContext());
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if( quizzesData != null ) {
//            quizzesData.open();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (quizzesData != null) {
//            quizzesData.close();
//        }
//    }
}