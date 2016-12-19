package com.pri.android.splitbills.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pri.android.splitbills.ContactsAdapter;
import com.pri.android.splitbills.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ContactsFragment extends Fragment {
    private static final String TAG = ContactsFragment.class.getName();
    private DatabaseReference mUsersRef;
    Context mContext;
    LinearLayout progressBar;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private Boolean isDataRetrieved = false;
    private GetContactsInterfce getContactsListner;
    private RecyclerView recyclerView;
    ArrayList<ContactClass> contactList = new ArrayList<>();
    ArrayList<String> userEmails = new ArrayList<>();
    ArrayList<String> userPhoneNumbers = new ArrayList<>();
    ContactsAdapter contactsAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactsFragment() {
    }

    public static ContactsFragment newInstance(String title, Context context) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.tabTitle), title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_item_list, container, false);
        progressBar = (LinearLayout) view.findViewById(R.id.contact_progress);
         recyclerView = (RecyclerView) view.findViewById(R.id.contacts_list);
        // Set the adapter
        Context context = view.getContext();
        mContext = context;

        if (!isDataRetrieved) {
            initList(context);
            isDataRetrieved = true;
        } else {
//            recyclerView.setAdapter(adapter);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        contactsAdapter = new ContactsAdapter(contactList, getContext());
        recyclerView.setAdapter(contactsAdapter);

        getContactsListner.getContact(contactList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private void initList(Context context) {
        //ask for permission
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(mContext, "Permission Required", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }


    public class ContactClass implements Serializable {
        public String name;
        public String phoneNumber;
        public String email;

        public ContactClass(String name, String phoneNumber, String email) {
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.name = name;
        }
    }

    public static class UserDetails {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getContactsListner = (GetContactsInterfce) context;
    }

    public interface GetContactsInterfce {
        void getContact(ArrayList<ContactClass> contacts);
    }

    /*
        * read mail name and number from contacts
        * and save to @contactList
        * */
    private void readContacts() {

        readContactsFromServer(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);

                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess() {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                readContactsFromDevice();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });
    }

    private void readContactsFromDevice() {
        ArrayList<String> tempEmails = new ArrayList<>(userEmails);
        ArrayList<String> tempPhNumbers = new ArrayList<>(userPhoneNumbers);

        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
                ContactsContract.Data.DISPLAY_NAME);
        long previousId = -1;
        boolean newContact = false;
        ContactClass previousContact = null;
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String data1 = c.getString(c.getColumnIndex(ContactsContract.Data.DATA1));
            String dataType = c.getString(c.getColumnIndex(ContactsContract.Data.MIMETYPE));
            String phoneNumber = "";
            String email = "";

            if (name.equals(data1)) {
                continue;
            }
            boolean isEmail = false;
            boolean isPhoneNumber = false;
            newContact = false;
            if (dataType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                email = data1;
                if (!tempEmails.contains(email)) {
                    isEmail = true;
                } else {
//                    tempEmails.remove(email);
                    isEmail = false;
                    Log.v(TAG, "email removed " + email);
                }
            } else if (dataType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                phoneNumber = data1;
                phoneNumber = phoneNumber.replaceAll("\\-", "").replaceAll(" ", "").replaceAll("\\+9111", "").replaceAll("\\+91", "");
                if (phoneNumber.length() >= 10 && !tempPhNumbers.contains(phoneNumber)) {
                    isPhoneNumber = true;
                } else {
//                    tempPhNumbers.remove(phoneNumber);
                    isPhoneNumber = false;
                    Log.v(TAG, "number removed" + phoneNumber);
                }
            }
            if (!isEmail && !isPhoneNumber) {
                continue;
            }

            if (previousId == id) {
                if (previousContact != null) {
                    if (isEmail && !previousContact.email.contains(email)) {
                        previousContact.email += ";" + email;
                    }
                    if (isPhoneNumber && !previousContact.phoneNumber.contains(phoneNumber)) {
                        previousContact.phoneNumber += ";" + phoneNumber;
                    }
                }
            } else {
                // make new contact
                newContact = true;
                if (previousContact != null) {
                    contactList.add(previousContact);
                }
            }

            if (newContact && (isPhoneNumber || isEmail)) {
                ContactClass currentContact = new ContactClass(name, phoneNumber, email);
                previousContact = currentContact;
                previousId = id;
            }
        }
        contactList.add(previousContact);
        contactsAdapter.notifyDataSetChanged();
//        System.out.println(id + ", name=" + contactList);

    }

    private void readContactsFromServer(final OnGetDataListener onGetDataListener) {
        onGetDataListener.onStart();
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String key = messageSnapshot.getKey();
                    key = key.replace(",", ".");
                    userEmails.add(key);
                    UserDetails userDetails = messageSnapshot.getValue(UserDetails.class);
                    userPhoneNumbers.add(userDetails.phone);
                }
                onGetDataListener.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetDataListener.onFailed(databaseError);
            }
        });

    }


    public interface OnGetDataListener {
        public void onStart();

        public void onSuccess();

        public void onFailed(DatabaseError databaseError);
    }
}

