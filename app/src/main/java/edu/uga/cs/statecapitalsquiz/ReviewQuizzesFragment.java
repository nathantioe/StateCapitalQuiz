package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewQuizzesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewQuizzesFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuizRecyclerAdapter recyclerAdapter;
    private ArrayList<Quiz> quizList = new ArrayList<>();
    private QuizzesData quizzesData;

    public ReviewQuizzesFragment() {
    }

    /**
     *
     * @return Fragment
     */
    public static ReviewQuizzesFragment newInstance() {
        ReviewQuizzesFragment fragment = new ReviewQuizzesFragment();
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_quizzes, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        recyclerView = getView().findViewById( R.id.recyclerview );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );

        quizzesData = new QuizzesData( getActivity() );
        quizzesData.open();

        new QuizDBReader().execute();
    }

    /** New functions added by Nathan **/

    /**
     * AsyncTask to retrieve all quizzes from db
     */
    public class QuizDBReader extends AsyncTask<Void, List<Quiz>> {

        @Override
        protected List<Quiz> doInBackground( Void... params ) {
            List<Quiz> allQuizzes = quizzesData.retrieveAllQuizzes();
            return allQuizzes;
        }

        /**
         * after retrieving quizzes, add them to the quiz list
         *
         * @param allQuizzes
         */
        @Override
        protected void onPostExecute(List<Quiz> allQuizzes) {
            quizList.addAll(allQuizzes);
            recyclerAdapter = new QuizRecyclerAdapter( getActivity(), quizList );
            recyclerView.setAdapter( recyclerAdapter );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (quizzesData != null) {
            quizzesData.open();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (quizzesData != null) {
            quizzesData.close();
        }
    }
}