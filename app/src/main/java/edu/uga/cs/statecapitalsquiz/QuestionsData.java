package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class facilitates in retrieving questions from the db.
 */
public class QuestionsData {

    public static final String DEBUG_TAG = "QuestionsData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private static final String[] allColumns = {
            DBHelper.QUESTIONS_COLUMN_ID,
            DBHelper.QUESTIONS_COLUMN_STATENAME,
            DBHelper.QUESTIONS_COLUMN_CAPITALCITY,
            DBHelper.QUESTIONS_COLUMN_SECONDCITY,
            DBHelper.QUESTIONS_COLUMN_THIRDCITY
    };

    /**
     * Assigns the dbHelper variable, so that we can use it later. Makes sure that there is
     * only one instance of the helper.
     *
     * @param context
     */
    public QuestionsData( Context context ) {
        this.dbHelper = DBHelper.getInstance( context );
    }

    /**
     * Open the database
     */
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuestionsData: db open" );
    }

    /**
     * Close the database
     */
    public void close() {
        if( dbHelper != null ) {
            dbHelper.close();
            Log.d(DEBUG_TAG, "QuestionsData: db closed");
        }
    }

    /**
     * Checks if the database is empty
     *
     * @return Boolean
     */
    public boolean isEmpty() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_QUESTIONS, null);
        if(cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                return false;
            }
            cursor.close();
        }
        return true;
    }

    /**
     * Checks if the database is open
     *
     * @return Boolean
     */
    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    /**
     * Retrieve all questions and return them as a List.
     * This is how we restore persistent objects stored as rows in the job leads table in the database.
     * For each retrieved row, we create a new Question (Java POJO object) instance and add it to the list.
     *
     * @return ArrayList<Question>
     */
    public ArrayList<Question> retrieveAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( DBHelper.TABLE_QUESTIONS, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_ID);
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_STATENAME);
                        String stateName = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_CAPITALCITY);
                        String capitalCity = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex(DBHelper.QUESTIONS_COLUMN_SECONDCITY);
                        String secondCity = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex(DBHelper.QUESTIONS_COLUMN_THIRDCITY);
                        String thirdCity = cursor.getString( columnIndex );

                        // create a new Question object and set its state to the retrieved values
                        Question question = new Question(stateName, capitalCity, secondCity, thirdCity);
                        question.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        questions.add(question);
                        Log.d(DEBUG_TAG, "Retrieved Question: " + question);
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
        // return a list of retrieved questions
        return questions;
    }

    /**
     * Shuffles the questions and returns six of them, cannot do duplicates.
     *
     * @return ArrayList<Question>
     */
    public ArrayList<Question> generate6QuizQuestions() {
        ArrayList<Question> allQuestions = retrieveAllQuestions();
        Collections.shuffle(allQuestions);
        ArrayList<Question> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(allQuestions.get(i));
        }
        return list;
    }

    /**
     * Insert new question into the databse
     *
     * @param question
     */
    public void storeQuestion(Question question) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.QUESTIONS_COLUMN_STATENAME, question.getStateName());
        values.put(DBHelper.QUESTIONS_COLUMN_CAPITALCITY, question.getCapitalCity());
        values.put(DBHelper.QUESTIONS_COLUMN_SECONDCITY, question.getSecondCity());
        values.put(DBHelper.QUESTIONS_COLUMN_THIRDCITY, question.getThirdCity());

        db.insert(DBHelper.TABLE_QUESTIONS, null, values );
    }


}
