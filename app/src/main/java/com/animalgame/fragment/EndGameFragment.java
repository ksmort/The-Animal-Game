package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animalgame.player.Player;
import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.theanimalgame.HighScoreController;
import com.animalgame.theanimalgame.R;

import java.util.Vector;

/*
 Gets the player with the best score and displays on screen.
 Has an exit, restart with same players, and play with new players game.
 Or, continue which keeps the same scores but changes the letter
 */
public class EndGameFragment extends Fragment {
    EndGameListener endGameListener;
    private final Vector<Player> players;
    private static final int TEXT_SIZE = 20;

    public EndGameFragment() {
        endGameListener = null;
        players = AnimalController.getPlayerVector();
    }
    /*Interface StartListener. */
    public interface EndGameListener {
        void displayHighScores(View v);
        void newGame(View v);
        void goBackToStartScreen(View v);
    }
    /*Makes sure fragment attached to container correctly, otherwise
    raises an exception.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            endGameListener = (EndGameFragment.EndGameListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EndGameListener");
        }
    }
    /*What to do with fragment when done. */
    @Override
    public void onDetach() {
        super.onDetach();
        endGameListener = null;
    }

    /*Inflate layout for fragment. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout gameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_end_game, container, false);
        LinearLayout playerScrollViewLinearLayout = gameLayout.findViewById(R.id.playerPointsScrollViewLinearLayout);
        HighScoreController controller = new HighScoreController();
        Vector<String> sortedPlayersVector = controller.getSortedPlayerStringVector(players);
        int playerIndex = 0;
        for (String playerInfo : sortedPlayersVector) {
            TextView playerTextView = AnimalController.createEvenOddTextView(getActivity(), playerInfo, false, TEXT_SIZE, getResources().getColor(R.color.textBlue), playerIndex, false);
            playerScrollViewLinearLayout.addView(playerTextView);
            playerIndex++;
        }

        TextView playedLettersTextView = gameLayout.findViewById(R.id.playedLettersTextView);
        playedLettersTextView.setText(AnimalController.getPlayedLetters());

        return gameLayout;
    }

}
