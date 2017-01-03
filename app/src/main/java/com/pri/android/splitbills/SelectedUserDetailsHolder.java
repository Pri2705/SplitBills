package com.pri.android.splitbills;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.pri.android.splitbills.Model.UserDetails;
import com.pri.android.splitbills.databinding.SelectedUserForFriendBinding;

/**
 * Created by Parth on 03-01-2017.
 */

public class SelectedUserDetailsHolder extends SortedListAdapter.ViewHolder<UserDetails> {

    private final SelectedUserForFriendBinding mBinding;

    public SelectedUserDetailsHolder(SelectedUserForFriendBinding binding, SelectedFriendAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);

        mBinding = binding;
    }

    @Override
    protected void performBind(UserDetails userDetails) {
        mBinding.setModel(userDetails);
    }
}
