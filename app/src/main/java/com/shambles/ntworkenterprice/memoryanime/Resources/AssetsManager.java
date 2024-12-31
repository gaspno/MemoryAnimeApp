package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class AssetsManager {
    public static Bitmap ImageViaAssets(String fileName, Context context){

        AssetManager assetmanager =context.getAssets();

        InputStream is = null;
        try{
            is = assetmanager.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;

    }
}
