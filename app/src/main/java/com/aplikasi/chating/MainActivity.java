package com.aplikasi.chating;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aplikasi.chating.pojo.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private TextView txt_user;
    private EditText edName, edEmail;
    private Button btn_save;
    private String userId;
    private LinearLayout linear_data;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_user = (TextView) findViewById(R.id.txt_user);
        edName = (EditText) findViewById(R.id.name);
        edEmail = (EditText) findViewById(R.id.email);
        btn_save = (Button) findViewById(R.id.btn_save);
        linear_data = (LinearLayout) findViewById(R.id.linear_data);

        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mFirebaseInstance.setPersistenceEnabled(true);
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("MainActivity", "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.wtf("MainActivity", String.format("Failed to read app title value.%s", error.toException()));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(userId)) {
                    createUser(name, email);
                } else {
                    updateUser(name, email);
                }
            }
        });

        //toggleButton();

        addUserChangeListener2();

    }

    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btn_save.setText("Save");
        } else {
            btn_save.setText("Update");
        }
    }

    private void createUser(String name, String email) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, email);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener2() {
        final DatabaseReference user = mFirebaseInstance.getReference("users");
        /*  User us = dataSnapshot.getValue(User.class);
            Log.wtf("name", "=> " + us.name);
            Log.wtf("email", "=> " + us.email);
        * */

        user.addChildEventListener(new ChildEventListener() { // sumber : https://firebase.google.com/docs/database/admin/retrieve-data
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User us = dataSnapshot.getValue(User.class);
               /* Log.wtf("namex", "=> " + us.name);
                Log.wtf("emailx", "=> " + us.email);*/

                TextView textView = new TextView(MainActivity.this);
                textView.setText(us.name);
                linear_data.addView(textView);
                TextView textView2 = new TextView(MainActivity.this);
                textView2.setText(us.email);
                linear_data.addView(textView2);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.wtf("onDataChange", "User data is null!");
                    return;
                }

                Log.e("onDataChange", "User data is changed! " + user.name + ", " + user.email);

                // Display newly updated name and email
                txt_user.setText(user.name + ", " + user.email);

                // clear edit text
                edEmail.setText("");
                edName.setText("");

                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("OnCancelled", String.format("Failed to read user%s", error.toException()));
            }
        });
    }

    private void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }
}
