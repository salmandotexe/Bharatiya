package com.kms.bharatiya;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
public class FilterDialog extends DialogFragment {
    private EditText e1,e2;
    private  filterdialoglistener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fdialog, null);
        e1 = view.findViewById(R.id.e1);
        e2 = view.findViewById(R.id.e2);

        builder.setView(view)
                .setTitle("Filters")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double val1 = Double.parseDouble(e1.getText().toString());
                        double val2 = Double.parseDouble(e2.getText().toString());
                        listener.Getvals(val1,val2);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (filterdialoglistener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dialoglistener.");
        }
    }

    public interface filterdialoglistener{
        void Getvals(double val1, double val2);
    }
}
