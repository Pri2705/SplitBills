package com.pri.android.splitbills.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pri.android.splitbills.Activity.GroupDetailsActivity;
import com.pri.android.splitbills.Model.GroupObject;
import com.pri.android.splitbills.GroupsHolder;
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
    private DatabaseReference mDatabaseRef;
    private String mUsername;

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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.my_prefs), getContext().MODE_PRIVATE);
        mUsername = sharedPreferences.getString(getString(R.string.username), "Anonymous");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference().child("UserDetails").child(mUsername).child("Groups");

        // Set the adapter
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGroups);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mAdapter = new FirebaseRecyclerAdapter<GroupObject, GroupsHolder>(GroupObject.class, R.layout.fragment_grouplistitem, GroupsHolder.class, mDatabaseRef) {
            @Override
            protected void populateViewHolder(GroupsHolder viewHolder, final GroupObject model, int position) {
                viewHolder.setmGroupNameField(model.getGroupName());
                viewHolder.setmLastBillNameField(model.getLastTransactionName());
                viewHolder.setmLastBillDateField(model.getLastTransactionDate());

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

            recyclerView.setAdapter(mAdapter);

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
