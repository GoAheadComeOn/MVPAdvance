package com.lsh.mvpadvance.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lsh.mvpadvance.R;

public class MainActivity extends AppCompatActivity {

    //test for stage
    //testgfgd 
    public ViewDataBinding mViewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //commit test
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, null, false);
//        mViewBinding = DataBindingUtil.bind(rootView);
        setContentView(R.layout.activity_main);


    }
}
