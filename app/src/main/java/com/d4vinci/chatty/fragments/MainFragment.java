package com.d4vinci.chatty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.d4vinci.chatty.R;
import com.d4vinci.chatty.models.MyUser;
import com.d4vinci.chatty.models.UserHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    public OnLogoutButtonClickedListener logoutButtonClickedListener;
    private RecyclerView rvUsers;
    private FirebaseRecyclerAdapter<MyUser, UserHolder> adapter;

    public interface OnLogoutButtonClickedListener {
        public void onLogoutButtonClicked();
    }

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        FirebaseDatabase.getInstance()
                .getReference("userProfiles")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(new MyUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid()));

        Log.d(TAG, "onCreateView: we have a user here: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

        TextView tvLogout = (TextView) view.findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButtonClickedListener.onLogoutButtonClicked();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userProfiles");
        rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);
        adapter = new FirebaseRecyclerAdapter<MyUser, UserHolder>(MyUser.class, R.layout.userholder, UserHolder.class, ref) {
            @Override
            protected void populateViewHolder(UserHolder userHolder, MyUser myUser, int position) {
                userHolder.updateUI(myUser);
            }
        };
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        final TextView tvMyName = (TextView) view.findViewById(R.id.tvMyName);
        FirebaseDatabase.getInstance()
                .getReference("userProfiles")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvMyName.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            logoutButtonClickedListener = (OnLogoutButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement OnLogoutButtonClickedListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
