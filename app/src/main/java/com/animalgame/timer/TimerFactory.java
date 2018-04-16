package com.animalgame.timer;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animalgame.theanimalgame.R;

public class TimerFactory {
    private static final String TIME_UP_MESSAGE = "Time's up! \n (Give to next player)";
    public static CountDownTimer createGameTimer(final Activity activity, FrameLayout gameLayout, long millisInFuture, final long countDownInterval) {
            final TextView countdownTextView = gameLayout.findViewById(R.id.countdown_text);
            final Button passButton = gameLayout.findViewById(R.id.pass_button);

        return new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished / countDownInterval));
            }
            @Override
            public void onFinish() {
                Toast.makeText(activity, TIME_UP_MESSAGE, Toast.LENGTH_SHORT).show();

                //have dialog fragment pop up and say time's up
                passButton.performClick();
            }
        };
    }

}
