package fpt.prm.orderfood.listener;

import java.util.List;

import fpt.prm.orderfood.entities.Cart;

public interface ICartLoadListener {
        void onCartLoadSuccess(List<Cart> cartModelList);
        void onCartLoadFailed(String message);
}
