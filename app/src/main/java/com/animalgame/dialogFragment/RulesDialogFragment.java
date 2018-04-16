package com.animalgame.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/*Class RulesDialogFragment to display rules in a pop-up. */
public class RulesDialogFragment extends DialogFragment {
    private static final String OK = "OK";
    private static final String RULES = "Rules";
    private static final String RULES_MESSAGE = "\tRules and Instructions\n * Target is to name the most animals"
            + "\n * Bugs and insects do not count, nor do family or species names"
            + "\n * Press (PASS) if you can't name an animal \n *Person with the most points wins"
            + "\n * Press (OK) to go back";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(RULES);
        builder.setMessage(RULES_MESSAGE);
        builder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Just dismiss upon click.
            }
        });
        return builder.create();
    }
}