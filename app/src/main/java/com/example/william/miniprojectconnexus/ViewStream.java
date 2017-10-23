package com.example.william.miniprojectconnexus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewStream extends AppCompatActivity {

    // URL of object to be parsed
    String JsonURL = "https://jsonplaceholder.typicode.com/photos";
    // This string will hold the results
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;
    JSONArray arrPhotos;
    JSONObject jsonObject;

    //test code
    private ArrayList<String> mEntries = new ArrayList<String>();
    String photoURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Log.e("Msg1","Inside Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        //Log.e("Msg2","Set Layout");



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String stream_name = extras.getString("STREAM_ID");
            String session_id = extras.getString("SESSION_ID");
        }



        JsonArrayRequest request = new JsonArrayRequest(JsonURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        //for(int i = 0; i < jsonArray.length(); i++) {
                        for(int i = 0; i < 6; i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                photoURL = jsonObject.getString("url");
                                Log.e("photo",photoURL);
                                mEntries.add(photoURL);
                            }
                            catch(JSONException e) {
                                mEntries.add("Error: " + e.getLocalizedMessage());
                            }
                        }

                        GridView grid = (GridView) findViewById(R.id.GridImages);


                        ImageAdapter customAdapter = new ImageAdapter( getApplicationContext(), mEntries );
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

        //Display the results in the grid.

        /*
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL, null,
                new Response.Listener<JSONObject>() {

                    //Takes the response from the JSON request
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("ImgObj");
                            int offset = obj.getInt("offset");
                            arrPhotos = obj.getJSONArray("images");

                        }
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                            Log.e("Volley", "Error:JSON Exception");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("Volley", error.toString());

                    }
                } );
        requestQueue = Volley.newRequestQueue(ViewStream.this);
        //Adds the JSON object request "obreq" to the request queue

        requestQueue.add(obreq);

        Log.e("Volley","Image URLS");
        */
        /*
        //Test <code></code>
        JSONObject jo = new JSONObject();
        try {
            jo.put("images", "http://localhost:8080/_ah/img/BMjBTv9yHB3KG6bJRuuHnQ==");
        }
        catch (JSONException e)
        {
            Log.e("Mini_project_android", "Unexpected JSON exception", e);
        }

        arrPhotos = new JSONArray();
        arrPhotos.put(jo);

        */



        /*
        List<String> listdata = new ArrayList<String>();
        Log.e("Mini_project_android", "Outside if condition");
        if (mEntries != null) {
            Log.e("Mini_project_android", "Inside if condition");
            for (int i=0;i<mEntries.size();i++){
                //listdata.add(mEntries.toString());
                listdata.add(mEntries.get(i));
                Log.e("Item:", mEntries.get(i) );
                /*
                try {
                    listdata.add(mEntries.toString());
                    Log.e("Mini_project_android", "Added new Item");
                }
                catch (exception e){
                    Log.e("Mini_project_android", "Unexpected JSON exception", e);
                }

            }
        }
        else
        {
            Log.e("Volly","Nothing Returned");
        }
        */



    }

}
