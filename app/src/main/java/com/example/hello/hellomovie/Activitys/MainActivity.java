package com.example.hello.hellomovie.Activitys;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.hellomovie.Adapters.MovieListDBCursorAdapter;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Interfaces.MovieJsonCallBack;
import com.example.hello.hellomovie.Interfaces.RecyclerItemClicker;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.Providers.MovieContentProvider;
import com.example.hello.hellomovie.R;
import com.example.hello.hellomovie.Utils.ToastUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieJsonCallBack, LoaderManager.LoaderCallbacks<Cursor>, RecyclerItemClicker {

    RecyclerView recycView;
    TextView txt_main_info;
    MovieListDBCursorAdapter adapter;
    String byOrder = "mCount";//默认按照热度排序
    boolean changedFlag = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MainActivity.this.getLoaderManager().restartLoader(MovieConstants.MOVIE_LOADER_FALG, null, MainActivity.this);
            txt_main_info.setVisibility(View.GONE);
            recycView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        //保存activity的当前状态
        View topView = recycView.getLayoutManager().getChildAt(0);
        if (topView == null) return;
        ;
        int lastOffset = topView.getTop();//记录可见项目的top
        int lastPosition = recycView.getLayoutManager().getPosition(topView);//记录可见项目的位置

        outState.putInt("lastOffset", lastOffset);
        outState.putInt("lastPosition", lastPosition);
    }

    //   @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Toast.makeText(MainActivity.this, "DOne", Toast.LENGTH_SHORT).show();
//        int lastOffset = savedInstanceState.getInt("lastOffset");
//        int lastPosition = savedInstanceState.getInt("lastPosition");
//        ((GridLayoutManager) recycView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //printInfo("请刷新");
        ToastUtil.showMsg(MainActivity.this, "请刷新");
        initView();
        //为activity销毁重建时保存状态  eg：横竖屏切换
        if (savedInstanceState != null) {
            int lastOffset = savedInstanceState.getInt("lastOffset");
            int lastPosition = savedInstanceState.getInt("lastPosition");
            ((GridLayoutManager) recycView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);

        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {

        txt_main_info = (TextView) findViewById(R.id.txt_main_info);
        txt_main_info.setVisibility(View.GONE);
        recycView = (RecyclerView) findViewById(R.id.recyclerView);
        if (adapter == null) {
            Cursor c = getContentResolver().query(MovieContentProvider.CONTENT_MOVIE_INFOS, null, null, null, null);
            adapter = new MovieListDBCursorAdapter(MainActivity.this, c, 1);
        }

        adapter.setmListener(this);
        recycView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        recycView.setItemAnimator(new DefaultItemAnimator());
        recycView.setHasFixedSize(true);
        recycView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                goSetting();
                break;
            case R.id.action_fresh:
                try {
                    recycView.setVisibility(View.GONE);
                    txt_main_info.setVisibility(View.VISIBLE);

                    fressh();//点击刷新
                } catch (Exception e) {
                    e.printStackTrace();
                    //  printInfo("刷新失败");
                    ToastUtil.showMsg(MainActivity.this, "刷新失败");
                }
                break;
            case R.id.action_filter:
                showCollection();//显示收藏列表
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void showCollection() {
        //弹出排序方式对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择排序方式");
        builder.setSingleChoiceItems(new String[]{"By Top", "By Hot"}, 0, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        byOrder = "mrate";
                        changeLoader();
                        break;
                    case 1:
                        byOrder = "mCount";
                        changeLoader();
                        break;

                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void changeLoader() {
        //通知loader重置
        changedFlag = true;
        MainActivity.this.getLoaderManager().restartLoader(MovieConstants.MOVIE_LOADER_FALG, null, MainActivity.this);

    }

    private void fressh() throws Exception {
        //点击刷新按钮响应事件

        //显示进度条
        // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        String className = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("MovieType", "com.example.hello.hellomovie.Threads.TopMovieThread");
        Object object = null;
        changedFlag = false;
        if (className == null) return;
        Class cls = Class.forName(className);
        Constructor construct = cls.getDeclaredConstructor(Context.class);
        Method setCallBack = cls.getMethod("setmCallBack", new Class[]{MovieJsonCallBack.class});
        Method tStart = cls.getMethod("start");
        object = construct.newInstance(MainActivity.this);
        setCallBack.invoke(object, MainActivity.this);

        if (isOnline()) {
            //有网则网络获取
            tStart.invoke(object);
        } else {
            //printInfo("请检查网络连接");
            ToastUtil.showMsg(MainActivity.this, "请检查网络连接");
        }
    }

    //使用loader机制读取数据
    private void initLoader() {
        getLoaderManager().initLoader(MovieConstants.MOVIE_LOADER_FALG, null, this);
    }

    //loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = null;
        if (!changedFlag)
            loader = new CursorLoader(MainActivity.this, MovieContentProvider.CONTENT_MOVIE_INFOS, null, null, null, null);
        else
            loader = new CursorLoader(MainActivity.this, MovieContentProvider.CONTENT_MOVIE_COLL, null, null, null, byOrder + " DESC");
        return loader;
    }

    //loader
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    //loader
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    public void onRecycleItemClick(View v, String s) {
        //当点击item后响应事件
        Intent intent = new Intent(MainActivity.this, ShowInfoActivity.class);
        intent.putExtra(MovieConstants.MOVIE_ID, s);
        startActivity(intent);


    }

    private void goSetting() {
        //跳转到setting
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private boolean isOnline() {
        //判断是否有网络
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

//    private void printInfo(String s) {
//        //打印提示消息
//        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onMovieJsonGot(ArrayList<MovieInfoBean> list) {
        handler.sendEmptyMessage(0);
    }
}
