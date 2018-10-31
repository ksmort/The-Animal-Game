package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.animalgame.picture.PictureManager;
import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.theanimalgame.R;
public class SwitchPlayerFragment extends Fragment {

    SwitchPlayerListener switchPlayerListener;
    public SwitchPlayerFragment() {
        switchPlayerListener = null;
    }
    /*Interface StartListener. */
    public interface SwitchPlayerListener {
        void goToNextPlayer(View v);
        void updateFact(View v);
        void hideKeyboard(View v);
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
            EditText funFactEditText = gameLayout.findViewById(R.id.funFactEditText);
            ImageView playedAnimalImageView = gameLayout.findViewById(R.id.playedAnimalImageView);

            if (animalName != null && !animalName.isEmpty()) {
                playedAnimalTextView.setText(animalName);
            }
            if (funFact != null && !funFact.isEmpty()) {
                funFactEditText.setText(funFact);
            }

            if (playedAnimalImageView != null) {
                PictureManager.setImageViewBitmap(gameLayout, pictureFilename, R.id.playedAnimalImageView);
            }

            funFactEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        AnimalController.hideKeyboard(getActivity(), v);
                    }
                }
            });
        }
        return gameLayout;
    }
}
