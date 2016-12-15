package com.pri.android.splitbills;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputPhone extends AppCompatActivity {

    private EditText mPhoneNumEt;
    private Button mSubmitBt;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseRef;
    private DatabaseReference mUserDetailsDatabaseRef;

    private String mUID;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone);

        mPhoneNumEt = (EditText)findViewById(R.id.phoneNumEt);
        mSubmitBt = (Button)findViewById(R.id.submitBt);

        mUID = getIntent().getStringExtra("mUID");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseRef = mFirebaseDatabase.getReference().child("Users");
        mUserDetailsDatabaseRef = mFirebaseDatabase.getReference().child("User Details");

        mProgressDialog = new ProgressDialog(this);

        mSubmitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.phoneNum), mPhoneNumEt.getText().toString());
                editor.putBoolean(getString(R.string.phoneNumSaved), true);
                editor.commit();
                addToFirebaseDatabase();
                finish();
            }
        });
    }

    private void addToFirebaseDatabase() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE);
        String username = sharedPreferences.getString(getString(R.string.username), "");
        String phoneNum = sharedPreferences.getString(getString(R.string.phoneNum), "");
        String email = sharedPreferences.getString(getString(R.string.email), "");
        String uid = sharedPreferences.getString(getString(R.string.userUID), "");
        String photoUrl = sharedPreferences.getString(getString(R.string.photoUrl), "");
        email = email.replace('.', ',');
        mUsersDatabaseRef.child(email).child("phone").setValue(phoneNum);
        mUsersDatabaseRef.child(email).child("name").setValue(username);
        mUsersDatabaseRef.child(email).child("photoUrl").setValue(photoUrl);
        mUsersDatabaseRef.child(email).child("uid").setValue(uid);
        mUserDetailsDatabaseRef.child("Groups");
        mUserDetailsDatabaseRef.child("Friends");
        mUserDetailsDatabaseRef.child("ReceivedNotifications");
    }
}
