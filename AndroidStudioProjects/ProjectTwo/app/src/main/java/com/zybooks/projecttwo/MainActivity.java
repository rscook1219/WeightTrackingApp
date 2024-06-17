package com.zybooks.projecttwo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zybooks.projecttwo.databases.UserDatabase;

public class MainActivity extends AppCompatActivity {

    private UserDatabase userDatabase;

    private EditText usernameEditText;

    private EditText passwordEditText;

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

        // initialize database and UI components
        userDatabase = new UserDatabase( this);
        usernameEditText = findViewById(R.id.usernameEnterText);
        passwordEditText = findViewById(R.id.pwdEnterText);
        Button loginButton = findViewById(R.id.buttonLogin);
        Button createAccountButton = findViewById(R.id.buttonCreateAccount);

        // set up button listeners
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                createUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        SQLiteDatabase db = userDatabase.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"_id", "username", "password"},
                "username = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            if (dbPassword.equals(password)) {
                // login successful
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(MainActivity.this, DataDisplayActivity.class);
                // startActivity(intent);
            } else {
                // incorrect password
                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            // user not found, create a new user
            createUser(username, password);
        }
    }

    private void createUser(String username, String password) {
        SQLiteDatabase db = userDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long newRowId = db.insert("users", null, values);

        if (newRowId != -1) {
            Toast.makeText(MainActivity.this, "Account created", Toast.LENGTH_SHORT).show();
            // proceed to next activity
            // Intent intent = new Intent(MainActivity.this, DataDisplayActivity.class);
            // startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
        }
    }
}