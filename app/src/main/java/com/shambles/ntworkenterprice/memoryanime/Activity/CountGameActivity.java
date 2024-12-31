package com.shambles.ntworkenterprice.memoryanime.Activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Dialog;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;


import com.shambles.ntworkenterprice.memoryanime.MainActivity;
import com.shambles.ntworkenterprice.memoryanime.R;
import com.shambles.ntworkenterprice.memoryanime.Resources.AdInterstitial;
import com.shambles.ntworkenterprice.memoryanime.Resources.AdReward;
import com.shambles.ntworkenterprice.memoryanime.Resources.DialogInter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.shambles.ntworkenterprice.memoryanime.Resources.AssetsManager.ImageViaAssets;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.FinishMusic;

import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.PlayerBackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.initiliazeplayerbackgroundMusic;

import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.loseLevelSound;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.loseSound;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.pauseBackgroundMusic;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.victoryLevelSound;


public class CountGameActivity extends AppCompatActivity {

    private ConstraintLayout campo;
    private final ImageView []slots=new ImageView[5];
    private final TextView[]slotst=new TextView[5];
    private List<String> models=new ArrayList<>();

    private  boolean firstplay=false;
    private AdView mAdView;

    private final Handler handler = new Handler();
    private int[] coutn;
    private TextView errorcount,level,hitScore;
    private int time;
    private int telas;
    private Timer T;
    private TextView timeCount;
    private final List <Integer> positionH=new ArrayList<>();
    private final List <Integer> positionW=new ArrayList<>();
    private final List <Integer> sequence=new ArrayList<>();
    private static String  theme;
    private int countTime=0;
    private TextView modeView;
    private TextView TipCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_game);
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdInterstitial.AdInterstitialResoruce(getApplicationContext());
        AdReward.LoadAd(getApplicationContext());
        ActivityCreateView1 viewone=new ActivityCreateView1();
        ActivityCreateView2 viewtwo=new ActivityCreateView2();
        ActivityCreateView3 viewthtree=new ActivityCreateView3();
        viewone.start();
        viewtwo.start();
        viewthtree.start();
        try {
            viewone.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handler.postDelayed(() -> {
            time=6000;
            level.setText((Integer.toString(getIntent().getIntExtra("Level",4))));
            modeView=findViewById(R.id.modeViewTextC);
            modeView.setText(getIntent().getStringExtra("mode"));
            try {
                theme=getIntent().getStringExtra("Theme");
                GameGenerate(getIntent().getIntExtra("Level",4));
            } catch (IOException e) {
                e.printStackTrace();
            }
        },500);


    }


    private void ViewAddCheck() {

            campo.removeAllViewsInLayout();
            for (int n = 0; n < telas; n++) {
                ImageView newImage = new ImageView(this);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(campo.getWidth() / 7, campo.getHeight() / 7);
                newImage.setImageBitmap(ImageViaAssets(theme + "/" + models.get(sequence.get(n)), getApplicationContext()));
                newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                params.startToStart = params.getMarginStart();
                params.topToTop = 0;
                params.leftMargin = positionW.get(n);
                params.topMargin = positionH.get(n);
                campo.addView(newImage, n, params);
            }


    }
    private void GameGenerate(int newtelas) throws IOException {
        switch(getIntent().getStringExtra("mode")){
            case "Normal":
                hitScore.setText("0");
                break;
            case "Challenge":
                hitScore.setText(Integer.toString(10));
                break;

        }
                    for(int n=0;n<5;n++) {

                        slotst[n].setTextColor(getColor(R.color.textmenucolor));

                        }
        models.clear();
        models.addAll(Arrays.asList(getAssets().list(theme)));
        errorcount.setText("0");
        telas=newtelas;
        Random random=new Random();
        int height,width;
        height=campo.getHeight();
        width=campo.getWidth();
        Collections.shuffle(models);
        models=models.subList(0,5);
        campo.destroyDrawingCache();
        campo.removeAllViews();
        coutn=new int[]{0,0,0,0,0};
        sequence.clear();
        for (int n = 0; n < 5; n++) {
            slotst[n].setText("0");
        }
        for(int n=0;n<telas;n++) {
           int consheight= (int) (height*random.nextFloat());
           int conswidth= (int) (width*random.nextFloat());
            if(consheight>height-height/7){
                consheight=height-height/7;
            }
            if(conswidth>width-width/7){
                conswidth=width-width/7;
            }

           positionH.add(consheight);
           positionW.add(conswidth);
           for (int j=n-1;j>=0;j--){

              if (((!((consheight< positionH.get(j)-height/7)||consheight > positionH.get(j)+height/7))&&!((conswidth<positionW.get(j)-width/7)||(conswidth >positionW.get(j)+width/7))))
                    {
                  consheight= (int) (height*random.nextFloat());
                  conswidth= (int) (width*random.nextFloat());
                  if(consheight>height-height/7-10){
                      consheight=height-height/7-10;
                  }
                  if(conswidth>width-width/7-5){
                      conswidth=width-width/7-5;
                  }
                  positionH.set(n, consheight);
                  positionW.set(n, conswidth);
                  j=n-1;
              }
           }
            positionH.set(n, consheight);
            positionW.set(n, conswidth);
            final ImageView newImage = new ImageView(this);
            final ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width/7, height/7);
            int num=random.nextInt(5);
            coutn[num]+=1;
            sequence.add(num);
            newImage.setImageBitmap(ImageViaAssets(theme+"/" + models.get(num), getApplicationContext()));
            newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            params.startToStart = params.getMarginStart();
            params.topToTop = 0;
            params.leftMargin = conswidth;
            params.topMargin =consheight;
            final int m=n;
            handler.postDelayed(() -> campo.addView(newImage, m, params), (int) ( time / telas) *n);

        }
        handler.postDelayed(() -> {
            timeCount.setText(""+0);
            countTime=0;
            setTime(10+Integer.valueOf(level.getText().toString())*5,6000,1000);
        },time);
        ViewImages(time);
    }
    private void SetButtonConfirmation(int timedelay) {
        int height,width;
        height=campo.getHeight();
        width=campo.getWidth();
        final Button confirmad = new Button(this);
        ConstraintLayout.LayoutParams params= new ConstraintLayout.LayoutParams(width/4, height/8);
        params.startToStart = params.getMarginStart();
        params.topToTop = 0;
        params.leftMargin =width/2-width/8;
        params.topMargin =  height/2-height/16;
        confirmad.setLayoutParams(params);
        confirmad.setTextColor(getColor(R.color.textmenucolor));
        confirmad.setText(R.string.confirm);
        confirmad.setBackgroundResource(R.drawable.gradiente2);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                campo.removeAllViews();
                campo.addView(confirmad);
                for (TextView edits:slotst
                     ) {
                    edits.setClickable(true);
                }
                confirmad.setOnClickListener(v -> {
                    if (slotst[0].getText().toString().equals("") || slotst[1].getText().toString().equals("") || slotst[2].getText().toString().equals("") || slotst[3].getText().toString().equals("") || slotst[4].getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.alert1, Toast.LENGTH_LONG).show();
                    } else {

                        if (VerifyCount()) {
                            DialogSituation(getResources().getString(R.string.Victory));
                            victoryLevelSound();

                        } else {
                            errorcount.setText(Integer.toString(Integer.parseInt(errorcount.getText().toString())+1));

                            hitScore.setText(Integer.toString(Integer.parseInt(hitScore.getText().toString())-1));
                            if(Integer.parseInt(hitScore.getText().toString())<=0){
                                DialogSituation(getString(R.string.Defeat));
                                loseLevelSound();
                            }else{
                                loseSound();
                            }

                        }
                    }
                });

            }
        },timedelay);
    }
    private void ViewImages(int timeview) {


        if(models.size()==5) {
            for (int n = 0; n < 5; n++) {
                slots[n].setImageResource(R.drawable.darkness);
                slotst[n].setClickable(false);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int n = 0; n < 5; n++) {
                        slots[n].setImageBitmap(ImageViaAssets(theme+ "/" + models.get(n), getApplicationContext()));
                        slots[n].setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }
                }
            },timeview);

            SetButtonConfirmation(timeview);
        }
    }
    private boolean VerifyCount() {
        boolean verify=true;
        String[] test=new String[5];
        for(int n=0;n<5;n++) {
            test[n] = slotst[n].getText().toString();
            if ((coutn[n]) == Integer.parseInt(slotst[n].getText().toString())) {
                //slotst[n].setTextColor(getColor(R.color.hitdobem));
            } else{
           // slotst[n].setTextColor(getColor(R.color.errorcolor));
            verify = false;
        }

        }
        return verify;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FinishMusic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("rating",true);
        startActivity(intent);
    }
    public void ContextKeyboard(final View view) {
        final Button[] buttons=new Button[12];
        int id=view.getId();
        if (id==R.id.slot1text) {
                slotst[0].setText("");
            }
        else if(id==R.id.slot2text) {
            slotst[1].setText("");
        }
        else if(id==R.id.slot3text) {
            slotst[2].setText("");
        }
        else if(id==R.id.slot4text) {
            slotst[3].setText("");
        }
        else if(id==R.id.slot5text) {
            slotst[4].setText("");
        }


        final Dialog dialog = new Dialog(CountGameActivity.this);

            dialog.setTitle(R.string.alert2);
            dialog.setContentView(R.layout.menucontext);
            buttons[0]=dialog.findViewById(R.id.oneId);
            buttons[1]=dialog.findViewById(R.id.twoId);
            buttons[2]=dialog.findViewById(R.id.threeId);
            buttons[3]=dialog.findViewById(R.id.fourId);
            buttons[4]=dialog.findViewById(R.id.fiveId);
            buttons[5]=dialog.findViewById(R.id.sixId);
            buttons[6]=dialog.findViewById(R.id.sevenId);
            buttons[7]=dialog.findViewById(R.id.eightId);
            buttons[8]=dialog.findViewById(R.id.nineId);
            buttons[9]=dialog.findViewById(R.id.zeroId);
            buttons[10]=dialog.findViewById(R.id.delId);
            buttons[11]=dialog.findViewById(R.id.checkId);

            buttons[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   checkNumberandText(view,Integer.parseInt(buttons[0].getText().toString()));
                    }

                });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[1].getText().toString()));
            }

        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[2].getText().toString()));
            }

        });
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[3].getText().toString()));
            }

        });
        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[4].getText().toString()));
            }

        });
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[5].getText().toString()));
            }

        });
        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[6].getText().toString()));
            }

        });
        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[7].getText().toString()));
            }

        });
        buttons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[8].getText().toString()));
            }

        });
        buttons[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view,Integer.parseInt(buttons[9].getText().toString()));
            }

        });
        buttons[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberandText(view);
            }

        });
        buttons[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }
    public void DialogSituation(String situation){
        if(T!=null){
            T.cancel();
            T=null;
        }
        String modeSave=getIntent().getStringExtra("mode");

        if(getIntent().getStringExtra("mode").equals("Normal")){
            modeSave="";
        }

        campo.removeAllViews();
        if(situation.equals(getString(R.string.Victory))&&firstplay) {
            AdInterstitial.AdInterstitialResoruceShow(CountGameActivity.this,getApplicationContext());
        }else{
            firstplay=true;
        }
        DialogInter.DialogInter(CountGameActivity.this,errorcount.getText().toString(),
                level.getText().toString(),countTime,getString(R.string.countpictures)+modeSave,getApplicationContext(),theme,situation);



            DialogInter.getTimeFinish().setText(R.string.RepeatLevel);
            DialogInter.getTimeFinish().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInter.getDialogInter().dismiss();
                    DialogInter.getDialogInter().cancel();
                    try {
                        GameGenerate(telas);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        if(situation.equals(getString(R.string.Defeat))) {

            DialogInter.getStatusBtn().setText(R.string.RepeatLevel);
            DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInter.getDialogInter().dismiss();
                    DialogInter.getDialogInter().cancel();
                    try {
                        GameGenerate(telas);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else if (situation.equals(getApplicationContext().getResources().getString(R.string.Victory))){

            if(telas==15){
                DialogInter.getStatusBtn().setText(timeCount.getText().toString()+"\n"+getString(R.string.RepeatLevel));
                Toast.makeText(getApplicationContext(), R.string.MaxLevel, Toast.LENGTH_LONG).show();
                DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            GameGenerate(telas);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                DialogInter.getStatusBtn().setText(R.string.NextLevel);
                DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DialogInter.getDialogInter().dismiss();
                        DialogInter.getDialogInter().cancel();
                        level.setText(Integer.toString(Integer.parseInt(level.getText().toString()) + 1));

                        try {
                            GameGenerate(telas + 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }
    }



    private void setTime(int stageTime,int delay,int period) {
        T=new Timer();
        if(getIntent().getStringExtra("mode").equals("Challenge")) {
            countTime=stageTime;
            T.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeCount.setText(getString(R.string.time) + "\n" + countTime);
                            countTime--;
                            if(countTime<=0){
                                DialogSituation(getString(R.string.Defeat));
                            }
                        }
                    });
                }
            }, delay, period);
        }else {

            T.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeCount.setText(getString(R.string.time) + "\n" + countTime);
                            countTime++;
                        }
                    });
                }
            }, delay, period);
        }

    }
    private void checkNumberandText(View view) {
        if (view.getId() == R.id.slot1text) {
            slotst[0].setText("");
        } else if (view.getId() == R.id.slot2text) {
            slotst[1].setText("");
        } else if (view.getId() == R.id.slot3text) {
            slotst[2].setText("");
        } else if (view.getId() == R.id.slot4text) {
            slotst[3].setText("");
        } else if (view.getId() == R.id.slot5text) {
            slotst[4].setText("");
        }
    }
    private void checkNumberandText(View view,int val) {
        if (view.getId() == R.id.slot1text) {
            slotst[0].setText(slotst[0].getText().toString() + Integer.toString(val));
        } else if (view.getId() == R.id.slot2text) {
            slotst[1].setText(slotst[1].getText().toString() + Integer.toString(val));
        } else if (view.getId() == R.id.slot3text) {
            slotst[2].setText(slotst[2].getText().toString() + Integer.toString(val));
        } else if (view.getId() == R.id.slot4text) {
            slotst[3].setText(slotst[3].getText().toString() + Integer.toString(val));
        } else if (view.getId() == R.id.slot5text) {
            slotst[4].setText(slotst[4].getText().toString() + Integer.toString(val));
        }
    }




    class ActivityCreateView1 extends Thread {
        @Override
        public void run() {
            super.run();


            campo = findViewById(R.id.campoVision);
            errorcount = findViewById(R.id.errorCount);
            level = findViewById(R.id.Levelstatus2);
            timeCount = findViewById(R.id.timeCount);
            TipCount = findViewById(R.id.tipcount);
            hitScore=findViewById(R.id.hitScore);
            ImageView time3 = findViewById(R.id.timeview3);

            time3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(TipCount.getText().toString()) > 0) {
                        TipCount.setText(Integer.toString(Integer.valueOf(TipCount.getText().toString()) - 1));

                        String[] test = new String[5];
                        for (int n = 0; n < 5; n++) {
                            test[n] = slotst[n].getText().toString();
                            if ((coutn[n]) == Integer.parseInt(slotst[n].getText().toString())) {
                                slotst[n].setTextColor(getColor(R.color.hitdobem));
                            } else {
                                slotst[n].setTextColor(getColor(R.color.errorcolor));
                            }
                        }
                        ViewAddCheck();
                        ViewImages(2000);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),R.string.checkinternet,Toast.LENGTH_LONG).show();
                        AdReward.AdRewardResource(getApplicationContext(),CountGameActivity.this,TipCount);
                        if (!(AdReward.getReward()==null)) {
                            Toast.makeText(getApplicationContext(),R.string.alertadd,Toast.LENGTH_LONG).show();
                            AdReward.getReward().setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    T.cancel();
                                    T = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when ad fails to show.

                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    // Set the ad reference to n
                                    // ull so you don't show the ad a second time.
                                    PlayerBackgroundMusic();
                                    setTime(countTime, 0, 1000);


                                }
                            });
                        }
                     //   AdReward.AdRewardResource(getApplicationContext(),CountGameActivity.this,TipCount);


                    }
                    if(Integer.parseInt(TipCount.getText().toString())==0){
                        AdReward.LoadAd(getApplicationContext());
                    }
                }

            });


        }
    }

        private class ActivityCreateView2 extends Thread {
            @Override
            public void run() {
                super.run();

                slots[0] = findViewById(R.id.slot1);
                slots[1] = findViewById(R.id.slot2);
                slots[2] = findViewById(R.id.slot3);
                slots[3] = findViewById(R.id.slot4);
                slots[4] = findViewById(R.id.slot5);
                slotst[0] = findViewById(R.id.slot1text);
                slotst[1] = findViewById(R.id.slot2text);
                slotst[2] = findViewById(R.id.slot3text);
                slotst[3] = findViewById(R.id.slot4text);
                slotst[4] = findViewById(R.id.slot5text);


            }
        }

        private class ActivityCreateView3 extends Thread {
            @Override
            public void run() {
                super.run();
                FrameLayout adContainerView = findViewById(R.id.adView2);
                mAdView = new AdView(getApplicationContext());
                mAdView.setAdUnitId("ca-app-pub-9324003669548340/4689440509");
                adContainerView.addView(mAdView);
                final AdRequest adRequest = new AdRequest.Builder().build();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 50));
                        mAdView.loadAd(adRequest);


                    }
                });
                initiliazeplayerbackgroundMusic(getApplicationContext());
                PlayerBackgroundMusic();

            }
        }




    @Override
    protected void onStop() {
        super.onStop();
        pauseBackgroundMusic();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PlayerBackgroundMusic();
    }

}


