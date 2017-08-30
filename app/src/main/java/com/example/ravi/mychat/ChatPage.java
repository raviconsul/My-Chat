package com.example.ravi.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class ChatPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    private ListView listView;

    // The Message View
    private EditText messageView;

    // Send Button
    private ImageButton send;

    // Test String
    private String[] array = {"A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A", };

    // Person Name and UID
    private String personName;
    private String personUID;
    private String myName;

    // Current User UID
    private String myUID;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = root.getReference();

    // Reference to Message Field
    DatabaseReference messageRef = rootRef.child("Messages");

    // Reference to One On One chat
    DatabaseReference oneOnOne;
    //DatabaseReference test = messageRef.child(personUID + "--" + myUID);

    // Keeping track of how many messages user has sent so that MSG count starts from the previous left number
    DatabaseReference mcount = rootRef.child("MsgCount");

    // Creating separate fields for each user to track different MSG count for different chats
    DatabaseReference myMsgCount;

    boolean d;

    private int msgCount = 0;

    // List of messages for One On One chat
    private ArrayList<String> messages;

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        mAuth = FirebaseAuth.getInstance();

        myUID = mAuth.getCurrentUser().getUid();

        // Intialising with current UID
        myMsgCount = mcount.child(myUID);

        // Get the person username and UID in the strings through Intent
        personName = getIntent().getStringExtra("NAME");
        personUID = getIntent().getStringExtra("UID");
        myName = getIntent().getStringExtra("MY_NAME");

        messages = new ArrayList<String>();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };


        // Setting the Toolbar with title person Name
        Toolbar toolbar = (Toolbar) findViewById(R.id.inc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(personName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);
        messageView = (EditText) findViewById(R.id.editText);
        send = (ImageButton) findViewById(R.id.send);


        Log.i("UID", personUID);

        // Setting initial refrence to null to avoid null pointer exception
        oneOnOne = messageRef.child("NULL");

        // Check wether particular field already exists
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(personUID + "--" + myUID))
                {
                    // Second person has already created the chat field, so go ahead to that path
                    oneOnOne = messageRef.child(personUID + "--" + myUID);
                    //Toast.makeText(getApplicationContext(), oneOnOne.toString(), Toast.LENGTH_SHORT).show();

                }
                else {
                    // No field created for these two person chat, so create a field and set the path
                    oneOnOne = messageRef.child(myUID + "--" + personUID);
                    //Toast.makeText(getApplicationContext(), oneOnOne.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Database Error: ", databaseError.toString());
            }
        });


        // Creating progress dialog for better loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Connection...");
        progressDialog.show();

        // Tracks the realtime Database Syncing and done after 2 sec to ensure oneOnOne to be set to a fiels
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                oneOnOne.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String value = dataSnapshot.getValue().toString();
                        Log.i("JSON VALUE", value);

                        // Adding value to messages and then showing in the list view
                        messages.add(value);
                        final ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_list_item, messages);
                        listView.setAdapter(arrayAdapter);

                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                // Select the last row so it will scroll into view...
                                listView.setSelection(arrayAdapter.getCount() - 1);
                            }
                        });

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

                        Log.i("Database Error: ", databaseError.toString());

                    }
                });

                progressDialog.dismiss();
            }
        }, 2000);

        // Updating and getting the msgCount value
        myMsgCount.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                String value = dataSnapshot.getValue().toString();
                Log.i("VALUE", value);
                Log.i("VALUE 1", dataSnapshot.getKey());

                // Gets the Key(Parent) of the particular field
                String key = dataSnapshot.getKey();

                // Check if it is a new chat or previous one to avoid overriding of MSG count
                if (key.equals(myUID + "--" + personUID))
                {
                    msgCount = Integer.parseInt(value);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue().toString();
                Log.i("VALUE", value);

                // Updating MSG count with every chat
                msgCount = Integer.parseInt(value);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Database Error: ", databaseError.toString());
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent homeIntent = new Intent(this, ChatList.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    // Sends Message
    public void sendMessage(View view)
    {
        String msg = messageView.getText().toString();
        messageView.setText("");


        // Checking if user accidently taps send button without typing any text
        if (msg == null || msg.isEmpty())
        {
            Toast.makeText(this, "C'mon, Write something", Toast.LENGTH_SHORT).show();
        }
        else {
            oneOnOne.child(myUID + "-" + msgCount).setValue(myName + ":- " + msg);

            msgCount++;

            // Setting the updated value of MSG count in the database
            myMsgCount.child(myUID + "--" + personUID).setValue(msgCount);
        }


    }


}

















































