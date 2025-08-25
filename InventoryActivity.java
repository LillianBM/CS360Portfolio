package com.example.inventoryapp;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    LinearLayout layoutInventoryItems;
    Button buttonAddItem;
    Button buttonLogout;

    private static final int SMS_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        dbHelper = new DatabaseHelper(this);
        layoutInventoryItems = findViewById(R.id.layoutInventoryItems);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        Button buttonSendSms = findViewById(R.id.buttonSendSms);

        //display current inventory items
        displayInventory();

        //add a new sample item when button is clicked
        buttonAddItem.setOnClickListener(v -> showAddItemDialog());
        buttonSendSms.setOnClickListener(v -> {
            checkSmsPermissionAndSend("Low inventory alert!!");
        });
        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //goes back to login screen
            finish(); //closes inventoryactivity
        });
    }

    private void displayInventory() {
        layoutInventoryItems.removeAllViews(); //clear the existing views
        Cursor cursor = dbHelper.getAllInventoryItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                //creating a layout for each row (item + delete button)
                LinearLayout itemContainer = new LinearLayout(this);
                itemContainer.setOrientation(LinearLayout.VERTICAL);
                itemContainer.setPadding(16, 16, 16, 16);
                itemContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
                LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                itemParams.setMargins(0, 0, 0, 32); //helps to add spacing between items
                itemContainer.setLayoutParams(itemParams);

                // TextView for items details
                TextView textView = new TextView(this);
                textView.setText("item: " + name + " \nQty: " + quantity + "\nNote: " + description);
                textView.setPadding(8, 8, 8, 8);
                textView.setTextSize(16);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setOnClickListener(v -> showUpdateItemDialog(id, name, quantity, description));
                textView.setLayoutParams(new LinearLayout.LayoutParams (
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));


                //Delete button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(v -> {
                    boolean success = dbHelper.deleteInventoryItem(id);
                    if (success) {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                        displayInventory(); //refreshes the list
                    } else {
                        Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show();
                    }
                });
                // add TextView and Button to row
                itemContainer.addView(textView);
                itemContainer.addView(deleteButton);

                //add row to inventory layout
                layoutInventoryItems.addView(itemContainer);


            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setText("No items in inventory.");
            layoutInventoryItems.addView(emptyView);


        }
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Item");

        //layout for input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        //item name input
        EditText itemNameInput = new EditText(this);
        itemNameInput.setHint("Item Name");
        layout.addView(itemNameInput);

        //quantity input
        EditText itemQtyInput = new EditText(this);
        itemQtyInput.setHint("Quantity");
        itemQtyInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(itemQtyInput);

        //description input
        EditText itemDescInput = new EditText(this);
        itemDescInput.setHint("Description");
        layout.addView(itemDescInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = itemNameInput.getText().toString().trim();
            String qtyStr = itemQtyInput.getText().toString().trim();
            String desc = itemDescInput.getText().toString().trim();

            if (!name.isEmpty() && !qtyStr.isEmpty()) {
                int quantity = Integer.parseInt(qtyStr);
                boolean success = dbHelper.insertInventoryItem(name, quantity, desc);
                if (success) {
                    displayInventory(); //refresh list
                    Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter item name and quantity.", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void showUpdateItemDialog(int id, String currentName, int currentQty, String currentDesc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Item");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        EditText nameInput = new EditText(this);
        nameInput.setText(currentName);
        layout.addView(nameInput);

        EditText qtyInput = new EditText(this);
        qtyInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        qtyInput.setText(String.valueOf(currentQty));
        layout.addView(qtyInput);

        EditText descInput = new EditText(this);
        descInput.setText(currentDesc);
        layout.addView(descInput);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedName = nameInput.getText().toString().trim();
            String updatedQtyStr = qtyInput.getText().toString().trim();
            String updatedDesc = descInput.getText().toString().trim();

            if (!updatedName.isEmpty() && !updatedQtyStr.isEmpty()) {
                int updatedQty = Integer.parseInt(updatedQtyStr);
                boolean success = dbHelper.updateInventoryItem(id, updatedName, updatedQty, updatedDesc);
                if (success) {
                    displayInventory();
                    Toast.makeText(this, "item updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(this, "Name and quantity required.", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void checkSmsPermissionAndSend(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            sendSmsAlert(message);
        }
    }

    private void sendSmsAlert(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("1234567890", null, message, null, null); //testing
            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}


