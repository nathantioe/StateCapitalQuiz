package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is facilitates storing and restoring quizzes stored.
 */
public class QuizzesData {

    public static final String DEBUG_TAG = "QuizzesData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    private static final String[] allColumns = {
            DBHelper.QUIZZES_COLUMN_ID,
            DBHelper.QUIZZES_COLUMN_DATE,
            DBHelper.QUIZZES_COLUMN_QUESTION1,
            DBHelper.QUIZZES_COLUMN_QUESTION2,
            DBHelper.QUIZZES_COLUMN_QUESTION3,
            DBHelper.QUIZZES_COLUMN_QUESTION4,
            DBHelper.QUIZZES_COLUMN_QUESTION5,
            DBHelper.QUIZZES_COLUMN_QUESTION6,
            DBHelper.QUIZZES_COLUMN_RESULT,
            DBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED
    };

    public QuizzesData( Context context ) {
        this.dbHelper = DBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuizzesData: db open" );
    }

    // Close the database
    public void close() {
        if( dbHelper != null ) {
            dbHelper.close();
            Log.d(DEBUG_TAG, "QuizzesData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all quizzes and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new Quiz (Java POJO object) instance and add it to the list.
    public List<Quiz> retrieveAllQuizzes() {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( DBHelper.TABLE_QUIZZES, allColumns,
                    null, null, null, null, null );

            // collect all quizzes into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 10) {

                        // get all attribute values of this quiz
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_ID);
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_DATE );
                        String date = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION1 );
                        long q1 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION2 );
                        long q2 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION3 );
                        long q3 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION4 );
                        long q4 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION5 );
                        long q5 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTION6 );
                        long q6 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_RESULT );
                        long result = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED );
                        long questionsAnswered = cursor.getLong( columnIndex );

                        // create a new quiz object and set its state to the retrieved values
                        Quiz quiz = new Quiz(date, q1, q2, q3, q4, q5, q6, result, questionsAnswered);
                        quiz.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        quizzes.add(quiz);
                        Log.d(DEBUG_TAG, "Retrieved Quiz: " + quiz);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved quiz
        return quizzes;
    }

    // Store a new job lead in the database.
    public Quiz storeQuiz( Quiz quiz) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Quiz argument.
        // This is how we are providing persistence to a Quiz (Java object) instance
        // by storing it as a new row in the database table representing quizzes.
        ContentValues values = new ContentValues();
        values.put( DBHelper.QUIZZES_COLUMN_DATE, quiz.getDate());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION1, quiz.getQuestion1());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION2, quiz.getQuestion2());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION3, quiz.getQuestion3());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION4, quiz.getQuestion4());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION5, quiz.getQuestion5());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTION6, quiz.getQuestion6());
        values.put( DBHelper.QUIZZES_COLUMN_RESULT, quiz.getResult());
        values.put( DBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED, quiz.getQuestionsAnswered());

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( DBHelper.TABLE_QUIZZES, null, values );

        // store the id (the primary key) in the quiz instance, as it is now persistent
        quiz.setId( id );

        Log.d( DEBUG_TAG, "Stored new quiz with id: " + String.valueOf( quiz.getId() ) );

        return quiz;
    }

    public void updateQuizByID(long id, String date, long result, long questionsAnswered) {
        String update = "UPDATE " + DBHelper.TABLE_QUIZZES
                + " SET " + DBHelper.QUIZZES_COLUMN_DATE + "='" + date + "'"
                + ", " + DBHelper.QUIZZES_COLUMN_RESULT + "=" + result
                + ", " + DBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED + "=" + questionsAnswered
                + " WHERE " + DBHelper.QUIZZES_COLUMN_ID + "=" + id;
        db.execSQL(update);
    }


}
