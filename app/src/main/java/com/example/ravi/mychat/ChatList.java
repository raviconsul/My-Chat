package com.example.ravi.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
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

import java.util.ArrayList;
import java.util.Iterator;

public class ChatList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    // Nameand email of the user
    private String name;
    private String email;
    ProgressDialog progressDialog;

    // Refrence to the Database
    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = root.getReference();
    //DatabaseReference users = rootRef.child("Users");

    // Views in the navigation drawer
    TextView pName, pEmail;
    ListView listView;

    //private String[] array = {"A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A", };

    // Names of other users
    ArrayList<String> myNames;
    UserInfo[] userInfo;

    // User Id's of all users to create chat field
    ArrayList<String> userUID;

    // For the userInfo class Object
    private int count = 0;

    // To set different views on the nav drawer and the main view
    private boolean drawerCase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Chat");


        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Updating List...");
        progressDialog.show();

        listView = (ListView) findViewById(R.id.listView);


        myNames = new ArrayList<String>();
        userUID = new ArrayList<String>();


        userInfo = new UserInfo[100];

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if (!drawerCase)
               {
                   setContentView(R.layout.activity_chat_list);
               }
               else
               {
                   setContentView(R.layout.nav_header_chat_list);
               }

               if (mAuth.getCurrentUser() == null)
               {
                   startActivity(new Intent(ChatList.this, MainActivity.class));
               }
            }
        };

        email = mAuth.getCurrentUser().getEmail().toString();

        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    //Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                    String value = dataSnapshot.getValue().toString();
                    JSONObject arr = new JSONObject(value);
                    //name = arr.getString("name");
                    JSONObject object = arr.getJSONObject(mAuth.getCurrentUser().getUid());
                    name = object.getString("name");

                    Iterator iterator = arr.keys();
                    while (iterator.hasNext())
                    {
                        String key = (String)iterator.next();

                        Log.i("KEY", key);

                        if (!key.equals(mAuth.getCurrentUser().getUid()))
                        {
                            JSONObject page = arr.getJSONObject(key);

                            String uName = page.getString("name");
                            String uEmail = page.getString("email");
                            String uPhone = page.getString("phone");

                            myNames.add(uName);
                            userUID.add(key);
                            userInfo[count] = new UserInfo(uName, uPhone, uEmail);
                            //Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();


                            count++;
                        }

                    }

                    progressDialog.dismiss();
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_list_item, myNames);
                    listView.setAdapter(arrayAdapter);


                    //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    Log.i("JSON", userUID.toString());
                    Log.i("JSON User Info", userInfo.toString());



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

                Log.i("Database Error: ", databaseError.toString());

            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                drawerCase = true;

                //Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();

                pName = (TextView) findViewById(R.id.personName);
                pName.setText(name);

                pEmail = (TextView) findViewById(R.id.personEmail);
                pEmail.setText(email);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                drawerCase = false;

                //Toast.makeText(getApplicationContext(), "Also Working", Toast.LENGTH_SHORT).show();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //startActivity(new Intent(getApplicationContext(), ChatPage.class));
                //Toast.makeText(ChatList.this, "Item Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChatList.this, ChatPage.class);
                intent.putExtra("NAME", myNames.get(position));
                intent.putExtra("UID", userUID.get(position));
                intent.putExtra("MY_NAME", name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_list, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_info) {

            startActivity(new Intent(getApplicationContext(), myInfo.class));

        } else if (id == R.id.sign_out) {

            progressDialog.setMessage("Signing Out...");
            progressDialog.show();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Sign Out Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChatList.this, MainActivity.class));
            progressDialog.dismiss();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_help) {

            Intent intent = new Intent(
                    Intent.ACTION_SENDTO,
                    Uri.parse("mailto:15uec046@lnmiit.ac.in")
            );
            startActivity(intent);


        } else if (id == R.id.dev) {

            startActivity(new Intent(ChatList.this, Developers.class));

        } else if (id == R.id.nav_share) {

           Toast.makeText(getApplicationContext(), "Not yet available", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
