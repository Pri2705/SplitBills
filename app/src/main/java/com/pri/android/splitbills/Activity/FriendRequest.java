package com.pri.android.splitbills.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pri.android.splitbills.Model.UserDetails;
import com.pri.android.splitbills.NonFriendAdapter;
import com.pri.android.splitbills.R;
import com.pri.android.splitbills.SelectedFriendAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FriendRequest extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView nonFriendRecyclerView;
    RecyclerView selectedRecyclerView;
    RelativeLayout selectedUsers;
    ArrayList<UserDetails> nonFriendUserDetails;
    ArrayList<UserDetails> selectedFriendUserDetails;
    NonFriendAdapter mAdapter;
    SelectedFriendAdapter selectedFriendAdapter;

    private static final Comparator<UserDetails> ALPHABETICAL_COMPARATOR = new Comparator<UserDetails>() {
        @Override
        public int compare(UserDetails a, UserDetails b) {
            return a.name.compareTo(b.name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Intent intent = getIntent();
        nonFriendUserDetails = (ArrayList<UserDetails>) intent.getSerializableExtra("nonFriends");
        selectedFriendUserDetails = new ArrayList<>();
        init();
        nonFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        selectedFriendAdapter = new SelectedFriendAdapter(FriendRequest.this, ALPHABETICAL_COMPARATOR, new SelectedFriendAdapter.Listener() {
            @Override
            public void onSelectedFriendClicked(UserDetails model) {
                selectedFriendUserDetails.remove(model);

                selectedFriendAdapter.edit()
                        .replaceAll(selectedFriendUserDetails)
                        .commit();

                nonFriendUserDetails.add(model);

                mAdapter.edit()
                        .replaceAll(nonFriendUserDetails)
                        .commit();

                if (selectedFriendUserDetails.size() == 0) {
                    selectedUsers.setVisibility(View.GONE);

                }
            }
        });


        mAdapter = new NonFriendAdapter(FriendRequest.this, ALPHABETICAL_COMPARATOR, new NonFriendAdapter.Listener() {
            @Override
            public void onNonFriendClicked(UserDetails model) {
                Toast.makeText(FriendRequest.this, "clicked " + model.name, Toast.LENGTH_SHORT).show();
                //add user in selected list

                selectedFriendUserDetails.add(model);

                selectedFriendAdapter.edit()
                        .replaceAll(selectedFriendUserDetails)
                        .commit();

                nonFriendUserDetails.remove(model);

                mAdapter.edit()
                        .replaceAll(nonFriendUserDetails)
                        .commit();


//                selectedFriendAdapter.notifyDataSetChanged();

                if (selectedFriendUserDetails.size() == 1) {
                    selectedUsers.setVisibility(View.VISIBLE);
                }

            }
        });


        selectedRecyclerView.setAdapter(selectedFriendAdapter);
        nonFriendRecyclerView.setAdapter(mAdapter);

        selectedFriendAdapter.edit()
                .replaceAll(selectedFriendUserDetails)
                .commit();

        mAdapter.edit()
                .replaceAll(nonFriendUserDetails)
                .commit();


    }

    private void init() {
        nonFriendRecyclerView = (RecyclerView) findViewById(R.id.nonFriendsRecyclerView);
        selectedRecyclerView = (RecyclerView) findViewById(R.id.selectedRecyclerView);
        selectedUsers = (RelativeLayout) findViewById(R.id.selectedUsers);
        selectedUsers.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<UserDetails> filteredModelList = filter(nonFriendUserDetails, newText);
        mAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private static ArrayList<UserDetails> filter(List<UserDetails> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<UserDetails> filteredModelList = new ArrayList<>();
        for (UserDetails model : models) {
            final String text = model.name.toLowerCase() + " " + model.email.toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
