package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fpt.prm.orderfood.R;

public class AllMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    ImageView food_image;
    TextView all_name;
    TextView opening_hour;
    TextView rating;

    private void bindingView(View view) {
        food_image = view.findViewById(R.id.food_image);
        all_name = view.findViewById(R.id.all_name);
        opening_hour = view.findViewById(R.id.opening_hour);
        rating = view.findViewById(R.id.rating);
    }

    private void bindingAction(View view) {
    }

    public AllMenuViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
