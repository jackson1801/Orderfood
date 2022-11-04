package fpt.prm.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import fpt.prm.orderfood.EventBus.MyUpdateCartEvent;
import fpt.prm.orderfood.entities.Cart;
import fpt.prm.orderfood.entities.Food;
import fpt.prm.orderfood.function.home.CartActivity;
import fpt.prm.orderfood.function.home.FoodAdapter;
import fpt.prm.orderfood.listener.ICartLoadListener;
import fpt.prm.orderfood.utils.LoggerUtil;

public class ShopActivity extends AppCompatActivity implements ICartLoadListener {

    private RecyclerView rcv_food;
    private ImageView shop_image;
    private TextView shop_name;
    private TextView address_shop;
    NotificationBadge badge;
    FloatingActionButton btnCart;

    String shopId;
    String shopName;
    String shopAddress;
    String shopImage;
    FirebaseDatabase database;
    DatabaseReference foodList;
    ICartLoadListener iCartLoadListener;

    private void bindingView() {
        rcv_food = findViewById(R.id.rcv_food);
        shop_image = findViewById(R.id.shop_image);
        shop_name = findViewById(R.id.shop_name);
        address_shop = findViewById(R.id.address_shop);
        badge = findViewById(R.id.badge);
        btnCart = findViewById(R.id.btnCart);
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        iCartLoadListener = this;
    }

    private void bindingAction() {
        getIntentData();
        SetDataShop();
        countCartItem();
        btnCart.setOnClickListener(this::ClickOnBtnCart);
    }

    private void ClickOnBtnCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

    void getIntentData() {
        shopId = getIntent().getStringExtra("ShopId");
        shopName = getIntent().getStringExtra("ShopName");
        shopAddress = getIntent().getStringExtra("ShopAddress");
        shopImage = getIntent().getStringExtra("ShopImage");
        if (!shopId.isEmpty() && shopId != null) {
            AllMenuAddFromFirebase(shopId);
        }
    }

    private void SetDataShop() {
        shop_name.setText(shopName);
        address_shop.setText(shopAddress);
        Glide.with(this).load(shopImage).placeholder(R.drawable.loading_image).error(R.drawable.error_image).diskCacheStrategy(DiskCacheStrategy.DATA).into(shop_image);
    }


    private void AllMenuAddFromFirebase(String shopId) {
        List<Food> Foods = new ArrayList<>();
        foodList.orderByChild("ShopID").equalTo(shopId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                        Food food = foodSnapshot.getValue(Food.class);
                        food.setKey(foodSnapshot.getKey());
                        Foods.add(food);
                    }
                    rcv_food.setAdapter(new FoodAdapter(Foods, iCartLoadListener, ShopActivity.this));
                } else {
                    Toast.makeText(ShopActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event) {
        countCartItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<Cart> Cart = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                            Cart carts = cartSnapshot.getValue(Cart.class);
                            carts.setKey(cartSnapshot.getKey());
                            Cart.add(carts);
                        }
                        iCartLoadListener.onCartLoadSuccess(Cart);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        bindingView();
        bindingAction();

        rcv_food.setLayoutManager(new GridLayoutManager(this, 2));
        rcv_food.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    public void onCartLoadSuccess(List<Cart> cartList) {
        int cartSum = 0;
        for (Cart cartModel : cartList)
            cartSum += cartModel.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Toasty.error(this, "Something wrong. Please try again", Toast.LENGTH_SHORT).show();
    }
}