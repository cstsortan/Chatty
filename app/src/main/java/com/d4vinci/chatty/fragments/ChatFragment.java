package com.d4vinci.chatty.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.d4vinci.chatty.R;
import com.d4vinci.chatty.models.Message;
import com.d4vinci.chatty.models.MessageHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // the fragment initialization parameter is just the uid of the user I wanna chat with
    private static final String ARG_USER_UID = "user_uid";

    private String userUid;

    private TextView btSend;
    private TextView etMessageField;
    private RecyclerView rvMessages;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference ref;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    public static ChatFragment newInstance(String uid) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userUid = getArguments().getString(ARG_USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        btSend = (Button) view.findViewById(R.id.btSend);
        etMessageField = (EditText) view.findViewById(R.id.etMessageField);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(etMessageField.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.push().setValue(message);
                FirebaseDatabase
                        .getInstance()
                        .getReference("userMessages")
                        .child(userUid)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push().setValue(message);
            }
        });

        rvMessages = (RecyclerView) view.findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ref = FirebaseDatabase.getInstance()
                .getReference("userMessages")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //my uid
                .child(userUid);   // the uid of the person I wanna chat

        adapter = new FirebaseRecyclerAdapter<Message, MessageHolder>(Message.class, R.layout.messageholder, MessageHolder.class, ref) {

            @Override
            protected void populateViewHolder(MessageHolder viewHolder, Message message, int position) {
                viewHolder.updateUI(message);
            }

            @Override
            public void onChildChanged(EventType type, DataSnapshot snapshot, int index, int oldIndex) {
                super.onChildChanged(type, snapshot, index, oldIndex);
                if(type.equals(EventType.ADDED)) {
                    rvMessages.smoothScrollToPosition(index);
                }
            }
        };
        rvMessages.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }
}
