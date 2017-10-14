package com.example.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.juwan.orlandowaves.R;
import com.example.juwan.orlandowaves.TabChanger.TabHelper;
import com.example.juwan.orlandowaves.toAccess.FragmentPagerAdapter;

//BASIC LOGIN CLASS THAT ATTACHES TO ALL TABS:FRAGMENTS - WHERE THE TABS ARE SET UP WITH FRAGMENTS

public class Home extends AppCompatActivity{

    private ViewPager viewPager;
    private int tabNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        //getSupportActionBar().setElevation(0f);
        setUpTabs();

    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this, this,tabs);
    }
}
