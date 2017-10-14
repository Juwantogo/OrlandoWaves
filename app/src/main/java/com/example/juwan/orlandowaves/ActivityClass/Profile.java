package com.example.juwan.orlandowaves.ActivityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.juwan.orlandowaves.R;
import com.example.juwan.orlandowaves.TabChanger.TabHelper;
import com.example.juwan.orlandowaves.toAccess.Config;

public class Profile extends AppCompatActivity {
    SharedPreferences preferences;
    private String first;
    private String last;
    private int tabNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent objIntent = this.getIntent();
        tabNum = objIntent.getIntExtra("tabNum", 0); //tabNum is the number from TabHelper sent to this activity
        setUpTabs();

        View button = findViewById(R.id.logout);
        preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        first = preferences.getString(Config.fName, "");
        last = preferences.getString(Config.lName, "");
        TextView name = (TextView) findViewById(R.id.Name);
        name.setText(first + " " + last);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                }
        );

    }

    public void setUpTabs(){
        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(this,this, tabs);
    }

    private void logout(){
        preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Getting editor
        SharedPreferences.Editor editor = preferences.edit();

        //Puting the value false for loggedin
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //Putting blank value to email
        editor.putString(Config.EMAIL_SHARED_PREF, "");

        //Saving the sharedpreferences
        editor.commit();

        //Starting login activity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
