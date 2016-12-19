package com.pri.android.splitbills.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pri.android.splitbills.Model.BillObject;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewBillActivity extends AppCompatActivity {

    private EditText mBillNameEt;
    private EditText mDescriptionEt;
    private EditText mAmountEt;
    private Button mCreateBill;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupHistoryDatabaseRef;
    private DatabaseReference mUserDetailsGroupDatabaseRef;

    private GroupObject mModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        mBillNameEt = (EditText) findViewById(R.id.bill_name_et);
        mDescriptionEt = (EditText)findViewById(R.id.bill_description_et);
        mAmountEt = (EditText)findViewById(R.id.bill_amount_et);
        mCreateBill = (Button)findViewById(R.id.create_bill_bt);

        mModel = (GroupObject)getIntent().getSerializableExtra("GroupObject");
        sharedPreferences = getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupHistoryDatabaseRef = mFirebaseDatabase.getReference().child(mModel.getGroupID()).child("History");
        mUserDetailsGroupDatabaseRef = mFirebaseDatabase.getReference().child("UserDetails").child(sharedPreferences.getString(getString(R.string.email), "").replace('.', ',')).child("Groups").child(mModel.getGroupID());

        mCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBill();
            }
        });
    }

    private void addBill() {
        BillObject bill = new BillObject();
        bill.setPaidBy(sharedPreferences.getString(getString(R.string.email), ""));
        bill.setBillName(mBillNameEt.getText().toString());
        bill.setDescription(mDescriptionEt.getText().toString());
        bill.setAmount(mAmountEt.getText().toString());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        bill.setCreatedDate(formattedDate);
        DatabaseReference currentBillRef = mGroupHistoryDatabaseRef.push();
        String currentbillID = currentBillRef.getKey();
        bill.setBillID(currentbillID);
        currentBillRef.setValue(bill);
        mUserDetailsGroupDatabaseRef.child("lastTransactionName").setValue(mBillNameEt.getText().toString());
        mUserDetailsGroupDatabaseRef.child("lastTransactionDate").setValue(formattedDate);
        GroupDetailsActivity.mAdapter.notifyDataSetChanged();
        finish();
    }
}
