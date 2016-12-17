package com.example.hello.hellomovie.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Interfaces.RecyclerItemClicker;
import com.example.hello.hellomovie.MovieConstants;
import com.example.hello.hellomovie.NetLib.RecyclerViewCursorAdapter;
import com.example.hello.hellomovie.R;
import com.example.hello.hellomovie.Views.MovieImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Hello on 2016/12/13.
 */

public class MovieListDBCursorAdapter extends RecyclerViewCursorAdapter<MovieListDBCursorAdapter.ViewHolder> implements View.OnClickListener {
    private LayoutInflater inflate;
    private Context context;
    private RecyclerItemClicker mListener;

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public MovieListDBCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.recycleitem, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        MovieInfoBean bean = new MovieInfoBean();
        bean.setTitle(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_TITLE)));
        bean.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_PHOTO)));
        bean.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_DATE)));
        bean.setVote_average(cursor.getFloat(cursor.getColumnIndex(MovieConstants.MOVIE_RATE)));
        bean.setOverview(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_CONTENT)));

        holder.txt_title.setText(bean.getTitle());
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(MovieConstants.HTTP_URL_PHOTO_PREFIX + bean.getPoster_path()).placeholder(R.drawable.placeholder).into(holder.image);

        holder.rb.setRating(bean.getVote_average() * 5 / 10);
        holder.itemView.setTag(cursor.getString(cursor.getColumnIndex(MovieConstants.MOVIE_ID)));

    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public void onClick(View view) {
        if(mListener!=null)
            mListener.onRecycleItemClick(view,String.valueOf(view.getTag()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;//标题
        public MovieImageView image;//电影图片
        public RatingBar rb;//星级评定控件

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.recycle_txt_title);
            image = (MovieImageView) itemView.findViewById(R.id.recycle_iv);
            rb = (RatingBar) itemView.findViewById(R.id.recycle_rb);
        }
    }

    public RecyclerItemClicker getmListener() {
        return mListener;
    }

    public void setmListener(RecyclerItemClicker mListener) {
        this.mListener = mListener;
    }
}
