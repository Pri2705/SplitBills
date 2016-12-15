package com.pri.android.splitbills;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
//
//    private TextView mUsernameTv;
//    private Button signOutBt;

    private ProgressDialog mProgress;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mUsernameTv = (TextView)findViewById(R.id.usernameTv);
//        signOutBt = (Button)findViewById(R.id.signoutBt);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mProgress = new ProgressDialog(this);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

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
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
//
//        signOutBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signout();
//            }
//        });
    }

    private void signout() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "signed out", Toast.LENGTH_SHORT).show();
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
//        mUsernameTv.setText(mUsername);
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


//    public class MyPagerAdapter extends FragmentPagerAdapter {
//        private int NUM_ITEMS = 3;

//        public MyPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        // Returns total number of pages
//        @Override
//        public int getCount() {
//            return NUM_ITEMS;
//        }
//
//        // Returns the fragment to display for that page
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0: // Fragment # 0 - This will show FirstFragment
////                    return FirstFragment.newInstance(0, "Page # 1");
//                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    return Groups.newInstance("Current Group", MainActivity.this);
//                case 2: // Fragment # 1 - This will show SecondFragment
////                    return SecondFragment.newInstance(2, "Page # 3");
//                default:
//                    return null;
//            }
//        }
//
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Page " + position;
//        }
//
//    }


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
