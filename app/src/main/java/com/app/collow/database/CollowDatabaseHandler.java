package com.app.collow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.collow.beans.Searchbean;
import com.app.collow.utils.CommonMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harmis on 31/01/17.
 */

public class CollowDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "collow";

    // Contacts table name
    private static final String TABLE_SEARCH_COMMUNITY_HISTORY = "seachCommuntiyHistory";

    // Contacts Table Columns names
    private static final String KEY_SEARCHED_RAW_ID = "_search_histoty_id";
    private static final String KEY_SEARCHED_COMMUNITY_NAME = "searched_community_name";
    private static final String KEY_CURRENT_LOGGED_USERID = "current_logged_user_id";

    public CollowDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SEARCHED_COMMUNITY = "CREATE TABLE " + TABLE_SEARCH_COMMUNITY_HISTORY + "("
                + KEY_SEARCHED_RAW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CURRENT_LOGGED_USERID + " TEXT,"
                + KEY_SEARCHED_COMMUNITY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_SEARCHED_COMMUNITY);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_COMMUNITY_HISTORY);

        // Create tables again
        onCreate(db);
    }


    public void saveSearchedHistory(String searchedCommunity, String useriD) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SEARCHED_COMMUNITY_NAME, searchedCommunity);
        values.put(KEY_CURRENT_LOGGED_USERID, useriD);

        // Inserting Row
        long a = db.insert(TABLE_SEARCH_COMMUNITY_HISTORY, null, values);
        if (a != 1) {
            CommonMethods.displayLog("raw", "instered");
        }
        db.close();
    }


    public List<Searchbean> getSearchedCommunities(String userID) {
        List<Searchbean> searchbeanList = new ArrayList<Searchbean>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_COMMUNITY_HISTORY + " where " + KEY_CURRENT_LOGGED_USERID + "=" + userID + " ORDER BY " + KEY_SEARCHED_RAW_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Searchbean searchbean = new Searchbean();
                searchbean.setCommunityName(cursor.getString(2));


                // Adding contact to list
                searchbeanList.add(searchbean);


            } while (cursor.moveToNext());
        }

        // return contact list
        return searchbeanList;
    }

    public boolean isThisCommunityExist(String text, String loggedUserID) {
        // Select All Query
        if (text.contains("'")) {
            text = text.replaceAll("'", "''");
        }
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_COMMUNITY_HISTORY + " where " + KEY_SEARCHED_COMMUNITY_NAME + " = '" + text + "' and " + KEY_CURRENT_LOGGED_USERID + " = " + loggedUserID;
        //String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_COMMUNITY_HISTORY + " where "+KEY_SEARCHED_COMMUNITY_NAME+" = '"+"Ted''s Montana Grill, West 51st Street, New York, NY, United States" + "' and "+KEY_CURRENT_LOGGED_USERID +" = "+loggedUserID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            return true;

        } else {
            return false;
        }

        // looping through all rows and adding to list


        // return contact list
    }

    public void deleteSearchedHistory(String communityText) {
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.delete(TABLE_SEARCH_COMMUNITY_HISTORY, KEY_SEARCHED_COMMUNITY_NAME + " = ?",
                new String[]{String.valueOf(communityText)});

        db.close();
    }
}