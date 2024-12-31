package com.shambles.ntworkenterprice.memoryanime.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.shambles.ntworkenterprice.memoryanime.Adapters.AdapterLevel;
import com.shambles.ntworkenterprice.memoryanime.MainActivity;
import com.shambles.ntworkenterprice.memoryanime.R;
import com.shambles.ntworkenterprice.memoryanime.RecyclerResource.RecyclerItemClickListener;
import com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux;
import com.shambles.ntworkenterprice.memoryanime.model.Level;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.shambles.ntworkenterprice.memoryanime.Resources.MediaAux.dreamInitPause;


public class LevelModelThemeSelect extends AppCompatActivity {

    private Spinner Theme,Mode;
    private RecyclerView recyclerView;
    private Intent intent;

    private List<Level> leveis=new ArrayList<>();
    private final List<String> imageResource = new ArrayList<>();
    private final Handler handler=new Handler();
    private SharedPreferences sharedpreferences,shared;
    private List<String> Niveis=new ArrayList<>();
    private TextView gameModeText;
    private String mode[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_model_theme_select);
        Theme = findViewById(R.id.SpinnerThemeId);
        Mode=findViewById(R.id.modeSelect);
        recyclerView= findViewById(R.id.LevelId);
        RecyclerView.LayoutManager layoutManager =  new GridLayoutManager(getApplicationContext(),2);
        mode=new String[]{"Challenge","Normal"};
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.mode,R.layout.spinneritem);
        arrayAdapter.setDropDownViewResource(R.layout.modelspinner);
        Mode.setAdapter(arrayAdapter);

        gameModeText = findViewById(R.id.GameModeTextId);
        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.optionsGameImage, R.layout.spinneritem);
        arrayAdapter2.setDropDownViewResource(R.layout.modelspinner);
        arrayAdapter2.setDropDownViewResource(R.layout.modelspinner);
        Theme.setAdapter(arrayAdapter2);
        shared = getSharedPreferences("GAMESTATUS", Context.MODE_PRIVATE);
        Theme.setSelection(shared.getInt("LAST",0));

        //sharedpreferences=getSharedPreferences(Theme.getSelectedItem().toString(), Context.MODE_PRIVATE);
        Theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("LAST",Theme.getSelectedItemPosition());
                editor.apply();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            imageResource.clear();
                            imageResource.addAll(Arrays.asList(getAssets().list(Theme.getSelectedItem().toString())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SetAdapterSelection();
                    }
                },100);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    imageResource.clear();
                    imageResource.addAll(Arrays.asList(getAssets().list(Theme.getSelectedItem().toString())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SetAdapterSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void SetAdapterSelection  (){
        leveis.clear();
        Niveis.clear();
        String img=Theme.getSelectedItem().toString()+ "/"+imageResource.get(0);
        String theme=Theme.getSelectedItem().toString();
        sharedpreferences=getSharedPreferences(theme, Context.MODE_PRIVATE);
        recyclerView.removeAllViews();
        int n=0;
        int next=0;
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
        switch (getIntent().getStringExtra("GameMode"))
        {

            case "CardGame":

                Niveis.addAll(Arrays.asList(getResources().getStringArray(R.array.options)));
                gameModeText.setText(R.string.cards);
                for (String lv:Niveis
                ) {

                    n=n+1;
                    if(!sharedpreferences.getString((getResources().getString(R.string.cards)+modeS+n),"").equals("")) {
                        next++;
                        String[] values = sharedpreferences.getString((getResources().getString(R.string.cards)+ modeS+ n), "").split(",");
                        leveis.add(new Level(lv, Integer.parseInt(values[0]), true,img));
                    }
                    else
                        leveis.add(new Level(lv,0,false,img));

                }
                if(!(next>=leveis.size())) {
                    leveis.get(next).setSet(true);
                }
                recyclerView.setAdapter(new AdapterLevel(leveis,getApplicationContext()));
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(leveis.get(position).getSet()||position==0) {
                                    intent = new Intent(getApplicationContext(), CardGameActivity.class);
                                    int i=Mode.getSelectedItemPosition();
                                    switch (mode[i]){
                                        case "Normal":
                                            intent.putExtra("mode","Normal");
                                            break;
                                        case "Challenge":
                                            intent.putExtra("mode","Challenge");
                                            break;
                                    }

                                    intent.putExtra("Level", Niveis.get(position));
                                    intent.putExtra("Theme", Theme.getSelectedItem().toString());
                                    intent.putExtra("LevelNumber", position);
                                    finish();
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),R.string.notUnblock,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        })
                );

                break;
            case "CountGame":
                Niveis.addAll(Arrays.asList(getResources().getStringArray(R.array.options3)));
                gameModeText.setText(R.string.countpictures);
                for (String lv:Niveis
                ) {
                    n=n+1;
                    if(!sharedpreferences.getString((getResources().getString(R.string.countpictures)+modeS+n),"").equals("")) {
                        next++;
                        String[] values = sharedpreferences.getString((getResources().getString(R.string.countpictures) +modeS+ n), "").split(",");
                        leveis.add(new Level(lv, Integer.parseInt(values[0]), true, img));
                    }
                    else
                        leveis.add(new Level(lv,0,false,img));

                }
                if(!(next>=leveis.size())) {
                    leveis.get(next).setSet(true);
                }
                recyclerView.setAdapter(new AdapterLevel(leveis,getApplicationContext()));
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(leveis.get(position).getSet()||position==0) {

                                    intent = new Intent(getApplicationContext(), CountGameActivity.class);
                                    int i=Mode.getSelectedItemPosition();
                                    switch (mode[i]){
                                        case "Normal":
                                            intent.putExtra("mode","Normal");
                                            break;
                                        case "Challenge":
                                            intent.putExtra("mode","Challenge");
                                            break;

                                    }
                                    intent.putExtra("Level", Integer.valueOf(Niveis.get(position)));
                                    intent.putExtra("Theme", Theme.getSelectedItem().toString());
                                    intent.putExtra("LevelNumber", position);
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),R.string.notUnblock,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        })
                );
                break;
            case "PhotoGraphic":
                Niveis.addAll(Arrays.asList(getResources().getStringArray(R.array.options2)));
                gameModeText.setText(R.string.photographic);
                for (String lv:Niveis
                ) {
                    n=n+1;
                    if(!sharedpreferences.getString((getResources().getString(R.string.photographic) +modeS+n),"").equals("")) {
                        next++;
                        String[] values = sharedpreferences.getString((getResources().getString(R.string.photographic)  +modeS+ n), "").split(",");
                        leveis.add(new Level(lv, Integer.parseInt(values[0]), true, img));
                    }
                    else
                        leveis.add(new Level(lv,0,false,img));

                }
                if(!(next>=leveis.size())) {
                    leveis.get(next).setSet(true);
                }
                recyclerView.setAdapter(new AdapterLevel(leveis,getApplicationContext()));
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(leveis.get(position).getSet()||position==0) {
                                intent = new Intent(getApplicationContext(), PhotographicMemoryActivity.class);
                                    int i=Mode.getSelectedItemPosition();
                                    switch (mode[i]){
                                        case "Normal":
                                            intent.putExtra("mode","Normal");
                                            break;
                                        case "Challenge":
                                            intent.putExtra("mode","Challenge");
                                            break;

                                    }
                                intent.putExtra("Level", Niveis.get(position));
                                intent.putExtra("Theme", Theme.getSelectedItem().toString());
                                intent.putExtra("LevelNumber", position);
                                finish();
                                startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(),R.string.notUnblock,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        })
                );
                break;

        }
    }




    @Override
public void onBackPressed() {
    super.onBackPressed();
    startActivity(new Intent(getApplicationContext(),MainActivity.class));
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