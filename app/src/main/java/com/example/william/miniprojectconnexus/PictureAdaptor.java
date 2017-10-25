package com.example.william.miniprojectconnexus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sycheng on 10/15/17.
 */

public class PictureAdaptor extends BaseAdapter {
    private Context mContext;

    private List<PictureInfo> pictureInfos;

    public PictureAdaptor(Context context, List<PictureInfo> pictureInfos) {
        this.mContext = context;
        this.pictureInfos = pictureInfos;
    }

    @Override
    public int getCount() {
        return pictureInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return pictureInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        TextView textView = null;
        //final int stream_index = i;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(100, 100));
            textView.setPadding(8,8,8,8);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(pictureInfos.get(i).getBitmap());
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent stream_view = new Intent(mContext, ViewStream.class);
//                stream_view.putExtra("stream_name", pictureInfos.get(stream_index).getName());
//                mContext.startActivity(stream_view);
//            }
//        });
        Log.d("Debug","In getview processing  " + pictureInfos.get(i).getName());
        Log.d("Debug","set photo image to adaptor: " + pictureInfos.get(i).getBitmap());

        return imageView;
    }
}
