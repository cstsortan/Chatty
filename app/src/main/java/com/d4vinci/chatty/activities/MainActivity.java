package com.d4vinci.chatty.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.d4vinci.chatty.R;
import com.d4vinci.chatty.fragments.LoginFragment;
import com.d4vinci.chatty.fragments.MainFragment;
import com.d4vinci.chatty.models.UserHolder;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity
        extends AppCompatActivity
        implements LoginFragment.OnLoginButtonClickedListener,
        MainFragment.OnLogoutButtonClickedListener,
        UserHolder.OnUserChosen{

    private static final int RC_SIGN_IN = 123;
    private LoginFragment loginFragment;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }

        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }

        if (findViewById(R.id.container_main) != null) {
            if (savedInstanceState != null) {
                return;
            }

            if(FirebaseAuth.getInstance().getCurrentUser()==null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_main, loginFragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container_main, mainFragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginButtonClicked() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Toast.makeText(this, "already signed in, going to messages", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_main, mainFragment)
                    .commit();
        } else {
            // not signed in
            Toast.makeText(this, "let's sign up!", Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, mainFragment)
                        .commit();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Signed in cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "Unknown sign in response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogoutButtonClicked() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container_main, loginFragment)
                                .commit();
                    }
                });
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserChosen(String uid) {
        startActivity(ChatActivity.launcher(this, uid));
    }
}
