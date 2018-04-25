package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.animalgame.animal.Animal;
import com.animalgame.database.AnimalDatabaseAdapter;
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

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_animal_edit_form, container, false);
        Bundle b = getArguments();

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

        if (b != null && b.containsKey("clickEvent")) {
            Object clickEvent = b.get("clickEvent");
            if (clickEvent != null) {
                if (clickEvent.equals("updateAnimal")) {
                    //remove add button
                    Button addButton = frameLayout.findViewById(R.id.addAnimalButton);
                    addButton.setVisibility(View.GONE);

                    Object animalName = b.get("animalName");

                    if (animalName != null) {
                        //get animal from database
                        AnimalDatabaseAdapter databaseAdapter = new AnimalDatabaseAdapter(getActivity());
                        Animal animal = databaseAdapter.getAnimalByName(animalName.toString());

                        animalIdTextView.setText(String.valueOf(animal.animal_ID));
                        animalNameEditText.setText(animal.animalName);
                        funFactEditText.setText(animal.fact);
                    }
                    //populate animal name, picture, fun fact
                } else {
                    //remove update and delete buttons
                    Button updateButton = frameLayout.findViewById(R.id.updateAnimalButton);
                    Button deleteButton = frameLayout.findViewById(R.id.deleteAnimalButton);

                    updateButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
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
    }
}
