package com.animalgame.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/*
 *//*Class RulesDialogFragment to display rules in a pop-up. */
public class HighScoreDialogFragment extends DialogFragment {
    private static final String OK = "OK";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("High Scores");
        builder.setMessage(b.getString("high_score_msg"));
        builder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Just dismiss upon click.
            }
        });
        return builder.create();
    }
}