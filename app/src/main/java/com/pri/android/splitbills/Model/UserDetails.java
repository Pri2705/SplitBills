package com.pri.android.splitbills.Model;

import java.io.Serializable;

/**
 * Created by Parth on 31-12-2016.
 */

public class UserDetails implements Serializable {


    public String phone;
    public String name;
    public String uid;
    public String photoUrl;

    public String email;

    public UserDetails() {
// Default constructor required for calls to DataSnapshot.getValue(UserDetails.class)
    }

    public UserDetails(String name, String phone, String photoUrl, String uid) {
        this.name = name;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }
}

