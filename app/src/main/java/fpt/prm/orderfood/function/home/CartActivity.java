package fpt.prm.orderfood.function.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import fpt.prm.orderfood.Common.Common;
import fpt.prm.orderfood.EventBus.MyUpdateCartEvent;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Cart;
import fpt.prm.orderfood.entities.Food;
import fpt.prm.orderfood.entities.Request;
import fpt.prm.orderfood.listener.ICartLoadListener;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    private TextView hi_user, address_ship, total_price;
    private Button submit_order;
    private RecyclerView rcv_cartlist;
    ICartLoadListener iCartLoadListener;

    List<Cart> cartList = new ArrayList<>();

    private void bindingView() {
        hi_user = findViewById(R.id.hi_user);
        address_ship = findViewById(R.id.address_ship);
        total_price = findViewById(R.id.total_price);
        submit_order = findViewById(R.id.submit_order);
        rcv_cartlist = findViewById(R.id.rcv_cartlist);
        iCartLoadListener = this;
    }

    private void bindingAction() {
        LoadCartFromFirebase();

        submit_order.setOnClickListener(this::SubmitOrder);
    }


    private void SubmitOrder(View view) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this);
        alertDialog.setTitle("Confirm order");
        alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().getCurrentUser();
                Request request = new Request(

                        FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        address_ship.getText().toString(),
                        total_price.getText().toString(),
                        cartList
                );
                FirebaseDatabase.getInstance().getReference("Requests").child(String.valueOf(System.currentTimeMillis())).setValue(request);
                finish();
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
        LoadCartFromFirebase();
    }

    private void LoadCartFromFirebase() {
        List<Cart> cartList = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot cartsnapshot : snapshot.getChildren()) {
                                Cart cartsnapshotValue = cartsnapshot.getValue(Cart.class);
                                cartsnapshotValue.setKey(cartsnapshot.getKey());
                                cartList.add(cartsnapshotValue);
                            }
                            iCartLoadListener.onCartLoadSuccess(cartList);
                        } else {
                            iCartLoadListener.onCartLoadFailed("Cart empty");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bindingView();
        bindingAction();

        rcv_cartlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCartLoadSuccess(List<Cart> cartModelList) {
        double sum = 0;
        for (Cart carts : cartModelList) {
            sum += carts.getTotalPrice();
        }
        total_price.setText(new StringBuilder(" đ").insert(0, sum));
        CartAdapter adapter = new CartAdapter(cartModelList, this);
        rcv_cartlist.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Toasty.warning(this, message, Toast.LENGTH_SHORT).show();
    }
}