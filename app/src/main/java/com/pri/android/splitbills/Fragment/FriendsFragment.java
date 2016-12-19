package com.pri.android.splitbills.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.android.splitbills.FrinedsRecyclerViewAdapter;
import com.pri.android.splitbills.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends Fragment {

    // TODO: Customize parameters
    private static final String TAG = FriendsFragment.class.getName();
    private int mColumnCount = 1;
    Context mContext;
    String loggedInEmail;
    private Boolean isDataRetrieved = false;
    LinearLayout progressBar;
    RecyclerView recyclerView;
    ArrayList<String> friendsEmail = new ArrayList<>();
    ArrayList<ContactsFragment.UserDetails> nonFriendUserDetails = new ArrayList<>();
    ArrayList<ContactsFragment.UserDetails> friendUserDetails = new ArrayList<>();
    private DatabaseReference mFriendsRef;
    private DatabaseReference mUsersRef;
    private OnListFragmentInteractionListener mListener;
    private FrinedsRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getFriends();
    }

    public static FriendsFragment newInstance(String title, Context context) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.tabTitle), title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frineds_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        init(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (!isDataRetrieved) {
            getFriends();
            isDataRetrieved = true;
        }else{
            recyclerView.setAdapter(adapter);
        }
//        getFriends();
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        adapter = new FrinedsRecyclerViewAdapter(friendUserDetails);
//        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }

//        getFriends(context);
    }

    private void getFriends() {
        readFriendsFromServer(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess() {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                readAllUsersFromServer(new OnGetDataListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess() {
                        adapter = new FrinedsRecyclerViewAdapter(friendUserDetails);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

        });
    }

    private void readAllUsersFromServer(final OnGetDataListener listner) {
//        onDataCInside[0] = 0;
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                onDataCInside[0] = 1;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String key = messageSnapshot.getKey();
                    ContactsFragment.UserDetails userDetails = messageSnapshot.getValue(ContactsFragment.UserDetails.class);
                    userDetails.email = key.replace(",", ".");
                    if (loggedInEmail.equals(key)) {
                        continue;//same user as logged in user
                    }
                    if (friendsEmail.contains(key)) {
                        //friend found
                        friendUserDetails.add(userDetails);
                    } else {
                        nonFriendUserDetails.add(userDetails);
                    }
                }
                listner.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //1 if onDataChange executed else 0
//        if (onDataCInside[0] == 0) {
//            listner.onSuccess();
//        }

    }

    void readFriendsFromServer(final OnGetDataListener onGetDataListener) {
        onGetDataListener.onStart();

        SharedPreferences pref = mContext.getSharedPreferences(getString(R.string.my_prefs), MODE_PRIVATE);
        loggedInEmail = pref.getString(getString(R.string.email), "");
        loggedInEmail = loggedInEmail.replace('.', ',');

        mFriendsRef = FirebaseDatabase.getInstance().getReference().child("UserDetails").child(loggedInEmail).child("Friends");

        Log.v(TAG, "after ref");
        mFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                onDataCInside[0] = 1;
//                fnExecuted[0]++;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String key = messageSnapshot.getKey();
                    friendsEmail.add(key);
                }
                onGetDataListener.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //complete details of all users

        //1 if onDataChange executed else 0
//        if (onDataCInside[0] == 0) {
//            onGetDataListener.onSuccess();
//        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_list);
        progressBar = (LinearLayout) view.findViewById(R.id.friend_progress);

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
        void onListFragmentInteraction();
    }


    public interface OnGetDataListener {
        public void onStart();

        public void onSuccess();
    }

    public class UserEmailAndName {
        public String userName;
        public String userEmail;

        public UserEmailAndName() {
        }

        public UserEmailAndName(String email, String name) {
            userEmail = email;
            userName = name;
        }
    }
}
