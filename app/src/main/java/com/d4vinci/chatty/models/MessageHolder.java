package com.d4vinci.chatty.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d4vinci.chatty.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by D4Vinci on 7/13/2017.
 */

public class MessageHolder extends RecyclerView.ViewHolder {

    private TextView tvAuthorName;
    private TextView tvText;

    public MessageHolder(View itemView) {
        super(itemView);
        tvAuthorName = (TextView) itemView.findViewById(R.id.tvAuthorName);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }

    public void updateUI(Message message) {
        tvText.setText(message.getText());
        FirebaseDatabase.getInstance()
                .getReference("userProfiles")
                .child(message.getUid())
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvAuthorName.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
