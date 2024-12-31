package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;


import com.shambles.ntworkenterprice.memoryanime.R;

public class MediaAux {

    private static MediaPlayer player,playersound,playerInit;
    private static Context contexto;
    private static float soundVolume;
    private static float musicVolume;
    private static int music=R.raw.music3;
    private static SharedPreferences sharedpreferences;
    public static void initiliazeplayerbackgroundMusic(Context context){
        sharedpreferences = context.getSharedPreferences("GAMESTATUS", Context.MODE_PRIVATE);
        contexto = context;
        if(!sharedpreferences.getBoolean("music",true)){

        }else {

            player = MediaPlayer.create(contexto.getApplicationContext(), music);

        }
    }




    public static void FinishMusic(){
        if(player!=null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public static void victorySound() {
        if(!sharedpreferences.getBoolean("sound",true)){

        }else {
            playersound = MediaPlayer.create(contexto.getApplicationContext(), R.raw.win);
            playersound.setVolume(soundVolume,
                    soundVolume);
            playersound.start();
        }
    }

    public static void loseSound() {
        if(!sharedpreferences.getBoolean("sound",true)){

        }else {
            playersound = MediaPlayer.create(contexto.getApplicationContext(), R.raw.lose);
            playersound.setVolume(soundVolume,
                    soundVolume);
            playersound.start();
        }

    }
    public static void PlayerBackgroundMusic(){
        if(!sharedpreferences.getBoolean("music",true)){

        }else if(player!=null&&!player.isPlaying()){
            player.start();
            player.setVolume(musicVolume, musicVolume);
            player.setLooping(true);
        }
    }


    public static void pauseBackgroundMusic(){
        if(player!=null){
            player.pause();
        }

    }
    public static void dreamInit(Context context){
        sharedpreferences = context.getSharedPreferences("GAMESTATUS", Context.MODE_PRIVATE);
        if(!sharedpreferences.getBoolean("music",true)){

        }else if((playerInit==null)) {
            contexto = context;
            playerInit = MediaPlayer.create(contexto.getApplicationContext(), R.raw.dreams);
            playerInit.start();
            playerInit.setVolume(musicVolume,
                    musicVolume);
            playerInit.setLooping(true);
        }else{
            playerInit.start();
        }
    }
    public static void dreamInitPause(){
        if((playerInit!=null)&&playerInit.isPlaying()) {
            playerInit.pause();
        }
    }
    public static void victoryLevelSound() {

        if(!sharedpreferences.getBoolean("sound",true)){

        }else {

            playersound = MediaPlayer.create(contexto.getApplicationContext(), R.raw.levelvictory);
            playersound.start();
            playersound.setVolume( soundVolume,
                    soundVolume);
           /* playersound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!player.isPlaying()&&player!= null) {
                        player.start();
                    }
                }
            });*/
        }
    }
    public static void loseLevelSound() {

        if(!sharedpreferences.getBoolean("sound",true)){

        }else {
            playersound = MediaPlayer.create(contexto.getApplicationContext(), R.raw.levellose);
            playersound.start();
            playersound.setVolume(soundVolume,
                    soundVolume);
            /*playersound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!player.isPlaying()&&player!= null) {
                        player.start();
                    }
                }
            });*/
        }
    }
    public static void setVolume(String playerName,float volume){

        switch (playerName){
            case "musics":
                musicVolume=(float) volume/100;
                if(playerInit!=null) {
                    playerInit.setVolume((float) volume / 100,
                            (float) volume / 100);
                }

                break;
            case "sounds":
                soundVolume=(float) volume/100;
                break;

        }
    }


}
