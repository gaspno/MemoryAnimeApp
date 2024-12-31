package com.shambles.ntworkenterprice.memoryanime.Resources;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.shambles.ntworkenterprice.memoryanime.R;


public class DialogInter {

   private static Dialog dialogInter;
   private static Button statusBtn;
   private static TextView status;
   private static TextView timeFinish;
   private static TextView levelStatus;
   private static RatingBar simpleRatingBar;
   private static TextView errorStatus;
   private static SharedManager manager;

   public static void DialogInter (Activity activity, String erros, String nivel, int countTime, String mode, Context context, String theme,String situation){

       dialogInter = new Dialog(activity);
       dialogInter.setContentView(R.layout.status);
       dialogInter.setCanceledOnTouchOutside(false);
       dialogInter.setOwnerActivity(activity);
       dialogInter.getWindow();
       dialogInter.setCancelable(false);
       levelStatus =dialogInter.findViewById(R.id.LevelstatusSituation);
       errorStatus=dialogInter.findViewById(R.id.ErrorCountSituation);
       statusBtn=dialogInter.findViewById(R.id.StatusButton);
       timeFinish=dialogInter.findViewById(R.id.timeSlaping);
       status =dialogInter.findViewById(R.id.StatusText);
       simpleRatingBar=dialogInter.findViewById(R.id.ratingLevelId);
       errorStatus.setText(String.valueOf(erros));
       levelStatus.setText(String.valueOf(nivel));
       int starsRating;
       if(situation.equals(context.getString(R.string.Defeat))){
           simpleRatingBar.setRating(0);
           status.setText(situation);
       }
       else {
           simpleRatingBar.setRating(stars(mode,nivel,context));
           status.setText( situation+"\n"+context.getResources().getString(R.string.NormalVictory));
           starsRating = stars(mode,nivel,context);

               manager = new SharedManager(mode,
                       Integer.parseInt(levelStatus.getText().toString()),
                       Integer.parseInt(errorStatus.getText().toString()),
                       countTime,
                       starsRating,
                       context,
                       theme
               );
               manager.save();
       }
       if(!activity.isDestroyed()) {dialogInter.show();}
   }
    public static TextView getTimeFinish() {
        return timeFinish;
    }
    public static Button getStatusBtn() {
        return statusBtn;
    }
    public static Dialog getDialogInter() {
        return dialogInter;
    }
    public static int stars(String modulo,String nivel,Context context){
       if(modulo.equals(context.getString(R.string.cards))||modulo.equals(context.getString(R.string.cards)+"Challenge")) {
           if (Integer.parseInt(errorStatus.getText().toString()) == 0) {
               return 5;
           } else if (Integer.parseInt(errorStatus.getText().toString()) <= Integer.parseInt(nivel)+2) {
               return 4;
           } else if (Integer.parseInt(errorStatus.getText().toString()) <= Integer.parseInt(nivel) + 4) {
               return 3;
           } else if (Integer.parseInt(errorStatus.getText().toString()) <= Integer.parseInt(nivel) + 6) {
               return 2;
           } else {
               return 1;
           }

       }

        if(modulo.equals(context.getString(R.string.countpictures))||modulo.equals(context.getString(R.string.countpictures)+"Challenge")) {
            if (Integer.parseInt(errorStatus.getText().toString()) == 0) {
                return 5;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <= 2) {
                return 4;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <= Integer.parseInt(nivel) + 4) {
                return 3;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <= Integer.parseInt(nivel) + 5) {
                return 2;
            } else {
                return 1;
            }

        }
        if(modulo.equals(context.getString(R.string.photographic))||modulo.equals(context.getString(R.string.photographic)+"Challenge")) {
            if (Integer.parseInt(errorStatus.getText().toString()) == 0) {
                return 5;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <=1) {
                return 4;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <=3) {
                return 3;
            } else if (Integer.parseInt(errorStatus.getText().toString()) <=5) {
                return 2;
            } else {
                return 1;
            }

        }
        return 0;
    }
}
