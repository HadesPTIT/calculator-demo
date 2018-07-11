package com.framgia.calculatordemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.framgia.calculatordemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CalculatorFragment()).commit();

    }
}

