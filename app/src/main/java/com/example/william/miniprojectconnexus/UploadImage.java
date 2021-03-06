package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
    String JsonURL= "http://10.0.2.2:8080/api/uploadfile";

    String stream_name;
    RequestQueue requestQueue;
    private String filepath;

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
            new MultipartUploadRequest(this, uploadId, UploadUrl)
                    .addFileToUpload(filepath,"image")
                    .addParameter("txtName",name)
                    .addParameter("stream_name",stream_name)
                    .addParameter("txtComments",name)
                    .addParameter("txtOffset","0")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
            buttonUpload.setEnabled(true);
        }
        if (v == buttonUpload) {
            uploadMultipart();

        }
        if (v == buttonCamera) {
            Intent intent = new Intent(UploadImage.this, Camera.class);
            intent.putExtra("Stream", stream_name);
            startActivity(intent);
        }
    }
}
