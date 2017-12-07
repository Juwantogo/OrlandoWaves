package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.juwan.orlandowaves.Fragments.BuyGameFragment;
import com.juwan.orlandowaves.Fragments.ShopFragment;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.FragmentStatePagerAdapter;
import com.juwan.orlandowaves.toAccess.games;

public class Shop extends AppCompatActivity implements ShopFragment.OnGridItemSelectedListener{

    //Paypal Configuration Object


    @Override
    public void onGridItemSelected(String name, int activityNumber) {
        //going to Game Fragment
        BuyGameFragment fragment = new BuyGameFragment();
        Bundle args = new Bundle();
        args.putString(getString(R.string.itemName), name);
        args.putString(getString(R.string.itemType), getString(R.string.SingleGames));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer,fragment);
        transaction.addToBackStack(getString(R.string.buyGameFrag));
        transaction.commit();
    }

//    @Override
//    public void onGridItemSelected2() {
//        //going to Game Fragment
//        BuyGameFragment fragment = new BuyGameFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(getString(R.string.itemName), game);
//        args.putString(getString(R.string.itemType), getString(R.string.SingleGames));
//        fragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragContainer,fragment);
//        transaction.addToBackStack(getString(R.string.buyGameFrag));
//        transaction.commit();
//    }

    public void reloadCart(Fragment fragment){
        getSupportFragmentManager().beginTransaction().detach(fragment).commitNowAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().attach(fragment).commitAllowingStateLoss();
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


//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e("DEBUG", "onResume of HomeFragment");
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
//        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Query query = myRef.child("user_account").child(userID);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                 @Override
//                                                 public void onDataChange(DataSnapshot dataSnapshot) {
//                                                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                         long FBC = ds.getValue(users.class).getFbc();
//                                                         SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                                                         SharedPreferences.Editor editor = preferences.edit();
//                                                         editor.putLong(Config.fbc, FBC);
//                                                     }
//
//                                                 }
//
//                                                 @Override
//                                                 public void onCancelled(DatabaseError databaseError) {
//
//                                                 }
//                                             });
//                //****************************************************GRID VIEW CODE!!!!!!!!!!!!!!!!
//    }

//    @Override
//    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
//        super.onDestroy();
//    }


//    @Override
//    public void onBackPressed() {
//        int i = getFragmentManager().getBackStackEntryCount() - 1;
//        String checkName = getFragmentManager().getBackStackEntryAt(i).getName();
//        if(checkName == getString(R.string.payFrag)){
//            getFragmentManager().popBackStack(getString(R.string.payFrag), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }
//        else{
//            super.onBackPressed();
//        }
//    }
}
