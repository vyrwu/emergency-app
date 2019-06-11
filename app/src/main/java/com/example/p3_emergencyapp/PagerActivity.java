package com.example.p3_emergencyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Screen Slider
 */

public class PagerActivity extends FragmentActivity {

    /**
     * Number of Pages
     */
    private static final int NUM_PAGES = 2;

    /**
     * Pager widget, handles swipping
     */
    private ViewPager mPager;
    private TabLayout mTabLayout;
    private FrameLayout mTextSign;

    /**
     * Pager adapter, provides pages with pager widget.
     */
    private PagerAdapter mPagerAdapter;
    public WiFiP2pServiceManager serviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        setStatusBarTranslucent(true);


        // Instantiate ViewPager, tabLayout and PagerAdapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabDots);
        mTextSign = (FrameLayout) findViewById(R.id.layout_header);

        TextView devDeviceId = (TextView) findViewById(R.id.devDeviceId);
        devDeviceId.setText(WiFiP2pServiceManager.deviceId);

        setPaddingForUIElements(mTabLayout, mTextSign);

        mTabLayout.setupWithViewPager(mPager, true);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        serviceManager = WiFiP2pServiceManager.getInstance();
        serviceManager.initialize(getApplicationContext());
    }

    @Override
    public void onPause() {
        Log.w(WiFiP2pServiceManager.TAG, "onPause() run");
        serviceManager.cleanup();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.w(WiFiP2pServiceManager.TAG, "onResume() run");
        // serviceManager.startServiceDiscovery();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.w(WiFiP2pServiceManager.TAG, "onDestroy() run");
//        serviceManager.cleanup();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // BACK BUTTON HANDLER HERE
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void setPaddingForUIElements(TabLayout mTabLayout, FrameLayout mTextSign) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mTabLayout.setPadding(0,0,0,dpToPx(48));
            mTextSign.setPadding(0,dpToPx(50),0,0);
        } else {
            mTextSign.setPadding(0,dpToPx(30),0,0);
        }
    }

    private int dpToPx(float dpValue) {
        // The gesture threshold expressed in dp
        final float GESTURE_THRESHOLD_DP = dpValue;
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HelpPageFragment(); }
            else {
                return new SafePageFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}



