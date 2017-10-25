package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NearbyPictures extends AppCompatActivity implements View.OnClickListener {

    public int image_offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_pictures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_offset = 0;
        findViewById(R.id.more_result).setOnClickListener(this);
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
        image_offset = 0;
        new Thread(new NearbyPicturesBackend(this, (double) 22, (double) 121, image_offset)).start();
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

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_result:
                TextView tv2 = (TextView) findViewById(R.id.search_stream);
                String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, Searching " + text2);
                image_offset += 8;
                new Thread(new SearchStreamBackend(this, text2, image_offset)).start();
                break;
        }
    }
}
