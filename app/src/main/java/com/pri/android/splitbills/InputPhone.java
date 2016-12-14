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

        mProgressDialog = new ProgressDialog(this);

        mSubmitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersDatabaseRef.child(mUID).child("phone").setValue(mPhoneNumEt.getText().toString());
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.phoneNumSaved), true);
                editor.commit();
                finish();
            }
        });
    }
}
