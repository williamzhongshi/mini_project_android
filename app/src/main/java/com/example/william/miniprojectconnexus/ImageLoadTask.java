package com.example.william.miniprojectconnexus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mukraswa on 10/22/2017.
 */

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
        Log.e("ImageLoadTask","Inside Constructor");
    }

    @Override
    protected Bitmap doInBackground(Void... paramas ) {
        try {
            Bitmap myBitmap = null;
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("ImageLoadTask","Error getting Bitmap");
            Log.e("ImageLoadTask",e.getLocalizedMessage());
            return null;
        }

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}
