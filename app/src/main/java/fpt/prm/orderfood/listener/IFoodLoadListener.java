package fpt.prm.orderfood.listener;

import java.util.List;

import fpt.prm.orderfood.entities.Food;


public interface IFoodLoadListener {
    void onFoodLoadSuccess(List<Food> foodList);
    void onFoodLoadFailed(String message);
}
