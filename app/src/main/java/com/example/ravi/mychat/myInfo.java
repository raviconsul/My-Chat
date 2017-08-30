package com.example.ravi.mychat;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class myInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    TextView title, mname, mphone, memail;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = root.getReference();
    DatabaseReference userRef = rootRef.child("Users");

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        title = (TextView) findViewById(R.id.myinfo);
        mname = (TextView) findViewById(R.id.mname);
        mphone = (TextView) findViewById(R.id.mphone);
        memail = (TextView) findViewById(R.id.memail);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        // Title Font
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/android7.ttf");
        title.setTypeface(typeface);

        floatingActionButton.setImageResource(R.drawable.ic_mode_edit_black_24dp);


        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue().toString();
                Log.i("JSON ", value);
                //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject object = new JSONObject(value);
                    JSONObject user = object.getJSONObject(mAuth.getCurrentUser().getUid());
                    //Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_SHORT).show();

                    String name = user.getString("name");
                    String phone = user.getString("phone");
                    String mail = user.getString("email");


                    mname.setText(name);
                    mphone.setText(phone);
                    memail.setText(mail);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void editInfo(View view)
    {
        startActivity(new Intent(getApplicationContext(), EditInfo.class));
    }
}


























