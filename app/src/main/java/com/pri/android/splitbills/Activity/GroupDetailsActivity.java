package com.pri.android.splitbills.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.android.splitbills.BillsHolder;
import com.pri.android.splitbills.Fragment.GroupsFragment;
import com.pri.android.splitbills.Model.BillObject;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.Model.UsersOfAGroupObject;
import com.pri.android.splitbills.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupDatabaseRef;
    private DatabaseReference mUsersDatabaseRef;
    public static FirebaseRecyclerAdapter mAdapter;

    private TextView grpName;
    private TextView participants;
    private RecyclerView recyclerView;
    private Button createNewBill;
    private Button viewSummary;

    private ProgressDialog mProgress;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<String> mParticipantsEmail;
    private ArrayList<String> mParticipantsName;

    private GroupObject mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        mModel = (GroupObject) getIntent().getSerializableExtra("GroupObject");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mGroupDatabaseRef = mFirebaseDatabase.getReference().child(mModel.getGroupID());
        mUsersDatabaseRef = mFirebaseDatabase.getReference().child("Users");

        grpName = (TextView)findViewById(R.id.group_name);
        participants = (TextView)findViewById(R.id.participants);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_bills);
        createNewBill = (Button)findViewById(R.id.create_new_bill);
        viewSummary = (Button)findViewById(R.id.view_summary);

        mParticipantsName = new ArrayList<>();
        mParticipantsEmail = new ArrayList<>();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading");
        mProgress.show();

        grpName.setText(mModel.getGroupName());

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        mGroupDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("History")) {
                    setAndPopulateAdapter();

                } else {
                    mProgress.dismiss();
                    setAndPopulateAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mGroupDatabaseRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, UsersOfAGroupObject> map = (HashMap<String, UsersOfAGroupObject>) dataSnapshot.getValue();
                Set<String> keysSet = map.keySet();
                mParticipantsEmail = new ArrayList<String>(keysSet);
                Toast.makeText(GroupDetailsActivity.this, "" + mParticipantsEmail, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUsersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(String email: mParticipantsEmail){
                    mParticipantsName.add((String)dataSnapshot.child(email).child("name").getValue());
                    String participantsNames = "Participants: ";
                    for(String name:mParticipantsName){
                        participantsNames += name + ", ";
                    }
                    if(participantsNames.length()>1)
                        participantsNames = participantsNames.substring(0, participantsNames.length()-2);
                    participants.setText(participantsNames);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        createNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newBill = new Intent(GroupDetailsActivity.this, NewBillActivity.class);
                newBill.putExtra("GroupObject", mModel);
                startActivity(newBill);
            }
        });
    }

    private void setAndPopulateAdapter() {
        mAdapter = new FirebaseRecyclerAdapter<BillObject, BillsHolder>(BillObject.class, R.layout.bill_listitem, BillsHolder.class, mGroupDatabaseRef.child("History")) {
            @Override
            protected void populateViewHolder(BillsHolder viewHolder, final BillObject model, int position) {
                mProgress.dismiss();
                viewHolder.setmBillName(model.getBillName());
                viewHolder.setmPaidBy("Paid By: " + model.getPaidBy());
                viewHolder.setmAmount("Amount: " + model.getAmount());

                viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent billDetailsIntent = new Intent(GroupDetailsActivity.this, BillDetailActivity.class);
                        billDetailsIntent.putExtra("BillObject", model);
                        billDetailsIntent.putExtra("GroupObject", mModel);
                        startActivity(billDetailsIntent);
                    }
                });
            }
        };

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
            }
        });

        recyclerView.setAdapter(mAdapter);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }


    @Override
    public void onPause() {
        super.onPause();
//        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GroupsFragment.mAdapter.notifyDataSetChanged();
    }
}
