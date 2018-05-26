package com.animalgame.theanimalgame;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.animalgame.dialogFragment.HighScoreDialogFragment;
import com.animalgame.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class HighScoreController {
    private static final String TAG = "HighScoreController";
    private static final int MAX_HIGH_SCORES = 10;
    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/animalGame/";
    private static final String HIGH_SCORE_FILENAME = ROOT + "high_scores.txt";
    private static final String BRACKET = ") ";
    private static final String COLON = ": ";
    private static final int SPLIT_LIMIT = 2;
    public void updateHighScores(Activity activity, Vector<Player> players) {
        Vector<Player> highScoresVector = new Vector<>();
        for (Player player : players) {
            //Only add players if their score is greater than 0
            if (player.getPoints() > 0) {
                highScoresVector.add(player);
            }
        }
        StringBuilder highScoresBuilder = new StringBuilder();

        File createDir = new File(ROOT);
        if (!createDir.exists()) {
            if (!createDir.mkdir()) {
                Toast.makeText(activity, "Failed to create directory: " + ROOT, Toast.LENGTH_SHORT).show();
            }
        }

        File file = new File(HIGH_SCORE_FILENAME);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Log.e(TAG, "Failed to create high score file.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error creating high score file: " + e.getMessage());
                Toast.makeText(activity, "Error creating high score file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                //Load players from high score file into vector
                while ((line = br.readLine()) != null) {
                    String[] playerStr = line.split("\\)", SPLIT_LIMIT);
                    String[] tmpStr = playerStr[1].split(COLON, SPLIT_LIMIT);
                    int pts = Integer.parseInt(tmpStr[1].trim());
                    highScoresVector.add(new Player(tmpStr[0].trim(), pts));
                }
                br.close();

                //sort by player scores
                Collections.sort(highScoresVector, new PlayerScoreComparator());

                int max_scores = highScoresVector.size() < MAX_HIGH_SCORES ? highScoresVector.size() : MAX_HIGH_SCORES;
                for (int i = 0; i < max_scores; i++) {
                    highScoresBuilder.append(i + 1);
                    highScoresBuilder.append(BRACKET);
                    highScoresBuilder.append(highScoresVector.elementAt(i).getName());
                    highScoresBuilder.append(COLON);
                    highScoresBuilder.append(highScoresVector.elementAt(i).getPoints());
                    highScoresBuilder.append("\n");
                }

                //write to file
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(highScoresBuilder.toString().getBytes());
                stream.flush();
                stream.close();

            } catch (Exception e) {
                Log.e(TAG, "Failed to write to high score file.");
                Log.getStackTraceString(e);
                Toast.makeText(activity, "Error creating high score file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(activity, "Saved high score.", Toast.LENGTH_SHORT).show();
    }

    ///TODO: implement high scores
    public void displayHighScores(Activity activity) {
        StringBuilder highScoreBuilder = new StringBuilder();

        //get high score file; create one if it doesn't exist
        File file = new File(HIGH_SCORE_FILENAME);
        if (!file.exists()) {
                Log.e(TAG, "Failed to read high score file.");
                highScoreBuilder.append("No high scores.");
        } else {
            try {
                FileInputStream in = new FileInputStream(file);

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = br.readLine()) != null) {
                    highScoreBuilder.append(line);
                    highScoreBuilder.append("\n");
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to read high score file.");
                Log.getStackTraceString(e);
                Toast.makeText(activity, "Error creating high score file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                highScoreBuilder.setLength(0);
                highScoreBuilder.trimToSize();
                highScoreBuilder.append("Error reading high scores.");
            }
        }

        if (highScoreBuilder.length() <= 0) {
            highScoreBuilder.append("No high scores.");
        }
        HighScoreDialogFragment frag = new HighScoreDialogFragment();
        Bundle args = new Bundle();
        args.putString("high_score_msg", highScoreBuilder.toString());
        frag.setArguments(args);
        frag.show(activity.getFragmentManager(), "HighScoreFragment");
    }

    private int getBestPlayerScoreIndex(Vector<Player> players){
        int index = 0;
        Player top = players.elementAt(0);
        for (int j = 1; j < players.size(); j++) {
            if (players.elementAt(j).getPoints() > top.getPoints()) {
                top = players.elementAt(j);
                index = j;
            }
        }
        return index;
    }

    public String getSortedPlayerList(Vector<Player> players) {
        Vector<Player> tempPlayers = new Vector<>();
        tempPlayers.addAll(players);
        Collections.sort(tempPlayers, new PlayerScoreComparator());

        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Player tempPlayer : tempPlayers) {
            builder.append(index++);
            builder.append(BRACKET);
            builder.append(tempPlayer.getName());
            builder.append(COLON);
            builder.append(tempPlayer.getPoints());
            builder.append("\n");
        }
        return builder.toString();
    }
    class PlayerScoreComparator implements Comparator<Player>
    {
        // Used for sorting player score in descending order
        public int compare(Player a, Player b)
        {
            return b.getPoints() - a.getPoints();
        }
    }
}
