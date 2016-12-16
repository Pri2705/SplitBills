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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pri.android.splitbills.ContactsAdapter;
import com.pri.android.splitbills.InviteDialog;
import com.pri.android.splitbills.R;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.id;

/**
 * A fragment representing a list of Items.
 */
public class ContactsFragment extends Fragment {
    Context mContext;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private  GetContactsInterfce getContactsListner;
    private int mColumnCount = 1;
    ArrayList<ContactClass> contactList = new ArrayList<>();
    InviteDialog inviteDialog;

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

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mContext = context;
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            initList(context);
            recyclerView.setAdapter(new ContactsAdapter(contactList, getContext()));
            getContactsListner.getContact(contactList);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayout.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
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

//    @Override
//    public void onListFragmentInteraction(int position) {
//        inviteDialog = new InviteDialog();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("contactClass",contactList.get(position));
//        inviteDialog.setArguments(bundle);
//        inviteDialog.show(getActivity().getFragmentManager(), "Invite");
//    }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getContactsListner = (GetContactsInterfce)context;
    }

    public interface GetContactsInterfce {
        void getContact(ArrayList<ContactClass> contacts);
    }

    /*
        * read mail name and number from contacts
        * and save to @contactList
        * */
    private void readContacts() {
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
                isEmail = true;
            } else if (dataType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                phoneNumber = data1;
                phoneNumber = phoneNumber.replaceAll("\\-", "").replaceAll(" ", "").replaceAll("\\+9111", "").replaceAll("\\+91", "");
                if (phoneNumber.length() >= 10) {
                    isPhoneNumber = true;
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
        System.out.println(id + ", name=" + contactList);
    }

}
