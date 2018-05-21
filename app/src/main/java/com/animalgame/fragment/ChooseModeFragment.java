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


public class ChooseModeFragment extends Fragment {
    ChooseModeFragment.ChooseModeListener chooseModeListener;

    public ChooseModeFragment() {
    }
    /*Interface StartListener. */
    public interface ChooseModeListener {
        void goBackToStartScreen(View v);
        void setStartLetterAndStartGame(View v);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            chooseModeListener = (ChooseModeFragment.ChooseModeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ChooseModeListener");
        }
    }
    /*What to do with fragment when done. */
    @Override
    public void onDetach() {
        super.onDetach();
        chooseModeListener = null;
    }

    /*Inflate layout for fragment. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_choose_mode, container, false);
        EditText chooseLetterEditText = frameLayout.findViewById(R.id.chooseLetterEditText);
        chooseLetterEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
