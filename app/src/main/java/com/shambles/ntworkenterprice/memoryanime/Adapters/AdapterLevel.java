package com.shambles.ntworkenterprice.memoryanime.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.shambles.ntworkenterprice.memoryanime.R;
import com.shambles.ntworkenterprice.memoryanime.model.Level;
import java.util.List;
import static com.shambles.ntworkenterprice.memoryanime.Resources.AssetsManager.ImageViaAssets;

public class AdapterLevel extends RecyclerView.Adapter <AdapterLevel.Vison>{

    private List<Level> lista;
    private Context context;

    public AdapterLevel(List<Level> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }
    @NonNull
    @Override
    public Vison onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterLevel.Vison((LayoutInflater.from(parent.getContext()).inflate(R.layout.levelblock,parent,false)));
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Vison holder, int position) {
        holder.level.setText(lista.get(position).getLevel());
        holder.scren.setRating(lista.get(position).getStars());
        if(lista.get(position).getSet()){
           holder.Img.setImageBitmap(ImageViaAssets(lista.get(position).getImg(),context));
        }else {
            holder.Img.setImageResource(R.drawable.darkness);
        }
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Vison extends RecyclerView.ViewHolder{

        TextView level;
        RatingBar scren;
        ImageView Img;
        ConstraintLayout Tuneel;

        public Vison(@NonNull View itemView) {
            super(itemView);

            level=itemView.findViewById(R.id.LevelTextId);
            scren=itemView.findViewById(R.id.ratingBarIdLevel);
            Tuneel=itemView.findViewById(R.id.levelTunnelId);
            Img=itemView.findViewById(R.id.Img);
        }

    }
}
