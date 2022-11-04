package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Shop;

public class AllMenuAdapter extends RecyclerView.Adapter<AllMenuViewHolder> {

    private List<Shop> AllList;
    private Context context;
    private LayoutInflater inflater;

    public AllMenuAdapter(List<Shop> allList, Context context) {
        AllList = allList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AllMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.all_menu_items, parent, false);
        return new AllMenuViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMenuViewHolder holder, int position) {
        Glide.with(context).load(AllList.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.food_image);
        holder.all_name.setText(AllList.get(position).getName());
        holder.rating.setText(AllList.get(position).getRating());
        holder.opening_hour.setText(AllList.get(position).getOpeningHours());
    }

    @Override
    public int getItemCount() {
        return AllList.size();
    }
}
