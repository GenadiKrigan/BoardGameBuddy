package com.example.boardgamebuddt.models;

import java.util.List;
import java.util.ArrayList;

public class Game implements java.io.Serializable {
    private String firebaseId;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int bestPlayers;
    private int avgPlayTime;
    private int difficulty;
    private List<String> categories;
    private String learningStatus;
    private boolean isUnplayed;
    private int totalPlayTime;
    private int playCount;
    private String rulesNote;

    public Game(){
        this.categories = new ArrayList<>();
    }

    public Game(String firebaseId, String name, int minPlayers, int maxPlayers, int bestPlayers, int avgPlayTime, int difficulty, List<String> categories, boolean alreadyPlayed, String rulesNote){
        this.firebaseId = firebaseId;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.bestPlayers = bestPlayers;
        this.avgPlayTime = avgPlayTime;
        this.difficulty = difficulty;
        this.categories = categories != null ? categories : new ArrayList<>();
        if(!alreadyPlayed){
            this.learningStatus = "Learn";
            this.isUnplayed = false;
        }
        else {
            this.learningStatus = "Need to learn";
            this.isUnplayed = true;
        }
        this.playCount = 0;
        this.totalPlayTime = 0;
        this.rulesNote = rulesNote.isEmpty() ? "" : rulesNote;

    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getBestPlayers() {
        return bestPlayers;
    }

    public int getAvgPlayTime() {
        return avgPlayTime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getLearningStatus() {
        return learningStatus;
    }

    public boolean isUnplayed() {
        return isUnplayed;
    }

    public int getTotalPlayTime() {
        return totalPlayTime;
    }

    public int getPlayCount() {
        return playCount;
    }

    public String getRulesNote() {
        return rulesNote;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setBestPlayers(int bestPlayers) {
        this.bestPlayers = bestPlayers;
    }

    public void setAvgPlayTime(int avgPlayTime) {
        this.avgPlayTime = avgPlayTime;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setLearningStatus(String learningStatus) {
        this.learningStatus = learningStatus;
    }

    public void setUnplayed(boolean unplayed) {
        isUnplayed = unplayed;
    }

    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void setRulesNote(String rulesNote) {
        this.rulesNote = rulesNote;
    }
}
