package com.example.hello.hellomovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Hello on 2016/12/12.
 */

public class MovieDBOpenHelper extends SQLiteOpenHelper {

      final String CREATE_TABLE_ITEM_SQL ="CREATE TABLE IF NOT EXISTS "+MovieConstants.TABLE_ITEM_NAME
              +"(_id INTEGER PRIMARY KEY AUTOINCREMENT ,mID TEXT NOT NULL,mtitle TEXT NOT NULL," +
              "mphoto TEXT NOT NULL,mdate TEXT NOT NULL,mrate FLOAT NOT NULL,mcontent TEXT NOT NULL,mpriview TEXT," +
              "mlength TEXT,mComment TEXT,mFlag TEXT DEFAULT 0,mCount INT);";
      final String CREATE_TABLE_COLL_SQL ="CREATE TABLE IF NOT EXISTS "+MovieConstants.TABLE_COLLECTION_NAME
              +"(_id INTEGER PRIMARY KEY AUTOINCREMENT ,mID TEXT NOT NULL,mtitle TEXT NOT NULL," +
              "mphoto TEXT NOT NULL,mdate TEXT NOT NULL,mrate FLOAT NOT NULL,mcontent TEXT NOT NULL,mpriview TEXT," +
              "mlength TEXT,mComment TEXT,mFlag TEXT DEFAULT 0,mCount INT);";
//    final String CREATE_TABLE_DEFAULT_SQL="CREATE TABLE IF NOT EXISTS "+MovieConstants.TABLE_DEFAULT_NAME
//            +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,mID TEXT NOT NULL,FOREIGN KEY(mID) REFERENCES infos(mID));";
//    final String CREATE_TABLE_COLLECTION_SQL="CREATE TABLE IF NOT EXISTS "+MovieConstants.TABLE_COLLECTION_NAME
//            +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,mID TEXT NOT NULL,FOREIGN KEY(mID) REFERENCES infos(mID));";
  //  final String CREATE_TABLE_COMMENT_SQL="CREATE TABLE IF NOT EXISTS "+MovieConstants.TABLE_COMMENT_NAME
    //        +"(_id INTEGER PRIMARY KEY AUTOINCREMENT ,mID TEXT NOT NULL,mComment TEXT NOT NULL,FOREIGN KEY(mID) REFERENCES infos(mID));";
    public MovieDBOpenHelper(Context context) {
        super(context,MovieConstants.DB_NAME, null, MovieConstants.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("SuccessFlag","start");
        //第一次访问时创建表
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_COLL_SQL);
       // sqLiteDatabase.execSQL(CREATE_TABLE_DEFAULT_SQL);
        //sqLiteDatabase.execSQL(CREATE_TABLE_COLLECTION_SQL);
       // sqLiteDatabase.execSQL(CREATE_TABLE_COMMENT_SQL);
        Log.e("SuccessFlag","successful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
