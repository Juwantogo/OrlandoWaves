package com.juwan.orlandowaves.ActivityClass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;

import static android.content.ContentValues.TAG;
import static com.juwan.orlandowaves.R.id.phone;

//BASIC LOGIN CLASS THAT ATTACHES TO ALL TABS:FRAGMENTS - WHERE THE TABS ARE SET UP WITH FRAGMENTS

public class Home extends AppCompatActivity{

    private ViewPager viewPager;
    private int tabNum;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        //getSupportActionBar().setElevation(0f);
        setUpTabs();
        setupFirebaseAuth();
    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this, this,tabs);
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        //checkCurrentUser(mAuth.getCurrentUser());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //checkCurrentUser(mAuth.getCurrentUser());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    private void checkCurrentUser(FirebaseUser user){
        if (user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
