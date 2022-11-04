package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categories;
    private LayoutInflater inflater;

    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
//    Category category = categories.get(position);
//    if(category == null){
//        return;
//
//    }
//        Glide.with(context).load(categories.get(position).getImage()).into(holder.imgCategory);
//    holder.cateName.setText(category.getName());

        Glide.with(context)
                .load(categories.get(position).getImage())
                .into(holder.imgCategory);
        holder.cateName.setText(new StringBuilder().append(categories.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView cateName;
        ImageView imgCategory;
        private Context context;
        private void bindingView(View view){
            cateName = view.findViewById(R.id.tv_nameCategory);
            imgCategory = view.findViewById(R.id.img_category);
        }
        public CategoryViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            bindingView(itemView);
        }
    }
}
