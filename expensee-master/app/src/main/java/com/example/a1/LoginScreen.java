package com.example.a1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private Button signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.SignUpButton); // Sửa ID của nút

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String pass = password.getText().toString();

                Cursor cursor = dbHelper.getUserByEmail(emailText);
                if (cursor != null && cursor.moveToFirst()) {
                    String storedPassword = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
                    if (pass.equals(storedPassword)) {
                        Intent intent = new Intent(LoginScreen.this, Home.class);
                        intent.putExtra("USER_EMAIL", emailText); // Chuyển email đến trang Home
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginScreen.this, "Invalid password!", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                } else {
                    Toast.makeText(LoginScreen.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang SignUp
                Intent intent = new Intent(LoginScreen.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}
