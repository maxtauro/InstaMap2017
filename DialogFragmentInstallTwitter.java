package com.example.max.instamap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Max on 2017-01-23.
 */

public class DialogFragmentInstallTwitter extends DialogFragment {
    final String appPackageName = "com.twitter.android";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This app won't work if Twitter is not installed, Get Twitter?")
                .setPositiveButton("Go to Play Store", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // this will link to twitter in the google play store
                        try { //https://stackoverflow.com/questions/11753000/how-to-open-the-google-play-store-directly-from-my-android-application
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
