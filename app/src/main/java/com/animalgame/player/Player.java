package com.animalgame.player;


/**
 * Player class for animal game.
 * Aug 16/16- basic getName, toString, addPoint, and Player methods created.
 * Created by Kiersten on 2016-08-16.
 */
public class Player {
    private String name;
    private int points = 0;
    private static final int POINTS_ADDED = 10;
    private boolean pass = false;
    public Player(String playerName, boolean pass) {
        name = playerName;
        this.pass = pass;
    }
    public Player(String playerName, int pts){
        name = playerName;
        points=pts;
    }

    //returns the name of the player
    public String getName() {
        return name;
    }

    //Adds one point to player
    public void addPoint(){
        points += POINTS_ADDED;
    }

    //returns to string for player
    public String toString(){
        return (name+"\t/"+points);
    }
    public boolean getPass(){
        return pass;
    }
    public void setPass(boolean newPass){
        pass = newPass;
    }
    public int getPoints() {
        return points;
    }
}

