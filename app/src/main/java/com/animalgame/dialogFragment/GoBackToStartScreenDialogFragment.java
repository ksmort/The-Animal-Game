package com.animalgame.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.animalgame.theanimalgame.R;

public class GoBackToStartScreenDialogFragment extends DialogFragment {

    public interface GoBackToStartScreenDialogListener {
        void onGoBackToStartScreenDialogPositiveClick(DialogFragment dialog);
    }

    GoBackToStartScreenDialogListener goBackToStartScreenDialogListener;

    // Override the Fragment.onAttach() method to instantiate the GoBackDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            goBackToStartScreenDialogListener = (GoBackToStartScreenDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GoBackDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.HighScoreStyle);
        builder.setTitle(R.string.quit_game_dialog);
        builder.setMessage(R.string.quit_game_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goBackToStartScreenDialogListener.onGoBackToStartScreenDialogPositiveClick(GoBackToStartScreenDialogFragment.this);
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
