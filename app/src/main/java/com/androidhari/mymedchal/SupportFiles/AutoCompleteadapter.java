package com.androidhari.mymedchal.SupportFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidhari.mymedchal.R;

import java.util.List;

import Classess.DetailsModel;

/**
 * Created by b on 8/5/19.
 */



public class AutoCompleteadapter extends ArrayAdapter {

    private List<DetailsModel> dataList;
    private Context mContext;
    private int itemLayout;

    public AutoCompleteadapter( Context context, int resource,List<DetailsModel> detailsModels) {


        super(context,resource,detailsModels);
        dataList = detailsModels;
        mContext = context;
        itemLayout = resource;

    }


    @Override
    public int getCount() {
        return dataList.size();
    }


    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                String.valueOf(dataList.get(position)));
        return String.valueOf(dataList.get(position));
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(getItem(position));
        return view;
    }

}

