package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.juwan.orlandowaves.Fragments.BuyGameFragment;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.FragmentStatePagerAdapter;

public class Shop extends AppCompatActivity {

    private int tabNum;
    private FragmentStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        setUpTabs();
        mViewPager = (ViewPager) findViewById(R.id.container);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_shop);

                setUpFragments();
    }

    private void setUpFragments(){
        pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new BuyGameFragment(), "Buy Game Fragment");
        //setup add BuyGameFragment to NEW FSpagerAdapter hench why the number is set on the item in the list
    }

    private void setViewPager(int fragmentNumber){
        mCoordinatorLayout.setVisibility(View.GONE);
        //navigating to fragmentNumber
        mViewPager.setAdapter(pagerAdapter);//ViewPager to PAGERADAPTER
        mViewPager.setCurrentItem(fragmentNumber); //setPage to fragNUMBER
    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this, this,tabs);
    }
}
