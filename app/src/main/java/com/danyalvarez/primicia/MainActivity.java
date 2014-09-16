package com.danyalvarez.primicia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.danyalvarez.primicia.classes.Resource;
import com.danyalvarez.primicia.fragments.ItemFragment;
import com.danyalvarez.primicia.util.Constants;
import com.danyalvarez.primicia.util.Util;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ArrayList<Resource> mResources;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        detectConnection();
        setTitles();


        // Create the adapter that will return a fragment fo
        // r each of the three
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

    private void detectConnection() {
        if (!Util.isOnline(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_network);
            builder.setMessage(R.string.no_network_detail)
                    .setPositiveButton(R.string.go_config, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create();
            builder.show();
        }

    }

    private void setTitles() {
        mResources = new ArrayList<Resource>();
        mResources.add(new Resource(Constants.TWITTER_BO_COMMUNICATION_LIST_ID, getString(R.string.communication), Constants.LIST_FEED));
        mResources.add(new Resource(Constants.TWITTER_BO_JOURNALISTS_LIST_ID, getString(R.string.journalists), Constants.LIST_FEED));
        mResources.add(new Resource(Constants.TWITTER_BO_SPORTS_LIST_ID, getString(R.string.sports), Constants.LIST_FEED));
        mResources.add(new Resource(Constants.TWITTER_BO_SERVICES_LIST_ID, getString(R.string.services), Constants.LIST_FEED));
        mResources.add(new Resource(Constants.TWITTER_BO_POLITICIANS_LIST_ID, getString(R.string.politicians), Constants.LIST_FEED));
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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

}
