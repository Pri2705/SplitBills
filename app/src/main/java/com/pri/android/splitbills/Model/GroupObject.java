package com.pri.android.splitbills.Model;

import java.io.Serializable;

/**
 * Created by Priyanshu on 15-12-2016.
 */

public class GroupObject implements Serializable {

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastTransactionName() {
        return LastTransactionName;
    }

    public void setLastTransactionName(String lastTransactionName) {
        LastTransactionName = lastTransactionName;
    }

    public String getLastTransactionDate() {
        return LastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        LastTransactionDate = lastTransactionDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public Boolean getSettlementStatus() {
        return SettlementStatus;
    }

    public void setSettlementStatus(Boolean settlementStatus) {
        SettlementStatus = settlementStatus;
    }

    public Boolean getCreationStatus() {
        return CreationStatus;
    }

    public void setCreationStatus(Boolean creationStatus) {
        CreationStatus = creationStatus;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    private String groupName;
    private String LastTransactionName;
    private String LastTransactionDate;
    private String CreatedBy;
    private Boolean SettlementStatus;
    private Boolean CreationStatus;
    private String CreatedDate;
    private String GroupID;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }
}
