package fpt.prm.orderfood.function.advertising;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.kalert.KAlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Advertising;

public class AdvertisingAdapter extends RecyclerView.Adapter<AdsViewHolder> {

    private List<Advertising> AdsItem;
    private ViewPager2 viewPager2;
    private Context context;
    private LayoutInflater inflater;

    public AdvertisingAdapter(List<Advertising> adsItem, ViewPager2 viewPager2, Context context) {
        this.AdsItem = adsItem;
        this.viewPager2 = viewPager2;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.advertising_items, parent, false);
        return new AdsViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
        Glide.with(context).load(AdsItem.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.AdsImage);

        holder.AdsImage.setOnClickListener(v -> {
            RoundedImageView gifImageView = new RoundedImageView(context);

            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(context);
            alertDialog.setTitle(AdsItem.get(position).getTitle());
            alertDialog.setMessage(AdsItem.get(position).getContent());
            Glide.with(context).load(AdsItem.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(gifImageView);
            alertDialog.setView(gifImageView);
            alertDialog.setPositiveButton("Done", null);
            alertDialog.show();
        });
    }


    @Override
    public int getItemCount() {
        return AdsItem.size();
    }
}
