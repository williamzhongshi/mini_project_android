package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewStream extends AppCompatActivity implements View.OnClickListener{

    // URL of object to be parsed
    //String JsonURL = "https://jsonplaceholder.typicode.com/photos";
    //String JsonURL = "http://10.0.2.2:8080/view_stream/and_viewpics?name=";
    String JsonURL= "http://williamztest2-182503.appspot.com/view_stream/and_viewpics?name=";

    //String JsonURL= "http://williamztest2-182503.appspot.com/view_stream/and_viewpics?name=test%20geo";

    String stream_name;
    String offset = "";

    //this needs to be changed to

    // This string will hold the results
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;
    JSONArray arrPhotos;
    JSONObject jsonObject;

    private Button morePics;
    private Button uploadBtn;
    private Button streamsBtn;


    //test code
    private ArrayList<String> mEntries = new ArrayList<String>();
    String photoURL="";
    int offsetInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Log.e("Msg1","Inside Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        //Log.e("Msg2","Set Layout");



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e("Extras", "Inside Extra");
            stream_name = extras.getString("STREAM_NAME");

            if (stream_name != null) {
                JsonURL = JsonURL + stream_name;
            }
            Log.d("Debug", "JsonURL is " + JsonURL);
//            if (JsonURL.contains("localhost")) {
//                JsonURL = JsonURL.replace("localhost", "10.0.2.2");
//            }

            String session_id = extras.getString("SESSION_ID");
            offset = extras.getString("OFFSET");
            Log.e("Debug", "Offset" + offset);

            if (offset != null) {
                offsetInt = offsetInt + Integer.parseInt(offset);
            }
        }

        morePics = (Button) findViewById(R.id.moreButton);
        morePics.setOnClickListener(this);

        uploadBtn = (Button) findViewById(R.id.uploadButton);
        uploadBtn.setOnClickListener(this);

        streamsBtn = (Button) findViewById(R.id.streamsButton);
        streamsBtn.setOnClickListener(this);

        // The following code gets the JSON results as an array

        JsonArrayRequest request = new JsonArrayRequest(JsonURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e("Volley:", "JSON Array");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            Log.e("Volley:", "JSON Array:Inside Loop");
                            //for(int i = 0; i < offsetInt; i++) {
                            try {
                                //JSONObject jsonObject = jsonArray.getJSONObject(i);
                                photoURL = jsonArray.get(i).toString();
                                if (photoURL.contains("localhost")) {
                                    photoURL = photoURL.replace("localhost", "10.0.2.2");
                                }
                                //photoURL = jsonObject.getString("url");
                                Log.e("photo", photoURL);

                                mEntries.add(photoURL);
                            }
                            catch(JSONException e)
                            {
                                Log.e("Volley", e.getLocalizedMessage());
                            }
                        }

                        List<String> sList = subList(mEntries,offsetInt,16);
                        GridView grid = (GridView) findViewById(R.id.GridImages);


                        ImageAdapter customAdapter = new ImageAdapter( getApplicationContext(), sList, null);
                        grid.setAdapter(customAdapter);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley", volleyError.toString());
                    }
                });
        requestQueue = Volley.newRequestQueue(ViewStream.this);
        //Adds the JSON object request "obreq" to the request queue

        requestQueue.add(request);
    }


    public void onClick(View v) {
        Log.e("onClick", "Inside Click Handler");
        if (v == morePics) {
            showMorePics();
        }
        if (v == uploadBtn) {
            showUpload();
        }
        if (v == streamsBtn) {
            showStreams();
        }
    }


    private void showMorePics() {
        //Handling Click for 'More Images' button
        Log.e("More Pics", "Button Clicked");
        Intent i = new Intent(getApplicationContext(), ViewStream.class);
        i.putExtra("STREAM_NAME", stream_name);
        i.putExtra("OFFSET",Integer.toString(offsetInt));
        getApplicationContext().startActivity(i);
    }


    private void showUpload() {
        Log.e("Upload", "Upload Button Clicked");
        Intent i = new Intent(getApplicationContext(), UploadImage.class);
        i.putExtra("STREAM_NAME", stream_name);
        Intent intent = new Intent(ViewStream.this, UploadImage.class);
        intent.putExtra("STREAM_NAME", stream_name);
        startActivity(intent);
    }

    private void showStreams() {
        Log.e("Streams", "Allstreams Button Clicked");
        Intent i = new Intent(getApplicationContext(), AllStream.class);
        getApplicationContext().startActivity(i);
    }

    public static <T> List<T> subList(List<T> list, int offset, int limit) {
        if (offset<0) throw new IllegalArgumentException("Offset must be >=0 but was "+offset+"!");
        if (limit<-1) throw new IllegalArgumentException("Limit must be >=-1 but was "+limit+"!");

        if (offset>0) {
            if (offset >= list.size()) {
                return list.subList(0, 0); //return empty.
            }
            if (limit >-1) {
                //apply offset and limit
                return list.subList(offset, Math.min(offset+limit, list.size()));
            } else {
                //apply just offset
                return list.subList(offset, list.size());
            }
        } else if (limit >-1) {
            //apply just limit
            return list.subList(0, Math.min(limit, list.size()));
        } else {
            return list.subList(0, list.size());
        }
    }

}