package com.danyalvarez.primicia;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.danyalvarez.primicia.util.Constants;
import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import twitter4j.User;


public class CelebrityActivity extends AppCompatActivity {

    private User user;

    private ImageView mCoverImageView;
    private ImageView mPhotoImageView;
    private TextView mNameTextView;

    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_celebrity);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.color.primary)
                .headerLayout(R.layout.activity_celebrity)
                .contentLayout(R.layout.activity_listview);
        setContentView(helper.createView(this));
        helper.initActionBar(this);


        ListView listView = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = new ArrayList<String>();
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        items.add("eesta es un apruba");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // User
        if (getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("user");
            Log.i(Constants.TAG_DEBUG, user.getName());
            // UI
            mCoverImageView = (ImageView) findViewById(R.id.coverImageView);
            mPhotoImageView = (ImageView) findViewById(R.id.photoImageView);
            mNameTextView = (TextView) findViewById(R.id.nameTextView);

            String urlCover = user.getProfileBannerURL();
            String urlPhoto = user.getBiggerProfileImageURL();
            Picasso.with(this).load(urlCover).into(mCoverImageView);
            Picasso.with(this).load(urlPhoto).into(mPhotoImageView);

            Log.i(Constants.TAG_DEBUG, urlPhoto);

            mNameTextView.setText(user.getName());


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.celebrity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
