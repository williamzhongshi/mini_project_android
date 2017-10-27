package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;


public class UploadImage extends AppCompatActivity implements View.OnClickListener{

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCamera;

    private EditText editText;
    private TextView viewStream;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

     //Uri to store the image uri
    private Uri filePath;
    String UploadUrl;
    String JsonURL= "https://apt-maroon-148823.appspot.com/view_stream/and_getURL";

    String stream_name;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(this);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(this);

        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.editTextName);
        viewStream = (TextView) findViewById(R.id.textStream);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e("Extras:UploadImage", "Inside Extra");
            stream_name = extras.getString("STREAM_NAME");

        }
        viewStream.setText( "Stream:" + stream_name);

        //ActivityCompat.requestPermissions(UploadImage.this,
         //       new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);

        // The following code gets the JSON results as an array

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL, null,
                new Response.Listener<JSONObject>() {

                    //Takes the response from the JSON request
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("objUrl");
                                 UploadUrl = obj.getString("url");
                                //photoURL = jsonObject.getString("url");
                                Log.e("photo:UploadImage", UploadUrl);
                                buttonChoose.setEnabled(true);
                            }
                            catch(JSONException e)
                            {
                                Log.e("Volley", e.getLocalizedMessage());
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley", volleyError.toString());
                    }
                });
        requestQueue = Volley.newRequestQueue(UploadImage.this);
        //Adds the JSON object request "obreq" to the request queue

        requestQueue.add(obreq);
        Log.e("Volley","No Result");
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Picture"),PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null  && data.getData() != null) {
            filePath = data.getData();
        }

    }


    public void uploadMultipart() {
        //Getting the name of the image
        Log.e("Image Upload", "Inside Upload Mulitpart");
        String name = editText.getText().toString().trim();

        //Getting the actual path of the image
        String path = getPath(filePath);



        try {
            String uploadId = UUID.randomUUID().toString();

            //creating a mulit part request
            new MultipartUploadRequest(this, uploadId, UploadUrl)
                    .addFileToUpload(path,"image")
                    .addParameter("name",name)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }


    public String getPath(Uri uri) {
        String path = "";
        Log.e("Get Path", uri.toString());
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        Log.e("Get Path:doc Id",document_id);

        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        Log.e("Get Path", "Second Cursor call");

        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();

        Log.e("Image Upload", path);
        return path;
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                   // mymethod(); //a sample method called
                    //uploadMultipart();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(UploadImage.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    */

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
            buttonUpload.setEnabled(true);
        }
        if (v == buttonUpload) {
            uploadMultipart();

        }
    }




}
