package com.example.a1;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private TextView fullNameTextView, phoneTextView, dobTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        // Đảm bảo rằng các id khớp với id trong layout XML
        fullNameTextView = findViewById(R.id.fullNameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        dobTextView = findViewById(R.id.dobTextView);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        if (userEmail != null) {
            Cursor cursor = dbHelper.getUserByEmail(userEmail);

            if (cursor != null && cursor.moveToFirst()) {
                int fullNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FULL_NAME);
                int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);
                int dobIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DOB);

                if (fullNameIndex != -1 && phoneIndex != -1 && dobIndex != -1) {
                    String fullName = cursor.getString(fullNameIndex);
                    String phone = cursor.getString(phoneIndex);
                    String dob = cursor.getString(dobIndex);

                    fullNameTextView.setText("Full Name: " + fullName);
                    phoneTextView.setText("Mobile Phone: " + phone);
                    dobTextView.setText("Date of Birth: " + dob);
                } else {
                    fullNameTextView.setText("Column index error");
                    phoneTextView.setText("Column index error");
                    dobTextView.setText("Column index error");
                }
                cursor.close();
            } else {
                fullNameTextView.setText("No data available");
                phoneTextView.setText("No data available");
                dobTextView.setText("No data available");
            }
        } else {
            fullNameTextView.setText("Email not provided");
            phoneTextView.setText("Email not provided");
            dobTextView.setText("Email not provided");
        }
    }
}
