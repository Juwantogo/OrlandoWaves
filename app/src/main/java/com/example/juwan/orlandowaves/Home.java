package com.example.juwan.orlandowaves;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

//BASIC LOGIN CLASS THAT ATTACHES TO ALL TABS:FRAGMENTS - WHERE THE TABS ARE SET UP WITH FRAGMENTS

public class Home extends FragmentActivity implements TabLayout.OnTabSelectedListener{

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getSupportActionBar().setElevation(0f);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        //FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());
        // Set the adapter onto the view pager
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab){
        //viewPager.setCurrentItem(tab.getPosition(),true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
