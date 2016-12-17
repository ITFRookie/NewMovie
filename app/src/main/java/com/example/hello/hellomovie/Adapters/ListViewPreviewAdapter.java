package com.example.hello.hellomovie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hello on 2016/12/15.
 */

public class ListViewPreviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MoviePreviewBean> list;
    private LayoutInflater inflater;

    public ListViewPreviewAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        View mView = inflater.inflate(R.layout.listviewpreview, null);
        TextView txt = (TextView) mView.findViewById(R.id.preview_name);
        Button btn_play = (Button) mView.findViewById(R.id.preview_play);
        txt.setText("预告片" + (i + 1));
        btn_play.setTag(list.get(i).getName());
        return mView;
    }
}
