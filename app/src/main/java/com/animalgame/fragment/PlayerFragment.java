package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.animalgame.player.Player;
import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.theanimalgame.R;

//fragment for each player for game
//user will input an animal name starting with a letter, double check that the animal name is valid and
//has not been already played, and then add a point if they are correct.  This will be done
//in relevance to a timer.  If the timer runs out, invalid animal name, or repeated animal name,
//then the player is out.  Else, goes to next player.

public class PlayerFragment extends Fragment {
    private final char currentLetter;
    private final Player player;
    PlayerListener playerListener;

    public PlayerFragment() {
        player = AnimalController.getPlayer();
        currentLetter = AnimalController.getLetter();
        playerListener = null;
    }
    /*Interface StartListener. */
    public interface PlayerListener {
        void verify(View v);
        void readRules(View v);
        void pass(View v);
        void goBackToStartScreen(View v);
    }

    /*Makes sure fragment attached to container correctly, otherwise
    raises an exception.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            playerListener = (PlayerFragment.PlayerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PlayerListener");
        }
    }
    /*What to do with fragment when done. */
    @Override
    public void onDetach() {
        super.onDetach();
        playerListener = null;
    }

    /*Inflate layout for fragment. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout gameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_player, container, false);

        Bundle b = getArguments();

        TextView pointsTextView = gameLayout.findViewById(R.id.numPoints_text);
        pointsTextView.setText(String.valueOf(player.getPoints()));
        TextView playerNameTextView = gameLayout.findViewById(R.id.playerName_text);
        playerNameTextView.setText(player.getName());
        TextView letterTextView = gameLayout.findViewById(R.id.letter_text);
        letterTextView.setText(String.valueOf(currentLetter));
        AnimalController.setPlayerCountdownTimer(getActivity(), gameLayout);

        //get the played animal list and add them to the playedAnimalsScrollLayout
        StringBuilder stringBuilder = new StringBuilder();

        if (b != null && b.containsKey("playedAnimals")) {
            String[] playedAnimals = b.getStringArray("playedAnimals");
            if (playedAnimals != null) {
                for (String animal : playedAnimals) {
                    stringBuilder.append(animal);
                    stringBuilder.append("\n");
                }
                TextView playedAnimalsScrollTextView = gameLayout.findViewById(R.id.playedAnimalsScrollTextView);
                playedAnimalsScrollTextView.setText(stringBuilder.toString());
            }
        }

        //have keyboard hide when click off it
        TextView animalTextView = gameLayout.findViewById(R.id.animalName_text);
        animalTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AnimalController.hideKeyboard(getActivity(), v);
                }
            }
        });
        return gameLayout;
    }
}
