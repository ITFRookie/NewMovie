package com.example.hello.hellomovie.Activitys;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.hellomovie.Adapters.ListViewCommentAdapter;
import com.example.hello.hellomovie.Adapters.ListViewPreviewAdapter;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.Beans.MovieTimeBean;
import com.example.hello.hellomovie.Interfaces.MovieInfoGetCallBack;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.Providers.MovieContentProvider;
import com.example.hello.hellomovie.R;
import com.example.hello.hellomovie.Threads.ShowMovieBeanThread;
import com.example.hello.hellomovie.Utils.MovieGetJsonUtil;
import com.example.hello.hellomovie.Utils.MovieJsonAndBeanTran;
import com.example.hello.hellomovie.Utils.MovieViewUtil;
import com.example.hello.hellomovie.Utils.ToastUtil;
import com.example.hello.hellomovie.Views.MovieImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowInfoActivity extends AppCompatActivity implements View.OnClickListener, MovieInfoGetCallBack {
    public static final int FLAG_GET = 100;//设置获取数据的标志
    public static final int FLAG_COLL_TRUE = 1000;//设置收藏标志
    public static final int FLAG__COLL_FALSE = 10001;//设置取消收藏标志
    public static final int FLAG_COLL_FAILED = 1002;//设置收藏失败标志
    String api_key;
    MovieInfoBean infoBean;
    String movieID;
    ArrayList listTime = null;
    ArrayList<MoviePreviewBean> listPreview = null;
    ArrayList listComment = null;
    TextView txt_title;
    MovieImageView iv;
    TextView txt_date;
    TextView txt_rate;
    TextView txt_content;
    TextView txt_length;
    TextView txt_info_preview;
    TextView txt_info_comment;
    Button btn_coll;
    ListView lv_preview;
    ListView lv_comment;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_GET:
                    showInfo();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        movieID = getIntent().getExtras().getString(MovieConstants.MOVIE_ID);
        //  printInfo("正在加载，请耐心等待");
        ToastUtil.showMsg(ShowInfoActivity.this, "正在加载，请耐心等待");
        initApi_key();
        initView();
        initContent();

    }

    private void initView() {

        txt_title = (TextView) findViewById(R.id.txt_tittle);
        txt_rate = (TextView) findViewById(R.id.txt_rate);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_content = (TextView) findViewById(R.id.txt_content);
        txt_length = (TextView) findViewById(R.id.txt_length);
        txt_info_preview = (TextView) findViewById(R.id.txt_info_pre);
        txt_info_comment = (TextView) findViewById(R.id.txt_info_comment);
        iv = (MovieImageView) findViewById(R.id.iv);
        btn_coll = (Button) findViewById(R.id.btn_coll);
        lv_preview = (ListView) findViewById(R.id.lv_preview);
        lv_comment = (ListView) findViewById(R.id.lv_comment);

        //默认提示信息隐藏
        txt_info_preview.setVisibility(View.GONE);
        txt_info_comment.setVisibility(View.GONE);

    }

    private void initApi_key() {


        try {
            api_key = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString("api_key");
        } catch (PackageManager.NameNotFoundException e) {
            // printInfo("获取失败");
            ToastUtil.showMsg(ShowInfoActivity.this, "获取失败");
        }
        ;
    }

    private void initContent() {

        ShowMovieBeanThread parseThread = null;
        if (!isOnline()) {
            // printInfo("检查网络连接");
            ToastUtil.showMsg(ShowInfoActivity.this, "检查网络连接");
        }
        try {
            parseThread = new ShowMovieBeanThread(ShowInfoActivity.this, movieID);
        } catch (PackageManager.NameNotFoundException e) {
            //  printInfo("获取失败");
            ToastUtil.showMsg(ShowInfoActivity.this, "获取失败");
        }
        parseThread.setmCallBack(this);
        parseThread.start();


    }

    private void showInfo() {
        //获取到数据显示
        if (infoBean != null) {
            txt_title.setText(infoBean.getTitle());
            txt_date.setText("上映时间：" + infoBean.getRelease_date());
            txt_rate.setText("评分：" + infoBean.getVote_average() + "分");
            txt_content.setText(infoBean.getOverview());
            Picasso.with(ShowInfoActivity.this).load(MovieConstants.HTTP_URL_PHOTO_PREFIX + infoBean.getPoster_path()).placeholder(R.drawable.placeholder).into(iv);
            if (listTime.size() > 0) {
                txt_length.setText("时长：" + ((MovieTimeBean) listTime.get(0)).getRuntime() + "分");

                btn_coll.setOnClickListener(this);
                if ("1".equals(infoBean.getFlag())) btn_coll.setText("取消收藏");
                if (listPreview.size() <= 0) txt_info_preview.setVisibility(View.VISIBLE);
                if (listComment.size() <= 0) txt_info_comment.setVisibility(View.VISIBLE);
                lv_comment.setAdapter(new ListViewCommentAdapter(ShowInfoActivity.this, listComment));
                lv_preview.setAdapter(new ListViewPreviewAdapter(ShowInfoActivity.this, listPreview));

                MovieViewUtil.setListViewHeightBasedOnChildren(lv_preview);
                MovieViewUtil.setListViewHeightBasedOnChildren(lv_comment);

                lv_preview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //点击预告播放
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + listPreview.get(i).getId()));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + listPreview.get(i).getId()));
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }

                    }
                });
            }

        }

    }

    //点击收藏按钮响应的事件
//    @Override
//    public void onClick(View view) {
//        //存储当前电影到收藏表中
//        if (btn_coll.getText().equals("收藏")) {
//            ContentValues values = new ContentValues();
//            ContentValues values2 = new ContentValues();
//            values2.put("mFlag", "1");
//            values.put(MovieConstants.MOVIE_ID, infoBean.getId());
//            values.put(MovieConstants.MOVIE_COUNT, infoBean.getVote_count());
//            values.put(MovieConstants.MOVIE_TITLE, infoBean.getTitle());
//            values.put(MovieConstants.MOVIE_PHOTO, infoBean.getPoster_path());
//            values.put(MovieConstants.MOVIE_DATE, infoBean.getRelease_date());
//            values.put(MovieConstants.MOVIE_RATE, infoBean.getVote_average());
//            values.put(MovieConstants.MOVIE_CONTENT, infoBean.getOverview());
//            values.put(MovieConstants.MOVIE_LENGTH, infoBean.getLength());
//            values.put(MovieConstants.MOVIE_PRIVIEW, infoBean.getPreview());
//            values.put(MovieConstants.MOVIE_COMMENT, infoBean.getComment());
//            getContentResolver().insert(MovieContentProvider.CONTENT_MOVIE_COLL, values);
//            getContentResolver().update(MovieContentProvider.CONTENT_MOVIE_INFOS, values2, "mID=?", new String[]{String.valueOf(infoBean.getId())});
//            btn_coll.setText("取消收藏");
//            //  printInfo("successful!");
//            ToastUtil.showMsg(ShowInfoActivity.this, "Successful");
//        } else {
//            ContentValues values = new ContentValues();
//            values.put("mFlag", "0");
//            getContentResolver().update(MovieContentProvider.CONTENT_MOVIE_INFOS, values, "mID=?", new String[]{String.valueOf(infoBean.getId())});
//            getContentResolver().delete(MovieContentProvider.CONTENT_MOVIE_COLL, "mID=?", new String[]{String.valueOf(infoBean.getId())});
//            btn_coll.setText("收藏");
//            //  printInfo("successful!");
//            ToastUtil.showMsg(ShowInfoActivity.this, "Successful");
//
//
//        }
//
//    }

    //回调接口
    @Override
    public void onMovieInfoGot(ArrayList list1, ArrayList list2, ArrayList list3, MovieInfoBean bean) {
        listTime = list1;
        listPreview = list2;
        listComment = list3;
        infoBean = bean;
        handler.sendEmptyMessage(FLAG_GET);
    }

    private boolean isOnline() {
        //判断是否有网络
        ConnectivityManager cm = (ConnectivityManager) ShowInfoActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    @Override
    public void onClick(View view) {
        //使用AsyncTask异步收藏和取消收藏
        AsyncTask<String, Void, Integer> mTask = new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... strings) {
                String txt = strings[0];
                //存储当前电影到收藏表中
                if (txt == null) return FLAG__COLL_FALSE;
                if (("收藏").equals(txt)) {
                    ContentValues values = new ContentValues();
                    ContentValues values2 = new ContentValues();
                    values2.put("mFlag", "1");
                    values.put(MovieConstants.MOVIE_ID, infoBean.getId());
                    values.put(MovieConstants.MOVIE_COUNT, infoBean.getVote_count());
                    values.put(MovieConstants.MOVIE_TITLE, infoBean.getTitle());
                    values.put(MovieConstants.MOVIE_PHOTO, infoBean.getPoster_path());
                    values.put(MovieConstants.MOVIE_DATE, infoBean.getRelease_date());
                    values.put(MovieConstants.MOVIE_RATE, infoBean.getVote_average());
                    values.put(MovieConstants.MOVIE_CONTENT, infoBean.getOverview());
                    values.put(MovieConstants.MOVIE_LENGTH, infoBean.getLength());
                    values.put(MovieConstants.MOVIE_PRIVIEW, infoBean.getPreview());
                    values.put(MovieConstants.MOVIE_COMMENT, infoBean.getComment());
                    getContentResolver().insert(MovieContentProvider.CONTENT_MOVIE_COLL, values);
                    getContentResolver().update(MovieContentProvider.CONTENT_MOVIE_INFOS, values2, "mID=?", new String[]{String.valueOf(infoBean.getId())});
                    return FLAG_COLL_TRUE;
//            btn_coll.setText("取消收藏");
//            //  printInfo("successful!");
//            ToastUtil.showMsg(ShowInfoActivity.this, "Successful");
                } else if (("取消收藏").equals(txt)) {
                    ContentValues values = new ContentValues();
                    values.put("mFlag", "0");
                    getContentResolver().update(MovieContentProvider.CONTENT_MOVIE_INFOS, values, "mID=?", new String[]{String.valueOf(infoBean.getId())});
                    getContentResolver().delete(MovieContentProvider.CONTENT_MOVIE_COLL, "mID=?", new String[]{String.valueOf(infoBean.getId())});
//            btn_coll.setText("收藏");
//            //  printInfo("successful!");
//            ToastUtil.showMsg(ShowInfoActivity.this, "Successful");
                    return FLAG__COLL_FALSE;

                } else return FLAG_COLL_FAILED;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                switch (integer) {
                    case FLAG_COLL_TRUE:
                        btn_coll.setText("取消收藏");
                        ToastUtil.showMsg(ShowInfoActivity.this, "successful!");
                        break;
                    case FLAG__COLL_FALSE:
                        btn_coll.setText("收藏");
                        ToastUtil.showMsg(ShowInfoActivity.this, "successful!");
                        break;


                }


            }
        };
        mTask.execute(btn_coll.getText().toString());
    }

//    private void printInfo(String s) {
//        //打印提示消息
//        Toast.makeText(ShowInfoActivity.this, s, Toast.LENGTH_SHORT).show();
//    }

}
