package com.pri.android.splitbills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pri.android.splitbills.Model.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Parth on 31-12-2016.
 */

public class NonFriendRecyclerViewAdapter extends RecyclerView.Adapter<NonFriendRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    private final ArrayList<UserDetails> mValues;

    public NonFriendRecyclerViewAdapter(ArrayList<UserDetails> items, Context context) {
        mValues = items;
        mContext = context;
//        try {
//            this.mListener = ((OnListFragmentInteractionListener) context);
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement AdapterCallback.");
//        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserDetails data = mValues.get(position);
        holder.mItem = mValues.get(position);
        holder.etEmail.setText(data.email);
        holder.etName.setText(data.name);
        Picasso.with(mContext)
                .load(data.photoUrl)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.userAvatar);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(position);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView etEmail;
        public final TextView etName;
        public final CircleImageView userAvatar;
        public UserDetails mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            etEmail = (TextView) view.findViewById(R.id.user_email);
            etName = (TextView) view.findViewById(R.id.user_name);
            userAvatar = (CircleImageView)view.findViewById(R.id.friend_avatar);
        }
    }


}
