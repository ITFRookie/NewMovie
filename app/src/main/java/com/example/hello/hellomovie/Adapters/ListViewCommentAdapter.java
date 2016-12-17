package com.example.hello.hellomovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hello.hellomovie.Beans.MovieCommentBean;
import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.R;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/15.
 */

public class ListViewCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieCommentBean> list;
    private LayoutInflater inflater;

    public ListViewCommentAdapter(Context context, ArrayList<MovieCommentBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = inflater.inflate(R.layout.listviewcomment, null);
        TextView txt_comment = (TextView) mView.findViewById(R.id.txt_com_content);
        TextView txt_author = (TextView) mView.findViewById(R.id.txt_com_author);
        txt_comment.setText(list.get(i).getContent());
        txt_author.setText("By "+list.get(i).getAuthor());

        return mView;
    }
}
