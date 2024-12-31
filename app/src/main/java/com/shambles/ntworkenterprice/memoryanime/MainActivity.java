package com.shambles.ntworkenterprice.memoryanime;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;



import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import com.google.android.gms.ads.MobileAds;

import com.shambles.ntworkenterprice.memoryanime.Activity.LevelModelThemeSelect;
import com.shambles.ntworkenterprice.memoryanime.Activity.RecordsActivity;
import com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux;

import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.FinishMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.dreamInitPause;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.initiliazeplayerbackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.setVolume;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView MCredits;
    private Switch sounds,music;
    private SharedPreferences sharedpreferences;
    private SeekBar musicVolume,soundVolume;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, initializationStatus -> {
        });
        sharedpreferences=getSharedPreferences("GAMESTATUS", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        musicVolume=findViewById(R.id.seekBarMusic);
        soundVolume=findViewById(R.id.seekBarSound);



        musicVolume.setProgress(sharedpreferences.getInt("musicvol",50));
        soundVolume.setProgress(sharedpreferences.getInt("soundvol",50));
        setVolume("musics", musicVolume.getProgress());
        setVolume("sounds", soundVolume.getProgress());
        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume("musics", seekBar.getProgress());
                editor.putInt("musicvol",seekBar.getProgress());
                editor.commit();
            }
        });
        soundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume("sounds", seekBar.getProgress());
                editor.putInt("soundvol",seekBar.getProgress());
                editor.commit();
            }
        });
        MCredits=findViewById(R.id.MCredits);
        Button card = findViewById(R.id.Memory);
        card.setOnClickListener(this);
        Button count = findViewById(R.id.Photographic);
        count.setOnClickListener(this);
        Button records = findViewById(R.id.Records);
        records.setOnClickListener(this);
        Button photo=findViewById(R.id.Count);
        photo.setOnClickListener(this);
        sounds=findViewById(R.id.soundsswitch);
        MCredits.setOnClickListener(this);


        music=findViewById(R.id.musicswitch);
        music.setChecked(sharedpreferences.getBoolean("music",true));
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editor.putBoolean("music", true);
                    editor.commit();
                    MediaAux.dreamInit(getApplicationContext());
                    initiliazeplayerbackgroundMusic(getApplicationContext());
                }else{
                    editor.putBoolean("music", false);
                    editor.commit();
                    dreamInitPause();
                    FinishMusic();
                }

            }

        });
        sounds.setChecked(sharedpreferences.getBoolean("sound",true));

        sounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editor.putBoolean("sound", true);
                }else{
                    editor.putBoolean("sound", false);
                }
                editor.commit();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        MediaAux.dreamInit(getApplicationContext());
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Button Not, MoreApps;
                RatingBar simpleRatingBar;
                Dialog dialogInter = new Dialog(MainActivity.this);
                dialogInter.setContentView(R.layout.promoappgasp);
                dialogInter.setCanceledOnTouchOutside(false);
                dialogInter.setOwnerActivity(MainActivity.this);
                dialogInter.getWindow();
                dialogInter.setCancelable(false);
                Not = dialogInter.findViewById(R.id.notId);
                MoreApps = dialogInter.findViewById(R.id.MoreApps);
                simpleRatingBar = dialogInter.findViewById(R.id.ratingLevelId);
                MoreApps.setOnClickListener(view -> {
                    Intent intentRating = new Intent(Intent.ACTION_VIEW);
                    intentRating.setData(Uri.parse(
                            "https://play.google.com/store/apps/dev?id=8934541135388811083"));
                    startActivity(intentRating);
                });
                Not.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                simpleRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        Intent intentRating = new Intent(Intent.ACTION_VIEW);
                        intentRating.setData(Uri.parse(
                                "https://play.google.com/store/apps/details?id=com.shambles.ntworkenterprice.memoryanime"));
                        startActivity(intentRating);
                    }
                });
                dialogInter.setCanceledOnTouchOutside(true);
                dialogInter.show();
            }
        });


    }
    @Override
    protected void onPause() {
        super.onPause();
        dreamInitPause();
    }

    @Override
    public void onClick(View v) {

        Intent intent=new Intent(getApplicationContext(), LevelModelThemeSelect.class);
        if (v.getId() == R.id.Memory) {
            intent.putExtra("GameMode", "CardGame");
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.Photographic) {
            intent.putExtra("GameMode", "CountGame");
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.Count) {
            intent.putExtra("GameMode", "PhotoGraphic");
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.Records) {
            startActivity(new Intent(getApplicationContext(), RecordsActivity.class));
            finish();
        } else if (v.getId() == R.id.MCredits) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.credits);
            Button close = dialog.findViewById(R.id.closeBtn);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOwnerActivity(MainActivity.this);

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }




}