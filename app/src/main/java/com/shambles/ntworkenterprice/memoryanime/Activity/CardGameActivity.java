
package com.shambles.ntworkenterprice.memoryanime.Activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.victorySound;


public class CardGameActivity extends AppCompatActivity {

    private static int find = 0;
    private static String[] first;
    private final String TAG = "CardGame";
    private static ImageView[] index;
    private static boolean hit;
    private int height, width;
    private final List<ImageView> imagesBackupIndex = new ArrayList<>();
    private LinearLayout tableGame;
    private List<String> imageResource = new ArrayList<>();
    private int columnsnumber;
    private int linesnumber;
    private TextView hitcount, errorcount, level;
    private int numberIdImage = 0;
    private final Handler handler = new Handler();
    private AdView mAdView;
    private int hitIndicator=0;
    private Runnable r;
    private TextView timeCount;
    private boolean firstplay=false;
    private TextView modeView;
    private Timer T;
    private static String  theme;
    private int countTime=0;
    private TextView TipCount;
    private Animation btnAnim;
    private final int Delay=6000;
    private final int Period=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdReward.LoadAd(getApplicationContext());
        AdInterstitial.AdInterstitialResoruce(getApplicationContext());
        ActivityCreateView1 threadone = new ActivityCreateView1();
        ActivityCreateView2 threadtwo = new ActivityCreateView2();
        ActivityCreateView3 threadthree = new ActivityCreateView3();
        threadone.setPriority(10);
        threadtwo.setPriority(9);
        threadthree.setPriority(8);
        threadone.start();
        threadtwo.start();
        threadthree.start();
        try {
            threadone.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            threadtwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btnAnim = AnimationUtils.loadAnimation(this, R.anim.animbtn);
        btnAnim.reset();
        handler.postDelayed(() -> {
            level.setText((Integer.toString((int) (getIntent().getIntExtra("LevelNumber", 0) + 1))));
            try {
                String tam = getIntent().getStringExtra("Level");
                String[] columns = tam.split("x");
                theme = getIntent().getStringExtra("Theme");
                CreateGame(Integer.parseInt(columns[1]), Integer.parseInt(columns[0]));
                handler.postDelayed(r, 5000);
                setTime(linesnumber * columnsnumber * 3, Delay, Period);
                modeView = findViewById(R.id.modeViewTextC);
                modeView.setText(getIntent().getStringExtra("mode"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 500);

    }


    public void CreateGame(int coluns, int lines) throws IOException {
        height = tableGame.getHeight();
        width = tableGame.getWidth();
        columnsnumber = coluns;
        linesnumber = lines;
        imageResource.clear();
        imageResource.addAll(Arrays.asList(getAssets().list(theme)));
        Collections.shuffle(imageResource);
        imageResource = imageResource.subList(0, (columnsnumber * linesnumber) / 2);
        List <String>aux=new ArrayList<>();
        for (String a:imageResource
             ) {
            aux.add(a);
            aux.add(a);
        }
        imageResource.clear();
        imageResource=aux;
        switch(getIntent().getStringExtra("mode")){
            case "Normal":
                hitcount.setText("0");
                break;
            case "Challenge":
                hitcount.setText(Integer.toString(10));
                break;
        }
        Collections.shuffle(imageResource);
        errorcount.setText("0");
        CreateViewGame(linesnumber, columnsnumber);
    }
    public void CreateViewGame(final int linesnumber, final int columnsnumber) {

        imagesBackupIndex.clear();
        LinearLayout[] lines = new LinearLayout[linesnumber];
        tableGame.destroyDrawingCache();
        tableGame.removeAllViews();
        numberIdImage = 0;
        for (int o = 0; o < linesnumber; o++) {
            lines[o] = new LinearLayout(this);
            lines[o].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height / linesnumber));
            for (int m = 0; m < columnsnumber; m++) {
                ImageView image = new ImageView(CardGameActivity.this);
                SetImage(image);
                image.setOnClickListener(v -> {
                    image.setImageBitmap(ImageViaAssets(theme + "/" + imageResource.get(v.getId()), getApplicationContext()));
                    image.startAnimation(btnAnim);
                    image.setClickable(false);
                    handler.postDelayed(() -> {
                        String aquieagora;
                        aquieagora = CheckingResult(imageResource.get(v.getId()), (ImageView) v);
                        if (aquieagora.isEmpty()) ;
                        else if (aquieagora.equals("acerto")) {
                            victorySound();
                            for (ImageView imageview : index) {

                                imageview.clearAnimation();
                            }
                            hitIndicator+=1;
                            if (hitIndicator == linesnumber * columnsnumber / 2) {
                                Toast.makeText(getApplicationContext(), R.string.Victory, Toast.LENGTH_SHORT).show();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        DialogResult(getString(R.string.Victory));

                                        victoryLevelSound();
                                        hitIndicator=0;
                                    }
                                },500);


                            }
                        } else if (aquieagora.equals("erro")) {

                            loseSound();
                            for (ImageView imageview : index) {
                                imageview.setImageResource(R.drawable.darkness);
                                imageview.setClickable(true);
                                imageview.clearAnimation();
                            }
                            errorcount.setText(Integer.toString(Integer.parseInt(errorcount.getText().toString()) +1));
                            if(getIntent().getStringExtra("mode").equals("Challenge")){
                                hitcount.setText(Integer.toString(Integer.parseInt(hitcount.getText().toString())-1));
                                if(Integer.parseInt(hitcount.getText().toString())<=0){
                                    DialogResult(getString(R.string.Defeat));
                                    loseLevelSound();
                                    hitIndicator=0;

                                }
                            }


                        }
                    }, 250);


                });
                image.setClickable(false);
                imagesBackupIndex.add(image);
                lines[o].addView(image, m);
                numberIdImage += 1;
            }
            tableGame.addView(lines[o], o);

        }

    }
    private void SetImage(ImageView image) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / columnsnumber, height / linesnumber);
        image.setLayoutParams(params);
        image.setId(numberIdImage);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setPadding(2,2,2,2);
        image.setImageBitmap(ImageViaAssets(theme + "/" + imageResource.get(numberIdImage), getApplicationContext()));

    }
    private static String CheckingResult(String s, ImageView parent) {

        int selects = 2;
        if (find == 0) {
            first = new String[selects];

            index = new ImageView[selects];
        }
        first[find] = s;

        index[find] = parent;
        find++;
        if (find < selects) {

            return "";
        } else {

            for (int n = 1; n < first.length; n++) {
                hit = true;
                if (!first[0].equals(first[n])) {
                    hit = false;
                    break;
                }
            }
        }
        if (hit) {
            ResetGameStatus();
            return "acerto";
        }
        ResetGameStatus();
        return "erro";
    }
    private static void ResetGameStatus() {
        find = 0;
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
    public void setTime(int stageTime,int delay,int period)  {
            T = new Timer();
            if (getIntent().getStringExtra("mode").equals("Challenge")) {
                countTime = stageTime;
                T.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeCount.setText(getString(R.string.time) + "\n" + countTime);
                                countTime--;
                                if (countTime <= 0) {
                                    DialogResult(getString(R.string.Defeat));
                                    hitIndicator=0;
                                }
                            }
                        });
                    }
                }, delay, period);
            } else {
                countTime = 0;

                T.scheduleAtFixedRate(new TimerTask() {
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


    public void SeeTip() {
        if(Integer.parseInt(TipCount.getText().toString())>0){
            TipCount.setText(Integer.toString(Integer.valueOf(TipCount.getText().toString())-1));
        for (final ImageView darkwar :
                imagesBackupIndex) {
            if (darkwar.isClickable()) {
                darkwar.setImageBitmap(ImageViaAssets(theme + "/" + imageResource.get(darkwar.getId()), getApplicationContext()));
                darkwar.setClickable(false);
            }
        }
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.checkinternet,Toast.LENGTH_LONG).show();
            int backup=countTime;
            AdReward.AdRewardResource(getApplicationContext(),CardGameActivity.this,TipCount);
            if (!(AdReward.getReward()==null)) {
                Toast.makeText(getApplicationContext(),R.string.alertadd,Toast.LENGTH_LONG).show();
                T.cancel();
                T = null;
                AdReward.getReward().setFullScreenContentCallback(new FullScreenContentCallback() {


                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to n
                        // ull so you don't show the ad a second time.
                        PlayerBackgroundMusic();
                        setTime(backup, 0, 1000);

                    }
                });
            }



        }
        if(Integer.parseInt(TipCount.getText().toString())==0){
            AdReward.LoadAd(getApplicationContext());
        }
    }
    public void DialogResult(String situation) {

        if(T!=null){
        T.cancel();
        T=null;
        }
        String modeSave=getIntent().getStringExtra("mode");


        if(getIntent().getStringExtra("mode").equals("Normal")){
            modeSave="";
        }

        tableGame.removeAllViews();
        if(situation.equals(getString(R.string.Victory))&&firstplay) {
            AdInterstitial.AdInterstitialResoruceShow(CardGameActivity.this, getApplicationContext());
        }else{
            firstplay=true;
        }
        DialogInter.DialogInter(CardGameActivity.this,errorcount.getText().toString(),
                level.getText().toString(),countTime,getString(R.string.cards)+modeSave,getApplicationContext(),theme,situation);
   



        DialogInter.getTimeFinish().setText(R.string.RepeatLevel);
        DialogInter.getTimeFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInter.getDialogInter().dismiss();
                DialogInter.getDialogInter().cancel();

                try {
                    CreateGame(columnsnumber, linesnumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(r, 5000);
                setTime(linesnumber * columnsnumber * 3,Delay,Period);
            }
        });
        if (situation.equals(getString(R.string.Defeat))) {

            DialogInter.getStatusBtn().setText(R.string.RepeatLevel);
            DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInter.getDialogInter().dismiss();
                    DialogInter.getDialogInter().cancel();
                    try {
                        CreateGame(columnsnumber, linesnumber);
                        handler.postDelayed(r, 5000);
                        setTime(linesnumber * columnsnumber * 3,Delay,Period);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else if(situation.equals(getString(R.string.Victory))){
            if (linesnumber ==9&& columnsnumber==5) {

                DialogInter.getStatusBtn().setText(R.string.RepeatLevel);
                Toast.makeText(getApplicationContext(), R.string.MaxLevel, Toast.LENGTH_LONG).show();
                DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInter.getDialogInter().dismiss();
                        DialogInter.getDialogInter().cancel();
                        try {
                            CreateGame(columnsnumber, linesnumber);
                            handler.postDelayed(r, 5000);
                            setTime(linesnumber*columnsnumber*3,Delay,Period);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }else{
            DialogInter.getStatusBtn().setText(R.string.NextLevel);
            DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInter.getDialogInter().dismiss();
                    DialogInter.getDialogInter().cancel();

                    level.setText(Integer.toString(Integer.parseInt(level.getText().toString()) + 1));
                    try {
                        if (linesnumber == 6 && (columnsnumber == 4 || columnsnumber == 5)) {
                            CreateGame(columnsnumber + 1, linesnumber);
                        } else if (linesnumber == 4 && columnsnumber == 3) {
                            CreateGame(columnsnumber + 1, linesnumber);
                        } else
                            CreateGame(columnsnumber, linesnumber + 1);
                        handler.postDelayed(r, 5000);
                        setTime(linesnumber * columnsnumber * 3,Delay,Period);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            }
        }

    }
    private class ActivityCreateView1 extends Thread {
        @Override
        public void run() {
            super.run();
            tableGame = findViewById(R.id.frameLayout);
            hitcount = findViewById(R.id.hitScore);
            errorcount = findViewById(R.id.errorCount);
            timeCount = findViewById(R.id.timeCount);
            level = findViewById(R.id.Levelstatus2);
            TipCount=findViewById(R.id.tipcount);
            ImageView viewTip = findViewById(R.id.timeview5);
            viewTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


            final List<ImageView> restore = new ArrayList<>();
                        for (final ImageView darkwar :
                                imagesBackupIndex) {
                            if (darkwar.isClickable()) {
                                restore.add(darkwar);
                            }
                        }
                        SeeTip();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (ImageView restoreView :
                                        restore) {
                                    restoreView.setImageResource(R.drawable.darkness);
                                    restoreView.setClickable(true);
                                }

                            }
                        }, 2000);



                    }

            });
        }
    }
    private class ActivityCreateView2 extends Thread {
        @Override
        public void run() {
            super.run();
            r = new Runnable() {
                public void run() {
                    for (ImageView OcultImg :
                            imagesBackupIndex) {
                        OcultImg.setImageResource(R.drawable.darkness);
                        OcultImg.setClickable(true);

                    }
                }
            };
        }
    }
    private class ActivityCreateView3 extends Thread {
        @Override
        public void run() {
            super.run();
            FrameLayout adContainerView = findViewById(R.id.adView);
            mAdView = new AdView(getApplicationContext());
            mAdView.setAdUnitId("ca-app-pub-9324003669548340/9293872255");
            adContainerView.addView(mAdView);
            final AdRequest adRequest = new AdRequest.Builder().build();




        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH,50));
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

