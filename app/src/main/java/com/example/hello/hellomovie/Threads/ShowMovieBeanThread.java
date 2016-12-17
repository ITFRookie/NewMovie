package com.example.hello.hellomovie.Threads;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;

import com.example.hello.hellomovie.Activitys.ShowInfoActivity;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Interfaces.MovieInfoGetCallBack;
import com.example.hello.hellomovie.Interfaces.MovieJsonCallBack;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.Providers.MovieContentProvider;
import com.example.hello.hellomovie.Utils.MovieGetJsonUtil;
import com.example.hello.hellomovie.Utils.MovieJsonAndBeanTran;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/16.
 */

public class ShowMovieBeanThread extends Thread {

    private Context context;
    private String movieID;
    private String api_key;
    ArrayList listTime = null;
    ArrayList listPreview = null;
    ArrayList listComment = null;
    private MovieInfoGetCallBack mCallBack;

    public ShowMovieBeanThread(Context context, String movieID) throws PackageManager.NameNotFoundException {
        this.context = context;
        this.movieID = movieID;
        api_key = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("api_key");
    }


    @Override
    public void run() {
        String timeUrl;
        String previewUrl;
        String commentUrl;
        String timeData;
        String previewData;
        String commentData;
        MovieInfoBean infoBean;
        int id;
        Cursor cursor = context.getContentResolver().query(MovieContentProvider.CONTENT_MOVIE_INFOS, null, "mID=?", new String[]{movieID}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            infoBean = new MovieInfoBean();
            infoBean.setId(cursor.getInt(cursor.getColumnIndex(MovieConstants.MOVIE_ID)));
            infoBean.setTitle(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_TITLE)));
            infoBean.setVote_average(cursor.getFloat(cursor.getColumnIndex(MovieConstants.MOVIE_RATE)));
            infoBean.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_DATE)));
            infoBean.setOverview(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_CONTENT)));
            infoBean.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_PHOTO)));
            infoBean.setFlag(cursor.getString(cursor.getColumnIndex("mFlag")));
            if (cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_LENGTH)) == null) {
                //如果数据库中没有数据则从网络获取json格式的length preview comment
                id = infoBean.getId();
                timeUrl = MovieConstants.HTTP_URL_TIME_PREFIX + id + MovieConstants.HTTP_URL_SUFFIX + api_key;
                previewUrl = MovieConstants.HTTP_URL_PREVIEW_PREFIX + id + "/videos" + MovieConstants.HTTP_URL_SUFFIX + api_key;
                commentUrl = MovieConstants.HTTP_URL_COMMENT_PREFIX + id + "/reviews" + MovieConstants.HTTP_URL_SUFFIX + api_key;
                timeData = MovieGetJsonUtil.getJsonResult(timeUrl);
                previewData = MovieGetJsonUtil.getJsonResult(previewUrl);
                commentData = MovieGetJsonUtil.getJsonResult(commentUrl);

                //存储到contentprovider中
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MovieConstants.MOVIE_LENGTH, timeData);
                values.put(MovieConstants.MOVIE_PRIVIEW, previewData);
                values.put(MovieConstants.MOVIE_COMMENT, commentData);
                contentResolver.update(MovieContentProvider.CONTENT_MOVIE_INFOS, values, "mID=?", new String[]{String.valueOf(id)});
                //处理解析到的json数据
                listTime = MovieJsonAndBeanTran.parseJsonToLength(timeData);
                listPreview = MovieJsonAndBeanTran.parseJsonToPreview(previewData);
                listComment = MovieJsonAndBeanTran.parseJsonToComment(commentData);


            }
            else {
                // 若数据库中有数据
                infoBean.setLength(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_LENGTH)));
                infoBean.setPreview(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_PRIVIEW)));
                infoBean.setComment(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_COMMENT)));

                //解析读取到的数据到list集合中
                listTime = MovieJsonAndBeanTran.parseJsonToLength(infoBean.getLength());
                listPreview = MovieJsonAndBeanTran.parseJsonToPreview(infoBean.getPreview());
                listComment = MovieJsonAndBeanTran.parseJsonToComment(infoBean.getComment());

            }
            //回调
            mCallBack.onMovieInfoGot(listTime, listPreview, listComment,infoBean);
        }
    }

    public MovieInfoGetCallBack getmCallBack() {
        return mCallBack;
    }

    public void setmCallBack(MovieInfoGetCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }
}
