package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SearchStream extends AppCompatActivity implements View.OnClickListener {
    public int image_offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stream);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        //setSupportActionBar(toolbar);
        findViewById(R.id.search_button).setOnClickListener(this);
        findViewById(R.id.more_search_result).setOnClickListener(this);

//        new Thread(new SearchStreamBackend(this)).start();

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new Thread(new SearchStreamBackend(this)).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_stream, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                TextView tv = (TextView) findViewById(R.id.search_stream);
                String text = tv.getText().toString();
                image_offset = 0;
                Log.d("Debug", "Button Clicked, Searching " +  text);
                new Thread(new SearchStreamBackend(this, text, 0)).start();
                break;
            case R.id.more_search_result:
                TextView tv2 = (TextView) findViewById(R.id.search_stream);
                String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, Searching " +  text2);
                image_offset+=8;
                new Thread(new SearchStreamBackend(this, text2, image_offset)).start();
                break;
        }
    }
}
