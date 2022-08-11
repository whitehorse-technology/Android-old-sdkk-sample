package com.dengage.android.kotlin.nawaz.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dengage.android.kotlin.nawaz.model.CartItem;
import com.dengage.sdk.DengageEvent;
import com.google.android.material.snackbar.Snackbar;
import com.dengage.android.kotlin.nawaz.R;
import com.dengage.android.kotlin.nawaz.adapters.ShopListAdapter;
import com.dengage.android.kotlin.nawaz.databinding.FragmentShopBinding;
import com.dengage.android.kotlin.nawaz.model.Product;
import com.dengage.android.kotlin.nawaz.viewModels.ShopViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShopFragment extends Fragment implements ShopListAdapter.ShopInterface {

    FragmentShopBinding fragmentShopBinding;
    private ShopListAdapter shopListAdapter;
    private ShopViewModel shopViewModel;
    private static final String TAG = "ShopFragment";
    private NavController navController;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentShopBinding = FragmentShopBinding.inflate(inflater, container, false);
        return fragmentShopBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopListAdapter = new ShopListAdapter(this);
        fragmentShopBinding.shopRecyclerViewId.setAdapter(shopListAdapter);
        fragmentShopBinding.shopRecyclerViewId.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        fragmentShopBinding.shopRecyclerViewId.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                shopListAdapter.submitList(products);
            }
        });

        navController = Navigation.findNavController(view);

    }

    @Override
    public void addItem(Product product) {

        boolean isAdded = shopViewModel.addItemToCart(product);
        Log.d(TAG, "addItem: " + product.getName() + " " + isAdded);

        if (isAdded) {
            sendAddToCartEvent(product);
            Snackbar.make(requireView(), product.getName() + " added to cart.", Snackbar.LENGTH_LONG)
                    .setAction("Checkout", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkout(shopViewModel.getCart());
                            navController.navigate(R.id.action_shopFragment_to_cartFragment2);
                        }
                    }).show();
        } else {
            Snackbar.make(requireView(), " Already have the max quantity in cart.", Snackbar.LENGTH_LONG).show();
        }


    }

    @Override
    public void addItemToWishList(Product product) {
        addWishListEvent(product);
    }

    @Override
    public void onItemClick(Product product) {
        Log.d(TAG, "onItemClick: " + product.toString());
     /*   shopViewModel.setProduct(product);
        navController.navigate(R.id.action_shopFragment_to_productDetailsFragment);
*/
    }


    void sendAddToCartEvent(Product product) {
        ArrayList<HashMap<String, Object>> cartItems = new ArrayList<>();

        HashMap<String, Object> item1 = new HashMap<>();
        item1.put("product_id", product.getId());
        item1.put("product_variant_id", product.getId() + "12");
        item1.put("quantity", 1);
        item1.put("unit_price", product.getPrice());
        item1.put("discounted_price", "0.0");
        cartItems.add(item1);
        HashMap<String, Object> data = new HashMap<>();
        data.put("coupon_code", "testing");
        data.put("cartItems", cartItems.toArray());
        DengageEvent.getInstance(getActivity()).addToCart(data);
    }


    void checkout(LiveData<List<CartItem>> cartItemlist) {
        ArrayList<HashMap<String, Object>> cartItems = new ArrayList<>();

        for (int i = 0; i < cartItemlist.getValue().size(); i++) {
            Product product = cartItemlist.getValue().get(i).getProduct();
            HashMap<String, Object> item1 = new HashMap<>();
            item1.put("product_id", product.getId());
            item1.put("product_variant_id", product.getId() + "12");
            item1.put("quantity", 1);
            item1.put("unit_price", product.getPrice());
            item1.put("discounted_price", "0.0");
            cartItems.add(item1);


        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("cartItems", cartItems.toArray());

        DengageEvent.getInstance(getActivity()).beginCheckout(data);
    }

    void addWishListEvent(Product product) {
        ArrayList<HashMap<String, Object>> wishlistItems = new ArrayList<>();

        HashMap<String, Object> item = new HashMap<>();
        item.put("product_id", product.getId());
        // ... extra columns in wishlist_events_detail table, can be added here
        wishlistItems.add(item);

// Add To Wish List
        HashMap<String, Object> data = new HashMap<>();
        data.put("product_id", product.getId());
        data.put("items", wishlistItems.toArray());
// ... extra columns in wishlist_events table, can be added here
        DengageEvent.getInstance(getActivity()).addToWishList(data);
    }
}