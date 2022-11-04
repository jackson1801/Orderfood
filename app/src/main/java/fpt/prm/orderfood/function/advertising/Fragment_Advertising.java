package fpt.prm.orderfood.function.advertising;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.Advertising;
import fpt.prm.orderfood.listener.IAdsLoadListener;
import fpt.prm.orderfood.utils.LoggerUtil;

public class Fragment_Advertising extends Fragment implements IAdsLoadListener {

    private ViewPager2 viewPager2;
    IAdsLoadListener iAdsLoadListener;

    private void bindingView(View view) {
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);
    }

    private void bindingAction(View view) {
        loadAdsFromFirebase();
    }

    private void loadAdsFromFirebase() {
        List<Advertising> Advertising = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Advertising").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot adsSnapshot : snapshot.getChildren()) {
                        Advertising advertising = adsSnapshot.getValue(Advertising.class);
                        advertising.setKey(adsSnapshot.getKey());
                        Advertising.add(advertising);
                    }
                    viewPager2.setAdapter(new AdvertisingAdapter(Advertising, viewPager2, getContext()));

                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(5);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer((page, position) -> {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.85f + r * 0.15f);
                    });

                    viewPager2.setPageTransformer(compositePageTransformer);
                } else {
                    iAdsLoadListener.onAdsLoadFailed("Can't find Advertising");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LoggerUtil.e(error.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView(view);
        bindingAction(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advertising, container, false);
    }

    @Override
    public void onAdsLoadSuccess(List<Advertising> AdsList) {
    }

    @Override
    public void onAdsLoadFailed(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_SHORT, true).show();
    }
}