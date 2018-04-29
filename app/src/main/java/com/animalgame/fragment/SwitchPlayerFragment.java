package com.animalgame.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animalgame.picture.PictureManager;
import com.animalgame.theanimalgame.R;
public class SwitchPlayerFragment extends Fragment {

    SwitchPlayerListener switchPlayerListener;
    public SwitchPlayerFragment() {
        switchPlayerListener = null;
    }
    /*Interface StartListener. */
    public interface SwitchPlayerListener {
        void goToNextPlayer(View v);
    }
    /*Makes sure fragment attached to container correctly, otherwise
    raises an exception.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            switchPlayerListener = (SwitchPlayerFragment.SwitchPlayerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SwitchPlayerListener");
        }
    }
    /*What to do with fragment when done. */
    @Override
    public void onDetach() {
        super.onDetach();
        switchPlayerListener = null;
    }

    /*Inflate layout for fragment. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        LinearLayout gameLayout = (LinearLayout) inflater.inflate(R.layout.fragment_switch_player, container, false);

        Bundle b = getArguments();

        String animalNameKey = "animalName";
        String funFactKey = "funFact";
        String pictureFilenameKey = "pictureFilename";
        if (b != null && b.containsKey(animalNameKey) && b.containsKey(funFactKey) && b.containsKey(pictureFilenameKey)) {
            String animalName = b.getString(animalNameKey);
            String funFact = b.getString(funFactKey);
            String pictureFilename = b.getString(pictureFilenameKey);

            TextView playedAnimalTextView = gameLayout.findViewById(R.id.playedAnimalTextView);
            TextView funFactTextView = gameLayout.findViewById(R.id.funFactTextView);
            ImageView playedAnimalImageView = gameLayout.findViewById(R.id.playedAnimalImageView);

            if (animalName != null && !animalName.isEmpty()) {
                playedAnimalTextView.setText(animalName);
            }
            if (funFact != null && !funFact.isEmpty()) {
                funFactTextView.setText(funFact);
            }

            if (playedAnimalImageView != null) {
                PictureManager.setImageViewBitmap(gameLayout, pictureFilename, R.id.playedAnimalImageView);
            }
        }


        return gameLayout;
    }
}
