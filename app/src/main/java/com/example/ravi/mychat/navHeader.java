package com.example.ravi.mychat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ravi on 21-07-2017.
 */

public class navHeader extends AppCompatActivity {

    TextView pName, pEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_chat_list);

        pName = (TextView) findViewById(R.id.personName);
        pEmail = (TextView) findViewById(R.id.personEmail);

        Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();

    }
}
