package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.ShopActivity;
import fpt.prm.orderfood.entities.Shop;

public class RecommendMenuAdapter extends RecyclerView.Adapter<RcmViewHolder> {

    private List<Shop> RcmList;
    private Context context;
    private LayoutInflater inflater;

    public RecommendMenuAdapter(List<Shop> rcmList, Context context) {
        RcmList = rcmList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RcmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recommend_items, parent, false);
        return new RcmViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RcmViewHolder holder, int position) {
        Glide.with(context).load(RcmList.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.food_image);
        holder.rcm_name.setText(RcmList.get(position).getName());
        holder.all_menu_rating.setText(RcmList.get(position).getRating());

        holder.cardview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShopActivity.class);
            intent.putExtra("ShopId", RcmList.get(position).getKey());
            intent.putExtra("ShopName", RcmList.get(position).getName());
            intent.putExtra("ShopAddress", RcmList.get(position).getLocation());
            intent.putExtra("ShopImage", RcmList.get(position).getImage());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return RcmList.size();
    }
}
