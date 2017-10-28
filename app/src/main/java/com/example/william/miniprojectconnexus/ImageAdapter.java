package com.example.william.miniprojectconnexus;

/**
 * Created by mukraswa on 10/20/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> logos;
    private List<String> parents;
    private ImageView imageView;
    private ImageLoader imageLoader;


    public ImageAdapter(Context context, List<String> logos, List<String> parents) {
        this.context = context;
        this.logos = logos;
        this.parents = parents;


        Log.e("ImageAdapter","Inside ImageAdapter");


    }

    @Override
    public int getCount() {
        return logos.size();
    }

    @Override
    public Object getItem(int i) {
        return logos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int index = i;
        ImageView imageview;
        if(view == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(175, 175));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        }
        else {
        imageView = (ImageView) view;
        }

        new ImageLoadTask(logos.get(i), imageView).execute();
        Log.e("Debug", "setting clicks lol" + parents);


        if (parents != null){
            Log.e("Debug", "setting clicks");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent stream_view = new Intent(context, ViewStream.class);
                    stream_view.putExtra("STREAM_NAME", parents.get(index));
                    context.startActivity(stream_view);
                }
            });
        }

        return imageView;

    }

    public Context getApplicationContext() {
        return context;
    }


}
