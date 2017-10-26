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

public class AllStream extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.to_nearby_pictures).setOnClickListener(this);
        new Thread(new StreamBackend(this)).start();
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
        new Thread(new StreamBackend(this)).start();
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
            case R.id.to_nearby_pictures:
                //TextView tv2 = (TextView) findViewById(R.id.more_result);
                //String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, go to nearby pictures ");
                startActivity(new Intent(this, NearbyPictures.class));
                break;

        }
    }
}
