package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuestionsDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "QuestionsDBHelper";

    private static final String DB_NAME = "questions.db";
    private static final int DB_VERSION = 1;

    // Define all names (strings) for table and column names.
    // This will be useful if we want to change these names later.
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_ID = "id";
    public static final String QUESTIONS_COLUMN_STATENAME = "stateName";
    public static final String QUESTIONS_COLUMN_CAPITALCITY = "capitalCity";
    public static final String QUESTIONS_COLUMN_SECONDCITY = "secondCity";
    public static final String QUESTIONS_COLUMN_THIRDCITY = "thirdCity";

    // This is a reference to the only instance for the helper.
    private static QuestionsDBHelper helperInstance;

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

    // Note that the constructor is private!
    // So, it can be called only from
    // this class, in the getInstance method.
    private QuestionsDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread can executes this method, at a time.
    public static synchronized QuestionsDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new QuestionsDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( CREATE_QUESTIONS );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_QUESTIONS );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " upgraded" );
    }
}
