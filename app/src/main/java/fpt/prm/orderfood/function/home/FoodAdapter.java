package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.prm.orderfood.EventBus.MyUpdateCartEvent;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Cart;
import fpt.prm.orderfood.entities.Food;
import fpt.prm.orderfood.entities.Shop;
import fpt.prm.orderfood.listener.ICartLoadListener;

public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {

    private List<Food> FoodList;
    private Context context;
    private LayoutInflater inflater;
    private ICartLoadListener iCartLoadListener;

    public FoodAdapter(List<Food> foodList, ICartLoadListener iCartLoadListener, Context context) {
        FoodList = foodList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shop_food_items, parent, false);
        return new FoodViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Glide.with(context).load(FoodList.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.thumb_image);
        holder.name.setText(FoodList.get(position).getName());
        holder.price.setText(new StringBuilder(" đ").insert(0, FoodList.get(position).getPrice()));
        holder.setListener((view, adapterPosition) -> addToCart(FoodList.get(position)));
    }


    private void addToCart(Food food) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        userCart.child(food.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    cart.setQuantity(cart.getQuantity() + 1);
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("quantity", cart.getQuantity());
                    updateData.put("totalPrice", cart.getQuantity() * Float.parseFloat(cart.getPrice()));

                    userCart.child(food.getKey())
                            .updateChildren(updateData)
                            .addOnSuccessListener(aVoid -> iCartLoadListener.onCartLoadFailed("Add To Cart Success"))
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                } else {
                    Cart cart = new Cart();
                    cart.setName(food.getName());
                    cart.setImage(food.getImage());
                    cart.setKey(food.getKey());
                    cart.setPrice(food.getPrice());
                    cart.setQuantity(1);
                    cart.setTotalPrice(Float.parseFloat(food.getPrice()));

                    userCart.child(food.getKey())
                            .setValue(food)
                            .addOnSuccessListener(aVoid -> iCartLoadListener.onCartLoadFailed("Add To Cart Success"))
                            .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                }
                EventBus.getDefault().postSticky(new MyUpdateCartEvent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iCartLoadListener.onCartLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return FoodList.size();
    }
}
