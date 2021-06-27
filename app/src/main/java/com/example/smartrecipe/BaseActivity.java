package com.example.smartrecipe;


import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(constraintLayout);
    }
}
