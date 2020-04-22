package com.sigma.moonlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class ProductDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);

        Toolbar toolbar = findViewById(R.id.toolbar_main);


        List<Product> catalog = com.sigma.moonlight.ShoppingCartHelper.getCatalog(getResources());
        List<Product> cart = ShoppingCartHelper.getCartList();

        int productIndex = getIntent().getExtras().getInt(com.sigma.moonlight.ShoppingCartHelper.PRODUCT_INDEX);
        String fromActivity = getIntent().getExtras().getString("SOURCE");

        Product selectedProduct = catalog.get(productIndex);


        //Checking fromActivity
        if (fromActivity != null)
            selectedProduct = cart.get(productIndex);


        // Set the text

        TextView productTitleTextView = findViewById(R.id.TextViewProductTitle);
        productTitleTextView.setText(selectedProduct.title);

        //Setting Toolbar same as name of selected product
        toolbar.setTitle(selectedProduct.title);
        setActionBar(toolbar);

        TextView productDetailsTextView = findViewById(R.id.TextViewProductDetails);
        productDetailsTextView.setText(selectedProduct.description);

        TextView productPriceTextView = findViewById(R.id.TextViewProductPrice);
        productPriceTextView.setText("Rs " + selectedProduct.price);

        // Update the current quantity in the cart
        TextView textViewCurrentQuantity = findViewById(R.id.textViewCurrentlyInCart);
        textViewCurrentQuantity.setText("Currently in Cart: " + com.sigma.moonlight.ShoppingCartHelper.getProductQuantity(selectedProduct));

        // Save a reference to the quantity edit text
        final EditText editTextQuantity = findViewById(R.id.editTextQuantity);

        Button addToCartButton = findViewById(R.id.ButtonAddToCart);

        final Product finalSelectedProduct = selectedProduct;
        addToCartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Check to see that a valid quantity was entered
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(editTextQuantity.getText()
                            .toString());

                    if (quantity < 0) {
                        Toast.makeText(getBaseContext(),
                                "Please enter a quantity of 0 or higher",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),
                            "Please enter a numeric quantity",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // If we make it here, a valid quantity was entered
                com.sigma.moonlight.ShoppingCartHelper.setQuantity(finalSelectedProduct, quantity);

                // Close the activity
                finish();
            }
        });

    }

}