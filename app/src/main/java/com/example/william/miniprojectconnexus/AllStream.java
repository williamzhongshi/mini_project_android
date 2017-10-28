/**
 * Created by William on 10/16/2017.
 */

package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AllStream extends AppCompatActivity implements View.OnClickListener {
    String user_email = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stream);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_email = extras.getString("EMAIL");
        }
        Button my_subscription_button = (Button) findViewById(R.id.my_subscription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.to_nearby_pictures).setOnClickListener(this);
        findViewById(R.id.to_search_stream).setOnClickListener(this);
        findViewById(R.id.my_subscription).setOnClickListener(this);
        if (user_email == null)
        {
            my_subscription_button.setVisibility(View.INVISIBLE);
            new Thread(new StreamBackend(this, null)).start();
        }else{
            new Thread(new StreamBackend(this, user_email)).start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_stream, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new StreamBackend(this, null)).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.AllStream) {
            startActivity(new Intent(this, AllStream.class));
            return true;
        }
        if (id == R.id.SearchStream) {
            startActivity(new Intent(this, SearchStream.class));
            return true;
        }
        if (id == R.id.NearbyPictures) {
            startActivity(new Intent(this, NearbyPictures.class));
            return true;
        }
        if (id == R.id.Camera) {
            startActivity(new Intent(this, Camera.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_nearby_pictures:
                //TextView tv2 = (TextView) findViewById(R.id.more_result);
                //String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, go to nearby pictures ");
                startActivity(new Intent(this, NearbyPictures.class));
                break;
            case R.id.to_search_stream:
                //TextView tv2 = (TextView) findViewById(R.id.more_result);
                //String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, go to search streams");
                Intent i = new Intent(this, SearchStream.class);
                EditText edittext = (EditText) findViewById(R.id.all_stream_search_text);
                String search_text = edittext.getText().toString();
                Log.d("Debug", "Calling search stream using name: " + search_text);
                i.putExtra("SEARCH_TEXT", search_text);
                startActivity(i);
                break;
            case R.id.my_subscription:
                //TextView tv2 = (TextView) findViewById(R.id.more_result);
                //String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, all stream with subscription streams ");
                new Thread(new StreamBackend(this, user_email)).start();
                break;
        }
    }
}
