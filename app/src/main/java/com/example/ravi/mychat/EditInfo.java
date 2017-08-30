package com.example.ravi.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditInfo extends AppCompatActivity {

    Button button;
    EditText mname, mphone;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView textView;
    ProgressDialog progressDialog;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = root.getReference();
    DatabaseReference users = rootRef.child("Users");

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        mAuth = FirebaseAuth.getInstance();

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.update);
        mname = (EditText) findViewById(R.id.editText);
        mphone = (EditText) findViewById(R.id.editText3);

        progressDialog = new ProgressDialog(this);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/android7.ttf");
        textView.setTypeface(typeface);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        };


    }


    public void toast(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void update(View view)
    {

        progressDialog.setMessage("Updating Info....");
        progressDialog.show();

        String name = mname.getText().toString();
        String phone = mphone.getText().toString();

        if (name == null || phone == null || name.isEmpty() || phone.isEmpty())
        {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast("Enter Valid Details");
                    progressDialog.dismiss();
                }
            }, 700);

        }
        else
        {
            UserInfo[] userInfos = new UserInfo[1];
            userInfos[0] = new UserInfo(name, phone, mAuth.getCurrentUser().getEmail());

            users.child(mAuth.getCurrentUser().getUid()).setValue(userInfos[0]);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast("Update Successful");
                    progressDialog.dismiss();
                    startActivity(new Intent(EditInfo.this, ChatList.class));
                }
            }, 700);

        }

        //progressDialog.dismiss();
    }

}





























