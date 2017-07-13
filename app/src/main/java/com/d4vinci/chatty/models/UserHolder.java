package com.d4vinci.chatty.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.d4vinci.chatty.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by D4Vinci on 7/12/2017.
 */

public class UserHolder extends RecyclerView.ViewHolder {

    private OnUserChosen userChosen;

    public interface OnUserChosen {
        public void onUserChosen(String uid);
    }

    private TextView tvName;
    public UserHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvHolderUserName);
    }

    public void updateUI(final MyUser myUser) {
        try {
            userChosen = (OnUserChosen) itemView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(itemView.getContext().toString() + " can't must have implemented OnUserChosen");
        }
        tvName.setText(myUser.getName());
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myUser.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Toast.makeText(itemView.getContext(), "Let's chat with "+myUser.getName(), Toast.LENGTH_SHORT).show();
                    userChosen.onUserChosen(myUser.getUid());
                } else {
                    Toast.makeText(itemView.getContext(), "Don't chat with yourself!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
