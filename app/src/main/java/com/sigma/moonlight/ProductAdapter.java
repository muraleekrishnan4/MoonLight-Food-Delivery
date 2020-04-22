package com.sigma.moonlight;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private List<Product> mProductList;
    //    private List<Product> originalProductList;
    private LayoutInflater mInflater;
    private boolean mShowQuantity;


    public ProductAdapter(List<Product> list, LayoutInflater inflater, boolean showQuantity) {
        this.mProductList = list;
        this.mInflater = inflater;
        this.mShowQuantity = showQuantity;
    }


    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            item = new ViewItem();

            item.productTitle = convertView.findViewById(R.id.item_name);

            item.productPrice = convertView.findViewById(R.id.food_price);

            item.productQuantity = convertView.findViewById(R.id.item_quantity);


            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }


        Product curProduct = mProductList.get(position);


        item.productTitle.setText(curProduct.title);

        String price = "Rs. " + curProduct.price;


        item.productPrice.setText(price);


        // Show the quantity in the cart or not
        if (mShowQuantity) {
            item.productQuantity.setText("x" + com.sigma.moonlight.ShoppingCartHelper.getProductQuantity(curProduct));
        } else {
            // Hid the view
            item.productQuantity.setVisibility(View.GONE);
        }


        return convertView;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase();

        CatalogActivity.mProductList.clear();


        if (charText.length() == 0) {
            CatalogActivity.mProductList.addAll(mProductList);
            Log.v("search", "" + CatalogActivity.mProductList);
        } else {
            for (Product wp : mProductList) {
                if (wp.getTitle().toLowerCase().contains(charText)) {
                    CatalogActivity.mProductList.add(wp);
                    Log.v("search", "" + CatalogActivity.mProductList);
                }
            }
        }

    }

    private class ViewItem {
        TextView productTitle;
        TextView productPrice;
        TextView productQuantity;
    }


}

