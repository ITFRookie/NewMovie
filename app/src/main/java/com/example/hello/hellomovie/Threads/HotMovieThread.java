package com.example.hello.hellomovie.Threads;

import android.content.Context;
import android.content.pm.PackageManager;

import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Utils.MovieGetJsonUtil;
import com.example.hello.hellomovie.Utils.MovieJsonAndBeanTran;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/12.
 */

public class HotMovieThread extends MovieThread {
    public HotMovieThread(Context context) throws PackageManager.NameNotFoundException {
        super(context);
    }

    @Override
    public void run() {
        String url = "http://api.themoviedb.org/3/movie/popular?language=zh&api_key=" + api_key;
        String data = MovieGetJsonUtil.getJsonResult(url);//得到返回的json
        ArrayList<MovieInfoBean> list = MovieJsonAndBeanTran.parseJsonFromNet(data);
        storeToDB(list);
      //  storeAllToDB(list);
        mCallBack.onMovieJsonGot(list);//回调借口的监听方法
    }
}
