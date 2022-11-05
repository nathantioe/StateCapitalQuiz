package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class that defines constants that help manage the database. Referenced often in
 * QuestionsData.java and QuizzesData.java
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "QuestionsDBHelper";

    private static final String DB_NAME = "data.db";
    private static final int DB_VERSION = 1;

    // Questions
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_ID = "id";
    public static final String QUESTIONS_COLUMN_STATENAME = "stateName";
    public static final String QUESTIONS_COLUMN_CAPITALCITY = "capitalCity";
    public static final String QUESTIONS_COLUMN_SECONDCITY = "secondCity";
    public static final String QUESTIONS_COLUMN_THIRDCITY = "thirdCity";

    // Quizzes
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_ID = "id";
    public static final String QUIZZES_COLUMN_DATE = "date";
    public static final String QUIZZES_COLUMN_QUESTION1 = "question_1";
    public static final String QUIZZES_COLUMN_QUESTION2 = "question_2";
    public static final String QUIZZES_COLUMN_QUESTION3 = "question_3";
    public static final String QUIZZES_COLUMN_QUESTION4 = "question_4";
    public static final String QUIZZES_COLUMN_QUESTION5 = "question_5";
    public static final String QUIZZES_COLUMN_QUESTION6 = "question_6";
    public static final String QUIZZES_COLUMN_RESULT = "result";
    public static final String QUIZZES_COLUMN_QUESTIONSANSWERED = "questions_answered";

    // This is a reference to the only instance for the helper.
    private static DBHelper helperInstance;

    // A Create table SQL statement to create a table for job leads.
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_QUESTIONS =
            "create table " + TABLE_QUESTIONS + " ("
                    + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTIONS_COLUMN_STATENAME + " TEXT, "
                    + QUESTIONS_COLUMN_CAPITALCITY + " TEXT, "
                    + QUESTIONS_COLUMN_SECONDCITY + " TEXT, "
                    + QUESTIONS_COLUMN_THIRDCITY + " TEXT"
                    + ")";

    private static final String CREATE_QUIZZES =
            "create table " + TABLE_QUIZZES + " ("
                    + QUIZZES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZZES_COLUMN_DATE + " TEXT, "
                    + QUIZZES_COLUMN_QUESTION1 + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTION2 + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTION3 + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTION4 + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTION5 + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTION6 + " INTEGER, "
                    + QUIZZES_COLUMN_RESULT + " INTEGER, "
                    + QUIZZES_COLUMN_QUESTIONSANSWERED + " INTEGER"
                    + ")";

    private DBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread can executes this method, at a time.
    public static synchronized DBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new DBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL(CREATE_QUESTIONS);
        db.execSQL(CREATE_QUIZZES);
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_QUESTIONS );
        db.execSQL( "drop table if exists " + TABLE_QUIZZES );
        onCreate(db);
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " upgraded" );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZZES + " upgraded" );
    }
}
