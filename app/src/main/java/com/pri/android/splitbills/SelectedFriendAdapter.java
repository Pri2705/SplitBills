package com.pri.android.splitbills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.pri.android.splitbills.Model.UserDetails;
import com.pri.android.splitbills.databinding.SelectedUserForFriendBinding;

import java.util.Comparator;

/**
 * Created by Parth on 03-01-2017.
 */

public class SelectedFriendAdapter extends SortedListAdapter<UserDetails> {

    public interface Listener {
        void onSelectedFriendClicked(UserDetails model);
    }

    private final Listener mListener;

    public SelectedFriendAdapter(Context context, Comparator<UserDetails> comparator, Listener listener) {
        super(context, UserDetails.class, comparator);
        mListener = listener;
    }

    @Override
    protected ViewHolder<? extends UserDetails> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int i) {
        final SelectedUserForFriendBinding binding = SelectedUserForFriendBinding.inflate(inflater, parent, false);
        return new SelectedUserDetailsHolder(binding, mListener);
    }

    @Override
    protected boolean areItemsTheSame(UserDetails item1, UserDetails item2) {
        return item1.email.equals(item2.email);
    }

    @Override
    protected boolean areItemContentsTheSame(UserDetails oldItem, UserDetails newItem) {
        return oldItem.equals(newItem);
    }
}
