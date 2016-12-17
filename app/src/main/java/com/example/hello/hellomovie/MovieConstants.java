package com.example.hello.hellomovie;

/**
 * Created by Hello on 2016/12/12
 * <p>
 * 常量类  记录该应用所要使用的常量.
 */

public class MovieConstants {
    public static final String HTTP_URL_PHOTO_PREFIX="https://image.tmdb.org/t/p/w185";//照片网址前缀
    public static final String HTTP_URL_TIME_PREFIX="http://api.themoviedb.org/3/movie/";//电影时长网址前缀
    public static final String HTTP_URL_PREVIEW_PREFIX="http://api.themoviedb.org/3/movie/";//预告片列表网址前缀
    public static final String HTTP_URL_COMMENT_PREFIX="http://api.themoviedb.org/3/movie/";//评论列表网址前缀
    public static final String HTTP_URL_SUFFIX="?api_key=";//请求参数后缀
    public static final int MOVIE_LOADER_FALG=1;//定义loader的标识
    public static final String AUTHER_STR = "com.example.hello.hellomovie.Providers";
    public static final String DB_NAME = "movie.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_ITEM_NAME = "infos";//电影详细信息表
    public static final String TABLE_COMMENT_NAME = "comments";//电影评论表
    public static final String TABLE_COLLECTION_NAME = "collections";//收藏表
    public static final String TABLE_DEFAULT_NAME = "dflist";//默认显示的表
    public static final String MOVIE_ID = "mID";
    public static final String MOVIE_TITLE = "mtitle";
    public static final String MOVIE_PHOTO = "mphoto";
    public static final String MOVIE_DATE = "mdate";
    public static final String MOVIE_RATE = "mrate";
    public static final String MOVIE_CONTENT = "mcontent";
    public static final String MOVIE_PRIVIEW = "mpriview";
    public static final String MOVIE_LENGTH = "mlength";
    public static final String MOVIE_COMMENT = "mComment";

    //urimatcher 匹配码
    public static final int MATCH_CODE_INFO=1;
    public static final int MATCH_CODE_DEFAULT=2;
    public static final int MATCH_CODE_COLLECTIONS=3;
    public static final int MATCH_CODE_COMMENTS=4;

    public static final String MOVIE_COUNT ="mCount" ;
}
