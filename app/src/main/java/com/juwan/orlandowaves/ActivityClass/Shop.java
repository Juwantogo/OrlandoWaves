package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
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
import com.juwan.orlandowaves.Fragments.ProfileFragment;
import com.juwan.orlandowaves.Fragments.ShopFragment;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.FragmentStatePagerAdapter;
import com.juwan.orlandowaves.toAccess.GridAdapter;
import com.juwan.orlandowaves.toAccess.games;
import com.juwan.orlandowaves.toAccess.UniversalImageLoader;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class Shop extends AppCompatActivity implements ShopFragment.OnGridItemSelectedListener{


    @Override
    public void onGridItemSelected(games game, int activityNumber) {
        //going to Game Fragment
        BuyGameFragment fragment = new BuyGameFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.itemName), game);
        args.putString(getString(R.string.itemType), getString(R.string.SingleGames));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer,fragment);
        transaction.addToBackStack(getString(R.string.buyGameFrag));
        transaction.commit();
    }

    //make new griditemselectedListener-------------------------------------------------
    public interface OnGridItemSelectedListener{
        void onGridItemSelected(games game, int activityNumber);
    }

    OnGridItemSelectedListener monGridItemSelectedListener;
    //-------------------------------------------------------------------------

    private int tabNum;
    private FragmentStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private CoordinatorLayout mCoordinatorLayout;
    private GridView gridView;
    private static final int NUM_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        //mViewPager = (ViewPager) findViewById(R.id.container);
        //mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_shop);
        gridView = (GridView) findViewById(R.id.gridView);


        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.currentActivity), tabNum);
        fragment.setArguments(args);

        FragmentTransaction transaction = Shop.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer, fragment);
        transaction.addToBackStack(getString(R.string.shopFrag));
        transaction.commit();
    }

    //****************************************************GRID VIEW CODE!!!!!!!!!!!!!!!!
}
