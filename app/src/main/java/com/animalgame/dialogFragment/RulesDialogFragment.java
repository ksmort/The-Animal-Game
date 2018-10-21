package com.animalgame.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.animalgame.theanimalgame.R;

/*Class RulesDialogFragment to display rules in a pop-up. */
public class RulesDialogFragment extends DialogFragment {
    private static final String BACK = "BACK";
    private static final String RULES = "Rules";
    private static final String RULES_MESSAGE = "The goal of the game is to get the most points by naming animals.\n"
            + "You may choose a letter by typing a letter at the screen before a new round, or keep field empty for a random letter."
            + "You have 60 seconds to name an animal.\n"
            + "Press the VERIFY button to check if your animal is valid. If it is, you are awarded 10 points.\n"
            +"Press the PASS button if you give up on your turn, and you will be out until the round is over.\n"
            + "You can update the animal database from the start screen and add/remove any animal you wish. You can also include "
            + "a fun fact and a picture for the animal.\n"
            + "The person with the most points wins! Good luck and have fun!";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RulesStyle);
        TextView title = new TextView(getActivity());
        title.setText(RULES);
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.textYellow));
        title.setTextSize(34);
        title.setTypeface(null, Typeface.BOLD);
        builder.setCustomTitle(title);

        builder.setMessage(RULES_MESSAGE);
        builder.setIcon(R.drawable.ic_arrow_left);
        builder.setPositiveButton(BACK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Just dismiss upon click.
            }
        });
        return builder.create();
    }
}