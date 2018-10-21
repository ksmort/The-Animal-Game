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

/*
 *//*Class RulesDialogFragment to display rules in a pop-up. */
public class HighScoreDialogFragment extends DialogFragment {
    private static final String OK = "OK";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //Use HighScoreStyle custom alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.HighScoreStyle);

        //set title programmatically so that we can center the title in the alert dialog
        TextView title = new TextView(getActivity());
        title.setText(R.string.high_scores_title);
        title.setBackgroundColor(Color.TRANSPARENT);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.textYellow));
        title.setTextSize(34);
        title.setTypeface(null, Typeface.BOLD);
        builder.setCustomTitle(title);

        builder.setMessage(b.getString("high_score_msg"));
        builder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Just dismiss upon click.
            }
        });
        return builder.create();
    }
}