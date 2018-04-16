package com.animalgame.theanimalgame;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.animalgame.player.Player;
import com.animalgame.timer.TimerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

public class AnimalController {
    private static final boolean GAME_OVER = true;
    private static final String ALL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String EMPTY_LETTER = "";
    private static final String KEYBOARD_ERROR = "Error getting keyboard";
    private static final int COUNTDOWN_TIME = 60000;
    private static final int COUNTDOWN_DENOMINATOR = 1000;

    public static final String NO_ANIMAL = "no_animal";
    private static final int NO_LETTERS = 0;
    private static final int FIRST_PLAYER_INDEX = 0;
    private static final int MINIMUM_PLAYER_AMOUNT = 2;
    private static CountDownTimer gameTimer;

    private static char animalLetter;
    private String availableLetters;
    private static int playerIndex;
    private Vector<String> playedAnimals;
    private static Vector<Player> players;
    private final Vector<String> allAnimals;

    public AnimalController() {
        availableLetters = ALL_LETTERS;
        AnimalController.animalLetter = ' ';
        AnimalController.playerIndex = 0;
        allAnimals = new Vector<>();
        AnimalController.players = new Vector<>();
        playedAnimals = new Vector<>();
        gameTimer = null;
    }

//    public void addAnimalsFromFile(Context c) throws IOException {
//        String line;
//
//        InputStream is = c.getResources().getAssets().open(FILENAME);
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//        while ((line=br.readLine())!=null){
//            allAnimals.add(line);
//        }
//        br.close();
//    }

    public void resetVariables() {
        AnimalController.players.clear();
        AnimalController.playerIndex = 0;
        resetAvailableLetters();
    }
    //Resets the letters available.  Use when length of string = 0, or reset game has been pressed.
    private void resetAvailableLetters() {
        availableLetters = AnimalController.ALL_LETTERS;
    }

    public static Player getPlayer (){

        //otherwise, return player
        return AnimalController.players.get(AnimalController.playerIndex);
    }
    public static char getLetter() {
        return AnimalController.animalLetter;
    }
    public static Vector<Player> getPlayerVector() {
        return AnimalController.players;
    }


//    public String findAnimal(String animal) {
//        return findAnimal(allAnimals, animal, 0, allAnimals.size()).trim();
//    }
//
//    private String findAnimal(Vector<String> array, String value, int left, int right){
//        if (left > right) {
//            return NO_ANIMAL;
//        }
//        int middle = (left + right) / 2;
//
//        if (array.get(middle).compareToIgnoreCase(value) == 0) {
//            return array.get(middle);
//        }
//        else if (array.get(middle).compareToIgnoreCase(value) > 0) {
//            return findAnimal(array, value, left, middle - 1);
//        }
//        else {
//            return findAnimal(array, value, middle + 1, right);
//        }
//    }


    //Gets random letter
    public void pickLetter() {
        if (availableLetters.length() <= NO_LETTERS){
            resetAvailableLetters();
        }
        Random randomGenerator = new Random();
        int randNum = randomGenerator.nextInt(availableLetters.length()-1);
        AnimalController.animalLetter = availableLetters.charAt(randNum);
        //remove the char from available letters
        availableLetters = availableLetters.replace(String.valueOf(AnimalController.animalLetter),EMPTY_LETTER);
    }

    public void startNewRound() {
        pickLetter();
        AnimalController.playerIndex = 0;
        playedAnimals.clear();

        //reset pass variable
        for (int i = AnimalController.players.size()-1; i >= 0; i--){
            AnimalController.players.get(i).setPass(false);
        }
    }
    public void resetPlayedAnimals() {
        playedAnimals.clear();
    }

    public void passPlayer() {
        AnimalController.players.get(AnimalController.playerIndex).setPass(true);
    }

    public void incrementPlayerIndex() {
        AnimalController.playerIndex++;
        if (AnimalController.playerIndex >= AnimalController.players.size()){
            AnimalController.playerIndex = FIRST_PLAYER_INDEX;
        }
    }

    public boolean checkGameOver() {
        int playersPassed = 0;
        //Go to the next player that hasn't passed.  If all players have passed, then end game.
        while (players.get(AnimalController.playerIndex).getPass()) {
            playersPassed++;
            if (playersPassed >= AnimalController.players.size()) {
                //if there is only one player that hasn't passed, then they are the winner and
                //go to the end game fragment
                return GAME_OVER;
            }
            incrementPlayerIndex();
        }
        return !GAME_OVER;
    }

    public void setPlayerIndex(int playerIndex) {
        AnimalController.playerIndex = playerIndex;
    }

    public boolean checkEnoughPlayers() {
        return (AnimalController.players.size() >= MINIMUM_PLAYER_AMOUNT);
    }

    public int getPlayerCount() {
        return AnimalController.players.size();
    }

    public void addPlayer(Player player) {
        AnimalController.players.add(player);
    }

    public void addPlayedAnimal(String animal) {
        playedAnimals.add(animal);
    }

    public void addPointToCurrentPlayer() {
        AnimalController.players.get(AnimalController.playerIndex).addPoint();
    }

    public boolean checkAnimalAlreadyPlayed(String animal) {
        for (String s : playedAnimals){
            if (s.equalsIgnoreCase(animal)){
                return true;
            }
        }
        return false;
    }

    public static void setPlayerCountdownTimer(final Activity activity, FrameLayout gameLayout) {
        AnimalController.gameTimer = TimerFactory.createGameTimer(activity, gameLayout, COUNTDOWN_TIME, COUNTDOWN_DENOMINATOR);
        AnimalController.gameTimer.start();
    }

    public CountDownTimer getGameTimer() {
        return AnimalController.gameTimer;
    }

    public void cancelTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Toast toast = Toast.makeText(activity, KEYBOARD_ERROR, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
