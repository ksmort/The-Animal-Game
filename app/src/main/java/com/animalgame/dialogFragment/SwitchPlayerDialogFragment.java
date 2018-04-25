package com.animalgame.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.animalgame.theanimalgame.R;

public class SwitchPlayerDialogFragment extends DialogFragment {
    boolean passBoolean = false;
    public interface SwitchPlayerDialogListener {
        void onSwitchPlayerDialogPositiveClick(DialogFragment dialog, boolean pass);
    }

    SwitchPlayerDialogFragment.SwitchPlayerDialogListener switchPlayerDialogListener;

    // Override the Fragment.onAttach() method to instantiate the GoBackDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            switchPlayerDialogListener = (SwitchPlayerDialogFragment.SwitchPlayerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SwitchPlayerDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_animal_dialog);
        Bundle b = getArguments();

        String message = "";
        if (b != null && b.containsKey("pass")) {
            Object passValue = b.get("pass");
            if (passValue != null && passValue.toString().equalsIgnoreCase("true")) {
                passBoolean = true;
            }
            message = "Time's up!";
        } else {
            //get animal name, fun fact, picture and put in fragment if exists
            if (b != null && b.containsKey("animalName")) {
                Object animal = b.get("animalName");
                if (animal != null) {
                    message = animal.toString();
                }
            }
            //otherwise, pass button
        }

        builder.setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switchPlayerDialogListener.onSwitchPlayerDialogPositiveClick(SwitchPlayerDialogFragment.this, passBoolean);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
