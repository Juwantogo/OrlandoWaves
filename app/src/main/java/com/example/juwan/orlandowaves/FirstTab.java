package com.example.juwan.orlandowaves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.juwan.orlandowaves.ActivityClass.Home;

public class FirstTab extends Home {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_tab);
    }
}
