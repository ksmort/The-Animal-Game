package com.animalgame.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.animalgame.theanimalgame.R;

public class TimeUpDialogFragment extends DialogFragment {
    public interface TimeUpDialogListener {
        void onTimeUpDialogPositiveClick(DialogFragment dialog);
    }

    TimeUpDialogFragment.TimeUpDialogListener timeUpDialogListener;

    // Override the Fragment.onAttach() method to instantiate the GoBackDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            timeUpDialogListener = (TimeUpDialogFragment.TimeUpDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TimeUpDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.end_of_turn);

        builder.setMessage("Time's up!")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        timeUpDialogListener.onTimeUpDialogPositiveClick(TimeUpDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
