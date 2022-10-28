package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragmentContainer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragmentContainer extends Fragment {


    ViewPager2 pager;
    QuizPagerAdapter qAdapter;
    private static ArrayList<Question> questions = new ArrayList<>();

    public static int[] rightAnswers = {0,0,0,0,0,0};
    public int hej = 1;
    boolean getsRotated = false;

    public static final String[] statesAndCapitals = {
            "Alabama", "Montgomery", "birmingham", "Auburn",
            "Alaska", "Juneau", "anchorage", "Fairbanks",
            "Arizona", "phonenic", "Tucson", "scottsdale",
            "arkanasas", "little rock", "hot springs", "bentonville",
            "californa", "sacramento", "los angeles", "san diego",
            "colorado", "denver", "boulder", "aspen"
    };



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragmentContainer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragmentContainer.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragmentContainer newInstance(String param1, String param2) {
        QuizFragmentContainer fragment = new QuizFragmentContainer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (savedInstanceState != null){
            rightAnswers = savedInstanceState.getIntArray("rightAnswers");
            //questions = savedInstanceState.getParcelableArrayList("questions");
            getsRotated = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {

        //here the database should give a list question on the same structure as below,
        // this code will then scrable the answers and keep track of the score.

        if (getsRotated == false){

            questions.add(new Question("Alabama", "Montgomery", "birmingham", "Auburn"));
            questions.add(new Question("Alaska", "Juneau", "anchorage", "Fairbanks"));
            questions.add(new Question("Arizona", "phonenic", "Tucson", "scottsdale"));
            questions.add(new Question("arkanasas", "little rock", "hot springs", "bentonville"));
            questions.add(new Question("californa", "sacramento", "los angeles", "san diego"));
            questions.add(new Question("colorado", "denver", "boulder", "aspen"));


            for (int i=0;i<6;i++){
                int random_int = (int)Math.floor(Math.random()*(6));
                Log.d("random int", Integer.toString(random_int));
                if (random_int == 0){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getCapitalCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getSecondCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getThirdCity();
                    rightAnswers[i] = 1;
                } else if (random_int == 1){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getCapitalCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getThirdCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getSecondCity();
                    rightAnswers[i] = 1;
                } else if (random_int == 2){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getSecondCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getCapitalCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getThirdCity();
                    rightAnswers[i] = 2;
                } else if (random_int == 3){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getThirdCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getCapitalCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getSecondCity();
                    rightAnswers[i] = 2;
                } else if (random_int == 4){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getSecondCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getThirdCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getCapitalCity();
                    rightAnswers[i] = 3;
                } else if (random_int == 5){
                    statesAndCapitals[i*4] = questions.get(i).getStateName();
                    statesAndCapitals[i*4 + 1] = questions.get(i).getThirdCity();
                    statesAndCapitals[i*4 + 2] = questions.get(i).getSecondCity();
                    statesAndCapitals[i*4 + 3] = questions.get(i).getCapitalCity();
                    rightAnswers[i] = 3;
                }

            }
        }


        Log.d("states and capitals", Arrays.toString(statesAndCapitals));
        Log.d("right answers", Arrays.toString(rightAnswers));


        pager = view.findViewById( R.id.viewPager );
        pager.setOffscreenPageLimit(8);
        qAdapter = new QuizPagerAdapter( getChildFragmentManager(), getLifecycle() );
        pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
        pager.setAdapter( qAdapter );
        //pager.setUserInputEnabled(false);




        //pager.setUserInputEnabled(false);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putIntArray("rightAnswers", rightAnswers);
        //savedInstanceState.putParcelableArrayList("questions", (ArrayList<? extends Parcelable>) questions);

        // etc.
    }


}