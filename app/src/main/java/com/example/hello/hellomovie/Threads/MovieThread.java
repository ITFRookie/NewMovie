package com.example.hello.hellomovie.Threads;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.hello.hellomovie.Beans.MovieCommentBean;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.Beans.MovieTimeBean;
import com.example.hello.hellomovie.Interfaces.MovieJsonCallBack;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.Providers.MovieContentProvider;
import com.example.hello.hellomovie.Utils.MovieGetJsonUtil;
import com.example.hello.hellomovie.Utils.MovieJsonAndBeanTran;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/12.
 */

public class MovieThread extends Thread {
    public Context context;
    public MovieJsonCallBack mCallBack;
    public String api_key;

    public MovieThread(Context context) throws PackageManager.NameNotFoundException {
        this.context = context;
        api_key = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("api_key");
    }

    public MovieJsonCallBack getmCallBack() {
        return mCallBack;
    }

    public void setmCallBack(MovieJsonCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void storeToDB(ArrayList<MovieInfoBean> list) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        if (list != null)
            contentResolver.delete(MovieContentProvider.CONTENT_MOVIE_INFOS,null,null);
            for (int i = 0; i < list.size(); i++) {
                values.put(MovieConstants.MOVIE_ID, list.get(i).getId());
                values.put(MovieConstants.MOVIE_COUNT, list.get(i).getVote_count());
                values.put(MovieConstants.MOVIE_TITLE, list.get(i).getTitle());
                values.put(MovieConstants.MOVIE_PHOTO, list.get(i).getPoster_path());
                values.put(MovieConstants.MOVIE_DATE, list.get(i).getRelease_date());
                values.put(MovieConstants.MOVIE_RATE, list.get(i).getVote_average());
                values.put(MovieConstants.MOVIE_CONTENT, list.get(i).getOverview());
                contentResolver.insert(MovieContentProvider.CONTENT_MOVIE_INFOS, values);
            }
    }

    public void storeAllToDB(ArrayList<MovieInfoBean> list) {
        String timeUrl;
        String previewUrl;
        String commentUrl;
        String timeData;
        String previewData;
        String commentData;
//        ArrayList<MovieTimeBean> timeList = new ArrayList<>();
//        ArrayList<MoviePreviewBean> previewList = new ArrayList<>();
//        ArrayList<MovieCommentBean> commentList = new ArrayList<>();
        int id;
        for (int i = 0; i < list.size(); i++) {
            id = list.get(i).getId();
            timeUrl = MovieConstants.HTTP_URL_TIME_PREFIX + id + MovieConstants.HTTP_URL_SUFFIX + api_key;
            previewUrl = MovieConstants.HTTP_URL_PREVIEW_PREFIX + id + "/videos" + MovieConstants.HTTP_URL_SUFFIX + api_key;
            commentUrl = MovieConstants.HTTP_URL_COMMENT_PREFIX + id + "/reviews" + MovieConstants.HTTP_URL_SUFFIX + api_key;

            //从网络上获取
            timeData = MovieGetJsonUtil.getJsonResult(timeUrl);
            previewData = MovieGetJsonUtil.getJsonResult(previewUrl);
            commentData = MovieGetJsonUtil.getJsonResult(commentUrl);

//            直接将json数据保存到数据库中 等使用的时候再解析
//
//            timeList = MovieJsonAndBeanTran.parseJsonFromNet(timeData);
//            previewList = MovieJsonAndBeanTran.parseJsonFromNet(previewData);
//            commentList = MovieJsonAndBeanTran.parseJsonFromNet(commentData);

            //存储到数据库中
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MovieConstants.MOVIE_LENGTH, timeData);
            values.put(MovieConstants.MOVIE_PRIVIEW, previewData);
            values.put(MovieConstants.MOVIE_COMMENT, commentData);
            //   contentResolver.insert(MovieContentProvider.CONTENT_MOVIE_INFOS, values);
            contentResolver.update(MovieContentProvider.CONTENT_MOVIE_INFOS, values, "mID=?", new String[]{String.valueOf(id)});

        }

    }

}
