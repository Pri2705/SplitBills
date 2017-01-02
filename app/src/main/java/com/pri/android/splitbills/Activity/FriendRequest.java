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

import com.pri.android.splitbills.Model.UserDetails;
import com.pri.android.splitbills.NonFriendRecyclerViewAdapter;
import com.pri.android.splitbills.R;

import java.util.ArrayList;
import java.util.List;


public class FriendRequest extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView nonFriendRecyclerView;
    RecyclerView selectedRecyclerView;
    RelativeLayout selectedUsers;
    NonFriendRecyclerViewAdapter nonFriendRecyclerViewAdapter;
    NonFriendRecyclerViewAdapter nonFriendRecyclerViewFilterAdapter;
    ArrayList<UserDetails> nonFriendUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Intent intent = getIntent();
        nonFriendUserDetails = (ArrayList<UserDetails>) intent.getSerializableExtra("nonFriends");
        init();
        nonFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nonFriendRecyclerViewAdapter = new NonFriendRecyclerViewAdapter(nonFriendUserDetails,FriendRequest.this);
        nonFriendRecyclerView.setAdapter(nonFriendRecyclerViewAdapter);
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
    public boolean onQueryTextSubmit(String query) {
        final ArrayList<UserDetails> filteredModelList = filter(nonFriendUserDetails, query);
        nonFriendRecyclerViewFilterAdapter = new NonFriendRecyclerViewAdapter(filteredModelList,getApplicationContext());
//        nonFriendRecyclerViewAdapter.edit()
//                .replaceAll(filteredModelList)
//                .commit();
        nonFriendRecyclerView.swapAdapter(nonFriendRecyclerViewFilterAdapter,false);
        nonFriendRecyclerView.scrollToPosition(0);
        return true;
    }
    private static ArrayList<UserDetails> filter(List<UserDetails> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<UserDetails> filteredModelList = new ArrayList<>();
        for (UserDetails model : models) {
            final String text = model.name.toLowerCase()+" "+model.email.toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
