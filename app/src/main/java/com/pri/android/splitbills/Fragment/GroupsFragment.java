package com.pri.android.splitbills.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.android.splitbills.Activity.GroupDetailsActivity;
import com.pri.android.splitbills.Activity.NewGroupActivity;
import com.pri.android.splitbills.GroupsHolder;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroupsFragment extends Fragment {

    private String mTitle;
    private Context mContext;
    private OnListFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter mAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGroupsDatabaseRef;
    private DatabaseReference mDatabaseRef;
    private String mEmail;

    private Button newGroupBt;
    private ProgressDialog mProgress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupsFragment newInstance(String title, Context context) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.tabTitle), title);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(getString(R.string.tabTitle));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        newGroupBt = (Button)view.findViewById(R.id.newGroupBt);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.my_prefs), getContext().MODE_PRIVATE);
        mEmail = sharedPreferences.getString(getString(R.string.email), "");
        mEmail = mEmail.replace('.', ',');

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();
        mGroupsDatabaseRef = mFirebaseDatabase.getReference().child("UserDetails").child(mEmail).child("Groups");

        // Set the adapter
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGroups);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading...");
        mProgress.show();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("UserDetails")){
                    if(dataSnapshot.child("UserDetails").hasChild(mEmail)){
                        if (dataSnapshot.child("UserDetails").child(mEmail).hasChild("Groups")){
                            mAdapter = new FirebaseRecyclerAdapter<GroupObject, GroupsHolder>(GroupObject.class, R.layout.fragment_grouplistitem, GroupsHolder.class, mGroupsDatabaseRef) {
                                @Override
                                protected void populateViewHolder(GroupsHolder viewHolder, final GroupObject model, int position) {
                                    mProgress.dismiss();
                                    viewHolder.setmGroupNameField(model.getGroupName());
                                    Log.v("lastTransactionName", "" + model.getLastTransactionName());
                                    Toast.makeText(getContext(), ""+ model.getLastTransactionName(), Toast.LENGTH_SHORT).show();
                                    if(model.getLastTransactionName() != "NA"){
                                        viewHolder.setmLastBillNameField(model.getLastTransactionName());
                                    }else{
                                        viewHolder.setmLastBillNameField("Created By: "+ model.getCreatedBy());
                                    }
                                    if(model.getLastTransactionDate() == "NA"){
                                        viewHolder.setmLastBillDateField(model.getCreatedDate());
                                    }else {
                                        viewHolder.setmLastBillDateField(model.getLastTransactionDate());
                                    }

                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent groupDetailsIntent = new Intent(getActivity(), GroupDetailsActivity.class);
                                            groupDetailsIntent.putExtra("GroupObject", model);
                                            startActivity(groupDetailsIntent);
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

                        }else{
                            mProgress.dismiss();
                        }
                    }else {
                        mProgress.dismiss();
                    }
                }else{
                    mProgress.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        newGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewGroupActivity.class));
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onListFragmentInteraction(DummyItem item);
    }
}
