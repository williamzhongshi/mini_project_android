package com.example.william.miniprojectconnexus;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by sycheng on 10/15/17.
 */

public class ImageAdaptor extends BaseAdapter {
    private Context mContext;

    private List<StreamInfo> streamInfos;

    public ImageAdaptor(Context context, List<StreamInfo> streamInfos) {
        this.mContext = context;
        this.streamInfos = streamInfos;
    }

    @Override
    public int getCount() {
        return streamInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return streamInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        final int stream_index = i;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(streamInfos.get(i).getBitmap());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stream_view = new Intent(mContext, ViewStream.class);
                stream_view.putExtra("stream_name", streamInfos.get(stream_index).getName());
                mContext.startActivity(stream_view);
            }
        });

        Log.d("Debug","set image to adaptor: " + streamInfos.get(i).getBitmap());

        return imageView;
    }
}
