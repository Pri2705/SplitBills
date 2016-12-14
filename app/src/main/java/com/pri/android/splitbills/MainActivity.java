package com.pri.android.splitbills;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;

    private static final int RC_SIGN_IN = 1;
    private static final String ANONYMOUS = "Anonymous";
    private String mUsername;
    private String mUID;

    private TextView mUsernameTv;
    private Button signOutBt;

    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameTv = (TextView)findViewById(R.id.usernameTv);
        signOutBt = (Button)findViewById(R.id.signoutBt);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mProgress = new ProgressDialog(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //signed in
                    onSignedInInitialize(user.getDisplayName(), user.getUid());
                }else{
                    //signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Unable to sign In!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedOutCleanup() {
        //what to do on signout
        mUsername = ANONYMOUS;
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE).edit();
        editor.putBoolean(getString(R.string.phoneNumSaved), false);
        editor.commit();

    }

    private void onSignedInInitialize(String displayName, String uid) {
        //what to do on signin
        mUsername = displayName;
        mUID = uid;
        checkUserExists();
        mUsernameTv.setText(mUsername);
    }

    private void checkUserExists() {
//        if(mUsersDatabaseReference.orderByKey().equalTo(mUID) == null){
//            Intent inputPhoneIntent = new Intent(MainActivity.this, InputPhone.class);
//            inputPhoneIntent.putExtra("mUID", mUID);
//            startActivity(inputPhoneIntent);
//        }

        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE);
        if(sharedPreferences.getBoolean(getString(R.string.phoneNumSaved), false) == false) {
            mProgress.setMessage("Checking Account...");
            mProgress.show();

            mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(mUID)) {
                        mProgress.dismiss();
                        Intent inputPhoneIntent = new Intent(MainActivity.this, InputPhone.class);
                        inputPhoneIntent.putExtra("mUID", mUID);
                        startActivity(inputPhoneIntent);
                    } else {
                        mProgress.dismiss();
                        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE).edit();
                        editor.putBoolean(getString(R.string.phoneNumSaved), true);
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}