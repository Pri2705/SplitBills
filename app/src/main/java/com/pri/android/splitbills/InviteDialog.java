package com.pri.android.splitbills;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pri.android.splitbills.Fragment.ContactsFragment;

import java.util.ArrayList;


/**
 * Created by Parth on 16-12-2016.
 */

public class InviteDialog extends DialogFragment {

    public InviteDialog() {

    }
private int lastSelectedSpinnerPos = 0;
    public static final int SELECTED_SMS = 0;
    public static final int SELECTED_EMAIL = 1;
    ArrayList<String> availableEmail;
    ArrayList<String> availablePhoneNumber;
    ArrayList<String> spinnerArray = new ArrayList<>();
    ArrayAdapter<String> availableContactArrayAdapter;
    int selectedMode;
    Boolean isCustom;
    TextView tvUserName;
    RadioGroup radioGroup;
    RadioButton rbSMS;
    RadioButton rbEmail;
    Spinner spinner;
    EditText etCustom;
    Button btInvite;
    ContactsFragment.ContactClass contactClass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        contactClass = (ContactsFragment.ContactClass)
                bundle.getSerializable("ContactDetails");
        View rootView = inflater.inflate(R.layout.invite_dialog, container, false);

        tvUserName = (TextView) rootView.findViewById(R.id.user_name);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        spinner = (Spinner) rootView.findViewById(R.id.invite_spinner);
        etCustom = (EditText) rootView.findViewById(R.id.custom_input);
        btInvite = (Button) rootView.findViewById(R.id.bt_invite);

        availablePhoneNumber = getPhoneNumbers();
        availableEmail = getEmails();
        spinnerArray = availablePhoneNumber;
        selectedMode = SELECTED_SMS;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1://SMS
                        spinnerArray = getPhoneNumbers();
                        selectedMode = SELECTED_SMS;
                        availableContactArrayAdapter.clear();
                        availableContactArrayAdapter.addAll(spinnerArray);
                        availableContactArrayAdapter.notifyDataSetChanged();
                        if (lastSelectedSpinnerPos!=spinnerArray.size()- 1) {
                            etCustom.setVisibility(View.GONE);
                            isCustom = false;
                        } else {
                            etCustom.setVisibility(View.VISIBLE);
                            etCustom.setHint("Mobile Number");
                            isCustom = true;
                        }
                        break;
                    case R.id.radioButton2://Email
                        spinnerArray = getEmails();
                        selectedMode = SELECTED_EMAIL;
                        availableContactArrayAdapter.clear();
                        availableContactArrayAdapter.addAll(spinnerArray);
                        availableContactArrayAdapter.notifyDataSetChanged();
                        if (lastSelectedSpinnerPos!=spinnerArray.size()- 1) {
                            etCustom.setVisibility(View.GONE);
                            isCustom = false;
                        } else {
                            etCustom.setVisibility(View.VISIBLE);
                            etCustom.setHint("E-Mail ID");
                            isCustom = true;
                        }
                        break;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lastSelectedSpinnerPos = position;
                if (position == spinnerArray.size() - 1) {
                    etCustom.setVisibility(View.VISIBLE);
                    if (selectedMode == SELECTED_SMS) {
                        etCustom.setHint("Mobile Number");
                    } else if (selectedMode == SELECTED_EMAIL) {
                        etCustom.setHint("E-Mail ID");
                    }
                    isCustom = true;
                } else {
                    etCustom.setVisibility(View.GONE);
                    isCustom = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String userName = contactClass.name.substring(0,1).toUpperCase() + contactClass.name.substring(1);
        tvUserName.setText(userName);

        availableContactArrayAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        availableContactArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(availableContactArrayAdapter);

        btInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Invite Send", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                //isCustom & selectedMode;
            }
        });

        return rootView;
    }

    private ArrayList<String> getPhoneNumbers() {
        availablePhoneNumber = new ArrayList<>();
        String phoneNumbers = contactClass.phoneNumber;
        String numbers[] = phoneNumbers.split(";");
        for (String a : numbers) {
            if (!a.equals("")) {
                availablePhoneNumber.add(a);
            }
        }
        availablePhoneNumber.add("Custom");
        return availablePhoneNumber;
    }

    public ArrayList<String> getEmails() {
        availableEmail = new ArrayList<>();
        String emails = contactClass.email;
        String emailsArray[] = emails.split(";");
        for (String a : emailsArray) {
            if (!a.equals("")) {
                availableEmail.add(a);
            }
        }
        availableEmail.add("Custom");
        return availableEmail;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
