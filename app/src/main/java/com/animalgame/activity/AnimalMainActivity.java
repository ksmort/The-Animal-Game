package com.animalgame.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.animalgame.animal.Animal;
import com.animalgame.database.AnimalDatabaseAdapter;
import com.animalgame.dialogFragment.DeleteAnimalDialogFragment;
import com.animalgame.dialogFragment.PassDialogFragment;
import com.animalgame.dialogFragment.TimeUpDialogFragment;
import com.animalgame.fragment.AnimalDatabaseFragment;
import com.animalgame.fragment.AnimalEditFormFragment;
import com.animalgame.fragment.ChooseModeFragment;
import com.animalgame.fragment.SwitchPlayerFragment;
import com.animalgame.picture.PictureManager;
import com.animalgame.player.Player;
import com.animalgame.fragment.PlayerFragment;
import com.animalgame.theanimalgame.AnimalController;
import com.animalgame.fragment.EndGameFragment;
import com.animalgame.dialogFragment.GoBackToStartScreenDialogFragment;
import com.animalgame.dialogFragment.HighScoreDialogFragment;
import com.animalgame.dialogFragment.QuitGameDialogFragment;
import com.animalgame.theanimalgame.R;
import com.animalgame.dialogFragment.RulesDialogFragment;
import com.animalgame.fragment.StartFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static com.animalgame.picture.PictureManager.SELECT_PHOTO;


public class AnimalMainActivity extends FragmentActivity implements StartFragment.StartListener, PlayerFragment.PlayerListener,
        EndGameFragment.EndGameListener, GoBackToStartScreenDialogFragment.GoBackToStartScreenDialogListener,
        AnimalDatabaseFragment.AnimalDatabaseListener, AnimalEditFormFragment.AnimalEditFormListener,
        DeleteAnimalDialogFragment.DeleteAnimalDialogListener, PassDialogFragment.PassDialogListener,
        TimeUpDialogFragment.TimeUpDialogListener, SwitchPlayerFragment.SwitchPlayerListener,
        ChooseModeFragment.ChooseModeListener {
    private static final String EMPTY_PLAYER_NAME = "";
    private static final String HIGH_SCORE_FILENAME = "HighScores.txt";
    private static final String NOT_ENOUGH_PLAYERS_MESSAGE = "Must have at least one player.";
    private static final int FIRST_PLAYER_INDEX = 0;
    private static final int NO_LETTERS = 0;
    private static final int TEXT_SIZE = 20;
    private static final String TAG = "AnimalMainActivity";
    private AnimalController animalController;
    private AnimalDatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE},100);
        animalController = new AnimalController();
        setContentView(R.layout.activity_animal_main);

        databaseAdapter = new AnimalDatabaseAdapter(this);
        goToStartGameFrag();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        animalController.cancelTimer();
    }
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onGoBackToStartScreenDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        animalController.getGameTimer().cancel();
        animalController.resetPlayedAnimals();
        goToStartGameFrag();
    }

    @Override
    public void onDeleteAnimalDialogPositiveClick(DialogFragment dialog) {
        Animal animal = new Animal();
        TextView animalIdTextView = findViewById(R.id.animalIdTextView);
        animal.animal_ID = Integer.parseInt(animalIdTextView.getText().toString());
        String pictureFilename = databaseAdapter.getPictureFilenameById(animal.animal_ID);
        long animalId = databaseAdapter.deleteAnimal(this, animal.animal_ID);

        if (animalId > 0) {
            PictureManager.deleteImage(pictureFilename);
            goToAnimalDatabaseFrag();
        }
    }

    @Override
    public void onTimeUpDialogPositiveClick(DialogFragment dialogFragment) {
        passAndSwitchPlayers();
    }
    @Override
    public void findAnimal(View v) {
        EditText findAnimalEditText = findViewById(R.id.findAnimalEditText);
        String findAnimalName = findAnimalEditText.getText().toString();

        AnimalDatabaseAdapter databaseAdapter = new AnimalDatabaseAdapter(this);
        List<HashMap<String, String>> animalList = databaseAdapter.getAnimalListByName(findAnimalName);
        populateAnimalLinearLayout(animalList);
    }

    private void populateAnimalLinearLayout(List<HashMap<String, String>> animalList) {
        LinearLayout animalListLinearLayout = findViewById(R.id.animalListLinearLayout);
        animalListLinearLayout.removeAllViewsInLayout();
        if (animalList.size() > 0) {
            for (HashMap<String, String> animal : animalList) {
                TextView animalTextView = new TextView(this);
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

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frag_container, animalEditFormFragment);
                        transaction.commit();
                    }

                });
                animalListLinearLayout.addView(animalTextView);
            }
        } else {
            TextView noAnimalTextView = new TextView(this);
            noAnimalTextView.setText(R.string.no_animals_to_display);
            noAnimalTextView.setTextSize(TEXT_SIZE);
            animalListLinearLayout.addView(noAnimalTextView);
        }
    }
    @Override
    public void refreshAnimalList(View v) {
        AnimalDatabaseAdapter databaseAdapter = new AnimalDatabaseAdapter(this);
        List<HashMap<String, String>> animalList = databaseAdapter.getAnimalList();
        populateAnimalLinearLayout(animalList);
    }

    @Override
    public void goToNextPlayer(View v) {
        switchPlayers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // Here we need to check if the activity that was triggers was the Image Gallery.
            // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
            // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
            if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
                // Let's read picked image data - its URI
                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                    int imageViewId = AnimalController.getAnimalImageViewId();
                    if (imageViewId > 0) {
                        ImageView imageView = findViewById(imageViewId);
                        imageView.setImageBitmap(bitmap);
                        AnimalController.setImagePathname(imagePath);
                    }
                    // Permission is not granted
                } else {
                    Log.e(TAG, "Permission denied to access phone storage.");
                }

                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void setStartLetterAndStartGame(View v) {
        EditText chooseLetterEditText = findViewById(R.id.chooseLetterEditText);
        if (chooseLetterEditText != null) {
            String letter = chooseLetterEditText.getText().toString().toUpperCase();
            if (!letter.isEmpty() && letter.trim().length() == 1) {
                //check that letter is valid letter
                char newLetter = letter.charAt(0);
                int newLetterValue = (int) newLetter;
                if ((newLetterValue >= 65 && newLetterValue <= 90)) {
                    //set letter if valid, otherwise letter will be random
                    animalController.setLetter(newLetter);
                }
            } else {
                animalController.pickLetter();
            }
        } else {
            animalController.pickLetter();
        }
        goToPlayerFrag();
    }
    //----------------------------------------------------------------------------------------------
    /*Switches to PlayerFragment. */
    private void switchPlayers() {
        animalController.incrementPlayerIndex();
        boolean gameOver = animalController.checkGameOver();
        if (gameOver) {
            animalController.getGameTimer().cancel();
            goToEndGameFrag();
        }
//==================================================================================================
        else {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            PlayerFragment gameScreenFrag = new PlayerFragment();
            Bundle args = new Bundle();
            args.putStringArray("playedAnimals", animalController.getPlayedAnimals().toArray(new String[0]));
            gameScreenFrag.setArguments(args);
            transaction.replace(R.id.frag_container, gameScreenFrag);
            transaction.commit();
        }
    }

    /*Switches to EndGameFragment. */
    private void goToEndGameFrag() {
        /*int numHighScores = 5;
        Context c = this.getApplicationContext();
        try {
            FileInputStream i_s = openFileInput(HIGH_SCORE_FILENAME);
            //InputStream i_s = c.getResources().getAssets().open(HIGH_SCORE_FILENAME);
            //BufferedReader b_r = new BufferedReader(new InputStreamReader(i_s));
            //addHighScore(numHighScores);
            if (debug){
                Toast.makeText(this, "loaded highscores", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }*/
        //Add high score here

        //go to end game fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        EndGameFragment endGameFrag = new EndGameFragment();
        transaction.replace(R.id.frag_container, endGameFrag);
        transaction.commit();
    }
    /*Switches to StartFragment. */
    @Override
    public void goToStartGameScreen(View v) {
        goToStartGameFrag();
    }

    private void goToStartGameFrag() {
        animalController.resetVariables();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        StartFragment startFragment = new StartFragment();
        transaction.replace(R.id.frag_container, startFragment);
        transaction.commit();
    }

    // Switches to player fragment
    private void goToPlayerFrag() {
        PlayerFragment playerFragment = new PlayerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, playerFragment);
        transaction.commit();
        animalController.setPlayerIndex(FIRST_PLAYER_INDEX);
    }
    //----------------------------------------------------------------------------------------------
    //StartListener method overrides
    @Override
    public void startGame(View v) {
        //ask user for number of players
        /*Switches to PlayerFragment. */
        if (!animalController.checkEnoughPlayers()){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, NOT_ENOUGH_PLAYERS_MESSAGE, duration);
            toast.show();
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ChooseModeFragment chooseModeFragment = new ChooseModeFragment();
            transaction.replace(R.id.frag_container, chooseModeFragment);
            transaction.commit();
        }
    }

    @Override
    public void addPlayer(View v){
        //add player to player vector.  Get name and player number.  Clear player name from
        //text box, and increase the player number
        EditText playerName = findViewById(R.id.addPlayer_text);
        TextView numberOfPlayersTextView = findViewById(R.id.NumPlayersText);
        int numberOfPlayers = animalController.getPlayerCount();
        int playerNumber = numberOfPlayers + 1;

        String name;
        if (playerName.getText().length() <= NO_LETTERS) {
            name = playerName.getHint().toString();
        } else {
            name = playerName.getText().toString();
        }
        Toast.makeText(this, name + " added.", Toast.LENGTH_SHORT).show();

        animalController.addPlayer(new Player(name, false));
        playerName.setHint("Player " + ++playerNumber);
        playerName.setText(EMPTY_PLAYER_NAME);
        numberOfPlayersTextView.setText(String.valueOf(++numberOfPlayers));
    }
    //Presses "Quit". Alert pops up to double-check if user really wants to quit game.
    @Override
    public void quitGame(View v) {
        QuitGameDialogFragment frag = new QuitGameDialogFragment();
        frag.show(getFragmentManager(), "QuitGameFragment");
    }

    //Presses "Rules". DialogFragment pops up with rules.
    @Override
    public void readRules(View v) {
        RulesDialogFragment frag = new RulesDialogFragment();
        frag.show(getFragmentManager(), "RulesFragment");
    }

    //----------------------------------------------------------------------------------------------

    //Player Fragment method overrides
    @Override
    public void verify(View v){
        //checks that the animal is valid in the database.  Do a serach that requires log n?
        //If valid. points are added to the player.  If not, then the player is out (pass set to true).
        TextView animalNameTextView = findViewById(R.id.animalName_text);
        String animalTextViewValue = animalNameTextView.getText().toString();

        String animalName = animalTextViewValue.toLowerCase().trim();
        if (!animalName.startsWith(String.valueOf(AnimalController.getLetter()).toLowerCase())) {
            Toast.makeText(this, R.string.invalid_animal_for_letter, Toast.LENGTH_SHORT).show();
        } else if (!databaseAdapter.checkIfAnimalExists(animalName)) {
            Toast.makeText(this, R.string.animal_not_found, Toast.LENGTH_SHORT).show();
        } else {
            boolean animalAlreadyPlayed = animalController.checkAnimalAlreadyPlayed(animalName);
            if (animalAlreadyPlayed) {
                Toast.makeText(this, R.string.animal_already_played, Toast.LENGTH_SHORT).show();
            } else {
                animalController.addPointToCurrentPlayer();
                animalController.addPlayedAnimal(animalName);
                Toast.makeText(this, R.string.animal_found, Toast.LENGTH_SHORT).show();
                animalController.getGameTimer().cancel();

                SwitchPlayerFragment switchPlayerFragment = new SwitchPlayerFragment();
                Bundle args = new Bundle();
                args.putString("animalName", animalName);
                Animal animal = databaseAdapter.getAnimalByName(animalName);
                args.putString("funFact", animal.fact);
                args.putString("pictureFilename", animal.pictureFilename);
                switchPlayerFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_container, switchPlayerFragment);
                transaction.commit();



                //switchPlayers();
            }
        }
    }

    @Override
    public void pass(View v) {
        //sets the pass boolean to true
//        animalController.passPlayer();
//        animalController.getGameTimer().cancel();
//        switchPlayers();
        PassDialogFragment fragment = new PassDialogFragment();
        fragment.show(getFragmentManager(), "PassDialogFragment");
    }

    @Override
    public void passDialogPositiveClick(DialogFragment dialog) {
        passAndSwitchPlayers();
    }

    private void passAndSwitchPlayers() {
        animalController.passPlayer();
        animalController.getGameTimer().cancel();
        switchPlayers();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void newGame(View v) {
        //clear all the players and go back to th start fragment
        animalController.getGameTimer().cancel();
        animalController.resetPlayedAnimals();
        goToStartGameFrag();
    }

    @Override
    public void goBackToStartScreen(View v) {
        //pause countdown timer
        GoBackToStartScreenDialogFragment goBackToStartScreenDialogFragment = new GoBackToStartScreenDialogFragment();
        goBackToStartScreenDialogFragment.show(getFragmentManager(), "GoBackoStartScreenFragment");
    }

    @Override
    public void goToAnimalDatabaseScreen(View v) {
        goToAnimalDatabaseFrag();
    }

    private void goToAnimalDatabaseFrag() {
        AnimalDatabaseFragment animalDatabaseFragment = new AnimalDatabaseFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, animalDatabaseFragment);
        transaction.commit();
    }
    @Override
    public void goToAddAnimalScreen(View v) {
        AnimalEditFormFragment animalEditFormFragment = new AnimalEditFormFragment();

        Bundle args = new Bundle();
        args.putString("clickEvent", "addAnimal");
        animalEditFormFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, animalEditFormFragment);
        transaction.commit();
    }

    public void getNextLetter(View v){
        animalController.startNewRound();
        goToPlayerFrag();
    }

    //-------------------------------
    //AnimalDatabase methods
    @Override
    public void addAnimal(View v) {
        //Open connection to write data
        Animal animal = new Animal();
        EditText animalNameEditText = findViewById(R.id.animalNameEditText);
        animal.animalName = animalNameEditText.getText().toString();
        animal.pictureFilename = PictureManager.createImageName(animal.animalName);
        EditText funFactEditText = findViewById(R.id.funFactEditText);
        animal.fact = funFactEditText.getText().toString();
        long animalId = databaseAdapter.addAnimal(this, animal);

        if (animalId > 0) {
            PictureManager.saveImageToDirectory(this, AnimalController.getImagePathname(), animal.pictureFilename);
            goToAnimalDatabaseScreen(v);
        }
    }

    @Override
    public void updateAnimal(View v) {
        Animal animal = new Animal();
        EditText animalNameEditText = findViewById(R.id.animalNameEditText);
        TextView animalIdTextView = findViewById(R.id.animalIdTextView);
        animal.animal_ID = Integer.parseInt(animalIdTextView.getText().toString());
        animal.animalName = animalNameEditText.getText().toString();
        animal.pictureFilename = PictureManager.createImageName(animal.animalName);

        EditText funFactEditText = findViewById(R.id.funFactEditText);
        animal.fact = funFactEditText.getText().toString();
        long animalId = databaseAdapter.updateAnimal(this, animal);
        if (animalId > 0) {
            String previousPathname = AnimalController.getImagePathname();
            PictureManager.saveImageToDirectory(this, previousPathname, animal.pictureFilename);
            goToAnimalDatabaseScreen(v);
        }
    }

    @Override
    public void deleteAnimal(View v) {
        DeleteAnimalDialogFragment deleteAnimalDialogFragment = new DeleteAnimalDialogFragment();
        deleteAnimalDialogFragment.show(getFragmentManager(), "DeleteAnimalDialogFragment");
    }



    //-------------------------------
    /*public void addHighScore(int highScoreSlots) {
        Vector <Player> highScores = new Vector<Player>();
        Vector <Player> temp = players;
        int i = 1;
        int index = 0;
        int end = players.size();
        Player top = players.elementAt(0);      //first element

        //read file
        Context c = this.getApplicationContext();
        try {
            String line;
            File file = new File(c.getFilesDir(), HIGH_SCORE_FILENAME);
            FileInputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line = br.readLine())!= null){

                String[] tmpStr = line.split("\\)", 1);
                //highScores.add(new Player(tmpStr[1]));
                String[] tempStr = tmpStr[1].split("Score:");
                int pts = Integer.parseInt(tempStr[1]);
                highScores.add(new Player(tempStr[0].trim(),pts));
            }
        }
        catch (IOException e){
                e.printStackTrace();
        }
    //put file in vector'

        //compare current win score with other scores
      //  while (i < end-1) {
      //  boolean done = false;
        int highScoreIndex;
        //while (!done) {
            index = MainActivity.getBestPlayerScore(temp);  //get best score

            for (int j = 0;i <highScores.size();i++){
                int tmp_size = temp.size();
                for (int k = 0;k<tmp_size;k++){
                    if (temp.elementAt(k).getPoints()>highScores.elementAt(j).getPoints()){
                        highScores.add(j,temp.elementAt(k));
                        temp.remove(k);
                    }
                }
            }
            //highScoreIndex = getIndex(highScores, temp.elementAt(index).getPoints(),0,temp.size()); //get high score index
            //text += i+1 + ") "+players.elementAt(index).toString()+"\n";
            //i++;
            // players.remove(index);
            //}

           // temp.remove(index);
        //}
        //add scores and then rewrite the file


        try {
            File file = new File(c.getFilesDir(), HIGH_SCORE_FILENAME);

            FileOutputStream stream = new FileOutputStream(file);
            try {
                String highScoreStr = "";
                for(int m = 0; m <highScoreSlots;m++){
                    highScoreStr+=(highScores.elementAt(m).toString()+"\n");
                }
                stream.write(highScoreStr.getBytes());
            }
            catch(IOException e) {
                e.printStackTrace();
            }finally {
                stream.close();
            }
            //OutputStream i_s = c.getResources().getAssets().open(HIGH_SCORE_FILENAME);
            //InputStream os = c.getResources().getAssets().open(HIGH_SCORE_FILENAME);

 *//*           if (highScore.exists()){
                highScore.delete();
                highScore.createNewFile();
                System.out.println("boooooooooooooooooooobs");
            }*//*
     *//*            System.out.println("file found");
            FileOutputStream b_r = openFileOutput(HIGH_SCORE_FILENAME, Context.MODE_PRIVATE); //this is probably wrong
                        //b_r.write("HIGH SCORES");
            for (int l = 0;l >highScoreSlots;l++) {
                b_r.write((highScores.elementAt(l).toString()+"\n").getBytes());
                b_r.flush();
            }
            b_r.close();*//*

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/

    public static int getBestPlayerScore(Vector <Player> players){
        int index = 0;
        Player top = players.elementAt(0);
        for (int j = 1; j < players.size(); j++) {
            if (players.elementAt(j).getPoints() > top.getPoints()) {
                top = players.elementAt(j);
                index = j;
            }
        }
        // players.remove(index);
        return index;
    }

    ///TODO: implement high scores
    @Override
    public void displayHighScores(View v) {
        Vector<Player> highScoresVector = new Vector<>();
        Vector<Player> temp = new Vector<>();
        temp.addAll(AnimalController.getPlayerVector());
        int i = 1;
        int index = 0;

        //read file
        Context c = this.getApplicationContext();
        //Toast toast;
        String highScores = "";
        FileOutputStream stream;

        try {
            //File file = new File(c.getFilesDir(), HIGH_SCORE_FILENAME);
            //FileOutputStream stream = new FileOutputStream(file);

            //FileInputStream in = new FileInputStream(file);
            FileInputStream in = c.openFileInput(HIGH_SCORE_FILENAME);

            System.out.println(in.toString());
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("022222222222222222");

                String line;

                while ((line = br.readLine()) != null) {
                    System.out.println("33333333333333333");

                    String[] tmpStr = line.split("\\)", 1);
                    //highScores.add(new Player(tmpStr[1]));
                    String[] tempStr = tmpStr[1].split("Score:");
                    int pts = Integer.parseInt(tempStr[1]);
                    highScoresVector.add(new Player(tempStr[0].trim(), pts));

                    // int highScoreIndex;
                    //while (!done) {
                    //index = MainActivity.getBestPlayerScore(temp);  //get best score

                    // for (int j = 0;i <5;i++){

                    //}
                }
                //br.close();
                stream = c.openFileOutput(HIGH_SCORE_FILENAME, Context.MODE_PRIVATE);

                int tmp_size = temp.size();
                int highScoresVectorSize = highScoresVector.size();
                for (int k = 0; k < tmp_size; k++) {
                    if (highScoresVectorSize < 1) {
                        System.out.println("6666");
                        index = AnimalMainActivity.getBestPlayerScore(temp);  //get best score
                        System.out.println(temp.elementAt(index).toString());
                        highScoresVector.add(temp.elementAt(index));
                        temp.remove(index);
                    } else {
                        if (temp.elementAt(k).getPoints() > highScoresVector.elementAt(k).getPoints()) {
                            highScoresVector.add(k, temp.elementAt(k));
                            temp.remove(k);
                        }
                    }
                }
                if (highScoresVector.size() < 1) {
                    highScores = "No high scores";
                }
                int max_scores = 5;
                if (highScoresVector.size() < 5) {
                    max_scores = highScoresVector.size();
                } else {
                    for (int j = 0; j < max_scores; j++) {
                        highScores += highScoresVector.elementAt(j).toString() + "\n";
                    }
                }
                //System.out.println(highScores);
                stream.write(highScores.getBytes());
                stream.flush();
                stream.close();
                System.out.println("55555555555555555");

                //in.read(bytes);
                //highScores+=new String(bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // is = c.getResources().getAssets().open(HIGH_SCORE_FILENAME); //false, do something not assets
/*            openFileInput(HIGH_SCORE_FILENAME);
            br = new BufferedReader(new InputStreamReader(is));
            String line;

            while((line = br.readLine())!=null){
                highScores+=line +"\n";
            }
            br.close();
            is.close();*/
            //toast = Toast.makeText(this,reader.readLine(),Toast.LENGTH_LONG);
            //toast.show();

        } catch (IOException e) {
            //toast = Toast.makeText(this,"Error loading "+filename,Toast.LENGTH_LONG);
            //  toast.show();
            e.printStackTrace();
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        HighScoreDialogFragment frag = new HighScoreDialogFragment();
        Bundle args = new Bundle();
        args.putString("high_score_msg", highScores);
        frag.setArguments(args);
        frag.show(getFragmentManager(), "HighScoreFragment");
    }
}
