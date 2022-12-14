package com.dengage.android.kotlin.nawaz.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dengage.android.kotlin.nawaz.R;
import com.dengage.android.kotlin.nawaz.model.CartItem;
import com.dengage.android.kotlin.nawaz.ui.base.BaseActivity;
import com.dengage.android.kotlin.nawaz.viewModels.ShopViewModel;

import java.util.List;

public class MainActivityEcm extends BaseActivity {

    private static final String TAG = "MainActivity";
    private NavController navController;
    ShopViewModel shopViewModel;
    private int cartQuantity = 0;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewFromChild(R.layout.activity_main_ecm);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                Log.d(TAG, "onChanged: "+cartItems.size());
                int quantity = 0;
                for (CartItem cartItem: cartItems){
                    quantity += cartItem.getQuantity();
                }
                cartQuantity = quantity;
                invalidateOptionsMenu();
            }
        });
        textView=findViewById(R.id.textView);
        textView.setText(getClass().getName());

    }

    // Handle back Btn
    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

    // Add Item Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        final MenuItem menuItem = menu.findItem(R.id.cartFragment);
        View actionView = menuItem.getActionView();
        TextView cartBadgeTV = actionView.findViewById(R.id.cardBadgeTV);
        cartBadgeTV.setText(String.valueOf(cartQuantity));
        cartBadgeTV.setVisibility(cartQuantity == 0 ? View.GONE : View.VISIBLE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    // Item Menu clickable
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*
        NavigationUI.onNavDestinationSelected(item,navController);
        return super.onOptionsItemSelected(item);*/

        // Or
        return NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item);

    }
}