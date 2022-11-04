package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.QuizHolder> {

    private final Context context;
    private List<Quiz> values;

    /**
     *
     * @param context
     * @param pastQuizzes
     */
    public QuizRecyclerAdapter(Context context, List<Quiz> pastQuizzes) {
        this.context = context;
        this.values = pastQuizzes;
    }


    /**
     * We need to make sure that all CardViews have the same, full width, allowed by the parent view.
     * This is a bit tricky, and we must provide the parent reference (the second param of inflate)
     * Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public QuizRecyclerAdapter.QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.pastquiz, parent, false );
        return new QuizHolder( view );
    }

    /**
     * This method fills in the values of a holder to show a Quiz.
     * The position parameter indicates the position on the list of quizlist.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull QuizRecyclerAdapter.QuizHolder holder, int position) {
        Quiz quiz = values.get( position );

        holder.score.setText( "Score: " + String.valueOf(quiz.getResult()));
        holder.date.setText( "Date: " + quiz.getDate() );
    }

    /**
     *
     * @return size of variable values
     */
    @Override
    public int getItemCount() {
        if( values != null )
            return values.size();
        else
            return 0;
    }

    /**
     * The adapter must have a ViewHolder class to "hold" one item to show.
     */
    public static class QuizHolder extends RecyclerView.ViewHolder {

        TextView score;
        TextView date;

        public QuizHolder(@NonNull View itemView) {
            super(itemView);

            score = itemView.findViewById( R.id.textView3);
            date = itemView.findViewById( R.id.textView5);
        }
    }
}
