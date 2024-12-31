package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.content.Context;
import android.content.SharedPreferences;



public class  SharedManager {
    private String GameMode;
    private int Level;
    private int Error;
    private int Time;
    private int Star;
    private SharedPreferences sharedpreferences ;

    public SharedManager(String gameMode, int level, int error,int time,int stars,final Context contexto,String Theme) {
        GameMode = gameMode;
        Level = level;
        Error = error;
        Time=time;
        Star=stars;
        sharedpreferences=contexto.getSharedPreferences(Theme, Context.MODE_PRIVATE);
    }


    public void  save() {

    SharedPreferences.Editor editor = sharedpreferences.edit();
    if(sharedpreferences.getString(GameMode+Level,"").equals("")){
        editor.putString(GameMode+Level, Star + ","+Time+"," + Error);
    }
    else {
        String[] values = sharedpreferences.getString(GameMode+Level, "").split(",");
        if (Integer.parseInt(values[0])<Star){
            editor.putString(GameMode+Level, Star + ","+Time+"," + Error);
        }
        else if (Integer.parseInt(values[0])==Star&&Integer.parseInt(values[1])<Time){
            editor.putString(GameMode+Level, Star + ","+Time+"," + Error );
        }
    }

    editor.apply();
    }


}
