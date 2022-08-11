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

import com.dengage.android.kotlin.nawaz.R;
import com.dengage.android.kotlin.nawaz.adapters.CartListAdapter;
import com.dengage.android.kotlin.nawaz.databinding.FragmentCartBinding;
import com.dengage.android.kotlin.nawaz.model.CartItem;
import com.dengage.android.kotlin.nawaz.model.Product;
import com.dengage.android.kotlin.nawaz.viewModels.ShopViewModel;
import com.dengage.sdk.DengageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CartFragment extends Fragment implements CartListAdapter.CartInterface {

    private static final String TAG = "CartFragment";
    private ShopViewModel shopViewModel;
    private FragmentCartBinding fragmentCartBinding;
    private NavController navController;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);
        return fragmentCartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        final CartListAdapter cartListAdapter = new CartListAdapter(this);

        fragmentCartBinding.cartRecyclerView.setAdapter(cartListAdapter);

        fragmentCartBinding.cartRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {

                cartListAdapter.submitList(cartItems);

                // enable or disable button
                fragmentCartBinding.placeOrderButton.setEnabled(cartItems.size() > 0);
            }
        });

        shopViewModel.getTotalPrice().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalPrice) {
                fragmentCartBinding.orderTotalTextView.setText("Total: $" + totalPrice.toString());

            }
        });

        fragmentCartBinding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order(shopViewModel.getCart());
                navController.navigate(R.id.action_cartFragment_to_orderFragment2);
            }
        });
    }

    @Override
    public void deleteItem(CartItem cartItem) {
        Log.d(TAG, "deleteItem: " + cartItem.getProduct().getName());
        shopViewModel.removeItemFromCart(cartItem);
        removeFromCheckout(shopViewModel.getCart(), cartItem);

    }

    @Override
    public void changeQuantity(CartItem cartItem, int quantity) {

        shopViewModel.changeQuantity(cartItem, quantity);
    }

    void removeFromCheckout(LiveData<List<CartItem>> cartItemlist, CartItem cartItem) {
        ArrayList<HashMap<String, Object>> cartItems = new ArrayList<>();
        HashMap<String, Object> item2 = new HashMap<>();
        item2.put("product_id", cartItem.getProduct().getId());
        item2.put("product_variant_id", cartItem.getProduct().getId() + "12");
        item2.put("quantity", 1);
        item2.put("unit_price", cartItem.getProduct().getPrice());
        item2.put("discounted_price", "0.0");
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

        DengageEvent.getInstance(getActivity()).removeFromCart(data);
    }

    void order(LiveData<List<CartItem>> cartItemlist) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("order_id", "123");
        data.put("item_count", cartItemlist.getValue().size());
        data.put("total_amount", shopViewModel.getTotalPrice().getValue());
        data.put("payment_method", "card");
        data.put("shipping", 5);
        data.put("discounted_price", "0");   // use total price if there is no discount
        data.put("coupon_code", "testing ");  // use if necessary

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
        data.put("cartItems", cartItems.toArray());  // ordered items
// ... extra columns in order_events table, can be added here
        DengageEvent.getInstance(getActivity()).order(data);
    }
}