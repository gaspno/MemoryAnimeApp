package com.shambles.ntworkenterprice.memoryanime.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.shambles.ntworkenterprice.memoryanime.MainActivity;
import com.shambles.ntworkenterprice.memoryanime.R;
import com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.shambles.ntworkenterprice.memoryanime.Resources.AssetsManager.ImageViaAssets;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.dreamInitPause;


public class RecordsActivity extends AppCompatActivity {


    private SharedPreferences sharedpreferences;
    private String Theme;
    private final Handler handler=new Handler();
    private Spinner Mode;
    private TextView mg,pm,cp,tt;
    private final List<String> imageResource = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private ImageView ImageTheme;
    private String mode[];
    private RatingBar CM,MG,PG,TOTAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ImageTheme = findViewById(R.id.imageTheme2);
        Mode = findViewById(R.id.modeSelect3);
        CM=findViewById(R.id.ratingCP);
        MG=findViewById(R.id.ratingMG);
        PG=findViewById(R.id.ratingPM);
        TOTAL=findViewById(R.id.ratingTO);
        mg=findViewById(R.id.mgProgress);
        pm=findViewById(R.id.pmProgress);
        cp=findViewById(R.id.cpProgress);
        tt=findViewById(R.id.ttProgress);
        final Spinner themeSelection = findViewById(R.id.spinner);
        ArrayAdapter arrayAdapterCard = ArrayAdapter.createFromResource(getApplicationContext(), R.array.options, R.layout.spinneritem);
        arrayAdapterCard.setDropDownViewResource(R.layout.modelspinner);
        ArrayAdapter arrayAdapterPhoto = ArrayAdapter.createFromResource(getApplicationContext(), R.array.options2, R.layout.spinneritem);
        arrayAdapterPhoto.setDropDownViewResource(R.layout.modelspinner);
        ArrayAdapter arrayAdapterCount = ArrayAdapter.createFromResource(getApplicationContext(), R.array.options3, R.layout.spinneritem);
        arrayAdapterCount.setDropDownViewResource(R.layout.modelspinner);
        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.optionsGameImage, R.layout.spinneritem);
        arrayAdapter2.setDropDownViewResource(R.layout.modelspinner);
        themeSelection.setAdapter(arrayAdapter2);
        Theme = themeSelection.getSelectedItem().toString();
        sharedpreferences = getSharedPreferences(Theme, Context.MODE_PRIVATE);
        themeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Theme = themeSelection.getSelectedItem().toString();
                sharedpreferences = getSharedPreferences(Theme, Context.MODE_PRIVATE);

                handler.postDelayed(() -> {
                    try {
                        imageResource.clear();
                        imageResource.addAll(Arrays.asList(getAssets().list(themeSelection.getSelectedItem().toString())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageTheme.setPadding(10, 10, 10, 10);
                    viewValuesTotal();
                    ImageTheme.setImageBitmap(ImageViaAssets(themeSelection.getSelectedItem().toString() + "/" + imageResource.get(0), getApplicationContext()));
                }, 100);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button reset = findViewById(R.id.resetId);
        reset.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(RecordsActivity.this);
            dialog.setTitle(getResources().getString(R.string.titlereset));
            dialog.setPositiveButton(R.string.yes, (dialog14, id) -> {
                sharedpreferences = getSharedPreferences(Theme, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                sharedpreferences = getSharedPreferences(Theme, Context.MODE_PRIVATE);
                Toast.makeText(getApplicationContext(), R.string.messagereset, Toast.LENGTH_LONG).show();
                handler.postDelayed(() -> {


                }, 100);
            }).setNegativeButton(R.string.no, (dialog1, id) -> {
            });


            dialog.show();

        });
        reset.setOnLongClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(RecordsActivity.this);
            dialog.setTitle(getResources().getString(R.string.titlereset2));
            dialog.setPositiveButton(R.string.yes, (dialog12, id) -> {
                String[] listaT = getResources().getStringArray(R.array.optionsGameImage);
                for (String t : listaT) {
                    sharedpreferences = getSharedPreferences(t, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();
                    sharedpreferences = getSharedPreferences(t, Context.MODE_PRIVATE);
                    Toast.makeText(getApplicationContext(), R.string.messagereset, Toast.LENGTH_LONG).show();
                    handler.postDelayed(() -> {


                    }, 100);
                }
            }).setNegativeButton(R.string.no, (dialog13, id) -> {
            });


            dialog.show();
            return true;
        });
        mode = new String[]{"Challenge", "Normal"};
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.mode, R.layout.spinneritem);
        arrayAdapter.setDropDownViewResource(R.layout.modelspinner);
        Mode.setAdapter(arrayAdapter);
        Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                viewValuesTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        viewValuesTotal();
    }
        private void viewValuesTotal(){
            String modeS="";
            int i=Mode.getSelectedItemPosition();
            switch (mode[i]){
                case "Normal":
                    modeS="";
                    break;
                case "Challenge":
                    modeS="Challenge";
                    break;
            }

            int t=0;
            int c = 0,m = 0,ph = 0;
            String aux=Theme;

                SharedPreferences sharedpreferencesAux = getSharedPreferences(aux, Context.MODE_PRIVATE);
                int n=1;
                int p=0;


                while(true) {
                    if (!sharedpreferencesAux.getString((getResources().getString(R.string.cards)+modeS + n), "").equals("")) {
                        String[] values = sharedpreferencesAux.getString(getResources().getString(R.string.cards)+modeS + n, "").split(",");
                        p += Integer.parseInt(values[0]);
                    } else {
                        break;
                    }
                    n++;
                }
                t+=p;
                m+=p;
                n=1;
                p=0;
                while(true) {
                    if (!sharedpreferencesAux.getString((getResources().getString(R.string.photographic)+modeS + n), "").equals("")) {
                        String[] values = sharedpreferencesAux.getString(getResources().getString(R.string.photographic)+modeS + n, "").split(",");
                        p += Integer.parseInt(values[0]);
                    } else {
                        break;

                    }
                    n++;
                }
                t+=p;
                ph+=p;
                n=1;
                p=0;
                while(true) {
                    if (!sharedpreferencesAux.getString((getResources().getString(R.string.countpictures)+modeS + n), "").equals("")) {
                        String[] values = sharedpreferencesAux.getString(getResources().getString(R.string.countpictures)+modeS + n, "").split(",");
                        p += Integer.parseInt(values[0]);
                    } else {

                        break;
                    }
                    n++;
                }
                c+=p;
                t+=p;


            CM.setRating((float)c/15);
            cp.setText(c+" de"+" "+15*5);

            MG.setRating((float)m/9);
            mg.setText(m+" de"+" "+9*5);

            PG.setRating((float)ph/8);
            pm.setText(ph+" de"+" "+8*5);

            TOTAL.setRating((float)t/36);
            tt.setText(t+" de"+" "+36*5);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("rating",true);
        startActivity(intent);

    }
    @Override
    protected void onStart() {
        super.onStart();

        MediaAux.dreamInit(getApplicationContext());


    }
    @Override
    protected void onPause() {
        super.onPause();
        dreamInitPause();
    }




}