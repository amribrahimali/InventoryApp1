package com.example.amr.inventoryapp1.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amr.inventoryapp1.Data.InventoryContract.InventoryEntry;
import com.example.amr.inventoryapp1.DetailsActivity;
import com.example.amr.inventoryapp1.R;


/**
 * Created by Amr on 05/04/2018.
 */

public class InventoryAdapter extends CursorAdapter {

    public InventoryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    String name;
    int price;
    int quantity;
    String currentPub;
    String pubNum;
    int currentId;

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final TextView inventoryTitle = view.findViewById(R.id.product_name_item);
        final TextView inventoryPrice = view.findViewById(R.id.product_price_item);
        TextView inventoryQuantity = view.findViewById(R.id.product_quantity_item);
        final TextView sellView = view.findViewById(R.id.sell);
        TextView detailsView=view.findViewById(R.id.details);
        name = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
        price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE));
        quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY));
        currentPub = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME));
        pubNum = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NUMBER));
        currentId=cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));
        inventoryTitle.setText(name);
        inventoryPrice.setText(String.valueOf(price));
        inventoryQuantity.setText(String.valueOf(quantity));
        final String selection = InventoryEntry._ID + " =?";
        final String[] selectionArgs = new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID)))};
        sellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    --quantity;
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_PRODUCT_NAME, String.valueOf(inventoryTitle.getText()));
                    values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, String.valueOf(inventoryPrice.getText()));
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(quantity));
                    values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, currentPub);
                    values.put(InventoryEntry.COLUMN_SUPPLIER_NUMBER, pubNum);
                    context.getContentResolver().update(InventoryEntry.CONTENT_URI, values, selection, selectionArgs);
                } else {
                    Toast.makeText(context, "Inventory are out of stock already", Toast.LENGTH_SHORT).show();
                }
            }
        });
        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra(InventoryEntry._ID,currentId);
                intent.putExtra(InventoryEntry.COLUMN_PRODUCT_NAME,name);
                intent.putExtra(InventoryEntry.COLUMN_PRODUCT_PRICE,price);
                intent.putExtra(InventoryEntry.COLUMN_PRODUCT_QUANTITY,quantity);
                intent.putExtra(InventoryEntry.COLUMN_SUPPLIER_NAME,currentPub);
                intent.putExtra(InventoryEntry.COLUMN_SUPPLIER_NUMBER,pubNum);
                context.startActivity(intent);
            }
        });
    }
}

