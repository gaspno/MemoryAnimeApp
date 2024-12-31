package com.shambles.ntworkenterprice.memoryanime.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdError;
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

public class PhotographicMemoryActivity extends AppCompatActivity {

    private static int find = 0;
    private Timer T;
    private static ImageView[] index;
    private int height, width;
    private final List<ImageView> imagesBackupIndex = new ArrayList<>();
    private LinearLayout tableGame;
    private List<String> imageResource = new ArrayList<>();
    private int columnsnumber;
    private int linesnumber;
    private TextView  errorcount, level;
    private int numberIdImage = 0;
    private final Handler handler = new Handler();
    private AdView mAdView;
    private Runnable r;
    private  boolean firstplay=false;
    private TextView timeCount,ind1,ind2,hitScore;
    private static String  theme;
    private TextView modeView;
    private int countTime=0;
    private TextView TipCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotographic_memory);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdInterstitial.AdInterstitialResoruce(getApplicationContext());
        AdReward.LoadAd(getApplicationContext());
        PhotographicMemoryActivity.ActivityCreateView3 threadthree = new PhotographicMemoryActivity.ActivityCreateView3();

        PhotographicMemoryActivity.ActivityCreateView1 threadone = new PhotographicMemoryActivity.ActivityCreateView1();

        PhotographicMemoryActivity.ActivityCreateView2 threadtwo = new PhotographicMemoryActivity.ActivityCreateView2();
        threadone.setPriority(10);
        threadtwo.setPriority(9);
        threadthree.setPriority(5);
        threadone.start();
        try {
            threadone.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadtwo.start();
        try {
            threadtwo.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadthree.start();
        handler.postDelayed(() -> {
            level.setText((Integer.toString((int) (getIntent().getIntExtra("LevelNumber",0)+1))));
            try {
                String tam = getIntent().getStringExtra("Level");
                String[] columns = tam.split("x");
                theme=getIntent().getStringExtra("Theme");
                CreateGame(Integer.parseInt(columns[1]), Integer.parseInt(columns[0]));
                handler.postDelayed(r, 3000);
                setTime(18+linesnumber*columnsnumber*2,3000,1000);
                modeView=findViewById(R.id.modeViewTextP);
                modeView.setText(getIntent().getStringExtra("mode"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        },500);

    }


    public void CreateGame(int coluns, int lines) throws IOException {

        height = tableGame.getHeight();
        width = tableGame.getWidth();
        columnsnumber = coluns;
        linesnumber = lines;
        imageResource.clear();
        imageResource.addAll(Arrays.asList(getAssets().list(theme)));
        Collections.shuffle(imageResource);
        imageResource = imageResource.subList(0, (columnsnumber * linesnumber) );
        Collections.shuffle(imageResource);
        errorcount.setText("0");
        CreateViewGame(linesnumber, columnsnumber);
    }
    public void CreateViewGame(final int linesnumber, final int columnsnumber) {

        imagesBackupIndex.clear();
        LinearLayout[] lines = new LinearLayout[linesnumber];
        tableGame.destroyDrawingCache();
        tableGame.removeAllViews();
        ind1.setVisibility(View.INVISIBLE);
        ind2.setVisibility(View.INVISIBLE);
        final ImageView SelectImage=findViewById(R.id.PhotoIdSelect);
        Random random=new Random();
        final int selectImageIndex=random.nextInt(imageResource.size()-1);
        switch(getIntent().getStringExtra("mode")){
            case "Normal":
                hitScore.setText("0");
                break;
            case "Challenge":
                hitScore.setText(Integer.toString(10));

                break;

        }
        SelectImage.setImageResource(R.drawable.darkness);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SelectImage.setImageBitmap(ImageViaAssets(theme+"/"+imageResource.get(selectImageIndex),getApplicationContext()));
                ind1.setVisibility(View.VISIBLE);
                ind2.setVisibility(View.VISIBLE);
            }
        },3100);

        numberIdImage = 0;
        for (int o = 0; o < linesnumber; o++) {

            lines[o] = new LinearLayout(this);
            lines[o].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height / linesnumber));

            for (int m = 0; m < columnsnumber; m++) {
                final ImageView image = new ImageView(PhotographicMemoryActivity.this);
                SetImage(image);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        image.setImageBitmap(ImageViaAssets(theme + "/" + imageResource.get(v.getId()), getApplicationContext()));
                        image.setClickable(false);


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String aquieagora;
                                aquieagora = CheckingResult(imageResource.get(v.getId()), (ImageView) v,imageResource.get(selectImageIndex));
                                if (aquieagora.isEmpty()) ;
                                else if (aquieagora.equals("acerto")) {


                                        DialogResult(getResources().getString(R.string.Victory));

                                        victoryLevelSound();


                                } else if (aquieagora.equals("erro")) {

                                    loseSound();
                                    for (ImageView imageview : index) {
                                        imageview.setImageResource(R.drawable.darkness);
                                        imageview.setClickable(true);
                                    }
                                    if(getIntent().getStringExtra("mode").equals("Challenge")){
                                        hitScore.setText(Integer.toString(Integer.parseInt(hitScore.getText().toString())-1));
                                        if(Integer.parseInt(hitScore.getText().toString())<=0){
                                            DialogResult(getString(R.string.Defeat));
                                            loseLevelSound();


                                        }
                                    }
                                    errorcount.setText(Integer.toString(Integer.parseInt(errorcount.getText().toString()) + 1));

                                }
                            }
                        }, 500);


                    }
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
        image.setImageBitmap(ImageViaAssets(theme + "/" + imageResource.get(numberIdImage), getApplicationContext()));

    }
    private static String CheckingResult(String s, ImageView parent,String p) {

        int selects = 1;
        String[] first = new String[selects];

        index = new ImageView[selects];
        first[find] = s;

        index[find] = parent;


        boolean hit;
        if (first[0].equals(p))
                    hit =true;
                else
                    hit =false;


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
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("rating",true);
        startActivity(intent);

    }
    private void setTime(int stageTime,int delay,int period){
        T=new Timer();
        if(getIntent().getStringExtra("mode").equals("Challenge")) {
            countTime=stageTime;
            T.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeCount.setText(getString(R.string.time) + "\n" + countTime);
                            countTime--;
                            if(countTime<=0){
                                DialogResult(getString(R.string.Defeat));
                            }
                        }
                    });
                }
            }, delay, period);
        }else {

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
            AdReward.AdRewardResource(getApplicationContext(),PhotographicMemoryActivity.this,TipCount);
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
            AdInterstitial.AdInterstitialResoruceShow(PhotographicMemoryActivity.this,getApplicationContext());
        }else{
            firstplay=true;
        }
        DialogInter.DialogInter(PhotographicMemoryActivity.this,errorcount.getText().toString(),
                level.getText().toString(),countTime,getString(R.string.photographic)+modeSave,getApplicationContext(),theme,situation);
        DialogInter.getTimeFinish().setText(R.string.RepeatLevel);
        DialogInter.getTimeFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInter.getDialogInter().dismiss();
                DialogInter.getDialogInter().cancel();

                try {
                    CreateGame(columnsnumber, linesnumber);
                    handler.postDelayed(r, 3000);
                    setTime(18+linesnumber*columnsnumber*2,3000,1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        if (situation.equals(getString(R.string.Defeat))) {

            DialogInter.getStatusBtn().setText(R.string.RepeatLevel);
            DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInter.getDialogInter().dismiss();

                    try {
                        CreateGame(columnsnumber, linesnumber);
                        handler.postDelayed(r, 3000);
                        setTime(18+linesnumber*columnsnumber*2,3000,1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else if(situation.equals(getString(R.string.Victory))){
            if (linesnumber ==5&& columnsnumber==5) {

                DialogInter.getStatusBtn().setText(R.string.RepeatLevel);
                Toast.makeText(getApplicationContext(), R.string.MaxLevel, Toast.LENGTH_LONG).show();
                DialogInter.getStatusBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInter.getDialogInter().dismiss();

                        try {
                            CreateGame(columnsnumber, linesnumber);
                            handler.postDelayed(r, 3000);
                            setTime(18+linesnumber*columnsnumber*2,3000,1000);
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

                            level.setText(Integer.toString(Integer.parseInt(level.getText().toString()) + 1));
                            try {

                                if (linesnumber == columnsnumber) {
                                    CreateGame(columnsnumber + 1, linesnumber);
                                } else if (linesnumber <= columnsnumber)
                                    CreateGame(columnsnumber, linesnumber + 1);
                                handler.postDelayed(r, 3000);
                                setTime(18+linesnumber*columnsnumber*2,3000,1000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

    }
    private  class ActivityCreateView1 extends Thread {
        @Override
        public void run() {
            super.run();
            tableGame = findViewById(R.id.frameLayout);
            errorcount = findViewById(R.id.errorCount);
            timeCount = findViewById(R.id.timeCount);
            level = findViewById(R.id.Levelstatus2);
            ind1=findViewById(R.id.ind1);
            ind2=findViewById(R.id.ind2);
            hitScore=findViewById(R.id.hitScore);
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
                    }, 200);
                }
            });
        }


    }
    private class ActivityCreateView2 extends Thread {
        @Override
        public void run() {
            super.run();
            r = () -> {
                for (ImageView OcultImg :
                        imagesBackupIndex) {
                    OcultImg.setImageResource(R.drawable.darkness);
                    OcultImg.setClickable(true);

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
            mAdView.setAdUnitId("ca-app-pub-9324003669548340/1592326264");
            adContainerView.addView(mAdView);
            final AdRequest adRequest = new AdRequest.Builder().build();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH,50));
                    mAdView.loadAd(adRequest);                ;

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