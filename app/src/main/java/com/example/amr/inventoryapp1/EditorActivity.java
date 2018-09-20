package com.example.amr.inventoryapp1;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amr.inventoryapp1.Data.InventoryContract.InventoryEntry;

/**
 * Created by Amr on 05/04/2018.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EDITOR_LOADER = 2;
    private Uri currentInventoryUri = null;
    EditText titleEdit,priceEdit,quantityEdit,pubNAmeEdit,pubNumberEdit;
    Button sell,buy,delete;
    int currentQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        currentInventoryUri = getIntent().getData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sell = findViewById(R.id.editor_sell);
        buy = findViewById(R.id.editor_restock);
        delete = findViewById(R.id.editor_delete);
        getLoaderManager().initLoader(EDITOR_LOADER, null, this);
        titleEdit = findViewById(R.id.title_edit);
        priceEdit = findViewById(R.id.price_edit);
        quantityEdit = findViewById(R.id.quantity_edit);
        pubNAmeEdit = findViewById(R.id.publisher_name_edit);
        pubNumberEdit = findViewById(R.id.publisher_num_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
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
        if (item.getItemId() == R.id.save) {
            if (!checkErrors()) {
                getContentResolver().update(currentInventoryUri, values, null, null);
                finish();
            }
        } else if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Product not saved", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_NUMBER
        };
        return new CursorLoader(this, currentInventoryUri, projection
                , null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE));
            final int quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY));
            String pubName = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME));
            String pubNum = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NUMBER));
            titleEdit.setText(title);
            priceEdit.setText(String.valueOf(price));
            pubNAmeEdit.setText(pubName);
            pubNumberEdit.setText(pubNum);
            currentQuantity = quantity;
            quantityEdit.setText(String.valueOf(currentQuantity));
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentQuantity > 0) {
                        --currentQuantity;
                        quantityEdit.setText(String.valueOf(currentQuantity));
                    } else {
                        Toast.makeText(EditorActivity.this, "Products are out of stock already", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ++currentQuantity;
                    quantityEdit.setText(String.valueOf(currentQuantity));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getContentResolver().delete(currentInventoryUri, null, null);
                    finish();
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
 
