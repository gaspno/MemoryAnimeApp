package com.shambles.ntworkenterprice.memoryanime.model;

public class Level {
    private String Level,Img;
    private int Stars;
    private boolean set;

    public Level(String level, int stars, boolean setVal, String img) {
        Level = level;
        Stars = stars;
        set = setVal;
        Img=img;
    }

    public String getLevel() {
        return Level;
    }

    public int getStars() {
        return Stars;
    }

    public boolean getSet() {
        return set;
    }

    public String getImg() {
        return Img;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
}
