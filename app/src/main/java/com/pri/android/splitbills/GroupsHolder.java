package com.pri.android.splitbills;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Priyanshu on 15-12-2016.
 */


public class GroupsHolder extends RecyclerView.ViewHolder {

    private final TextView mGroupNameField;
    private final TextView mLastBillNameField;
    private final TextView mLastBillDateField;
    final View mView;

    public GroupsHolder(View itemView) {
        super(itemView);
        mGroupNameField = (TextView) itemView.findViewById(R.id.group_name);
        mLastBillNameField = (TextView) itemView.findViewById(R.id.last_bill_name);
        mLastBillDateField = (TextView) itemView.findViewById(R.id.last_bill_date);
        mView = itemView;
    }

    public void setmGroupNameField(String name) {
        mGroupNameField.setText(name);
    }

    public void setmLastBillNameField(String text) {
        mLastBillNameField.setText(text);
    }

    public void setmLastBillDateField(String text) {
        mLastBillDateField.setText(text);
    }
}
