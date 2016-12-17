package com.example.hello.hellomovie.Interfaces;

import com.example.hello.hellomovie.Beans.MovieInfoBean;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/12.
 */

public interface MovieJsonCallBack {
public void onMovieJsonGot(ArrayList<MovieInfoBean> list);
}
