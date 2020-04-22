package com.sigma.moonlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShoppingCartActivity extends Activity {

    FirebaseFirestore db;
    private List<Product> mCartList;
    private ProductAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        Button btn = findViewById(R.id.Button02);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Shopping Cart");
        setActionBar(toolbar);


        mCartList = com.sigma.moonlight.ShoppingCartHelper.getCartList();


        // Make sure to clear the selections
        for (int i = 0; i < mCartList.size(); i++) {
            mCartList.get(i).selected = false;
        }


        // Create the list
        final ListView listViewCatalog = findViewById(R.id.ListViewCatalog);

        final ArrayList<Product> cartList = new ArrayList<>(mCartList.size());

        cartList.addAll(mCartList);

        mProductAdapter = new ProductAdapter(cartList, getLayoutInflater(), true);
        listViewCatalog.setAdapter(mProductAdapter);


        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), ProductDetailsActivity.class);
                productDetailsIntent.putExtra(com.sigma.moonlight.ShoppingCartHelper.PRODUCT_INDEX, position);
                productDetailsIntent.putExtra("SOURCE", "fromShoppingCartActivity");//Go to product details if intenting from this activity
                startActivity(productDetailsIntent);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCartList.size() > 0) {
                    Intent intent = new Intent(ShoppingCartActivity.this, FoodPreparingActivity.class);
                    startActivity(intent);
                    order();
                    finish();
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "Your cart is empty", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //reads and updates orderID and calls orderItem()
    private void order() {


        //fetching ad updating oderID
        db.collection("orderID")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.v("cart", "List is empty");
                        } else {
                            int orderID = 0;
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists())
                                    orderID = Objects.requireNonNull(documentSnapshot.getLong("orderID")).intValue();
                            }
                            //updating orderID
                            db.collection("orderID").document("idNumber").update("orderID", ++orderID);

                            orderItems(orderID);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch orderID. Try again", Toast.LENGTH_LONG).show();
                    }
                });


    }


    //orders fooditem using orderID
    private void orderItems(int orderID) {
        //ordering items
        for (int i = 0; i < mCartList.size(); i++) {

            // Create a new user with a first and last name
            Map<String, Object> order = new HashMap<>();
            order.put(orderID + "foodName" + (i + 1), mCartList.get(i).getTitle());
            order.put(orderID + "foodPrice" + (i + 1), mCartList.get(i).getPrice());
            order.put(orderID + "foodQuantity" + (i + 1), ShoppingCartHelper.getProductQuantity(mCartList.get(i)));
            order.put(orderID + "update" + (i + 1), 0);


            db.collection("orders").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .set(order, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ShoppingCartActivity.this, "You've sucessfully ordered your food", Toast.LENGTH_LONG).show();
                            emptyCart();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Order couldn't be processed. Please try again", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private void emptyCart() {
        for (Product p : ShoppingCartHelper.getCartList()) {
            ShoppingCartHelper.removeProduct(p);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the data
        if (mProductAdapter != null) {
            mProductAdapter.notifyDataSetChanged();
        }

        double subTotal = 0;
        for (Product p : mCartList) {
            int quantity = com.sigma.moonlight.ShoppingCartHelper.getProductQuantity(p);
            subTotal += p.price * quantity;
        }

        TextView productPriceTextView = findViewById(R.id.TextViewSubtotal);
        productPriceTextView.setText("Subtotal: Rs " + subTotal);
    }
}