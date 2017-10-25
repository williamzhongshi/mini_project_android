package com.example.william.miniprojectconnexus;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by William on 10/18/2017.
 */

public class NearbyPicturesBackend implements Runnable{
    private Context context;
    private double lat;
    private double lng;
    private int image_offset;

    public NearbyPicturesBackend(Context context, double lat, double lng, int image_offset) {
        this.context = context;
        this.lat = lat;
        this.lng = lng;
        this.image_offset = image_offset;
    }

    @Override
    public void run() {
        String url = "http://10.0.2.2:8080/api/nearby_pictures"+"/" + this.lat + "_" + this.lng;  // append with search term here
        Log.i("Info", url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>()
            {
                    @Override
                    public void onResponse(JSONObject response) {
                        // your response
                        Toast.makeText(context, "nearby_pictures" + response, Toast.LENGTH_SHORT).show();
                        //Log.i("My success", "" + response.toString());
                        //Log.i("Response body", response.getClass().getName());

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response.toString());
                            JSONArray pictures = jsonObj.getJSONArray("body");
                            List<PictureInfo> PictureInfos = new ArrayList<>();
                            for (int i = 0; i < pictures.length(); i++) {
                                JSONObject c = pictures.getJSONObject(i);
                                String name = c.getString("name");
                                String cover_url = c.getString("url");

                                ImageLoader loader = MySingleton.getInstance(context).getImageLoader();
                                cover_url = "https://i.ytimg.com/vi/AGsvuFB_aEY/maxresdefault.jpg";
                                //cover_url = "http://lh3.googleusercontent.com/JdpPGgD-_TvNKFw9jXxvIX-wCGJLF0hEAd8gOuuUWy53K3uvBMj0MrQQVQNhB_Dgpr78faG8I-L27T_jcTNyLzVfrg";
                                if (cover_url.contains("localhost")) {
                                    cover_url = cover_url.replace("localhost", "10.0.2.2");
                                }
                                final PictureInfo s1 = new PictureInfo(name, cover_url);
                                PictureInfos.add(s1);
                                Log.i("Debug","GOT URL " + s1.getCoverUrl());

                                loader.get(cover_url, new ImageLoader.ImageListener() {

                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error", "Volley Error");
                                        error.printStackTrace();
                                    }

                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            s1.setBitmap(response.getBitmap());
                                            Log.d("Debug", "Processing photo: " + s1.getName());
                                            Log.d("Debug","Response: get the bitmap " + s1.getBitmap());
                                        }
                                    }
                                });
                            }
                            Collections.sort(PictureInfos, new Comparator<PictureInfo>(){
                                public int compare(PictureInfo s1, PictureInfo s2){
                                    return s1.getName().compareTo(s2.getName());
                                }
                            });
                            if(image_offset>PictureInfos.size()-1){
                                image_offset = PictureInfos.size()-8-1;
                            }
                            int end_index = image_offset+8 < PictureInfos.size()-1 ? image_offset+8 : PictureInfos.size()-1;
                            List <PictureInfo> sub_infos = PictureInfos.subList(image_offset, end_index);
                            Log.d("Debug","Response: " + sub_infos);


                            GridView gv = (GridView) ((NearbyPictures)context).findViewById(R.id.nearby_gridview);
                            Log.d("Debug", "GridView : " + gv.toString());
                            PictureAdaptor adaptor = new PictureAdaptor(context, sub_infos);
                            gv.setAdapter(adaptor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
            }, new Response.ErrorListener()
            {
                // error
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context, "my error :" + error, Toast.LENGTH_LONG).show();
                    Log.i("My error", "" + error);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("search_string", "cat");

                    return map;
                }
            };
        //queue.add(jsonRequest);
        MySingleton.getInstance(this.context).addToRequestQueue(jsonRequest);
        }
//    JsonObjectRequest jsObjRequest = new JsonObjectRequest
//            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    Toast.makeText(context, "Response: " + response.toString(), Toast.LENGTH_LONG);
//                    Log.d("Debug","Response: " + response.toString());
//
//                    try {
//                        JSONObject jsonObj = new JSONObject(response.toString());
//                        JSONArray streams = jsonObj.getJSONArray("body");
//
//                        List<PictureInfo> PictureInfos = new ArrayList<>();
//                        for (int i = 0; i < streams.length(); i++) {
//                            JSONObject c = streams.getJSONObject(i);
//                            String name = c.getString("name");
//                            String url = c.getString("cover_image");
//                            Log.d("Debug","GOT URL " + url);
//                            final PictureInfo s = new PictureInfo(name, url);
//                            PictureInfos.add(s);
//
//                            ImageLoader loader = MySingleton.getInstance(context).getImageLoader();
//                            loader.get(url, new ImageLoader.ImageListener() {
//
//                                public void onErrorResponse(VolleyError error) {
//                                    error.printStackTrace();
//                                }
//
//                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                                    if (response.getBitmap() != null) {
//                                        s.setBitmap(response.getBitmap());
//
//                                        Log.d("Debug","Response: get the bitmap" + s.getBitmap());
//                                    }
//                                }
//                            });
//                        }
//
//                        Log.d("Debug","Response: " + PictureInfos);
//
//                        GridView gv = (GridView) ((AllStream)context).findViewById(R.id.gridview);
//
//                        ImageAdaptor adaptor = new ImageAdaptor(context, PictureInfos);
//                        gv.setAdapter(adaptor);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });
//
//    // Access the RequestQueue through your singleton class.
//        MySingleton.getInstance(this.context).addToRequestQueue(jsObjRequest);

    }

class PictureInfo {
    private String name;
    private String coverUrl;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PictureInfo(String name, String coverUrl) {
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