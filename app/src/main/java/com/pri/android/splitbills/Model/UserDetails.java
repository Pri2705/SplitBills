package com.pri.android.splitbills.Model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.pri.android.splitbills.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by Parth on 31-12-2016.
 */

public class UserDetails implements Serializable, SortedListAdapter.ViewModel {


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

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return photoUrl;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.avatar)
                .into(imageView);

    }

    public String firstName() {
        return name.split(" ")[0];
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

