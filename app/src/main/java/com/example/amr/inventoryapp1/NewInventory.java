package com.example.amr.inventoryapp1;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amr.inventoryapp1.Data.InventoryContract.InventoryEntry;

/**
 * Created by Amr on 05/04/2018.
 */

public class NewInventory extends AppCompatActivity {

    EditText titleEdit,priceEdit,quantityEdit,pubNAmeEdit,pubNumberEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inventroy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleEdit = findViewById(R.id.title_new);
        priceEdit = findViewById(R.id.price_new);
        quantityEdit = findViewById(R.id.quantity_new);
        pubNAmeEdit = findViewById(R.id.publisher_name_new);
        pubNumberEdit = findViewById(R.id.publisher_num_new);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, String.valueOf(titleEdit.getText()));
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, String.valueOf(priceEdit.getText()));
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, String.valueOf(quantityEdit.getText()));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, String.valueOf(pubNAmeEdit.getText()));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NUMBER, String.valueOf(pubNumberEdit.getText()));
        if (item.getItemId() == R.id.add_option) {
            if (!checkErrors()) {
                getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
                finish();
            }
        } else if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Product not saved", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    public boolean checkErrors() {
        if (TextUtils.isEmpty(titleEdit.getText())) {
            Toast.makeText(this, "Product title can't be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(priceEdit.getText())) {
            Toast.makeText(this, "Product must have price", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(quantityEdit.getText())) {
            Toast.makeText(this, "Enter quantity", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(pubNAmeEdit.getText())) {
            Toast.makeText(this, "Enter supplier name", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(pubNumberEdit.getText())) {
            Toast.makeText(this, "Enter supplier number", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

