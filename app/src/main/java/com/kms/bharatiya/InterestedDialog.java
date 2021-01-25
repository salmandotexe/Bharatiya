package com.kms.bharatiya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class InterestedDialog extends DialogFragment {
    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(MainActivity.data)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder path = new StringBuilder();
                        path.append("users/");
                        path.append(MainActivity.whoishe);
                        path.append("/messages");
                        String pp=path.toString();

                        StringBuilder message = new StringBuilder();
                        message.append(MainActivity.whoami);
                        message.append(" is interested in your property at ");
                        message.append(MainActivity.hishouseaddr);

                        Log.d("EMAIL",path.toString());
                        Log.d("EMAIL",message.toString());

                        Map<String,Object> dat = new HashMap<>();
                        dat.put("msg",message.toString());
                        fdb.collection(pp).add(dat);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
