package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import fpt.prm.orderfood.EventBus.MyUpdateCartEvent;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Cart;
import fpt.prm.orderfood.entities.Shop;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Cart> CartList;
    private Context context;
    private LayoutInflater inflater;

    public CartAdapter(List<Cart> allcartList, Context context) {
        CartList = allcartList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Glide.with(context).load(CartList.get(position).getImage()).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.cart_image);
        holder.cart_name.setText(CartList.get(position).getName());
        holder.price.setText(new StringBuilder(" đ").insert(0, CartList.get(position).getPrice()));
        holder.txtQuantity.setText(new StringBuilder().append(CartList.get(position).getQuantity()));

        holder.btnDelete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(context);
            alertDialog.setTitle("Delete item");
            alertDialog.setMessage("Do you really want to delete it ?");
            alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            alertDialog.setPositiveButton("OK", (dialog1, which) -> {

                notifyItemRemoved(position);

                deleteFromFirebase(CartList.get(position));
                dialog1.dismiss();
            }).create();
            alertDialog.show();
        });
        holder.removebtn.setOnClickListener(v ->
        {
            removeCartItems(holder, CartList.get(position));

        });
        holder.addbtn.setOnClickListener(v ->
        {
            plusCartItems(holder, CartList.get(position));
        });
    }

    private void deleteFromFirebase(Cart cart) {
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID").child(cart.getKey())
                .removeValue().addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItems(CartViewHolder holder, Cart cart) {
        cart.setQuantity(cart.getQuantity() + 1);
        cart.setTotalPrice(cart.getQuantity() * Float.parseFloat(cart.getPrice()));

        holder.txtQuantity.setText(new StringBuilder().append(cart.getQuantity()));
        updateFirebase(cart);
    }

    private void removeCartItems(CartViewHolder holder, Cart cart) {
        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getQuantity() * Float.parseFloat(cart.getPrice()));

            holder.txtQuantity.setText(new StringBuilder().append(cart.getQuantity()));
            updateFirebase(cart);
        }
    }

    private void updateFirebase(Cart cart) {
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID").child(cart.getKey())
                .setValue(cart).addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return CartList.size();
    }
}
