package com.pri.android.splitbills.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewGroupActivity extends AppCompatActivity {

    private EditText mGroupNameEt;
    private Button mCreateBt;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDetailsDatabaseRef;
    private DatabaseReference mDatabaseRef;

    private int numOfMembers = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE);
        final String email = sharedPreferences.getString(getString(R.string.email), "");
        final String email_replaced = email.replace('.', ',');
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDetailsDatabaseRef = mFirebaseDatabase.getReference().child("UserDetails").child(email_replaced).child("Groups");
        mDatabaseRef = mFirebaseDatabase.getReference();

        mGroupNameEt = (EditText)findViewById(R.id.group_name_et);
        mCreateBt = (Button)findViewById(R.id.create_bt);

        mCreateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = mGroupNameEt.getText().toString();
                GroupObject newGroup = new GroupObject();
                newGroup.setCreatedBy(email);
                newGroup.setCreationStatus(false);
                newGroup.setGroupName(groupName);
                newGroup.setSettlementStatus(false);
                newGroup.setLastTransactionName("NA");
                newGroup.setLastTransactionDate("NA");
                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                newGroup.setCreatedDate(formattedDate);
                DatabaseReference db_ref = mUserDetailsDatabaseRef.push();
                Log.d("push key", "" + db_ref.getKey());
                Toast.makeText(NewGroupActivity.this, "" + db_ref.getKey(), Toast.LENGTH_SHORT).show();
                newGroup.setGroupID(db_ref.getKey());
                db_ref.setValue(newGroup);
                mDatabaseRef.child(db_ref.getKey()).child("Users").child(email_replaced).child("Join Status").setValue(true);
                finish();
            }
        });
    }
}
