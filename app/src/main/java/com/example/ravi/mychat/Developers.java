package com.example.ravi.mychat;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Developers extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        textView = (TextView) findViewById(R.id.myTitle);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/android7.ttf");
        textView.setTypeface(typeface);
    }
}
