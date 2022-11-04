package fpt.prm.orderfood.function.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fpt.prm.orderfood.HelloActivity;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.ShopActivity;
import fpt.prm.orderfood.entities.Shop;
import fpt.prm.orderfood.function.advertising.Fragment_Advertising;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment_Advertising fragment_advertising;
    private FragmentManager fragmentManager;
    //    private RecyclerView rcv_cate;
    private RecyclerView recommended_recycler;
    private RecyclerView all_menu_recycler;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView navigationView;
    public ImageView nav;

    private SearchView searchView;
    private ListView myListView;
    private Shop item;
    private ArrayAdapter<Shop> adapter;

    private void bindingView() {
        fragment_advertising = new Fragment_Advertising();
        fragmentManager = getSupportFragmentManager();
//        rcv_cate = findViewById(R.id.rcv_cate);
        recommended_recycler = findViewById(R.id.recommended_recycler);
        all_menu_recycler = findViewById(R.id.all_menu_recycler);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        nav = findViewById(R.id.nav);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        searchView = findViewById(R.id.searchView);
        myListView = findViewById(R.id.listView);

    }

    private void bindingAction() {
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        nav.setOnClickListener(this::NavMenuClick);
        navigationView.setNavigationItemSelectedListener(this);

        Ads_Fragment();
//        CategoryAddFromFirebase();
        RcmMenuAddFromFirebase();
        AllMenuAddFromFirebase();
        searchfunc();
        search();
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equalsIgnoreCase("")) {
                    myListView.setVisibility(View.GONE);
                    return true;
                }

                myListView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(s);
                return false;
            }

        });

        myListView.setOnItemClickListener((adapterView, view, i, l) -> {
            item = adapter.getItem(i);
            sendDataToStoreActivity(item);
        });

    }

    private void sendDataToStoreActivity(Shop itemStoreData) {
        Intent intent = new Intent(this, ShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemData", itemStoreData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void NavMenuClick(View view) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    private void AllMenuAddFromFirebase() {
        List<Shop> Shops = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                        Shop shop = shopSnapshot.getValue(Shop.class);
                        shop.setKey(shopSnapshot.getKey());
                        Shops.add(shop);
                    }
                    all_menu_recycler.setAdapter(new AllMenuAdapter(Shops, HomeActivity.this));
                } else {
                    Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchfunc() {
        myListView.setVisibility(View.GONE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stores());
        myListView.setAdapter(adapter);
        myListView.setVisibility(View.VISIBLE);

    }

    private List<Shop> stores() {
        List<Shop> stores = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                        Shop shop = shopSnapshot.getValue(Shop.class);
                        shop.setKey(shopSnapshot.getKey());
                        stores.add(shop);
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return stores;
    }

    private void RcmMenuAddFromFirebase() {
        List<Shop> shops = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                        Shop shop = shopSnapshot.getValue(Shop.class);
                        shop.setKey(shopSnapshot.getKey());
                        shops.add(shop);
                    }
                    recommended_recycler.setAdapter(new RecommendMenuAdapter(shops, HomeActivity.this));
                } else {
                    Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void CategoryAddFromFirebase() {
//        List<Category> categories = new ArrayList<>();
//        FirebaseDatabase.getInstance().getReference("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot cateSnapshot : snapshot.getChildren()) {
//                        Category category = cateSnapshot.getValue(Category.class);
//                        category.setKey(cateSnapshot.getKey());
//                        categories.add(category);
//                    }
//                    rcv_cate.setAdapter(new CategoryAdapter(categories, HomeActivity.this));
//                } else {
//                    Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void Ads_Fragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.ads_fragment, fragment_advertising);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindingView();
        bindingAction();

//        rcv_cate.setLayoutManager(new GridLayoutManager(this, 4));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recommended_recycler.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagerAll = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        all_menu_recycler.setLayoutManager(layoutManagerAll);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Toast.makeText(this, "Click log out", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, HelloActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getApplicationContext());
                alertDialog.setTitle("Log Out Failed");
                alertDialog.setMessage("You're not logged in. Please log in first");
                alertDialog.setPositiveButton("Close", null);
                alertDialog.show();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }
}