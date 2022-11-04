package fpt.prm.orderfood.function.home;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import fpt.prm.orderfood.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    ImageView btnDelete;
    ImageView removebtn;
    ImageView addbtn;
    ImageView cart_image;
    TextView cart_name;
    TextView price;
    EditText txtQuantity;


    private void bindingView(View view) {
        btnDelete = view.findViewById(R.id.btnDelete);
        removebtn = view.findViewById(R.id.removebtn);
        addbtn = view.findViewById(R.id.addbtn);
        cart_image = view.findViewById(R.id.cart_image);
        cart_name = view.findViewById(R.id.cart_name);
        price = view.findViewById(R.id.price);
        txtQuantity = view.findViewById(R.id.txtQuantity);
    }

    private void bindingAction(View view) {

    }


    public CartViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        bindingView(itemView);
        bindingAction(itemView);
    }

    @Override
    public void onClick(View v) {
    }
}
