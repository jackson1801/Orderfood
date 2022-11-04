package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.listener.IRecyclerClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     ImageView thumb_image;
     TextView name;
     TextView price;
    private Context context;

    private IRecyclerClickListener listener;

    public void setListener(IRecyclerClickListener Listener) {
        this.listener = Listener;
    }

    private void bindingView(View view) {
        thumb_image = view.findViewById(R.id.thumb_image);
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
    }

    private void bindingAction(View view) {
        view.setOnClickListener(this);
    }

    public FoodViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction(itemView);

    }

    @Override
    public void onClick(View v) {
        listener.onItemClickListener(v, getAdapterPosition());
    }
}
