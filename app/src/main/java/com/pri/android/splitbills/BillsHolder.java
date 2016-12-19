package com.pri.android.splitbills;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Priyanshu on 16-12-2016.
 */

public class BillsHolder extends RecyclerView.ViewHolder{

    public View getmView() {
        return mView;
    }

    public void setmBillName(String billName) {

        mBillName.setText(billName);
    }

    public void setmPaidBy(String paidBy) {
        mPaidBy.setText(paidBy);
    }

    public void setmAmount(String amount) {
        mAmount.setText(amount);
    }

    private View mView;
    private TextView mBillName;
    private TextView mPaidBy;
    private TextView mAmount;

    public BillsHolder(View itemView) {
        super(itemView);
        mBillName = (TextView) itemView.findViewById(R.id.bill_name_et);
        mPaidBy = (TextView) itemView.findViewById(R.id.bill_paidby);
        mAmount = (TextView) itemView.findViewById(R.id.amount_paid);
        mView = itemView;
    }
}
