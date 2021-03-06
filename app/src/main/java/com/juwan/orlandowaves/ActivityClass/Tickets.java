package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.juwan.orlandowaves.Fragments.TicketsFragment;
import com.juwan.orlandowaves.R;

public class Tickets extends AppCompatActivity {

    private int tabNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_shop);
        //gridView = (GridView) findViewById(R.id.gridView);


        TicketsFragment fragment = new TicketsFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.currentActivity), tabNum);
        fragment.setArguments(args);

        FragmentTransaction transaction = Tickets.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer, fragment);
        transaction.addToBackStack(getString(R.string.tixFrag));
        transaction.commit();

    }

}
