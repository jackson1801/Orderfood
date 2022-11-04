package fpt.prm.orderfood.function.advertising;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import fpt.prm.orderfood.R;

public class AdsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final Context context;
    RoundedImageView AdsImage;

    private void bindingView(View view) {
        AdsImage = view.findViewById(R.id.imageSlide);
    }

    private void bindingAction(View view) {
        AdsImage.setOnClickListener(this::clickOnAds);
    }

    private void clickOnAds(View view) {
    }

    public AdsViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
