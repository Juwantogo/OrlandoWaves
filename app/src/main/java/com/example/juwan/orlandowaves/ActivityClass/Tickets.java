package com.example.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juwan.orlandowaves.R;
import com.example.juwan.orlandowaves.TabChanger.TabHelper;

public class Tickets extends AppCompatActivity {

    private int tabNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        setUpTabs();

    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this, this,tabs);
    }
}
