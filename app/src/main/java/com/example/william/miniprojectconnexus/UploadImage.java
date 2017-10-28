package com.example.william.miniprojectconnexus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;

public class UploadImage extends AppCompatActivity implements View.OnClickListener{

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCamera;

    private EditText editText;
    private TextView viewStream;
    double latitude;
    double longitude;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

     //Uri to store the image uri
    private Uri filePath;
    String UploadUrl;
    String JsonURL= "http://10.0.2.2:8080/api/uploadfile";

    String stream_name;
    RequestQueue requestQueue;
    private String filepath;
    private LocationManager locationmanager;
    private LocationListener locationlistener;

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

        stream_name = getIntent().getStringExtra("STREAM_NAME");

        viewStream.setText( "Stream:" + stream_name);
        JsonURL += "?stream_name=" + stream_name;
        Log.e("**************", "URL:" + JsonURL);

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL, null,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        try {
                            UploadUrl = response.getString("url");
                            buttonChoose.setEnabled(true);
                            Log.e("Volley response", ""+ UploadUrl);
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

        requestQueue.add(obreq);
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                Log.i("Debug", "My location changed to f" + Double.toString(latitude) + " " + Double.toString(longitude));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
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
        final String docId = DocumentsContract.getDocumentId(filePath);

        filepath = Environment.getExternalStorageDirectory() + "/" + "pic.jpg";

        filepath = getPath(filePath);

        String fileName = null;

        Cursor c = getContentResolver().query(filePath, null, null, null, null);


        int id = c.getColumnIndex(MediaStore.Images.Media.TITLE);

        Log.e("SELECT", ""+filepath);
        Log.e("SELECT", ""+docId);
        Log.e("SELECT", ""+"" + id);
    }

    public void uploadMultipart() {
        String name = editText.getText().toString().trim();

        //Getting the actual path of the image
        Log.e("ImageUpload", "Inside Upload Mulitpart|" + filePath);
        String cameraPath = getIntent().getStringExtra("path");
        if(cameraPath != null) {
            filepath = cameraPath;
            stream_name = getIntent().getStringExtra("Stream");
        }
        Log.e("ImageUpload", "stream file|" + filepath +"|"+ stream_name);


        try {
            String uploadId = UUID.randomUUID().toString();

            //creating a mulit part request
            Random rand = new Random();
            new MultipartUploadRequest(this, uploadId, UploadUrl)
                    .addFileToUpload(filepath ,"image")
                    .addParameter("txtName",name + rand.nextInt())
                    .addParameter("stream_name",stream_name)
                    .addParameter("txtComments",name)
                    .addParameter("txtOffset","0")
                    .addParameter("latitude", String.valueOf(latitude))
                    .addParameter("longitude", String.valueOf(longitude))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        }
        catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
            buttonUpload.setEnabled(true);
        }
        if (v == buttonUpload) {
            uploadMultipart();
            Intent intent = new Intent(UploadImage.this, AllStream.class);
            startActivity(intent);
        }
        if (v == buttonCamera) {
            Intent intent = new Intent(UploadImage.this, Camera.class);
            intent.putExtra("Stream", stream_name);
            startActivity(intent);
        }
    }
}
