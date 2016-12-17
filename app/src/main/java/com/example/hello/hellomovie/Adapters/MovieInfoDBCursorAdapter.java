package com.example.hello.hellomovie.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hello.hellomovie.Beans.MovieCommentBean;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.Beans.MovieTimeBean;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.NetLib.RecyclerViewCursorAdapter;
import com.example.hello.hellomovie.R;
import com.example.hello.hellomovie.Views.MovieImageView;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/15.
 */

public class MovieInfoDBCursorAdapter extends RecyclerViewCursorAdapter<MovieInfoDBCursorAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public MovieInfoDBCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

        MovieInfoBean infoBean = new MovieInfoBean();
        ArrayList<MoviePreviewBean> previewList;
        ArrayList<MovieTimeBean> timeList;
        ArrayList<MovieCommentBean> commentList;
        //从数据库中获取
        infoBean.setTitle(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_ID)));
        infoBean.setVote_average(cursor.getFloat(cursor.getColumnIndex(MovieConstants.MOVIE_RATE)));
        infoBean.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_DATE)));
        infoBean.setOverview(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_CONTENT)));
        infoBean.setPoster_path(MovieConstants.HTTP_URL_PHOTO_PREFIX + cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_PHOTO)));
        infoBean.setPreview(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_PRIVIEW)));
        infoBean.setComment(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_COMMENT)));
        infoBean.setLength(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_LENGTH)));


    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_show_info, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;
        public TextView txt_date;
        public TextView txt_rate;
        public TextView txt_content;
        public MovieImageView image;
        public Button btn_coll;
        public ListView lv_preview;
        public ListView lv_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_tittle);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_rate = (TextView) itemView.findViewById(R.id.txt_rate);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            image = (MovieImageView) itemView.findViewById(R.id.iv);
            btn_coll = (Button) itemView.findViewById(R.id.btn_coll);
            lv_preview = (ListView) itemView.findViewById(R.id.lv_preview);
            lv_comment = (ListView) itemView.findViewById(R.id.lv_comment);
        }
    }
}
