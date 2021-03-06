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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FrinedsRecyclerViewAdapter extends RecyclerView.Adapter<FrinedsRecyclerViewAdapter.ViewHolder> {

    private final List<UserDetails> mValues;
    private Context mContext;
//    private final OnListFragmentInteractionListener mListener;

    public FrinedsRecyclerViewAdapter(ArrayList<UserDetails> userEmailAndNames) {
        mValues = userEmailAndNames;
//        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_frineds, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.etEmail.setText(mValues.get(position).email);
        holder.etName.setText(mValues.get(position).name);
        Picasso.with(mContext)
                .load(mValues.get(position).photoUrl)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(holder.userAvatar);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
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

        @Override
        public String toString() {
            return super.toString() + " '" + etEmail.getText() + "'";
        }
    }
}
