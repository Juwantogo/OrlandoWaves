package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.Fragments.BuyGameFragment;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.FragmentStatePagerAdapter;
import com.juwan.orlandowaves.toAccess.GridAdapter;
import com.juwan.orlandowaves.toAccess.games;
import com.juwan.orlandowaves.toAccess.UniversalImageLoader;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class Shop extends AppCompatActivity {

    private int tabNum;
    private FragmentStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private CoordinatorLayout mCoordinatorLayout;
    private GridView gridView;
    private static final int NUM_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        setUpTabs();
        mViewPager = (ViewPager) findViewById(R.id.container);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_shop);
        gridView = (GridView) findViewById(R.id.gridView);

                setUpFragments();
        setupGridView();
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


    //****************************************************GRID VIEW CODE!!!!!!!!!!!!!!!!
    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        final ArrayList<games> gameList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("team").child("games");//looks at game node
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){ //interates through all childs in query(games node: 1, 2...)
                    gameList.add(singleSnapshot.getValue(games.class));
                }
                //setup our image grid
                //int gridWidth = getResources().getDisplayMetrics().widthPixels;
                //int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                //gridView.setColumnWidth(imageWidth);
                String game = getString(R.string.SingleGames);

                ArrayList<String> imgUrls = new ArrayList<String>();
                ArrayList<String> price = new ArrayList<String>();
                ArrayList<String> name = new ArrayList<String>();
                for(int i = 0; i < 1; i++){
                    imgUrls.add(gameList.get(i).getImage_url()); //CHANGE - CHANGE
                }
                for(int i = 0; i < 1; i++){
                    price.add(gameList.get(i).getAdult_price()); //TO - To - 2
                }
                for(int i = 0; i < 1; i++){
                    name.add(game); //ONE - 1 - 1 - 1 ALWAYS
                }
                GridAdapter adapter = new GridAdapter(Shop.this, price, name, imgUrls);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

    }
    //****************************************************GRID VIEW CODE!!!!!!!!!!!!!!!!
}
