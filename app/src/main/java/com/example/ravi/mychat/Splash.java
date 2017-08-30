package com.example.ravi.mychat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView imageView;

    @Override
    protected void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        imageView = (ImageView) findViewById(R.id.iv);

        // Checking if user already logged in or not

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user == null)
                {
                    init();

                }
                else
                {
                    startActivity(new Intent(Splash.this, ChatList.class));
                    Log.i("SPLASH", "Working");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Splash.this.finish();
                        }
                    }, 2000);
                    //toast();
                }

            }
        },3000);
    }

    public void init()
    {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, "x");

        //Toast.makeText(this, "No current User", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent, options.toBundle());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash.this.finish();
            }
        }, 2000);
    }

    private void toast()
    {
        Toast.makeText(this, "User already logged in", Toast.LENGTH_SHORT).show();
    }


}
