package com.example.william.miniprojectconnexus;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by William on 10/18/2017.
 */

public class SearchStreamBackend implements Runnable{
    private Context context;
    private String search_text;
    private int image_offset;

    public SearchStreamBackend(Context context, String search_text, int image_offset) {
        this.context = context;
        this.search_text = search_text;
        this.image_offset = image_offset;
    }

    @Override
    public void run() {
        String url = "http://10.0.2.2:8080/api/search_stream"+"/" + this.search_text;  // append with search term here
        Log.i("Info", url);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>()
            {
                    @Override
                    public void onResponse(JSONObject response) {
                        // your response
                        Toast.makeText(context, "SearchStream" + response, Toast.LENGTH_SHORT).show();
                        //Log.i("My success", "" + response.toString());
                        //Log.i("Response body", response.getClass().getName());

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response.toString());
                            JSONArray streams = jsonObj.getJSONArray("body");
                            List<StreamInfo> streamInfos = new ArrayList<>();
                            for (int i = 0; i < streams.length(); i++) {
                                JSONObject c = streams.getJSONObject(i);
                                String name = c.getString("name");
                                String cover_url = c.getString("cover_image");
                                Log.i("Debug","GOT URL " + cover_url);
                                final StreamInfo s1 = new StreamInfo(name, cover_url);
                                streamInfos.add(s1);

                                //Log.i("Debug","Stream Info " + streamInfos.toString());
                                // Log.i("Debug","Context " + context.toString());
                                ImageLoader loader = MySingleton.getInstance(context).getImageLoader();
                                loader.get(cover_url, new ImageLoader.ImageListener() {

                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Error", "Volley Error");
                                        error.printStackTrace();
                                    }

                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            s1.setBitmap(response.getBitmap());

                                            Log.d("Debug","Response: get the bitmap " + s1.getBitmap());
                                        }
                                    }
                                });
                            }
                            Collections.sort(streamInfos, new Comparator<StreamInfo>(){
                                public int compare(StreamInfo s1, StreamInfo s2){
                                    return s1.getName().compareTo(s2.getName());
                                }
                            });
                            if(image_offset>streamInfos.size()-1){
                                image_offset = streamInfos.size()-8-1;
                            }
                            int end_index = image_offset+8 < streamInfos.size()-1 ? image_offset+8 : streamInfos.size()-1;
                            List <StreamInfo> sub_infos = streamInfos.subList(image_offset, end_index);
                            Log.d("Debug","Response: " + sub_infos);


                            GridView gv = (GridView) ((SearchStream)context).findViewById(R.id.search_gridview);
                            Log.d("Debug", "GridView : " + gv.toString());
                            ImageAdaptor adaptor = new ImageAdaptor(context, sub_infos);
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
//                        List<StreamInfo> streamInfos = new ArrayList<>();
//                        for (int i = 0; i < streams.length(); i++) {
//                            JSONObject c = streams.getJSONObject(i);
//                            String name = c.getString("name");
//                            String url = c.getString("cover_image");
//                            Log.d("Debug","GOT URL " + url);
//                            final StreamInfo s = new StreamInfo(name, url);
//                            streamInfos.add(s);
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
//                        Log.d("Debug","Response: " + streamInfos);
//
//                        GridView gv = (GridView) ((AllStream)context).findViewById(R.id.gridview);
//
//                        ImageAdaptor adaptor = new ImageAdaptor(context, streamInfos);
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
