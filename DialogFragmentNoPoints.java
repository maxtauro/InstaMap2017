package com.example.max.instamap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Max on 2017-01-24.
 */

public class DialogFragmentNoPoints extends DialogFragment {
    public String newTwitterUser;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_no_locations,null);
        final EditText input=(EditText)layout.findViewById(R.id.new_twitterusername);//https://stackoverflow.com/questions/13584063/adding-edittext-to-alert-dialog
        // Get the layout inflater

        builder.setMessage("The Twitter user you are trying to look at has no locations " +
                ", please pick another user");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newTwitterUser = input.getText().toString();
                        Main callingActivity = (Main) getActivity();
                        callingActivity.onNewTwitterUser(newTwitterUser);
                        DialogFragmentNoPoints.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
