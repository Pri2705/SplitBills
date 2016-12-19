package com.pri.android.splitbills.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pri.android.splitbills.Model.BillObject;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.R;

public class BillDetailActivity extends AppCompatActivity {

    private GroupObject mGroupObject;
    private BillObject mBillObject;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private TextView mGroupName;
    private TextView mBillName;
    private TextView mDescription;
    private TextView mPaidByAndDate;
    private TextView mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        mGroupObject = (GroupObject)getIntent().getSerializableExtra("GroupObject");
        mBillObject = (BillObject)getIntent().getSerializableExtra("BillObject");

        mGroupName = (TextView)findViewById(R.id.group_name);
        mBillName = (TextView)findViewById(R.id.bill_name);
        mDescription = (TextView)findViewById(R.id.bill_description);
        mPaidByAndDate = (TextView)findViewById(R.id.paidby_date);
        mAmount = (TextView)findViewById(R.id.amount);

        mGroupName.setText(mGroupObject.getGroupName());
        mBillName.setText(mBillObject.getBillName());
        mDescription.setText(mBillObject.getDescription());
        mPaidByAndDate.setText("Paid By " + mBillObject.getPaidBy() + " on " + mBillObject.getCreatedDate());
        mAmount.setText("Amount : Rs. " + mBillObject.getAmount());

    }
}
