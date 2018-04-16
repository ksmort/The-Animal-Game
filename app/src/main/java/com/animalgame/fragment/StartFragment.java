package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.theanimalgame.R;

public class StartFragment extends Fragment {
    StartListener startListener;

    /*Interface StartListener. */
    public interface StartListener {
        void goToAnimalDatabaseScreen(View v);
        void startGame(View v);
        void readRules(View v);
        void quitGame(View v);
        void addPlayer(View v);
    }

    /*Makes sure fragment attached to container correctly, otherwise
    raises an exception.
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            startListener = (StartListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StartListener");
        }
    }
    /*What to do with fragment when done. */
    @Override
    public void onDetach() {
        super.onDetach();
        startListener = null;
    }

    /*Inflate layout for fragment. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_start, container, false);

        //have keyboard hide when click off it
        EditText playerName = frameLayout.findViewById(R.id.addPlayer_text);
        playerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AnimalController.hideKeyboard(getActivity(), v);
                }
            }
        });
        return frameLayout;
    }

}
