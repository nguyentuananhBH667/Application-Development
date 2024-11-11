package com.example.a1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText fullName, email, phone, dob, password, confirmPassword;
    private Button signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dob = findViewById(R.id.dob);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.sign_up_button);

        dbHelper = new DatabaseHelper(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                String emailText = email.getText().toString();
                String phoneText = phone.getText().toString();
                String dobText = dob.getText().toString();
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (pass.equals(confirmPass)) {
                    Cursor cursor = dbHelper.getUserByEmail(emailText);
                    if (cursor != null && cursor.getCount() > 0) {
                        Toast.makeText(SignUp.this, "Existed email!", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.addUser(name, emailText, phoneText, dobText, pass);
                        Toast.makeText(SignUp.this, "Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, LoginScreen.class));
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Password is not matched!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LoginScreen.class));
            }
        });
    }
}
