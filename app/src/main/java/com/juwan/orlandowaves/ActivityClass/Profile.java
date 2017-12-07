package com.juwan.orlandowaves.ActivityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.Config;
import com.juwan.orlandowaves.toAccess.users;

import static android.content.ContentValues.TAG;

public class Profile extends AppCompatActivity {
    SharedPreferences preferences;
    private String first;
    private String last;
    private long fbc;
    private int tabNum;
    private String userID;
    private TextView name;
    private TextView FBCtv;
    SharedPreferences.Editor editor;
    TabLayout tabs;


    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        name = (TextView) findViewById(R.id.Name);
        FBCtv = (TextView) findViewById(R.id.FBC);
        setUpTabs();
        setupFirebaseAuth();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

        View button = findViewById(R.id.logout);
        preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        //first = preferences.getString(Config.fName, "");
        //last = preferences.getString(Config.lName, "");


        //name.setText(first + " " + last);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                }
        );

    }

    private void setUserInfo(users newUserInfo){
        //newUserInfo already has the info we need so:::    newUserInfo.getEmail();
        name.setText(newUserInfo.getFullname());
        fbc = newUserInfo.getFbc();
        if(fbc == 0){
            FBCtv.setText(R.string.notfbc);
            editor.putLong(Config.fbc, fbc);
            editor.commit();
            FBCtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Profile.this, FBCpush.class));
                }
            });
        }
        else{
            FBCtv.setText(R.string.fbc);
            editor.putLong(Config.fbc, fbc);
            editor.commit();
            FBCtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabs.getTabAt(1).select();
                }
            });
        }

    }

    public void setUpTabs(){
        tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this,this, tabs);
    }

    private void logout(){
        mAuth.signOut();
        finish();
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDB = FirebaseDatabase.getInstance();   // COPY of Database
        myRef = mFirebaseDB.getReference(); //Database. Reference which makes reference of the whole DB

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

                    Log.d(TAG, "onAuthStateChanged: navigating back to login screen.");
                    Intent intent = new Intent(Profile.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setUserInfo(getUserInfo(dataSnapshot));
                //call setUserInfo Function which sets the GOTTEN data to it's respective view on the profile page. as setUserInfos(users newUserInfo)
                //getUserInfo already returns "info" which is a users method instance;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //new users class with name getUserInfo -- DataSnapshot is the information being passed into it
    //user_accounts
    // ***auth-email-fbc-fullname-phone-userid
    // ***auth
    //dataSnapchat
    private users getUserInfo(DataSnapshot dataSnapshot) {
        users info = new users();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if(ds.getKey().equals("user_accounts")){
                try {
                    //info = new app side user
                    //ds gets children
                    //ds.get Child with /key "user_accounts"
                    //users method setsWHATEVER - ds with KEY . get child with userID
                    //getaValue using the users class
                    //get Whatever function you want
                    info.setEmail(
                            ds.child(userID)
                                    .getValue(users.class)
                                    .getEmail()
                    );
                    info.setFbc(
                            ds.child(userID)
                                    .getValue(users.class)
                                    .getFbc()
                    );
                    info.setFullname(
                            ds.child(userID)
                                    .getValue(users.class)
                                    .getFullname()
                    );
                    info.setPhone(
                            ds.child(userID)
                                    .getValue(users.class)
                                    .getPhone()
                    );
                    info.setUserid(
                            ds.child(userID)
                                    .getValue(users.class)
                                    .getUserid()
                    );
                }
                catch(NullPointerException e){
                    Log.e(TAG, "getUserInfo");
                }
            }
        }
        return info;
        //return this info object that was MADE from the users method:)
    }

}
