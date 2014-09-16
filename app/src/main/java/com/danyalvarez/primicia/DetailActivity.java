package com.danyalvarez.primicia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.danyalvarez.primicia.classes.Resource;
import com.danyalvarez.primicia.fragments.ItemFragment;
import com.danyalvarez.primicia.util.Constants;

import java.util.ArrayList;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserMentionEntity;


public class DetailActivity extends ActionBarActivity implements ActionBar.TabListener {

    private Status mStatus;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    private ArrayList<Resource> mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detectContent();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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

    */

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Resource resource = mResources.get(position);
            return ItemFragment.newInstance(resource.getId(), resource.getType(), resource.getValue());
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mResources.get(position).getName();
        }
    }

    public void detectContent() {
        mResources = new ArrayList<Resource>();

        if (getIntent().getExtras() != null) {
            mStatus = (Status) getIntent().getSerializableExtra("status");
            Log.i(Constants.TAG_DEBUG, mStatus.getText());

            User user = mStatus.getUser();
            mResources.add(new Resource(user.getId(), user.getName(), Constants.USER_FEED));

            if (mStatus.isRetweet()) {
                Status retweetStatus = mStatus.getRetweetedStatus();
                User userRoot = retweetStatus.getUser();
                mResources.add(new Resource(userRoot.getId(), userRoot.getName(), Constants.USER_FEED));
            }

            // Mentions
            UserMentionEntity[] userMentionEntities = mStatus.getUserMentionEntities();
            for (UserMentionEntity entity : userMentionEntities) {
                mResources.add(new Resource(entity.getId(), "@" + entity.getScreenName(), Constants.USER_FEED));
            }

            // Tags

            HashtagEntity[] hashtagEntities = mStatus.getHashtagEntities();
            for (HashtagEntity entity : hashtagEntities) {

                String hashtag = entity.getText();
                mResources.add(new Resource(hashtag, "#" + hashtag, Constants.HASHTAG_FEED));
            }


        } else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
