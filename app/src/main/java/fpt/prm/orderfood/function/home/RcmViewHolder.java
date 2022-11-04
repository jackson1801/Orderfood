package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.listener.IRecyclerClickListener;

public class RcmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView food_image;
    TextView rcm_name;
    TextView all_menu_rating;
    private Context context;
    CardView cardview;

    private void bindingView(View view) {
        food_image = view.findViewById(R.id.food_image);
        rcm_name = view.findViewById(R.id.rcm_name);
        all_menu_rating = view.findViewById(R.id.all_menu_rating);
        cardview = view.findViewById(R.id.cardview);
    }

    private void bindingAction(View view) {
        view.setOnClickListener(this);
        cardview.setOnClickListener(this::clickDetailShop);
    }

    private void clickDetailShop(View view) {
    }


    public RcmViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction(itemView);
    }

    @Override
    public void onClick(View v) {
    }
}
