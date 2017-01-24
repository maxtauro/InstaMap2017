package com.example.max.instamap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

/**
 * Created by Max on 2017-01-24.
 */

public class DialogFragmentNoPoints extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("The Twitter user you are trying to look at has no locations " +
                ", please pick another user");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_no_locations, null))
                // Add action buttons
                .setPositiveButton("GO!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // this doesnt do anything yet, will have to grab points for another user
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogFragmentNoPoints.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
