package com.juwan.orlandowaves.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.Config;
import com.juwan.orlandowaves.toAccess.Currency;
import com.juwan.orlandowaves.toAccess.FragmentStatePagerAdapter;
import com.juwan.orlandowaves.toAccess.GridAdapter;
import com.juwan.orlandowaves.toAccess.games;
import com.juwan.orlandowaves.toAccess.users;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {


    //make new griditemselectedListener-------------------------------------------------
    public interface OnGridItemSelectedListener{
        void onGridItemSelected(String name, int activityNumber);
    }

    public interface OnGridItemSelectedListener2{
        void onGridItemSelected2(games game, int activityNumber);
    }

    OnGridItemSelectedListener monGridItemSelectedListener;
    OnGridItemSelectedListener monGridItemSelectedListener2;

    //-------------------------------------------------------------------------

    private int tabNum;
    private FragmentStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private CoordinatorLayout mCoordinatorLayout;
    private GridView gridView;
    private static final int NUM_GRID_COLUMNS = 3;
    private Context mContext;
    private View view;
    private long FBC;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Currency currency;
    String game, seasonpass;

    //private FirebaseMethods mFirebaseMethods;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_shop, container, false);
Button cart = view.findViewById(R.id.viewcart);
        //get arguments
        tabNum = getArguments().getInt(getString(R.string.currentActivity));
        currency = new Currency();

        setUpTabs();
        //mViewPager = (ViewPager) view.findViewById(R.id.container);
        //mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.activity_shop);
        gridView = (GridView) view.findViewById(R.id.gridView);
        mContext = getActivity();
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        setupFirebaseAuth();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getInstance().getCurrentUser().getUid();
        }
        //checkFBC();
        //setUpFragments();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart fragment = new Cart();
                //Bundle args = new Bundle();
                //args.putParcelable(getString(R.string.itemName), game);
                //args.putString(getString(R.string.itemType), getString(R.string.SingleGames));
                //fragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragContainer,fragment);
                transaction.addToBackStack(getString(R.string.shopFrag));
                transaction.commit();
            }
        });
        return view;
    }

    private void setUpFragments(){
        //pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager());
        //pagerAdapter.addFragment(new BuyGameFragment(), "Buy Game Fragment");
        //setup add BuyGameFragment to NEW FSpagerAdapter hench why the number is set on the item in the list
    }

    @Override
    public void onAttach(Context context) {
        try{
            monGridItemSelectedListener = (OnGridItemSelectedListener) getActivity();
            monGridItemSelectedListener2 = (OnGridItemSelectedListener) getActivity();
            mContext = (FragmentActivity) context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    private void setViewPager(int fragmentNumber){
        mCoordinatorLayout.setVisibility(View.GONE);
        //navigating to fragmentNumber
        mViewPager.setAdapter(pagerAdapter);//ViewPager to PAGERADAPTER
        mViewPager.setCurrentItem(fragmentNumber); //setPage to fragNUMBER
    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(getActivity(), getActivity(),tabs);
    }

    //public void onAttach(Fragment fragment) = only used for attaching fragment to parent activity


    //****************************************************GRID VIEW CODE!!!!!!!!!!!!!!!!
    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        //DB-team-games
        final ArrayList<games> gameList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference;//.child("team").child("games"); //looks at game node
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot gameSS = dataSnapshot.child("team").child("games");
                DataSnapshot seasonSS = dataSnapshot.child("items").child("season_pass");
                for ( DataSnapshot singleSnapshot :  gameSS.getChildren()){//
                    //interates through all childs in query(games node: 1, 2...)
                    gameList.add(singleSnapshot.getValue(games.class));
                    Log.e(TAG, "check FBC" +  FBC);
                    //add each entire node aka singleSnapshot=[i...i*n] to gameList array attach the games.class to it
                    //gameList.i -> i*n = each node taken
                }
                //Toast.makeText(getActivity(), "getting in",Toast.LENGTH_SHORT).show();
                //setup our image grid
                //int gridWidth = getResources().getDisplayMetrics().widthPixels;
                //int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                //gridView.setColumnWidth(imageWidth);
                if(getActivity()!= null){
                    game = getString(R.string.SingleGames);
                    seasonpass = getString(R.string.Season);
                }

                //new arraylists for everything listed in gridview
                final ArrayList<String> imgUrls = new ArrayList<String>();
                final ArrayList<String> price = new ArrayList<String>();
                final ArrayList<String> name = new ArrayList<String>();
                for(int i = 0; i < 1; i++){
                    imgUrls.add(gameList.get(i).getImage_url()); //CHANGE - CHANGE
                }
                for(int i = 0; i < 1; i++){
                    if(FBC != 0L){
                        price.add(currency.stringPercent(gameList.get(i).getAdult_price(),10));
                    }
                    else{
                        price.add(gameList.get(i).getAdult_price()); //TO - To - 2

                    }
                }
                for(int i = 0; i < 1; i++){
                    name.add(game); //ONE - 1 - 1 - 1 ALWAYS
                }
                name.add(seasonSS.child("name").getValue().toString());
                price.add(seasonSS.child("price").getValue().toString());
                imgUrls.add(seasonSS.child("image_url").getValue().toString());
                //set EverythingUp in the FIRST GRID VIEW
                GridAdapter adapter = new GridAdapter(getActivity(), price, name, imgUrls);
                gridView.setAdapter(adapter);


                //setOnClickListener for each item AdapterView automatically gets gridViews adapter for positioning!!!!
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //       //pass a game from gameList, and tabNum
                        if(position == 0){
                            monGridItemSelectedListener.onGridItemSelected(name.get(position), tabNum);
                        }
                        else{
                            monGridItemSelectedListener.onGridItemSelected(name.get(position), tabNum);
                            String text = name.get(position);
                            Log.e(TAG, "check textView TEXT: " +  text);

                            //monGridItemSelectedListener2.onGridItemSelected((Te));

                        }

                   }});
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
                Toast.makeText(getActivity(), "NOT getting in",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getInstance().getCurrentUser().getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                //setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));
                checkFBC(dataSnapshot);
                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //new users class with name getUserInfo -- DataSnapshot is the information being passed into it
    //user_accounts
    // ***auth-email-fbc-fullname-phone-userid
    // ***auth
    //dataSnapchat
    private void checkFBC(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if(ds.getKey().equals("user_accounts")){
                try{
                    FBC = ds.child(userID).getValue(users.class).getFbc();
                    editor.putLong(Config.fbc, FBC);
                    editor.commit();
                    Log.e(TAG, "check FBCSP " + preferences.getLong(Config.fbc, FBC));
                }catch(NullPointerException e){

                }
                setupGridView();
            }
        }


        //return info;
        //return this info object that was MADE from the users method:)
    }
}