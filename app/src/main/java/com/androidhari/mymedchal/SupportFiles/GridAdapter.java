package com.androidhari.mymedchal.SupportFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidhari.mymedchal.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by b on 28/3/19.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final String[] Imageid;

    public GridAdapter(Context c, List<String> web, List<String> Imageid ) {
        mContext = c;
        this.Imageid = Imageid.toArray(new String[0]);
        this.web = web.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.gridtemplate, null);
            TextView textView = (TextView) grid.findViewById(R.id.statusmsg);
            ImageView imageView = (ImageView)grid.findViewById(R.id.picture);


            textView.setText(web[position]);
            //imageView.setImageResource(R.drawable.common_full_open_on_phone);
            Glide.with(mContext).load(Imageid[position]).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}