package com.statickidz.versioncheckerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.statickidz.versionchecker.VersionChecker;

public class MainActivity extends AppCompatActivity {

    VersionChecker versionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionChecker = new VersionChecker(this);
        versionChecker.check();
    }
}
