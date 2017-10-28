package com.example.william.miniprojectconnexus;

/**
 * Created by mukraswa on 10/20/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.w3c.dom.Text;

import java.util.List;

public class PictureImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> logos;
    private List<Double> dist;
    private ImageView imageView;
    private ImageLoader imageLoader;


    public PictureImageAdapter(Context context, List<String> logos, List<Double> dist) {
        this.context = context;
        this.logos = logos;
        this.dist = dist;
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


    public View getView(int i, View view, ViewGroup viewGroup) {

        //ImageView imageview;
        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            grid = new View(context);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            //TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            imageView = (ImageView) grid.findViewById(R.id.grid_image);
            imageView.setLayoutParams(new GridView.LayoutParams(175, 175));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            textView.setText(dist.get(i).toString());
        }
        else {
            //imageView = (ImageView) view;
            grid = (View) view;
        }

        new ImageLoadTask(logos.get(i), imageView).execute();

        return grid;

    }

    public Context getApplicationContext() {
        return context;
    }


}