package com.d4vinci.chatty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.d4vinci.chatty.R;
import com.d4vinci.chatty.fragments.ChatFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    ChatFragment chatFragment;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uid = getIntent().getStringExtra("uid");
        FirebaseDatabase
                .getInstance()
                .getReference("userProfiles")
                .child(uid)
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toolbar.setTitle(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if (chatFragment == null) {
            chatFragment = ChatFragment.newInstance(uid);
        }

        if (findViewById(R.id.container_chat) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_chat, chatFragment)
                    .commit();
        }
    }

    public static Intent launcher(AppCompatActivity activity, String uid) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("uid", uid);
        return intent;
    }
}
