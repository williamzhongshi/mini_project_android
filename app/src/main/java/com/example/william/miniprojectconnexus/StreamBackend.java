package com.example.william.miniprojectconnexus;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sycheng on 10/15/17.
 */

public class StreamBackend implements Runnable{
    private Context context;
    private String user_email;

    public StreamBackend(Context context, String in_email) {
        this.context = context;
        this.user_email = in_email;
    }

    @Override
    public void run() {
        String url = "http://williamztest2-182503.appspot.com/api/all_stream" + "/" + user_email;
        Log.i("Info", url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Response: " + response.toString(), Toast.LENGTH_LONG);
                        Log.d("Debug","Response: " + response.toString());

                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());
                            JSONArray streams = jsonObj.getJSONArray("body");

                            List<StreamInfo> streamInfos = new ArrayList<>();
                            for (int i = 0; i < streams.length(); i++) {
                                JSONObject c = streams.getJSONObject(i);
                                String name = c.getString("name");
                                String url = c.getString("cover_image");
                                Log.d("Debug","GOT URL " + url);
                                final StreamInfo s = new StreamInfo(name, url);
                                streamInfos.add(s);

                                ImageLoader loader = MySingleton.getInstance(context).getImageLoader();
                                loader.get(url, new ImageLoader.ImageListener() {

                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }

                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            s.setBitmap(response.getBitmap());
                                            Log.d("Debug","Response: get the bitmap" + s.getBitmap());
                                        }
                                    }
                                });
                            }

                            Log.d("Debug","Response: " + streamInfos);

                            GridView gv = (GridView) ((AllStream)context).findViewById(R.id.gridview);

                            ImageAdaptor adaptor = new ImageAdaptor(context, streamInfos);
                            gv.setAdapter(adaptor);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this.context).addToRequestQueue(jsObjRequest);
    }
}

class StreamInfo {
    private String name;
    private String coverUrl;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public StreamInfo(String name, String coverUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
