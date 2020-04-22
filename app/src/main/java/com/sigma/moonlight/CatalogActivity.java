package com.sigma.moonlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends Activity implements SearchView.OnQueryTextListener {

    public static List<com.sigma.moonlight.Product> mProductList;

    ProductAdapter adapter;


    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catlog);

        searchView = findViewById(R.id.searchview);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("MoonLight");
        setActionBar(toolbar);


        // Obtain a reference to the product catalog
        mProductList = com.sigma.moonlight.ShoppingCartHelper.getCatalog(getResources());

        ArrayList<Product> originalProductList = new ArrayList<>(mProductList.size());
        originalProductList.addAll(mProductList);


        // Create the list
        ListView listViewCatalog = findViewById(R.id.ListViewCatalog);
        adapter = new com.sigma.moonlight.ProductAdapter(originalProductList, getLayoutInflater(), false);
        listViewCatalog.setAdapter(adapter);

        listViewCatalog.setTextFilterEnabled(true);


        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), com.sigma.moonlight.ProductDetailsActivity.class);
                productDetailsIntent.putExtra(com.sigma.moonlight.ShoppingCartHelper.PRODUCT_INDEX, position);
                startActivity(productDetailsIntent);
            }
        });

        Button viewShoppingCart = findViewById(R.id.ButtonViewCart);
        viewShoppingCart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewShoppingCartIntent = new Intent(getBaseContext(), com.sigma.moonlight.ShoppingCartActivity.class);
                startActivity(viewShoppingCartIntent);
            }
        });


        searchView.setOnQueryTextListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = findViewById(R.id.toolbar_main);

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Activity selectedActivity = null;
        switch (item.getItemId()) {
            case R.id.nav_pending_orders:
                selectedActivity = new FoodPreparingActivity();
                break;
            case R.id.nav_profile:
                selectedActivity = new ProfileActivity();
                break;
            default:
                break;
        }
        Intent intent = new Intent(getApplicationContext(), selectedActivity.getClass());
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        Log.v("search", "cleared focus");
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        adapter.notifyDataSetChanged();
        Log.v("search", "filtered" + mProductList);
        return false;
    }


}


