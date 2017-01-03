package com.pri.android.splitbills;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.pri.android.splitbills.Model.UserDetails;
import com.pri.android.splitbills.databinding.UserListItemBinding;

/**
 * Created by Parth on 02-01-2017.
 */

public class UserDetailsHolder extends SortedListAdapter.ViewHolder<UserDetails> {

    private final UserListItemBinding mBinding;

    public UserDetailsHolder(UserListItemBinding binding, NonFriendAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);

        mBinding = binding;
    }

    @Override
    protected void performBind(UserDetails userDetails) {
        mBinding.setModel(userDetails);
    }
}
