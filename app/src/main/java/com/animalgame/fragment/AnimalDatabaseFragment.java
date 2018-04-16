package com.animalgame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.animalgame.database.AnimalDatabaseAdapter;
import com.animalgame.theanimalgame.R;

import java.util.HashMap;
import java.util.List;

public class AnimalDatabaseFragment extends Fragment {
    private static final int TEXT_SIZE = 30;
    AnimalDatabaseListener animalDatabaseListener;

    /*Interface StartListener. */
    public interface AnimalDatabaseListener {
        void goToAddAnimalScreen(View v);
        void goToStartGameScreen(View v);
    }
    public AnimalDatabaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_animal_database, container, false);

        //get the animal database list and append it to the linear layout
        ScrollView animalListScrollView = frameLayout.findViewById(R.id.animalListScrollView);
        LinearLayout animalListLinearLayout = animalListScrollView.findViewById(R.id.animalListLinearLayout);
        AnimalDatabaseAdapter databaseAdapter = new AnimalDatabaseAdapter(getActivity());
        List<HashMap<String, String>> animalList = databaseAdapter.getAnimalList();

        if (animalList.size() > 0) {
            for (HashMap<String, String> animal : animalList) {
                TextView animalTextView = new TextView(getActivity());
                final String animalName = animal.get("name");
                animalTextView.setText(animalName);
                animalTextView.setTextSize(TEXT_SIZE);

                animalTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnimalEditFormFragment animalEditFormFragment = new AnimalEditFormFragment();

                        Bundle args = new Bundle();
                        args.putString("clickEvent", "updateAnimal");
                        args.putString("animalName", animalName);
                        animalEditFormFragment.setArguments(args);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frag_container, animalEditFormFragment);
                        transaction.commit();
                    }

                 });
                animalListLinearLayout.addView(animalTextView);
            }

        } else {
            TextView noAnimalTextView = new TextView(getActivity());
            noAnimalTextView.setText(R.string.no_animals_to_display);
            animalListLinearLayout.addView(noAnimalTextView);
        }
        // Inflate the layout for this fragment
        return frameLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnimalDatabaseListener) {
            animalDatabaseListener = (AnimalDatabaseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AnimalDatabaseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        animalDatabaseListener = null;
    }
}
