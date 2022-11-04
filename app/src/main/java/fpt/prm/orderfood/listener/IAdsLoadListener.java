package fpt.prm.orderfood.listener;

import java.util.List;

import fpt.prm.orderfood.entities.Advertising;

public interface IAdsLoadListener {
    void onAdsLoadSuccess(List<Advertising> AdsList);
    void onAdsLoadFailed(String message);
}
