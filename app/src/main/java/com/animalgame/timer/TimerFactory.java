package com.animalgame.timer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animalgame.dialogFragment.TimeUpDialogFragment;
import com.animalgame.theanimalgame.R;

public class TimerFactory {
    private static final String TIME_UP_MESSAGE = "Time's up! \n (Give to next player)";
    public static CountDownTimer createGameTimer(final Activity activity, FrameLayout gameLayout, long millisInFuture, final long countDownInterval) {
            final TextView countdownTextView = gameLayout.findViewById(R.id.countdown_text);

        return new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                long timeLeft = 0;
                if (millisUntilFinished > 0) {
                    timeLeft = millisUntilFinished / countDownInterval;
                }
                countdownTextView.setText(String.valueOf(timeLeft));
            }
            @Override
            public void onFinish() {
                Toast.makeText(activity, TIME_UP_MESSAGE, Toast.LENGTH_SHORT).show();
                countdownTextView.setText("0");
                TimeUpDialogFragment timeUpDialogFragment = new TimeUpDialogFragment();
                Bundle args = new Bundle();
                timeUpDialogFragment.show(activity.getFragmentManager(), "SwitchPlayerDialogFragment");
                //have dialog fragment pop up and say time's up
            }
        };
    }

}
