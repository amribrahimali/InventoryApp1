package com.example.amr.inventoryapp1;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amr.inventoryapp1.Data.InventoryContract.InventoryEntry;


/**
 * Created by Amr on 05/04/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    TextView title,price,quantity,pub,number;
    Button call,sell,restock,delete;
    int currentId,currentQuantity;
    String actualNumber;
    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.details_product);
        price = findViewById(R.id.details_price);
        quantity = findViewById(R.id.details_quantity);
        pub = findViewById(R.id.details_pub);
        number = findViewById(R.id.details_pup_num);
        call = findViewById(R.id.call);
        sell = findViewById(R.id.details_sell);
        restock = findViewById(R.id.details_restock);
        delete = findViewById(R.id.details_delete);
        currentId = getIntent().getIntExtra(InventoryEntry._ID, 0);
        currentQuantity = getIntent().getIntExtra(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 0);
        actualNumber = getIntent().getStringExtra(InventoryEntry.COLUMN_SUPPLIER_NUMBER);
        title.setText(getIntent().getStringExtra(InventoryEntry.COLUMN_PRODUCT_NAME));
        price.setText(String.valueOf(getIntent().getIntExtra(InventoryEntry.COLUMN_PRODUCT_PRICE, 0)));
        quantity.setText(String.valueOf(currentQuantity));
        pub.setText(getIntent().getStringExtra(InventoryEntry.COLUMN_SUPPLIER_NAME));
        number.setText(actualNumber);
        currentUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, currentId);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + actualNumber));
                startActivity(intent);
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuantity > 0) {
                    currentQuantity--;
                    quantity.setText(String.valueOf(currentQuantity));
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_PRODUCT_NAME, String.valueOf(title.getText()));
                    values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, String.valueOf(price.getText()));
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(currentQuantity));
                    values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pub.getText()));
                    values.put(InventoryEntry.COLUMN_SUPPLIER_NUMBER, actualNumber);
                    getContentResolver().update(currentUri, values, null, null);
                } else {
                    Toast.makeText(DetailsActivity.this, "Products already out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
        restock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuantity++;
                quantity.setText(String.valueOf(currentQuantity));
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_PRODUCT_NAME, String.valueOf(title.getText()));
                values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, String.valueOf(price.getText()));
                values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(currentQuantity));
                values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pub.getText()));
                values.put(InventoryEntry.COLUMN_SUPPLIER_NUMBER, actualNumber);
                getContentResolver().update(currentUri, values, null, null);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to delete this product?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getContentResolver().delete(currentUri, null, null);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}


