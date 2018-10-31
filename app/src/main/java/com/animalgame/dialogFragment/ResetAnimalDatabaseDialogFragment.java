package com.animalgame.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.animalgame.theanimalgame.R;

public class ResetAnimalDatabaseDialogFragment extends DialogFragment {
    public interface ResetAnimalDatabaseDialogListener {
        void onResetAnimalDatabasePositiveClick(DialogFragment dialog);
    }

    ResetAnimalDatabaseDialogFragment.ResetAnimalDatabaseDialogListener resetAnimalDatabaseDialogListener;

    // Override the Fragment.onAttach() method to instantiate the GoBackDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            resetAnimalDatabaseDialogListener = (ResetAnimalDatabaseDialogFragment.ResetAnimalDatabaseDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TimeUpDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.HighScoreStyle);
        builder.setMessage(R.string.reset_database_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetAnimalDatabaseDialogListener.onResetAnimalDatabasePositiveClick(ResetAnimalDatabaseDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
