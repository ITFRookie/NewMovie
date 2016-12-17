package com.example.hello.hellomovie.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hello.hellomovie.R;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Hello on 2016/12/12.
 *
 * 弃用的文件
 */

public class RecycleMovieAdapter extends Adapter {


    private View.OnClickListener mItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem, null);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void setmItemClickListener(OnClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;//标题
        public ImageView image;//电影图片
        public RatingBar rb;//星级评定控件

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.recycle_txt_title);
            image = (ImageView) itemView.findViewById(R.id.recycle_iv);
            rb = (RatingBar) itemView.findViewById(R.id.recycle_rb);
        }
    }
}
