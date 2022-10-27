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
 * This class is facilitates storing and restoring job leads stored.
 */
public class QuizzesData {

    public static final String DEBUG_TAG = "JobLeadsData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper quizzesDbHelper;
    private static final String[] allColumns = {
            QuizzesDBHelper.QUIZZES_COLUMN_ID,
            QuizzesDBHelper.QUIZZES_COLUMN_DATE,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION1,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION2,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION3,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION4,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION5,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTION6,
            QuizzesDBHelper.QUIZZES_COLUMN_RESULT,
            QuizzesDBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED
    };

    public QuizzesData( Context context ) {
        this.quizzesDbHelper = QuizzesDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = quizzesDbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "JobLeadsData: db open" );
    }

    // Close the database
    public void close() {
        if( quizzesDbHelper != null ) {
            quizzesDbHelper.close();
            Log.d(DEBUG_TAG, "JobLeadsData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Quiz> retrieveAllJobLeads() {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizzesDBHelper.TABLE_QUIZZES, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 10) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_ID);
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_DATE );
                        String date = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION1 );
                        long q1 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION2 );
                        long q2 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION3 );
                        long q3 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION4 );
                        long q4 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION5 );
                        long q5 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION6 );
                        long q6 = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_RESULT );
                        long result = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizzesDBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED );
                        long questionsAnswered = cursor.getLong( columnIndex );

                        // create a new JobLead object and set its state to the retrieved values
                        Quiz quiz = new Quiz(date, q1, q2, q3, q4, q5, q6, result, questionsAnswered);
                        quiz.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        quizzes.add(quiz);
                        Log.d(DEBUG_TAG, "Retrieved JobLead: " + quiz);
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
        // return a list of retrieved job leads
        return quizzes;
    }

    // Store a new job lead in the database.
    public Quiz storeJobLead( Quiz quiz) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the JobLead argument.
        // This is how we are providing persistence to a JobLead (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_DATE, quiz.getDate());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION1, quiz.getQuestion1());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION2, quiz.getQuestion2());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION3, quiz.getQuestion3());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION4, quiz.getQuestion4());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION5, quiz.getQuestion5());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTION6, quiz.getQuestion6());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_RESULT, quiz.getResult());
        values.put( QuizzesDBHelper.QUIZZES_COLUMN_QUESTIONSANSWERED, quiz.getQuestionsAnswered());

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( QuizzesDBHelper.TABLE_QUIZZES, null, values );

        // store the id (the primary key) in the JobLead instance, as it is now persistent
        quiz.setId( id );

        Log.d( DEBUG_TAG, "Stored new job lead with id: " + String.valueOf( quiz.getId() ) );

        return quiz;
    }


}
