package com.juwan.orlandowaves;

import android.os.Bundle;

import com.juwan.orlandowaves.ActivityClass.Home;
import com.juwan.orlandowaves.R;

public class FirstTab extends Home {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_tab);
    }
}
