package com.example.hello.hellomovie.Providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.MovieDBOpenHelper;

/**
 * Created by Hello on 2016/12/12.
 */

public class MovieContentProvider extends ContentProvider {
    public static final Uri CONTENT_MOVIE_COLL = Uri.parse("content://" + MovieConstants.AUTHER_STR + "/" + MovieConstants.TABLE_COLLECTION_NAME);
    public static final Uri CONTENT_MOVIE_INFOS = Uri.parse("content://" + MovieConstants.AUTHER_STR + "/" + MovieConstants.TABLE_ITEM_NAME);
    private SQLiteDatabase db;
    private Context mContext;
    public static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int INFOS = 1;
    public static final int DEFAULT = 2;
    public static final int COMMENT = 3;
    public static final int COLLECTION = 4;

    static {
        matcher.addURI(MovieConstants.AUTHER_STR, "/infos", MovieConstants.MATCH_CODE_INFO);
        matcher.addURI(MovieConstants.AUTHER_STR, "/dflist", MovieConstants.MATCH_CODE_DEFAULT);
        matcher.addURI(MovieConstants.AUTHER_STR, "/comments", MovieConstants.MATCH_CODE_COMMENTS);
        matcher.addURI(MovieConstants.AUTHER_STR, "/collections", MovieConstants.MATCH_CODE_COLLECTIONS);
    }


    @Override
    public boolean onCreate() {
        mContext = getContext();
        db = new MovieDBOpenHelper(getContext()).getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        String name = getTableName(uri);
        if (name == null) {
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        Cursor cursor = db.query(name, strings, s, strings1, null, null, s1, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String name = getTableName(uri);
        if (name == null) {
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        db.insert(name, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        if (s == null && strings == null) {
            //每次刷新删除数据库原来的数据
            db.execSQL("delete from infos");
            db.execSQL("update sqlite_sequence SET seq = 0 where name ='infos';");
            return 1;

        }
        String name = getTableName(uri);
        if (name == null) {
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        int count = db.delete(name, s, strings);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        String name = getTableName(uri);
        if (name == null) {
            throw new IllegalArgumentException("Unsupported URI" + uri);
        }
        int row = db.update(name, contentValues, s, strings);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String name = null;
        switch (matcher.match(uri)) {
            case MovieConstants.MATCH_CODE_INFO:
                name = MovieConstants.TABLE_ITEM_NAME;
                break;
            case MovieConstants.MATCH_CODE_COLLECTIONS:
                name = MovieConstants.TABLE_COLLECTION_NAME;
                break;
            case MovieConstants.MATCH_CODE_COMMENTS:
                name = MovieConstants.TABLE_COMMENT_NAME;
                break;
            case MovieConstants.MATCH_CODE_DEFAULT:
                name = MovieConstants.TABLE_DEFAULT_NAME;
                break;
        }
        return name;
    }
}
