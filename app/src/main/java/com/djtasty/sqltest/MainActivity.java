package com.djtasty.sqltest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GroceryAdapter adapter;
    private SQLiteDatabase database;
    private EditText editTextName;
    private TextView textViewAmount;
    private int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        database = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        editTextName = findViewById(R.id.edittext_name);
        textViewAmount = findViewById(R.id.textview_amount);

        Button buttonIncrease = findViewById(R.id.button_increase);
        Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);

        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void increase() {
        amount++;
        textViewAmount.setText(String.valueOf(amount));
    }

    private void decrease() {
        if (amount > 0) {
            amount--;
            textViewAmount.setText(String.valueOf(amount));
        }
    }

    private void addItem (){
        if (editTextName.getText().toString().trim().length() == 0 || amount == 0) {
            return;
        }

        String name = editTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, amount);

        database.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        adapter.swapCursor(getAllItems());

        editTextName.getText().clear();

    }

    private Cursor getAllItems() {
        return database.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}





























