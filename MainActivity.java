package com.example.inventoryapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initializing database and UI elements
        dbHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonCreate = findViewById(R.id.buttonCreateAccount);

        buttonLogin.setOnClickListener(V -> {
            String user = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both fields.", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.checkLogin(user, pass)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                // TODO: LAUNCH INVENTORY SCREEN LATER
                Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "invalid login. Try again or create account.", Toast.LENGTH_SHORT).show();
            }


        });
        buttonCreate.setOnClickListener(v -> {
            String user = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both fields.", Toast.LENGTH_SHORT).show();
            }else if (dbHelper.registerUser(user, pass)) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Error creating account.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}