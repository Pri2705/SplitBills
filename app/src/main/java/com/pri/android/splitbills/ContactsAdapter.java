package com.pri.android.splitbills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pri.android.splitbills.Fragment.ContactsFragment;

import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    InviteDialog inviteDialog;
    Context mContext;
    private final ArrayList<ContactsFragment.ContactClass> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ContactsAdapter(ArrayList<ContactsFragment.ContactClass> items, Context context) {
        mValues = items;
        mContext = context;
        try {
            this.mListener = ((OnListFragmentInteractionListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactsFragment.ContactClass contactData = mValues.get(position);
        holder.mConatctName.setText(contactData.name);
//        holder.mContactEmail.setText(contactData.phoneNumber);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                inviteDialog = new InviteDialog();
//                inviteDialog.show(getFragmentManager(), "Time Picker");
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mConatctName;
//        public final TextView mContactEmail;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mConatctName = (TextView) view.findViewById(R.id.contact_name);
//            mContactEmail = (TextView) view.findViewById(R.id.contact_email);
        }
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
        void onListFragmentInteraction(int position);
    }
}
