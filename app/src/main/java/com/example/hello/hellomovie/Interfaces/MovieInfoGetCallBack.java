package com.example.hello.hellomovie.Interfaces;

import com.example.hello.hellomovie.Beans.MovieInfoBean;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/16.
 * 当点击详情页面获取到剩余的时长 评论  预告片信息的回调
 */

public interface MovieInfoGetCallBack {

    public void onMovieInfoGot(ArrayList list1, ArrayList list2, ArrayList list3, MovieInfoBean bean);


}
