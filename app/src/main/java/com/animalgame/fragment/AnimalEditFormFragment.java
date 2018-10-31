package com.animalgame.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.animalgame.animal.Animal;
import com.animalgame.database.AnimalDatabaseAdapter;
import com.animalgame.picture.PictureManager;
import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.theanimalgame.R;

public class AnimalEditFormFragment extends Fragment {

    AnimalEditFormListener animalEditFormListener;

    public AnimalEditFormFragment() {
        // Required empty public constructor
    }

    /*Interface StartListener. */
    public interface AnimalEditFormListener {
        void addAnimal(View v);
        void updateAnimal(View v);
        void deleteAnimal(View v);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_animal_edit_form, container, false);
        Bundle b = getArguments();

        AnimalController.setImageViewId(0);
        AnimalController.setImagePathname("");

        TextView animalIdTextView = frameLayout.findViewById(R.id.animalIdTextView);
        EditText animalNameEditText = frameLayout.findViewById(R.id.animalNameEditText);
        animalNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AnimalController.hideKeyboard(getActivity(), v);
                }
            }
        });

        final TextView funFactEditText = frameLayout.findViewById(R.id.funFactEditText);
        funFactEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AnimalController.hideKeyboard(getActivity(), funFactEditText);
                }
            }
        });

        ImageView animalImageView = frameLayout.findViewById(R.id.animalImageView);

        if (b != null && b.containsKey("clickEvent")) {
            Object clickEvent = b.get("clickEvent");
            if (clickEvent != null) {
                if (clickEvent.equals("updateAnimal")) {
                    //remove add button
                    ImageButton addButton = frameLayout.findViewById(R.id.addAnimalButton);
                    addButton.setVisibility(View.GONE);

                    Object animalName = b.get("animalName");

                    if (animalName != null) {
                        String animalNameString = animalName.toString();
                        //get animal from database
                        AnimalDatabaseAdapter databaseAdapter = new AnimalDatabaseAdapter(getActivity());
                        Animal animal = databaseAdapter.getAnimalByName(animalNameString);
                        PictureManager.setImageViewBitmap(frameLayout, animal.pictureFilename, R.id.animalImageView);
                        AnimalController.setImagePathname(animal.pictureFilename);
                        animalIdTextView.setText(String.valueOf(animal.animal_ID));
                        animalNameEditText.setText(animalNameString);
                        funFactEditText.setText(animal.fact);

                    }
                    //populate animal name, picture, fun fact
                } else {
                    //remove update and delete buttons
                    ImageButton updateButton = frameLayout.findViewById(R.id.updateAnimalButton);
                    ImageButton deleteButton = frameLayout.findViewById(R.id.deleteAnimalButton);

                    updateButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                }
                if (activity != null && animalImageView != null) {
                    PictureManager.setImageViewListener(activity, animalImageView);
                }
            }
            // Inflate the layout for this fragment

        }

        return frameLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnimalEditFormListener) {
            animalEditFormListener = (AnimalEditFormListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AnimalEditFormListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        animalEditFormListener = null;
        AnimalController.setImageViewId(0);
        AnimalController.setImagePathname("");
    }
}
